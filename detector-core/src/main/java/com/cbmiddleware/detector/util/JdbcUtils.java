package com.cbmiddleware.detector.util;


import com.cbmiddleware.detector.constant.DataBaseType;
import com.cbmiddleware.detector.constant.DetectorConstants;
import com.cbmiddleware.detector.constant.RdsCoreKey;
import com.cbmiddleware.detector.entity.TableInfo;
import com.cbmiddleware.detector.entity.ColumnInfo;
import com.cbmiddleware.detector.exception.DetectorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Eason(bo.chenb)
 * @description
 * @date 2020-03-06
 **/
public class JdbcUtils {

    private static final Logger logger = LoggerFactory.getLogger(JdbcUtils.class);

    /**
     * 获取数据库连接
     *
     * @param url      连接地址
     * @param username 用户名
     * @param password 密码
     * @return
     * @throws Exception
     */
    public static Connection getConnection(String url, String username, String password) throws SQLException {
        return getConnection(url, username, password, 5);
    }

    /**
     * 获取数据库连接
     *
     * @param url      连接地址
     * @param username 用户名
     * @param password 密码
     * @param timeout  超时时间 单位秒
     * @return
     * @throws SQLException
     */
    public static Connection getConnection(String url, String username, String password, int timeout) throws SQLException {
        /* 设置超时时间 */
        DriverManager.setLoginTimeout(timeout);
        return DriverManager.getConnection(url, username, password);
    }

    /**
     * @param url
     * @param username
     * @param password
     * @return
     */
    public static boolean isConnection(String url, String username, String password, String testSql) throws SQLException {
        return isConnection(url, username, password, testSql, 5);
    }

    public static boolean isConnection(String url, String username, String password, String testSql, int timeout) throws SQLException {
        Connection connection = null;
        try {

            connection = getConnection(url, username, password, timeout);

            connection.prepareStatement(testSql);

            return true;
        } finally {
            closeConnection(connection, null, null);
        }
    }


    /**
     * 获取当前用户下的所有表名
     *
     * @param connectUrl
     * @param username
     * @param password
     * @return
     */
    public static List<TableInfo> getTableInfos(String connectUrl, String username, String password, String tableSql) throws SQLException {
        List<TableInfo> tableInfos = new ArrayList<>();
        Connection connection = null;
        ResultSet resultSet = null;

        try {
            /* 获取连接 */
            connection = getConnection(connectUrl, username, password);

            resultSet = connection.prepareStatement(tableSql).executeQuery();
            /* 获取表名以及相关信息 */
            while (resultSet.next()) {

                TableInfo tableInfo = new TableInfo();
                tableInfo.setProject(resultSet.getString(RdsCoreKey.project));
                tableInfo.setTableOwner(resultSet.getString(RdsCoreKey.table_owner));
                tableInfo.setRemark(resultSet.getString(RdsCoreKey.table_remark));
                tableInfo.setTableCharsetName(resultSet.getString(RdsCoreKey.table_char_name));
                Timestamp createTime = resultSet.getTimestamp(RdsCoreKey.table_create_time);
                if (createTime != null) {
                    tableInfo.setTableCreateTime(new Date(createTime.getTime()));
                }
                Timestamp updateTime = resultSet.getTimestamp(RdsCoreKey.table_update_time);
                if (updateTime != null) {
                    tableInfo.setTableUpdateTime(new Date(updateTime.getTime()));
                }

                tableInfo.setTableName(resultSet.getString(RdsCoreKey.table_name));
                tableInfo.setTableType(resultSet.getString(RdsCoreKey.table_type));
                tableInfo.setTableRows(resultSet.getLong(RdsCoreKey.table_rows));
                tableInfo.setTableStorageSize(resultSet.getLong(RdsCoreKey.table_storage_size));
                tableInfo.setTablePhysicalSize(resultSet.getLong(RdsCoreKey.table_physical_size));
                tableInfo.setStorageUnit("byte");

                tableInfos.add(tableInfo);
            }
            return tableInfos;
        } finally {
            closeConnection(connection, null, resultSet);
        }

    }

