package com.cbmiddleware.detector.sql.multiple;

import com.cbmiddleware.detector.sql.constant.ConditionType;
import com.cbmiddleware.detector.sql.function.SqlFunctionParam;

/**
 * @author Eason(bo.chenb)
 * @description
 *    originalColumnName : xingming
 *    alias                name
 *    tableName            user
 *    tableAlias           u
 *    u.xingming as name
 * @date 2020-03-18
 **/
public class QueryColumn {

    /**
     * 表名
     */
    private String tableName;
    /**
     * 表的别名
     */
    private String tableAlias;
    /**
     * 原始列名
     */
    private String originalColumnName;

    /**
     * 别名
     */
    private String columnAlias;
    /**
     * 字段标化参数
     */
    private SqlFunctionParam functionParam;

    /**
     * 条件类型
     */
    private ConditionType conditionType;


    public QueryColumn(String tableAlias, String originalColumnName, String columnAlias) {
        this.tableAlias = tableAlias;
        this.originalColumnName = originalColumnName;
        this.columnAlias = columnAlias;
    }

    public QueryColumn(String tableName, String tableAlias, String originalColumnName, String columnAlias) {
        this.tableName = tableName;
        this.tableAlias = tableAlias;
        this.originalColumnName = originalColumnName;
        this.columnAlias = columnAlias;
    }

    public String getOriginalColumnName() {
        return originalColumnName;
    }

    public void setOriginalColumnName(String originalColumnName) {
        this.originalColumnName = originalColumnName;
    }

    public String getColumnAlias() {
        return columnAlias;
    }

    public void setColumnAlias(String columnAlias) {
        this.columnAlias = columnAlias;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableAlias() {
        return tableAlias;
    }

    public void setTableAlias(String tableAlias) {
        this.tableAlias = tableAlias;
    }

    public ConditionType getConditionType() {
        return conditionType;
    }

    public void setConditionType(ConditionType conditionType) {
        this.conditionType = conditionType;
    }

    public SqlFunctionParam getFunctionParam() {
        return functionParam;
    }

    public void setFunctionParam(SqlFunctionParam functionParam) {
        this.functionParam = functionParam;
    }
}
