package com.cbmiddleware.detector.sql;

import com.cbmiddleware.detector.sql.constant.ColumnRelation;
import com.cbmiddleware.detector.sql.constant.ConditionType;


/**
 * @author Eason(bo.chenb)
 * @description 额外条件
 * @date 2020-03-20
 **/
public class ExtraCondition {

    /**
     * 表名
     */
    private String tableName;
    /**
     * 表别名
     */
    private String tableAlias;
    /**
     * 字段
     */
    private String columnName;

    /**
     * 条件类型
     */
    private ConditionType conditionType;
    /**
     * 字段值
     */
    private Object value;

    private ColumnRelation relation;


    public ExtraCondition() {
        this.relation = ColumnRelation.and;
    }

    public ExtraCondition(String tableName, String columnName, ConditionType conditionType, Object value) {
        this.tableName = tableName;
        this.columnName = columnName;
        this.conditionType = conditionType;
        this.value = value;
    }

    public ExtraCondition(String tableName, String columnName, ConditionType conditionType, Object value, ColumnRelation relation) {
        this.tableName = tableName;
        this.columnName = columnName;
        this.conditionType = conditionType;
        this.value = value;
        this.relation = relation;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public ConditionType getConditionType() {
        return conditionType;
    }

    public void setConditionType(ConditionType conditionType) {
        this.conditionType = conditionType;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public ColumnRelation getRelation() {
        return relation;
    }

    public void setRelation(ColumnRelation relation) {
        this.relation = relation;
    }

    public String getTableAlias() {
        return tableAlias;
    }

    public void setTableAlias(String tableAlias) {
        this.tableAlias = tableAlias;
    }
}