    public static List<TableInfo> getTableSize(String connectUrl, String username, String password, String tableSizeSql) throws SQLException {

        List<TableInfo> tableInfos = new ArrayList<>();
        Connection connection = null;
        ResultSet resultSet = null;

        try {
            /* 获取连接 */
            connection = getConnection(connectUrl, username, password);

            resultSet = connection.prepareStatement(tableSizeSql).executeQuery();

            while (resultSet.next()) {
                TableInfo tableInfo = new TableInfo();
                tableInfo.setProject(resultSet.getString(RdsCoreKey.project));
                tableInfo.setTableName(resultSet.getString(RdsCoreKey.table_name));
                tableInfo.setTableRows(resultSet.getLong(RdsCoreKey.table_rows));
                tableInfo.setTableStorageSize(resultSet.getLong(RdsCoreKey.table_storage_size));
                tableInfo.setTablePhysicalSize(resultSet.getLong(RdsCoreKey.table_physical_size));
                tableInfo.setStorageUnit("byte");
                tableInfos.add(tableInfo);
            }
            return tableInfos;
        } finally {
            closeConnection(connection, null, resultSet);
        }
    }

    public static Map<String, Map<String, String>> getOracleColumnNameAndComments(String connectUrl, String username, String password, List<String> tableNames)
            throws SQLException {
        Map<String, Map<String, String>> result = new HashMap<>();

        StringBuilder builder = new StringBuilder();

        tableNames.forEach(tableName -> {
            builder.append("'").append(tableName).append("',");
        });
        String sql = new StringBuilder("select TABLE_NAME, COLUMN_NAME, COMMENTS from user_col_comments where Table_Name in (")
                .append(builder.substring(0, builder.length() - 1).toUpperCase())
                .append(")").toString();


        Connection connection = null;
        ResultSet resultSet = null;
        try {
            /* 获取连接 */
            connection = getConnection(connectUrl, username, password);

            resultSet = connection.prepareStatement(sql).executeQuery();

            while (resultSet.next()) {
                String columnName = resultSet.getString("COLUMN_NAME");
                String comments = resultSet.getString("COMMENTS");
                String tableName = resultSet.getString("TABLE_NAME");

                Map<String, String> remarks = result.get(tableName);
                if (remarks == null) {
                    remarks = new HashMap<>();
                    result.put(tableName, remarks);
                }
                remarks.put(columnName, comments);
            }

            return result;
        } finally {
            closeConnection(connection, null, resultSet);

        }
    }


    public static Map<String, List<ColumnInfo>> getOracleFieldInfo(String connectUrl, String username, String password,
                                                                   String sql, List<String> tableNames) throws SQLException {
        return getFieldInfo(connectUrl, username, password, sql, tableNames, DataBaseType.oracle);
    }

    public static Map<String, List<ColumnInfo>> getMysqlFieldInfo(String connectUrl, String username, String password,
                                                                  String sql, List<String> tableNames) throws SQLException {
        return getFieldInfo(connectUrl, username, password, sql, tableNames, DataBaseType.mysql);
    }

    /**
     * 获取表中所有的字段详细信息
     *
     * @param connectUrl   连接地址
     * @param username     用户名
     * @param password     密码
     * @param sql          查询字段信息sql
     * @param tableNames   表名
     * @param dataBaseType 探测类型 只支持mysql oracle
     * @return
     * @throws SQLException
     */
    private static Map<String, List<ColumnInfo>> getFieldInfo(String connectUrl, String username, String password, String sql,
                                                              List<String> tableNames, DataBaseType dataBaseType) throws SQLException {

        Map<String, List<ColumnInfo>> tableFieldInfos = new HashMap<>();

        ResultSet resultSet = null;

        boolean isMysql = DataBaseType.mysql == dataBaseType;
        Connection connection = null;
        try {
            /* 获取连接 */
            connection = getConnection(connectUrl, username, password);

            resultSet = connection.prepareStatement(sql).executeQuery();

            Map<String, Map<String, String>> comments = new HashMap<>();
            if (!isMysql) {
                comments = getOracleColumnNameAndComments(connectUrl, username, password, tableNames);
            }


            while (resultSet.next()) {
                ColumnInfo fieldInfo = new ColumnInfo();


                String tableName = String.valueOf(resultSet.getObject("TABLE_NAME"));
                fieldInfo.setTableName(tableName);

                List<ColumnInfo> fieldInfos = tableFieldInfos.get(tableName);
                if (fieldInfos == null) {
                    fieldInfos = new ArrayList<>();
                    tableFieldInfos.put(tableName, fieldInfos);
                }

                fieldInfo.setProject(String.valueOf(resultSet.getObject(isMysql ? "TABLE_SCHEMA" : "OWNER")));

                String columnName = String.valueOf(resultSet.getObject("COLUMN_NAME"));
                fieldInfo.setColumnName(columnName);
                fieldInfo.setOrdinalPosition(Integer.parseInt(resultSet.getObject(isMysql ? "ORDINAL_POSITION" : "COLUMN_ID").toString()));
                fieldInfo.setColumnDefault(String.valueOf(resultSet.getObject(isMysql ? "COLUMN_DEFAULT" : "DATA_DEFAULT")));
                String nullable = String.valueOf(resultSet.getObject(isMysql ? "IS_NULLABLE" : "NULLABLE"));
                if ("YES".equals(nullable) || "Y".equals(nullable)) {
                    fieldInfo.setAllowedNull(true);
                } else {
                    fieldInfo.setAllowedNull(false);
                }

                fieldInfo.setDataType(String.valueOf(resultSet.getObject("DATA_TYPE")));

                if (isMysql) {
                    fieldInfo.setCharacterMaxLength(String.valueOf(resultSet.getObject("CHARACTER_MAXIMUM_LENGTH")));
                    fieldInfo.setCharsetName(String.valueOf(resultSet.getObject("CHARACTER_SET_NAME")));
                    fieldInfo.setCharsetValue(String.valueOf(resultSet.getObject("COLLATION_NAME")));
                    fieldInfo.setColumnType(String.valueOf(resultSet.getObject("COLUMN_TYPE")));
                    fieldInfo.setColumnKey(String.valueOf(resultSet.getObject("COLUMN_KEY")));
                    fieldInfo.setExtra(String.valueOf(resultSet.getObject("EXTRA")));
                    fieldInfo.setColumnComment(String.valueOf(resultSet.getObject("COLUMN_COMMENT")));
                } else {
                    Map<String, String> remarks = comments.get(tableName);
                    if (remarks != null) {
                        fieldInfo.setColumnComment(remarks.get(columnName));
                    }

                }

                fieldInfos.add(fieldInfo);

            }

            return tableFieldInfos;
        } finally {
            closeConnection(connection, null, resultSet);
        }
    }


