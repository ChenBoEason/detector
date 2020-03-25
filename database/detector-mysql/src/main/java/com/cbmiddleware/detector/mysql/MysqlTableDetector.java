package com.cbmiddleware.detector.mysql;

import com.cbmiddleware.detector.DetectResponse;
import com.cbmiddleware.detector.DetectorActionListener;
import com.cbmiddleware.detector.constant.DataBaseType;
import com.cbmiddleware.detector.entity.Threshold;
import com.cbmiddleware.detector.mysql.constant.MysqlCoreConstants;
import com.cbmiddleware.detector.DetectRequest;
import com.cbmiddleware.detector.AbstractTableDetector;
import com.cbmiddleware.detector.constant.DetectorType;
import com.cbmiddleware.detector.entity.ColumnInfo;
import com.cbmiddleware.detector.entity.TableInfo;
import com.cbmiddleware.detector.exception.DetectorException;
import com.cbmiddleware.detector.mysql.constant.MysqlSystemStatement;
import com.cbmiddleware.detector.util.DataSourceUtils;
import com.cbmiddleware.detector.util.JdbcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.*;

import static com.alibaba.druid.util.JdbcConstants.MYSQL;
import static com.cbmiddleware.detector.util.JdbcUtils.*;

/**
 * @author Eason(bo.chenb)
 * @description mysql表结构探测器
 * 探测信息：
 * 1、当前用户可访问权限下表信息
 * 2、表结构字段数据
 * @date 2020-03-06
 **/
@Component("mysqlTableDetector")
public class MysqlTableDetector extends AbstractTableDetector {

    private static final Logger logger = LoggerFactory.getLogger(MysqlTableDetector.class);

    private static volatile boolean isLoadDriver = false;


    @Override
    public MysqlDetectResponse detect(DetectRequest detectRequest) throws DetectorException {
        long start = System.currentTimeMillis();
        MysqlDetectRequest request = (MysqlDetectRequest) detectRequest;

        String username = request.getUsername();
        String password = request.getPassword();
        String project = request.getProject();
        String address = request.getAddress();
        String port = request.getPort();

        logger.info("Mysql table detector username:[{}], password:[{}], project:[{}], host:[{}], port:[{}]", username, password, project, address, port);
        /* 加载驱动*/
        loadDriver(request.getVersion());

        /**
         * 测试连通性
         */
        String connectUrl = getMysqlConnectUrl(address, port, project);

        if (request.isTestConnection() && !isConnect(detectRequest)) {
            throw new DetectorException(String.format("Connect mysql database failed, connect url:[%s]", connectUrl));
        }

        /**
         * 获取表信息
         */

        List<String> requestTableNames = request.getTableNames();
        String tableSql = null;
        if (requestTableNames != null && requestTableNames.size() > 0) {
            tableSql = String.format(MysqlSystemStatement.SOME_TABLES, project, DataSourceUtils.concatTableNames(requestTableNames));
        } else {
            tableSql = String.format(MysqlSystemStatement.TABLES, project);
        }

        List<TableInfo> tableInfos = null;

        logger.info("Begin get mysql table info...");
        long startTableInfoTime = System.currentTimeMillis();
        try {
            tableInfos = JdbcUtils.getTableInfos(connectUrl, username, password, tableSql);
        } catch (SQLException e) {
            logger.error("Get mysql table information error, sql :[{}]", tableSql, e);
            throw new DetectorException(String.format("Get mysql table information error, sql:[%s]", tableSql), e);
        }
        logger.info("Get mysql table info completed, table size:[{}], time consume:[{}ms]", tableInfos.size(), System.currentTimeMillis() - startTableInfoTime);

        List<String> tableNames = new ArrayList<>(tableInfos.size());

        StringBuilder builder = new StringBuilder();

        tableInfos.forEach(tableInfo -> {
            tableNames.add(tableInfo.getTableName());
            builder.append("'").append(tableInfo.getTableName()).append("',");
        });

        /**
         * 获取表中字段信息
         */
        String fieldSql = String.format(MysqlSystemStatement.TABLE_INFO, builder.substring(0, builder.length() - 1).toUpperCase());
        Map<String, List<ColumnInfo>> mysqlFieldInfo = new HashMap<>();

        logger.info("Begin get mysql table field info...");
        long startTableFieldInfoTime = System.currentTimeMillis();
        try {
            mysqlFieldInfo = JdbcUtils.getMysqlFieldInfo(connectUrl, username, password, fieldSql, tableNames);
        } catch (SQLException e) {
            logger.error("Get mysql table field information error, sql :[{}]", fieldSql, e);
            throw new DetectorException(String.format("Get mysql table field information error, sql:[%s]", fieldSql), e);
        }
        logger.info("Get mysql table field info completed, time consume:[{}ms]", System.currentTimeMillis() - startTableFieldInfoTime);

        Map<String, TableInfo> tableInfoMap = new HashMap<>();
        for (TableInfo tableInfo : tableInfos) {

            String tableName = tableInfo.getTableName();
            tableInfo.setFieldInfos(mysqlFieldInfo.get(tableName));
            tableInfoMap.put(tableName, tableInfo);
        }

        MysqlDetectResponse response = new MysqlDetectResponse();
        response.setTableInfos(tableInfoMap);
        logger.info("Mysql table detect completed, test consume:[{}ms]", System.currentTimeMillis() - start);
        return response;
    }

