package com.cbmiddleware.detector.sql.function;

import com.cbmiddleware.detector.sql.constant.ConditionType;

/**
 * @author Eason(bo.chenb)
 * @description
 *
 * case a.name = 'eason' then 'chenbo'
 * @date 2020-03-21
 **/
public class CaseWhenCondition {
    /**
     * 条件值 类似 eason
     */
    private Object conditionValue;
    /**
     * 条件
     */
    private ConditionType conditionType;
    /**
     * then之后的值  类似 chenbo
     */
    private Object value;

    public CaseWhenCondition() {
        this.conditionType = ConditionType.eq;
    }

    public CaseWhenCondition(Object conditionValue, ConditionType conditionType, Object value) {
        this.conditionValue = conditionValue;
        this.conditionType = conditionType;
        this.value = value;
    }

    public Object getConditionValue() {
        return conditionValue;
    }

    public void setConditionValue(Object conditionValue) {
        this.conditionValue = conditionValue;
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
}