    /**
     * 关闭连接
     */
    public static void closeConnection(Connection conn, PreparedStatement statement, ResultSet resultSet) {


        if (resultSet != null) {

            try {

                if (resultSet.isClosed()) {
                    return;
                }

                resultSet.close();
            } catch (SQLException e) {
                logger.error("ResultSet close error", e);
            }
        }

        if (statement != null) {
            try {

                if (statement.isClosed()) {
                    return;
                }

                statement.close();
            } catch (SQLException e) {
                logger.error("PreparedStatement close error", e);
            }
        }

        if (conn != null) {
            try {

                if (conn.isClosed()) {
                    return;
                }

                conn.close();
            } catch (SQLException e) {
                logger.error("Connection close error", e);
            }
        }
    }


    /**
     * @param address 连接主机地址hostname
     * @param port    端口
     * @param project 连接库名
     * @return
     */
    public static String getMysqlConnectUrl(String address, String port, String project) {
        return new StringBuilder(DetectorConstants.MYSQL_URL_PREFIX)
                .append(address)
                .append(DetectorConstants.SIGN_COLON)
                .append(port)
                .append(DetectorConstants.SIGN_SLASH)
                .append(project)
                .append(DetectorConstants.MYSQL_SUFFIX).toString();
    }

    /**
     * oracle 连接url
     *
     * @param address 连接主机地址hostname
     * @param port    端口
     * @param sid     oracle sid
     * @return
     */
    public static String getOracleConnectUrl(String address, String port, String sid) {
        return new StringBuilder(DetectorConstants.ORACLE_URL_PREFIX)
                .append(address)
                .append(DetectorConstants.SIGN_COLON)
                .append(port)
                .append(DetectorConstants.SIGN_COLON)
                .append(sid).toString();
    }

