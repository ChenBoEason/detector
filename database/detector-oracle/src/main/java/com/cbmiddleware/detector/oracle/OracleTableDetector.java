package com.cbmiddleware.detector.oracle;

import com.cbmiddleware.detector.DetectRequest;
import com.cbmiddleware.detector.DetectResponse;
import com.cbmiddleware.detector.DetectorActionListener;
import com.cbmiddleware.detector.constant.DataBaseType;
import com.cbmiddleware.detector.constant.DetectorType;
import com.cbmiddleware.detector.entity.ColumnInfo;
import com.cbmiddleware.detector.entity.TableInfo;
import com.cbmiddleware.detector.entity.Threshold;
import com.cbmiddleware.detector.exception.DetectorException;
import com.cbmiddleware.detector.oracle.constant.OracleCoreConstants;
import com.cbmiddleware.detector.oracle.constant.OracleSystemStatement;
import com.cbmiddleware.detector.util.DataSourceUtils;
import com.cbmiddleware.detector.util.JdbcUtils;
import com.cbmiddleware.detector.AbstractTableDetector;
import com.cbmiddleware.detector.util.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alibaba.druid.util.JdbcConstants.ORACLE;

/**
 * @author Eason(bo.chenb)
 * @description oracle表探测器
 * 探测信息：
 * 1、当前用户可访问权限下表信息
 * 2、表结构字段数据
 * @date 2020-03-09
 **/
@Component("oracleTableDetector")
public class OracleTableDetector extends AbstractTableDetector {

    private static final Logger logger = LoggerFactory.getLogger(OracleTableDetector.class);

    private static volatile boolean isLoadDriver = false;

    @Override
    public DetectResponse detect(DetectRequest detectRequest) throws DetectorException {
        long start = System.currentTimeMillis();
        OracleDetectRequest request = (OracleDetectRequest) detectRequest;

        String username = request.getUsername();
        String password = request.getPassword();
        String port = request.getPort();
        String project = request.getProject();
        String sid = request.getSid();
        String address = request.getAddress();


        logger.info("Oracle table detector username:[{}], password:[{}], project:[{}], host:[{}], port:[{}]", username, password, project, address, port);

        loadDriver(request.getVersion());

        /**
         * 测试连通性
         */
        String connectUrl = JdbcUtils.getOracleConnectUrl(address, port, sid);

        if (request.isTestConnection() && !isConnect(detectRequest)) {
            throw new DetectorException(String.format("Connect oracle database failed, connect url:[%s]", connectUrl));
        }

        String tableSql = null;
        List<String> requestTableNames = request.getTableNames();

        if (requestTableNames != null && requestTableNames.size() > 0) {
            tableSql = String.format(OracleSystemStatement.SOME_TABLES, project, DataSourceUtils.concatTableNames(requestTableNames));
        } else {
            tableSql = String.format(OracleSystemStatement.TABLES, project);
        }

        /**
         * 获取表信息
         */
        logger.info("Begin get oracle table info...");
        long startTableInfoTime = System.currentTimeMillis();

        List<TableInfo> tableInfos = tableInfos(connectUrl, username, password, tableSql);

        logger.info("Get oracle table info completed, table size:[{}], time consume:[{}ms]", tableInfos.size(), System.currentTimeMillis() - startTableInfoTime);


        StringBuilder builder = new StringBuilder();

        List<String> tableNames = new ArrayList<>(tableInfos.size());

        tableInfos.forEach(tableInfo -> {
            tableNames.add(tableInfo.getTableName());
            builder.append("'").append(tableInfo.getTableName()).append("',");
        });

        /**
         * 获取表中字段信息
         */
        String fieldSql = String.format(OracleSystemStatement.TABLE_INFO, builder.substring(0, builder.length() - 1).toUpperCase());
        Map<String, List<ColumnInfo>> mysqlFieldInfo = new HashMap<>();

        logger.info("Begin get oracle table field info...");
        long startTableFieldInfoTime = System.currentTimeMillis();
        try {
            mysqlFieldInfo = JdbcUtils.getOracleFieldInfo(connectUrl, username, password, fieldSql, tableNames);
        } catch (SQLException e) {
            logger.error("Get oracle table field information error, sql :[{}]", fieldSql, e);
            throw new DetectorException(String.format("Get oracle table field information error, sql:[%s]", fieldSql), e);
        }
        logger.info("Get oracle table field info completed, time consume:[{}ms]", System.currentTimeMillis() - startTableFieldInfoTime);

        Map<String, TableInfo> tableInfoMap = new HashMap<>();
        /* 封装表信息 */
        for (TableInfo tableInfo : tableInfos) {

            String tableName = tableInfo.getTableName();
            tableInfo.setFieldInfos(mysqlFieldInfo.get(tableName));
            tableInfoMap.put(tableName, tableInfo);
        }

        OracleDetectResponse response = new OracleDetectResponse();
        response.setTableInfos(tableInfoMap);
        logger.info("Oracle table detect completed, test consume:[{}ms]", System.currentTimeMillis() - start);
        return response;
    }

