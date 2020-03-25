package com.cbmiddleware.detector.function.param;

import com.cbmiddleware.detector.constant.FunctionType;

/**
 * @author Eason(bo.chenb)
 * @description 函数对照数据
 * @date 2020-03-20
 **/
public interface FunctionParam<T> {


    /**
     * 默认值
     * @param
     * @return
     */
    T defaultValue();


    /**
     * 设置默认值
     * @param defaultValue
     * @return
     */
    void setDefaultValue(T defaultValue);


    FunctionType type();
}
