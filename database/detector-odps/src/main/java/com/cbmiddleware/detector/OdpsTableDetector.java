package com.cbmiddleware.detector;

import com.aliyun.odps.*;
import com.aliyun.odps.data.Record;
import com.aliyun.odps.task.SQLTask;
import com.aliyun.odps.utils.StringUtils;
import com.cbmiddleware.detector.constant.DataBaseType;
import com.cbmiddleware.detector.constant.DetectorConstants;
import com.cbmiddleware.detector.constant.OdpsSystemStatement;
import com.cbmiddleware.detector.entity.Threshold;
import com.cbmiddleware.detector.util.DataSourceUtils;
import com.cbmiddleware.detector.util.DetectorOdpsUtils;
import com.cbmiddleware.detector.constant.DetectorType;
import com.cbmiddleware.detector.entity.ColumnInfo;
import com.cbmiddleware.detector.entity.TableInfo;
import com.cbmiddleware.detector.exception.DetectorException;
import com.cbmiddleware.detector.util.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.alibaba.druid.util.JdbcConstants.ODPS;

/**
 * @author Eason(bo.chenb)
 * @description odps 表结构探测器
 * @date 2020-03-09
 **/
@Component("odpsTableDetector")
public class OdpsTableDetector extends AbstractTableDetector {

    private static final Logger logger = LoggerFactory.getLogger(OdpsTableDetector.class);

    @Override
    public DetectResponse detect(DetectRequest detectRequest) throws DetectorException {

        OdpsDetectResponse response = new OdpsDetectResponse();
        detectSync(detectRequest, new Threshold(), null, response);
        return response;
    }

    @Override
    public void detect(DetectRequest detectRequest, Threshold threshold, DetectorActionListener<DetectResponse> listener) throws DetectorException {

        detectSync(detectRequest, threshold, listener, new OdpsDetectResponse());
    }

    private void detectSync(DetectRequest detectRequest, Threshold threshold, DetectorActionListener<DetectResponse> listener, OdpsDetectResponse response) throws DetectorException {

        long startTime = System.currentTimeMillis();
        OdpsDetectRequest request = (OdpsDetectRequest) detectRequest;

        validation(request);

        String accessId = request.getAccessId();
        String accessKey = request.getAccessKey();
        String url = request.getUrl();
        String project = request.getProject();
        List<String> tableNames = request.getTableNames();

        logger.info("Odps table detector, accessId:[{}], accessKey:[{}], url:[{}], project:[{}]", accessId, accessKey, url, project);

        /* 是否连通 */
        if (request.isTestConnection() && !isConnect(detectRequest)) {
            throw new DetectorException(String.format("Connect odps database failed, accessId:[%s], accessKey:[%s], url:[%s], project:[%s]",
                    accessId, accessKey, url, project));
        }

        Odps odps = DetectorOdpsUtils.createOdps(request.getAccessId(), request.getAccessKey(), request.getUrl(), request.getProject());

        Map<String, TableInfo> tableInfoMap = new HashMap<>();

        long startReadTableTime = System.currentTimeMillis();

        Iterator<Table> tablesIterator = null;

        if (tableNames != null && tableNames.size() > 0) {
            logger.info("Begin get odps some tables info... ");

            tablesIterator = reloadedTables(tableNames, odps, project);
        } else {
            logger.info("Begin get odps all tables info... ");
            tablesIterator = odps.tables().iterator();
        }

        if (response == null) {
            response = new OdpsDetectResponse();
        }

        response.setTableInfos(tableInfoMap);

        /* 记录 列数和表数据量 */
        long columnCount = 0, tableCount = 0, totalTableCount = 0, totalColumnCount = 0;


        final Integer thresholdTableCount = Validator.validatInteger(threshold.getTableCount(), Integer.MAX_VALUE);
        final Integer thresholdColumnCount = Validator.validatInteger(threshold.getColumnCount(), Integer.MAX_VALUE);

        /* 轮训获取所有表信息 */

        while (tablesIterator.hasNext()) {
            Table table = tablesIterator.next();
            TableInfo tableInfo = new TableInfo();

            /* 表名 */
            String tableName = table.getName();
            String tableProject = table.getProject();

            tableInfo.setTableName(tableName);
            tableInfo.setRemark(table.getComment());
            tableInfo.setTableOwner(table.getOwner());
            tableInfo.setTableCreateTime(table.getCreatedTime());
            tableInfo.setTableUpdateTime(table.getLastMetaModifiedTime());
            tableInfo.setTableStorageSize(table.getSize());
            tableInfo.setTablePhysicalSize(table.getPhysicalSize());
            tableInfo.setStorageUnit("byte");

            tableInfoMap.put(tableName, tableInfo);

            tableCount++;

            TableSchema tableSchema = table.getSchema();

            if (tableSchema.getPartitionColumns().size() > 0) {
                List<String> tableAllPartitions = DetectorOdpsUtils.getTableAllPartitions(table);

                if (tableAllPartitions.size() > 0) {
                    Map<String, Object> tablePartitionMap = new HashMap<>(1);
                    tablePartitionMap.put(DetectorConstants.TABLE_CONF_KEY_PARTITION, tableAllPartitions);
                    tableInfo.setTableConf(tablePartitionMap);
                }

            }

            /* 获取字段信息 */

            /* 封装分区字段信息 */
            List<ColumnInfo> partitionFieldInfos = buildPartitionInfo(tableSchema, tableName, tableProject);
            tableInfo.setPartitionFieldInfos(partitionFieldInfos);
            columnCount += partitionFieldInfos.size();

            /* 封装普通字段信息 */
            List<ColumnInfo> fieldInfos = buildFieldInfo(tableSchema, tableName, tableProject);
            tableInfo.setFieldInfos(fieldInfos);
            columnCount += fieldInfos.size();


            /* 满足情况 */
            boolean flag = (thresholdColumnCount <= columnCount || thresholdTableCount <= tableCount) && listener != null;
            if (flag) {
                logger.info("Reaching threshold, thresholdTableCount:[{}], currentTableCount:[{}], thresholdColumnCount:[{}], currentColumnCount:[{}]",
                        thresholdTableCount, tableCount, thresholdColumnCount, columnCount);
                long startInvokeTime = System.currentTimeMillis();
                listener.onResponse(response);
                logger.info("Completed invoke listener onResponse method, time consume:[{}ms]", System.currentTimeMillis() - startInvokeTime);
                /* 清空已返回的数据 */
                tableInfoMap.clear();
                totalTableCount += tableCount;
                totalColumnCount += columnCount;
                columnCount = 0;
                tableCount = 0;
            }

            if (tableCount > 0 && tableCount % 100 == 0) {
                logger.info("Getting odps table and field info, current table size:[{}], current column size:[{}], read test consume:[{}ms]", tableCount, columnCount, System.currentTimeMillis() - startReadTableTime);
            }


        }

        if (tableInfoMap.size() > 0 && listener != null) {
            listener.onResponse(response);
        }

        logger.info("Get odps table and field info completed, total table size:[{}], total column size:[{}], time consume:[{}ms]", totalTableCount, totalColumnCount, System.currentTimeMillis() - startReadTableTime);

        logger.info("Odps table detect completed, test consume:[{}ms]", System.currentTimeMillis() - startTime);
    }