    private List<TableInfo> tableInfos(String connectUrl, String username, String password, String tableSql)
            throws DetectorException {

        try {
            return JdbcUtils.getTableInfos(connectUrl, username, password, tableSql);
        } catch (SQLException e) {
            logger.error("Get oracle table information error, sql :[{}]", tableSql, e);
            throw new DetectorException(String.format("Get oracle table information error, sql:[%s]", tableSql), e);
        }
    }

    @Override
    public void detect(DetectRequest detectRequest, Threshold threshold, DetectorActionListener<DetectResponse> listener) throws DetectorException {
        detectSync(detectRequest, threshold, listener, new OracleDetectResponse());
    }

    @Override
    public void detectAsync(DetectRequest detectRequest, Threshold threshold, DetectorActionListener<DetectResponse> listener) throws DetectorException {

    }

    @Override
    public boolean isConnect(DetectRequest detectRequest) throws DetectorException {
        long startTime = System.currentTimeMillis();
        OracleDetectRequest request = (OracleDetectRequest) detectRequest;

        loadDriver(request.getVersion());

        String connectUrl = JdbcUtils.getOracleConnectUrl(request.getAddress(), request.getPort(), request.getSid());
        try {
            logger.info("Begin connecting oracle......");
            boolean isConnection = JdbcUtils.isConnection(connectUrl, request.getUsername(), request.getPassword(), OracleSystemStatement.TEST_SQL);
            logger.info("Oracle connection completed, time consume :[{}ms]", System.currentTimeMillis() - startTime);
            return isConnection;
        } catch (SQLException e) {
            logger.error("Test oracle connect error, test sql:[{}] connect url :[{}]", OracleSystemStatement.TEST_SQL, connectUrl, e);
            throw new DetectorException(String.format("Test oracle connect error, test sql:%s connect url:[%s]", OracleSystemStatement.TEST_SQL, connectUrl), e);
        }
    }

    @Override
    public DetectResponse detectTableSize(DetectRequest detectRequest, List<String> tableNames) throws DetectorException {
        long start = System.currentTimeMillis();
        OracleDetectRequest request = (OracleDetectRequest) detectRequest;

        if (tableNames == null || tableNames.size() < 1) {
            throw new NullPointerException("tables is empty");
        }

        String username = request.getUsername();
        String password = request.getPassword();
        String project = request.getProject();
        String address = request.getAddress();
        String port = request.getPort();
        String sid = request.getSid();


        logger.info("Oracle table size username:[{}], password:[{}], project:[{}], host:[{}], port:[{}], tables:{}",
                username, password, project, address, port, tableNames);

        loadDriver(request.getVersion());

        /**
         * 测试连通性
         */
        String connectUrl = JdbcUtils.getOracleConnectUrl(address, port, sid);

        if (request.isTestConnection() && !isConnect(detectRequest)) {
            throw new DetectorException(String.format("Connect oracle database failed, connect url:[%s]", connectUrl));
        }

        String tableSizeSql = String.format(OracleSystemStatement.TABLE_SIZE, project, DataSourceUtils.concatTableNames(tableNames));
        List<TableInfo> tableInfos = null;
        try {
            tableInfos = JdbcUtils.getTableSize(connectUrl, username, password, tableSizeSql);
        } catch (SQLException e) {
            logger.error("Get oracle table size information error, sql :[{}]", tableSizeSql, e);
            throw new DetectorException(String.format("Get oracle table field information error, sql:[%s]", tableSizeSql), e);
        }

        Map<String, TableInfo> tableInfoMap = new HashMap<>();

        for (TableInfo tableInfo : tableInfos) {
            tableInfoMap.put(tableInfo.getTableName(), tableInfo);
        }

        OracleDetectTableSizeResponse response = new OracleDetectTableSizeResponse();
        response.setTableInfos(tableInfoMap);
        logger.info("Get oracle table size completed, test consume:[{}ms]", System.currentTimeMillis() - start);
        return response;
    }

