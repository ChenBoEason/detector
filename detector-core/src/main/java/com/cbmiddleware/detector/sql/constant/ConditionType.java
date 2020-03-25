package com.cbmiddleware.detector.sql.constant;


/**
 * @author Eason(bo.chenb)
 * @description 条件类型
 * @date 2020-03-18
 **/
public enum ConditionType {



    /**
     * 小于
     */
    lt("<"),
    /**
     * 小于等于
     */
    lte("<="),
    /**
     * 大于
     */
    gt(">"),
    /**
     * 大于等于
     */
    gte(">="),
    /**
     * 等于
     */
    eq("="),

    ;

    private String symbol;


    ConditionType(String symbol) {
        this.symbol = symbol;
    }

    public static ConditionType parse(String symbol){

        for (ConditionType conditionType : ConditionType.values()){
            if(conditionType.name().equalsIgnoreCase(symbol)){
                return conditionType;
            }
        }

        return null;
    }


    public String getSymbol() {
        return symbol;
    }
}
