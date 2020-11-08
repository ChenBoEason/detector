package com.cbmiddleware.detector.sql.multiple;

import com.cbmiddleware.detector.sql.constant.ColumnRelation;
import com.cbmiddleware.detector.sql.constant.ConditionType;

/**
 * @author Eason(bo.chenb)
 * @description
 * @date 2020-03-18
 **/
public class ConditionInfo {

    /**
     * 条件名
     */
    private String name;

    /**
     *
     */
    private ConditionType conditionType;
    /**
     * 条件值
     */
    private Object value;
    /**
     * 作为条件的表的别名
     */
    private String conditionTableAlias;
    /**
     * 作为条件的表的名称
     */
    private String conditionColumnName;

    /**
     * 条件类型 这里的类型只有 or and
     */
    private ColumnRelation columnRelation;
    /**
     * 关联条件
     */
    private ConditionInfo conditionInfo;

    public ConditionInfo() {
    }

    public ConditionInfo(String name, ConditionType conditionType, Object value) {
        this.name = name;
        this.conditionType = conditionType;
        this.value = value;
    }

    public ConditionInfo(String name, ConditionType conditionType, String conditionTableAlias, String conditionColumnName) {
        this.name = name;
        this.conditionType = conditionType;
        this.conditionTableAlias = conditionTableAlias;
        this.conditionColumnName = conditionColumnName;
    }

    public ConditionInfo(String name, ConditionType conditionType, Object value, ColumnRelation columnRelation) {
        this.name = name;
        this.conditionType = conditionType;
        this.value = value;
        this.columnRelation = columnRelation;
        this.conditionInfo = conditionInfo;
    }

    public ConditionInfo(String name, ConditionType conditionType, Object value, ColumnRelation columnRelation, ConditionInfo conditionInfo) {
        this.name = name;
        this.conditionType = conditionType;
        this.value = value;
        this.columnRelation = columnRelation;
        this.conditionInfo = conditionInfo;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public ConditionType getConditionType() {
        return conditionType;
    }

    public void setConditionType(ConditionType conditionType) {
        this.conditionType = conditionType;
    }

    public ColumnRelation getColumnRelation() {
        return columnRelation;
    }

    public void setColumnRelation(ColumnRelation columnRelation) {
        this.columnRelation = columnRelation;
    }

    public void setConditionInfo(ConditionInfo conditionInfo) {
        this.conditionInfo = conditionInfo;
    }

    public String getConditionTableAlias() {
        return conditionTableAlias;
    }

    public void setConditionTableAlias(String conditionTableAlias) {
        this.conditionTableAlias = conditionTableAlias;
    }

    public String getConditionColumnName() {
        return conditionColumnName;
    }

    public void setConditionColumnName(String conditionColumnName) {
        this.conditionColumnName = conditionColumnName;
    }

    public ConditionInfo getConditionInfo() {
        return conditionInfo;
    }
}
