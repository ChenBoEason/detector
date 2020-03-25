package com.cbmiddleware.detector.function;

import com.cbmiddleware.detector.constant.FunctionType;
import com.cbmiddleware.detector.exception.FunctionConvertException;
import com.cbmiddleware.detector.function.param.CaseWhenFunctionParam;
import com.cbmiddleware.detector.sql.constant.ConditionType;
import com.cbmiddleware.detector.sql.function.CaseWhenCondition;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Eason(bo.chenb)
 * @description
 * @date 2020-03-21
 **/
public class CaseWhenFunction implements Function<Object, CaseWhenFunctionParam> {

    @Override
    public Object convert(CaseWhenFunctionParam param) throws FunctionConvertException {
        Object value = param.getValue();

        if (value == null || value.toString().length() == 0) {
            return param.defaultValue();
        }
        /* 映射关系 */
        List<CaseWhenCondition> conditions = param.getConditions();

        if (conditions == null) {
            return param.defaultValue();
        }
        Object result = null;


        if (value instanceof String) {
            String str = value.toString();
            for (CaseWhenCondition condition : conditions) {

                whenString(str, condition.getConditionType(), condition.getConditionValue(), condition.getValue(), result);

                if (result != null) {
                    return result;
                }
            }
        }

        if (value instanceof Double) {
            double v = Double.parseDouble(value.toString());
            for (CaseWhenCondition condition : conditions) {

                number(new BigDecimal(v), condition.getConditionType(), new BigDecimal((Double) condition.getConditionValue()), condition.getValue(), result);

                if (result != null) {
                    return result;
                }
            }
        }

        if (value instanceof Integer) {
            int v = Integer.parseInt(value.toString());
            for (CaseWhenCondition condition : conditions) {

                number(new BigDecimal(v), condition.getConditionType(), new BigDecimal((Integer) condition.getConditionValue()), condition.getValue(), result);

                if (result != null) {
                    return result;
                }
            }
        }

        if (value instanceof Long) {
            long v = Long.parseLong(value.toString());
            for (CaseWhenCondition condition : conditions) {

                number(new BigDecimal(v), condition.getConditionType(), new BigDecimal((Long) condition.getConditionValue()), condition.getValue(), result);

                if (result != null) {
                    return result;
                }
            }
        }


        if (result == null) {
            return param.defaultValue();
        }

        return result;
    }

    private void whenString(String str, ConditionType conditionType, Object conditionValue, Object thenValue, Object result) {
        switch (conditionType) {
            case eq:
                if (str.equals(conditionValue)) {
                    result = thenValue;
                }
                break;
            case gt:
                if (str.compareTo(conditionValue.toString()) > 0) {
                    result = thenValue;
                }
                break;
            case gte:
                if (str.compareTo(conditionValue.toString()) >= 0) {
                    result = thenValue;
                }
                break;
            case lt:
                if (str.compareTo(conditionValue.toString()) < 0) {
                    result = thenValue;
                }
                break;

            case lte:
                if (str.compareTo(conditionValue.toString()) <= 0) {
                    result = thenValue;
                }
                break;
            default:
                break;
        }
    }

    private void number(BigDecimal value, ConditionType conditionType, BigDecimal conditionValue, Object thenValue, Object result) {
        switch (conditionType) {
            case eq:
                if (value.equals(conditionValue)) {
                    result = thenValue;
                }
                break;
            case gt:
                if (value.subtract(conditionValue).doubleValue() > 0) {
                    result = thenValue;
                }
                break;
            case gte:
                if (value.subtract(conditionValue).doubleValue() >= 0) {
                    result = thenValue;
                }
                break;
            case lt:
                if (conditionValue.subtract(value).doubleValue() > 0) {
                    result = thenValue;
                }
                break;

            case lte:
                if (conditionValue.subtract(value).doubleValue() >= 0) {
                    result = thenValue;
                }
                break;
            default:
                break;
        }
    }

    @Override
    public FunctionType type() {
        return FunctionType.casewhen;
    }
}