    @Override
    public DetectResponse executeSql(DetectRequest detectRequest, String sql) throws DetectorException {

        sql = DataSourceUtils.formatSql(sql, ORACLE);

        long start = System.currentTimeMillis();
        OracleDetectRequest request = (OracleDetectRequest) detectRequest;

        String sid = request.getSid();
        String username = request.getUsername();
        String address = request.getAddress();
        String password = request.getPassword();
        String project = request.getProject();
        String port = request.getPort();

        logger.info("execute sql username:[{}], password:[{}], project:[{}], host:[{}], port:[{}]", username, password, project, address, port);

        loadDriver(request.getVersion());

        /**
         * 测试连通性
         */
        String connectUrl = JdbcUtils.getOracleConnectUrl(address, port, sid);

        if (request.isTestConnection() && !isConnect(detectRequest)) {
            throw new DetectorException(String.format("Connect oracle database failed, connect url:[%s]", connectUrl));
        }

        logger.info("start execute oracle sql\n{}", sql);
        List<Map<String, Object>> records = JdbcUtils.query(username, password, connectUrl, sql);

        OracleDetectExecuteSqlResponse response = new OracleDetectExecuteSqlResponse();

        response.setRecords(records);
        logger.info("execute oracle sql completed, time consumer:[{}ms], record size:[{}]", System.currentTimeMillis() - start, records.size());
        return response;
    }


    @Override
    public DetectorType detectorType() {
        return DetectorType.table;
    }

    @Override
    public DataBaseType databaseType() {
        return DataBaseType.oracle;
    }

    public void detectSync(DetectRequest detectRequest, Threshold threshold, DetectorActionListener<DetectResponse> listener,
                           OracleDetectResponse response) throws DetectorException {

        long start = System.currentTimeMillis();
        OracleDetectRequest request = (OracleDetectRequest) detectRequest;

        String username = request.getUsername();
        String password = request.getPassword();
        String project = request.getProject();
        String address = request.getAddress();
        String port = request.getPort();
        String sid = request.getSid();


        logger.info("Oracle table detector username:[{}], password:[{}], project:[{}], host:[{}], port:[{}]", username, password, project, address, port);

        loadDriver(request.getVersion());

        /**
         * 测试连通性
         */
        String connectUrl = JdbcUtils.getOracleConnectUrl(address, port, sid);

        if (request.isTestConnection() && !isConnect(detectRequest)) {
            throw new DetectorException(String.format("Connect oracle database failed, connect url:[%s]", connectUrl));
        }
        List<String> requestTableNames = request.getTableNames();
        String tableSql = null;
        if (requestTableNames != null && requestTableNames.size() > 0) {
            tableSql = String.format(OracleSystemStatement.SOME_TABLES, project, DataSourceUtils.concatTableNames(requestTableNames));
        } else {
            tableSql = String.format(OracleSystemStatement.TABLES, project);
        }

        /**
         * 获取表信息
         */

        Map<String, TableInfo> tableInfoMap = new HashMap<>();
        response.setTableInfos(tableInfoMap);

        logger.info("Begin get oracle table info...");
        long startTableInfoTime = System.currentTimeMillis();

        List<TableInfo> tableInfos = tableInfos(connectUrl, username, password, tableSql);

        logger.info("Get oracle table info completed, table size:[{}], time consume:[{}ms]", tableInfos.size(), System.currentTimeMillis() - startTableInfoTime);

        List<String> tableNames = new ArrayList<>(tableInfos.size());

        StringBuilder builder = new StringBuilder();

        final Integer thresholdTableCount = Validator.validatInteger(threshold.getTableCount(), Integer.MAX_VALUE);
        final Integer thresholdColumnCount = Validator.validatInteger(threshold.getColumnCount(), Integer.MAX_VALUE);

        /* 记录 列数和表数据量 */
        long totalTableCount = 0, totalColumnCount = 0;
        long startReadTableTime = System.currentTimeMillis();

        Long columnCount = 0L, tableCount = 0L;

        for (int i = 0; i < tableInfos.size(); i++) {

            TableInfo tableInfo = tableInfos.get(i);

            tableNames.add(tableInfo.getTableName());
            builder.append("'").append(tableInfo.getTableName()).append("',");

            boolean flag = (thresholdColumnCount <= columnCount || thresholdTableCount <= tableCount) && listener != null;
            if (flag) {

                getColumns(request, connectUrl, tableNames, builder, tableInfos, tableInfoMap, tableCount, columnCount);

                long startInvokeTime = System.currentTimeMillis();
                listener.onResponse(response);
                logger.info("Completed invoke listener onResponse method, time consume:[{}ms]", System.currentTimeMillis() - startInvokeTime);
                /* 清空已返回的数据 */
                tableInfoMap.clear();
                totalTableCount += tableCount;
                totalColumnCount += columnCount;
                columnCount = 0L;
                tableCount = 0L;

            }

            if (tableCount > 0 && tableCount % 100 == 0) {
                getColumns(request, connectUrl, tableNames, builder, tableInfos, tableInfoMap, tableCount, columnCount);
                totalTableCount += tableCount;
                totalColumnCount += columnCount;
            }

        }

        if (tableNames.size() > 0) {
            getColumns(request, connectUrl, tableNames, builder, tableInfos, tableInfoMap, tableCount, columnCount);
            totalTableCount += tableCount;
            totalColumnCount += columnCount;
        }

        if (listener != null) {
            listener.onResponse(response);
        }

        logger.info("Get oracle table and field info completed, total table size:[{}], total column size:[{}], time consume:[{}ms]", totalTableCount, totalColumnCount, System.currentTimeMillis() - startReadTableTime);
        logger.info("Oracle table detect completed, test consume:[{}ms]", System.currentTimeMillis() - start);

    }

