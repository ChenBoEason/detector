package com.cbmiddleware.detector;

import java.util.List;

/**
 * @author Eason(bo.chenb)
 * @description
 * @date 2020-03-06
 **/
public abstract class AbstractDetectRequest implements DetectRequest{
    private static final long serialVersionUID = 6740154232371671341L;

    /**
     * 探测目标的版本号
     */
    private String version;
    /**
     * 是否测试连接
     */
    private boolean testConnection;

    /**
     * 表名
     */
    private List<String> tableNames;
    /**
     * 执行的sql语句
     */
    private String sql;

    public AbstractDetectRequest() {
        this.testConnection = false;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isTestConnection() {
        return testConnection;
    }

    public void setTestConnection(boolean testConnection) {
        this.testConnection = testConnection;
    }

    public List<String> getTableNames() {
        return tableNames;
    }

    public void setTableNames(List<String> tableNames) {
        this.tableNames = tableNames;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