    /**
     * 查询
     *
     * @param username
     * @param password
     * @param connectUrl
     * @param sql
     */
    public static List<Map<String, Object>> query(String username, String password, String connectUrl, String sql) throws DetectorException {

        List<Map<String, Object>> records = new ArrayList<>();

        Connection connection = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection(connectUrl, username, password);
            resultSet = query(connection, sql, RdsCoreKey.FETCH_SIZE, RdsCoreKey.QUERY_TIMEOUT);

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnNumber = metaData.getColumnCount();
            while (resultSet.next()) {
                Map<String, Object> record = buildRecord(resultSet, columnNumber, metaData);

                if (record != null) {
                    records.add(record);
                }
            }

            return records;
        } catch (SQLException e) {
            logger.error("query data is error\n{}", sql, e);
            throw new DetectorException(String.format("query data is error, sql\n%s", sql), e);
        } finally {
            closeConnection(connection, null, resultSet);
        }

    }

    private static Map<String, Object> buildRecord(ResultSet rs, int columnNumber, ResultSetMetaData metaData) {

        Map<String, Object> record = new HashMap<>(columnNumber);


        try {
            for (int i = 1; i <= columnNumber; i++) {
                String columnName = metaData.getColumnName(i);

                boolean containsKey = record.containsKey(columnName);

                if(containsKey){
                    String tableName = metaData.getTableName(i);
                    columnName = new StringBuilder(tableName).append(".").append(columnName).toString();
                }

                switch (metaData.getColumnType(i)) {

                    case Types.CHAR:
                    case Types.NCHAR:
                    case Types.VARCHAR:
                    case Types.LONGVARCHAR:
                    case Types.NVARCHAR:
                    case Types.LONGNVARCHAR:
                    case Types.CLOB:
                    case Types.NCLOB:
                        record.put(columnName, rs.getString(i));
                        break;

                    case Types.SMALLINT:
                    case Types.TINYINT:
                    case Types.INTEGER:
                    case Types.BIGINT:

                        String value = rs.getString(i);
                        if (value != null && value.trim().length() > 0) {
                            BigInteger rawData = new BigDecimal(value).toBigInteger();
                            record.put(columnName, rawData.longValue());
                        } else {
                            record.put(columnName, null);
                        }


                        break;

                    case Types.NUMERIC:
                    case Types.DECIMAL:
                        value = rs.getString(i);
                        if (value != null && value.trim().length() > 0) {
                            record.put(columnName, new BigDecimal(value));
                        } else {
                            record.put(columnName, null);
                        }

                        break;

                    case Types.FLOAT:
                    case Types.REAL:
                    case Types.DOUBLE:
                        value = rs.getString(i);
                        if (value != null && value.trim().length() > 0) {
                            BigDecimal bigDecimal = new BigDecimal(value);
                            record.put(columnName, bigDecimal.doubleValue());
                        } else {
                            record.put(columnName, null);
                        }

                        break;

                    case Types.TIME:
                        Time time = rs.getTime(i);
                        Date dateTime = null;
                        if (time != null) {
                            dateTime = new Date(time.getTime());
                        }
                        record.put(columnName, dateTime);
                        break;

                    case Types.DATE:

                        if (metaData.getColumnTypeName(i).equalsIgnoreCase("year")) {

                            record.put(columnName, new Date(rs.getInt(i)));
                        } else {
                            record.put(columnName, rs.getDate(i));
                        }
                        break;

                    case Types.TIMESTAMP:
                        Timestamp timestamp = rs.getTimestamp(i);
                        Date date = null;
                        if (timestamp != null) {
                            date = new Date(timestamp.getTime());
                        }
                        record.put(columnName, date);
                        break;

                    case Types.BINARY:
                    case Types.VARBINARY:
                    case Types.BLOB:
                    case Types.LONGVARBINARY:
                        record.put(columnName, rs.getBytes(i));
                        break;

                    // warn: bit(1) -> Types.BIT 可使用BoolColumn
                    // warn: bit(>1) -> Types.VARBINARY 可使用BytesColumn
                    case Types.BOOLEAN:
                    case Types.BIT:

                        record.put(columnName, rs.getBoolean(i));
                        break;

                    case Types.NULL:
                        String stringData = null;
                        if (rs.getObject(i) != null) {
                            stringData = rs.getObject(i).toString();
                        }
                        record.put(columnName, stringData);
                        break;

                    default:
                        logger.warn("incorrect sql information. Database reading of this field type is not supported. columnName:[{}], columnType:[{}], columnClassName:[{}]. This field data will not be returned.",
                                metaData.getColumnName(i), metaData.getColumnType(i), metaData.getColumnClassName(i));
                }
            }

            return record;
        } catch (Exception e) {
            logger.error("read data {} occur exception:", record.toString(), e);
            return null;
        }

    }

    public static ResultSet query(Statement stmt, String sql)
            throws SQLException {
        return stmt.executeQuery(sql);
    }

    /**
     * @param conn
     * @param sql
     * @param fetchSize
     * @param queryTimeout seconds
     * @return
     * @throws SQLException
     */
    public static ResultSet query(Connection conn, String sql, int fetchSize, int queryTimeout)
            throws SQLException {
        // make sure autocommit is off
        conn.setAutoCommit(false);
        Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY);
        stmt.setFetchSize(fetchSize);
        stmt.setQueryTimeout(queryTimeout);
        return query(stmt, sql);
    }
}
