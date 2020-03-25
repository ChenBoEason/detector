package com.cbmiddleware.detector.sql.function;

/**
 * @author Eason(bo.chenb)
 * @description
 * @date 2020-03-21
 **/
public abstract class AbstractSqlFunctionParam implements SqlFunctionParam {

    /**
     * 表的别名
     */
    private String tableAlias;

    /**
     * 原始列名
     */
    private String columnName;


    public String getTableAlias() {
        return tableAlias;
    }

    public void setTableAlias(String tableAlias) {
        this.tableAlias = tableAlias;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
}
