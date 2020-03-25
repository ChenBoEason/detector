package com.cbmiddleware.detector.sql.function;

import com.cbmiddleware.detector.constant.FunctionType;

import java.util.List;

/**
 * @author Eason(bo.chenb)
 * @description
 * @date 2020-03-21
 **/
public class ColumnCaseWhen extends AbstractSqlFunctionParam {

    /**
     * case when 关系
     */
    private List<CaseWhenCondition> conditions;

    @Override
    public FunctionType type() {
        return FunctionType.casewhen;
    }

    public List<CaseWhenCondition> getConditions() {
        return conditions;
    }

    public void setConditions(List<CaseWhenCondition> conditions) {
        this.conditions = conditions;
    }
}