    @Override
    public void detectAsync(DetectRequest detectRequest, Threshold threshold, DetectorActionListener<DetectResponse> listener) throws DetectorException {

    }

    @Override
    public DetectResponse detectTableSize(DetectRequest detectRequest, List<String> tableNames) throws DetectorException {
        long startTime = System.currentTimeMillis();
        OdpsDetectRequest request = ((OdpsDetectRequest) detectRequest);

        validation(request);

        String url = request.getUrl();
        String project = request.getProject();
        String accessId = request.getAccessId();
        String accessKey = request.getAccessKey();

        if (tableNames == null) {
            throw new NullPointerException("tables is null");
        }

        logger.info("Odps table size detector, accessId:[{}], accessKey:[{}], url:[{}], project:[{}]", accessId, accessKey, url, project);

        /* 是否连通 */
        if (request.isTestConnection() && !isConnect(detectRequest)) {
            throw new DetectorException(String.format("Connect odps database failed, accessId:[%s], accessKey:[%s], url:[%s], project:[%s]",
                    accessId, accessKey, url, project));
        }

        Odps odps = DetectorOdpsUtils.createOdps(request.getAccessId(), request.getAccessKey(), request.getUrl(), request.getProject());


        long startReadTableTime = System.currentTimeMillis();

        Map<String, TableInfo> tableInfoMap = new HashMap<>();

        /* 加载表信息 */
        Iterator<Table> tableIterator = reloadedTables(tableNames, odps, project);

        while (tableIterator.hasNext()) {
            Table table = tableIterator.next();
            TableInfo tableInfo = new TableInfo();
            String tableName = table.getName();

            tableInfo.setTableName(tableName);

            tableInfo.setTableOwner(table.getOwner());
            tableInfo.setTableCreateTime(table.getCreatedTime());
            tableInfo.setTableUpdateTime(table.getLastMetaModifiedTime());
            tableInfo.setTableStorageSize(table.getSize());
            tableInfo.setRemark(table.getComment());
            tableInfo.setTablePhysicalSize(table.getPhysicalSize());
            tableInfo.setStorageUnit("byte");

            //todo 目前未重sdk中找到可以获取数据量的接口，所以只能通过sql语句进行查询，思考 待优化
            /* 查询数据量 */
            String countSql = String.format(OdpsSystemStatement.COUNT_SQL, tableName);

            List<Record> records = execute(odps, countSql);
            /* 查询总数 结果只会有一条数据 */
            if (records.size() == 1) {
                Record record = records.get(0);
                tableInfo.setTableRows(Long.parseLong(record.getString(0)));
            }

            tableInfoMap.put(tableName, tableInfo);
        }

        logger.info("Get odps table size completed, time consume:[{}ms]", System.currentTimeMillis() - startReadTableTime);

        OdpsDetectTableSizeResponse response = new OdpsDetectTableSizeResponse();
        response.setTableInfos(tableInfoMap);
        logger.info("Odps table size detect completed, test consume:[{}ms]", System.currentTimeMillis() - startTime);
        return response;
    }


