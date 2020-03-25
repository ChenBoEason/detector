package com.cbmiddleware.detector.function;

import com.cbmiddleware.detector.constant.FunctionType;
import com.cbmiddleware.detector.exception.FunctionConvertException;
import com.cbmiddleware.detector.function.param.FunctionParam;

/**
 * @author Eason(bo.chenb)
 * @description 函数功能
 *  T 返回参数类型 Date/String......
 *  D 入参参数
 * @date 2020-03-20
 **/
public interface Function<T, D extends FunctionParam> {

    /**
     * 数据转换
     * @param param
     * @return
     * @throws FunctionConvertException
     */
    T convert(D param) throws FunctionConvertException;

    /**
     * 函数类型
     * @return
     */
    FunctionType type();
}
