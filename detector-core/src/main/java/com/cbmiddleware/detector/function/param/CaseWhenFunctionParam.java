package com.cbmiddleware.detector.function.param;

import com.cbmiddleware.detector.constant.FunctionType;
import com.cbmiddleware.detector.sql.function.CaseWhenCondition;

import java.util.List;

/**
 * @author Eason(bo.chenb)
 * @description
 * @date 2020-03-21
 **/
public class CaseWhenFunctionParam implements FunctionParam<Object> {

    /**
     * 真实值-待转换的参数值
     */
    private Object value;
    /**
     * case when 关系
     */
    private List<CaseWhenCondition> conditions;

    private Object defaultValue;


    public CaseWhenFunctionParam() {
    }

    public CaseWhenFunctionParam(Object value, List<CaseWhenCondition> conditions) {
        this.value = value;
        this.conditions = conditions;
    }

    public CaseWhenFunctionParam(Object value, List<CaseWhenCondition> conditions, String defaultValue) {
        this.value = value;
        this.conditions = conditions;
        this.defaultValue = defaultValue;
    }

    @Override
    public Object defaultValue() {
        return this.defaultValue;
    }

    @Override
    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public FunctionType type() {
        return FunctionType.casewhen;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public List<CaseWhenCondition> getConditions() {
        return conditions;
    }

    public void setConditions(List<CaseWhenCondition> conditions) {
        this.conditions = conditions;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }
}