    @Override
    public DetectResponse executeSql(DetectRequest detectRequest, String sql) throws DetectorException {

        DataSourceUtils.formatSql(sql, ODPS);

        long startTime = System.currentTimeMillis();
        OdpsDetectRequest request = ((OdpsDetectRequest) detectRequest);

        validation(request);

        String accessId = request.getAccessId();
        String accessKey = request.getAccessKey();
        String url = request.getUrl();
        String project = request.getProject();

        if (StringUtils.isBlank(sql)) {
            throw new NullPointerException("sql is null");
        }

        logger.info("Odps execute sql , accessId:[{}], accessKey:[{}], url:[{}], project:[{}], sql:[{}]", accessId, accessKey, url, project, sql);

        /* 是否连通 */
        if (request.isTestConnection() && !isConnect(detectRequest)) {
            throw new DetectorException(String.format("Connect odps database failed, accessId:[%s], accessKey:[%s], url:[%s], project:[%s]",
                    accessId, accessKey, url, project));
        }

        Odps odps = DetectorOdpsUtils.createOdps(request.getAccessId(), request.getAccessKey(), request.getUrl(), request.getProject());

        List<Record> records = execute(odps, sql);

        List<Map<String, Object>> datas = new ArrayList<>();
        for (Record record : records) {
            /*获得记录中包含的字段数量*/
            Column[] columns = record.getColumns();
            Map<String, Object> data = new HashMap<>(columns.length);

            for (Column column : columns) {
                String columnName = column.getName();
                Object value = record.get(columnName);
                data.put(columnName, value);
            }

            datas.add(data);
        }

        OdpsDetectExecuteSqlResponse response = new OdpsDetectExecuteSqlResponse();

        response.setRecords(datas);
        logger.info("Odps execute sql completed, result size:[{}] test consume:[{}ms]", datas.size(), System.currentTimeMillis() - startTime);
        return response;
    }


    @Override
    public boolean isConnect(DetectRequest detectRequest) throws DetectorException {
        long startTime = System.currentTimeMillis();

        OdpsDetectRequest request = (OdpsDetectRequest) detectRequest;

        Odps odps = DetectorOdpsUtils.createOdps(request.getAccessId(), request.getAccessKey(), request.getUrl(), request.getProject());

        try {
            /* 执行未出现异常表示连接成功 */
            logger.info("Begin connecting odps......");
            SQLTask.run(odps, OdpsSystemStatement.TEST_SQL);
            logger.info("Odps connection completed, time consume :[{}ms]", System.currentTimeMillis() - startTime);
            return true;
        } catch (OdpsException e) {
            logger.error("Odps connect error, accessId:[{}], accessKey:[{}], url:[{}], project:[{}]",
                    request.getAccessId(), request.getAccessKey(), request.getUrl(), request.getProject(), e);
            throw new DetectorException(String.format("Test odps connect error, test sql:%s", OdpsSystemStatement.TEST_SQL), e);
        }


    }

    @Override
    public DetectorType detectorType() {
        return DetectorType.table;
    }

    @Override
    public DataBaseType databaseType() {
        return DataBaseType.odps;
    }

    /**
     * 构建分区信息
     *
     * @param tableSchema
     * @param tableName
     * @param tableProject
     * @return
     */
    private List<ColumnInfo> buildPartitionInfo(TableSchema tableSchema, String tableName, String tableProject) {
        /* 分区字段信息 */
        List<Column> partitionColumns = tableSchema.getPartitionColumns();

        List<ColumnInfo> partitionFieldInfos = new ArrayList<>(partitionColumns.size());

        for (Column column : partitionColumns) {
            ColumnInfo columnInfo = new ColumnInfo();
            columnInfo.setColumnDefault(column.getDefaultValue());
            columnInfo.setAllowedNull(column.isNullable());
            columnInfo.setDataType(column.getTypeInfo().getTypeName());
            columnInfo.setColumnComment(column.getComment());
            columnInfo.setProject(tableProject);
            columnInfo.setTableName(tableName);
            String fieldName = column.getName();
            columnInfo.setColumnName(fieldName);
            columnInfo.setOrdinalPosition(tableSchema.getPartitionColumnIndex(fieldName));

            partitionFieldInfos.add(columnInfo);
        }
        partitionColumns.clear();

        return partitionFieldInfos;
    }