    @Override
    public void detect(DetectRequest detectRequest, Threshold threshold, DetectorActionListener<DetectResponse> listener) throws DetectorException {

    }

    @Override
    public void detectAsync(DetectRequest detectRequest, Threshold threshold, DetectorActionListener<DetectResponse> listener) throws DetectorException {

    }

    @Override
    public boolean isConnect(DetectRequest detectRequest) throws DetectorException {
        long startTime = System.currentTimeMillis();
        MysqlDetectRequest request = (MysqlDetectRequest) detectRequest;

        loadDriver(request.getVersion());

        String connectUrl = getMysqlConnectUrl(request.getAddress(), request.getPort(), request.getProject());
        logger.info("Begin connecting mysql......");
        try {
            boolean isConnect = isConnection(connectUrl, request.getUsername(), request.getPassword(), MysqlSystemStatement.TEST_SQL);
            logger.info("Mysql connection completed, time consume :[{}ms]", System.currentTimeMillis() - startTime);
            return isConnect;
        } catch (SQLException e) {
            logger.error("Test mysql connect error, test sql:{} connect url :[{}]", MysqlSystemStatement.TEST_SQL, connectUrl, e);
            throw new DetectorException(String.format("Test mysql connect error, test sql:%s connect url:[%s]", MysqlSystemStatement.TEST_SQL, connectUrl), e);
        }

    }


    @Override
    public DetectResponse detectTableSize(DetectRequest detectRequest, List<String> tableNames) throws DetectorException {
        long start = System.currentTimeMillis();
        MysqlDetectRequest request = (MysqlDetectRequest) detectRequest;

        if (tableNames == null || tableNames.size() < 1) {
            throw new NullPointerException("tables is empty");
        }
        String username = request.getUsername();
        String password = request.getPassword();
        String project = request.getProject();
        String address = request.getAddress();
        String port = request.getPort();

        logger.info("Mysql table size username:[{}], password:[{}], project:[{}], host:[{}], port:[{}], tables:{}", username, password, project, address, port, tableNames);
        /* 加载驱动*/
        loadDriver(request.getVersion());

        /**
         * 测试连通性
         */
        String connectUrl = getMysqlConnectUrl(address, port, project);

        if (request.isTestConnection() && !isConnect(detectRequest)) {
            throw new DetectorException(String.format("Connect mysql database failed, connect url:[%s]", connectUrl));
        }

        String tableSizeSql = String.format(MysqlSystemStatement.TABLE_SIZE, project, DataSourceUtils.concatTableNames(tableNames));
        List<TableInfo> tableInfos = null;
        try {
            tableInfos = JdbcUtils.getTableSize(connectUrl, username, password, tableSizeSql);
        } catch (SQLException e) {
            logger.error("Get mysql table size information error, sql :[{}]", tableSizeSql, e);
            throw new DetectorException(String.format("Get mysql table field information error, sql:[%s]", tableSizeSql), e);
        }

        Map<String, TableInfo> tableInfoMap = new HashMap<>();

        for (TableInfo tableInfo : tableInfos) {
            tableInfoMap.put(tableInfo.getTableName(), tableInfo);
        }

        MysqlDetectTableSizeResponse response = new MysqlDetectTableSizeResponse();
        response.setTableInfos(tableInfoMap);
        logger.info("Get mysql table size completed, test consume:[{}ms]", System.currentTimeMillis() - start);
        return response;
    }

    @Override
    public DetectResponse executeSql(DetectRequest detectRequest, String sql) throws DetectorException {
        long start = System.currentTimeMillis();
        sql = DataSourceUtils.formatSql(sql, MYSQL);

        MysqlDetectRequest request = (MysqlDetectRequest) detectRequest;

        String address = request.getAddress();
        String port = request.getPort();
        String username = request.getUsername();
        String password = request.getPassword();
        String project = request.getProject();


        logger.info("execute mysql sql, username:[{}], password:[{}], project:[{}], host:[{}], port:[{}]", username, password, project, address, port);
        /* 加载驱动*/
        loadDriver(request.getVersion());

        /**
         * 测试连通性
         */
        String connectUrl = getMysqlConnectUrl(address, port, project);

        if (request.isTestConnection() && !isConnect(detectRequest)) {
            throw new DetectorException(String.format("Connect mysql database failed, connect url:[%s]", connectUrl));
        }

        logger.info("start execute sql\n{}", sql);
        List<Map<String, Object>> records = JdbcUtils.query(username, password, connectUrl, sql);

        MysqlDetectExecuteSqlResponse response = new MysqlDetectExecuteSqlResponse();

        response.setRecords(records);
        logger.info("execute mysql sql completed, time consumer:[{}ms], record size:[{}]", System.currentTimeMillis() - start, records.size());
        return response;
    }

    @Override
    public DetectorType detectorType() {
        return DetectorType.table;
    }

    @Override
    public DataBaseType databaseType() {
        return DataBaseType.mysql;
    }

    private void loadDriver(String version) throws DetectorException {
        if (!isLoadDriver) {
            synchronized (this) {
                if (!isLoadDriver) {
                    try {
                        Class.forName(MysqlCoreConstants.DRIVER);
                        isLoadDriver = true;
                        logger.info("mysql driver loaded, driver:[{}] mysql version:[{}]", MysqlCoreConstants.DRIVER, version);
                    } catch (ClassNotFoundException e) {
                        logger.error("Mysql table detector load driver error", e);
                        throw new DetectorException("Load mysql driver failed", e);
                    }
                }
            }
        }

    }
}
