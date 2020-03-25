package com.cbmiddleware.detector.constant;

/**
 * @author Eason(bo.chenb)
 * @description 探测类型
 * @date 2020-03-05
 **/
public enum DetectorType {


    database,
    table,
    ;

    public static DetectorType parse(String type){

        for (DetectorType dataSourceType : DetectorType.values()){
            if(dataSourceType.name().equalsIgnoreCase(type)){
                return dataSourceType;
            }
        }

        return null;
    }
}