    /**
     * 构建普通字段信息
     *
     * @param tableSchema
     * @param tableName
     * @param tableProject
     * @return
     */
    private List<ColumnInfo> buildFieldInfo(TableSchema tableSchema, String tableName, String tableProject) {
        /* 字段信息 */
        List<Column> columns = tableSchema.getColumns();
        List<ColumnInfo> fieldInfos = new ArrayList<>(columns.size());
        for (Column column : columns) {
            ColumnInfo fieldInfo = new ColumnInfo();
            fieldInfo.setProject(tableProject);
            fieldInfo.setTableName(tableName);
            String fieldName = column.getName();
            fieldInfo.setColumnName(fieldName);
            fieldInfo.setColumnDefault(column.getDefaultValue());
            fieldInfo.setAllowedNull(column.isNullable());
            fieldInfo.setDataType(column.getTypeInfo().getTypeName());
            fieldInfo.setColumnComment(column.getComment());
            fieldInfo.setOrdinalPosition(tableSchema.getColumnIndex(fieldName));

            fieldInfos.add(fieldInfo);
        }
        columns.clear();

        return fieldInfos;
    }

    /**
     * 执行sql（查询大数据量会失败，后面应该提供，tunnel进行数据拉去）
     *
     * @param odps
     * @param sql
     * @return
     * @throws DetectorException
     */
    private List<Record> execute(Odps odps, String sql) throws DetectorException {
        /* odps任务名 */
        String taskName = new StringBuilder("detector").append(UUID.randomUUID().toString().replace("-", "")).toString();
        try {
            Instance instance = SQLTask.run(odps, odps.getDefaultProject(), sql, taskName, null, null, 3);
            /* 等待执行成功 */
            instance.waitForSuccess();

            Instance.TaskStatus taskStatus = instance.getTaskStatus().get(taskName);

            long costTime = instance.getEndTime().getTime() - instance.getStartTime().getTime();

            switch (taskStatus.getStatus()) {
                case SUCCESS:
                    logger.info("execute sql completed, time consume:[{}ms],taskName:{}, sql:[{}]", costTime, taskName, sql);
                    return SQLTask.getResult(instance, taskName);
                case FAILED:
                case WAITING:
                case RUNNING:
                case SUSPENDED:
                case CANCELLED:
                    logger.info("execute sql {}, time consume:[{}ms], sql:[{}],taskName:{}, result:{}", taskStatus.getName(), costTime, taskName, sql, instance.getTaskResults());
                    return Collections.emptyList();
                default:
                    return Collections.emptyList();
            }


        } catch (OdpsException e) {
            throw new DetectorException(String.format("Odps run sql error, sql:[%s]", sql), e);
        }
    }

    /**
     * 加载指定表
     *
     * @param tableNames 表名
     * @param odps
     * @param project
     * @return
     * @throws DetectorException
     */
    private Iterator<Table> reloadedTables(List<String> tableNames, Odps odps, String project) throws DetectorException {

        if (tableNames == null || tableNames.size() < 1) {
            return Collections.emptyIterator();
        }


        List<Table> tables = new ArrayList<>(100);
        List<Table> reloadedTables = new ArrayList<>(tableNames.size());

        for (String tableName : tableNames) {

            tables.add(odps.tables().get(project, tableName));
            /* odps 批量查询每次只允许100张表 */
            if (tables.size() >= 100) {

                try {
                    reloadedTables.addAll(odps.tables().reloadTables(tables));
                } catch (OdpsException e) {
                    throw new DetectorException("Batch load tables info error", e);
                }
                tables.clear();
            }
        }

        /* 加载剩余表信息 */
        if (tables.size() > 0) {
            try {
                reloadedTables.addAll(odps.tables().reloadTables(tables));
            } catch (OdpsException e) {
                throw new DetectorException("Batch load tables info error", e);
            }
            tables.clear();
        }

        return reloadedTables.iterator();
    }

    private void validation(OdpsDetectRequest request) {
        if (StringUtils.isBlank(request.getAccessId()) || StringUtils.isBlank(request.getAccessKey()) ||
                StringUtils.isBlank(request.getUrl()) || StringUtils.isBlank(request.getProject())) {
            throw new NullPointerException("[accessId][accessKey][url][project] is null");
        }
    }
}