    private void getColumns(OracleDetectRequest request, String connectUrl, List<String> tableNames, StringBuilder builder,
                            List<TableInfo> tableInfos, Map<String, TableInfo> tableInfoMap, long tableCount, long columnCount) throws DetectorException {

        String username = request.getUsername();
        String password = request.getPassword();
        /**
         * 获取表中字段信息
         */
        String fieldSql = String.format(OracleSystemStatement.TABLE_INFO, builder.substring(0, builder.length() - 1).toUpperCase());
        Map<String, List<ColumnInfo>> mysqlFieldInfo = new HashMap<>();

        logger.info("Begin get oracle table column info...");
        long startTableFieldInfoTime = System.currentTimeMillis();
        try {
            mysqlFieldInfo = JdbcUtils.getOracleFieldInfo(connectUrl, username, password, fieldSql, tableNames);
        } catch (SQLException e) {
            logger.error("Get oracle table column information error, sql :[{}]", fieldSql, e);
            throw new DetectorException(String.format("Get oracle table column information error, sql:[%s]", fieldSql), e);
        }
        logger.info("Get oracle table column info completed, time consume:[{}ms]", System.currentTimeMillis() - startTableFieldInfoTime);


        /* 封装表信息 */
        for (TableInfo tableInfo : tableInfos) {

            String tableName = tableInfo.getTableName();
            List<ColumnInfo> columnInfos = mysqlFieldInfo.get(tableName);

            tableInfo.setFieldInfos(columnInfos);
            tableInfoMap.put(tableName, tableInfo);
            ++tableCount;
            columnCount += columnInfos.size();
        }
    }

    private void loadDriver(String version) throws DetectorException {
        if (!isLoadDriver) {
            synchronized (this) {
                if (!isLoadDriver) {
                    try {
                        Class.forName(OracleCoreConstants.DRIVER);
                        isLoadDriver = true;
                        logger.info("oracle driver loaded, driver:[{}] oracle version:[{}]", OracleCoreConstants.DRIVER, version);
                    } catch (ClassNotFoundException e) {
                        logger.error("Oracle table detector load driver error", e);
                        throw new DetectorException("Load oracle driver failed", e);
                    }
                }
            }
        }

    }
}
