package com.cbmiddleware.detector.constant;

/**
 * @author Eason(bo.chenb)
 * @description 数据库探测类型
 * @date 2020-03-05
 **/
public enum DataBaseType {


    oracle,
    mysql,
    odps,
    ;

    public static DataBaseType parse(String type){

        for (DataBaseType dataSourceType : DataBaseType.values()){
            if(dataSourceType.name().equalsIgnoreCase(type)){
                return dataSourceType;
            }
        }

        return null;
    }
}
