package com.cbmiddleware.detector.constant;

/**
 * @author Eason(bo.chenb)
 * @description 函数类型
 * @date 2020-03-20
 **/
public enum FunctionType {

    /**
     * 字符转时间格式转换
     */
    string2date,
    /**
     * 时间转字符串
     */
    date2String,
    /**
     * 字典数据转换
     */
    dictionary,
    /**
     * case when
     */
    casewhen,
    /**
     * 区间
     */
    range
    ;
}
