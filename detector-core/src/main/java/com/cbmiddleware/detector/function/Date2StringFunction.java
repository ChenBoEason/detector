package com.cbmiddleware.detector.function;


import com.cbmiddleware.detector.constant.FunctionType;
import com.cbmiddleware.detector.exception.FunctionConvertException;
import com.cbmiddleware.detector.function.param.Date2StringFunctionParam;
import org.joda.time.DateTime;

import java.util.Date;

/**
 * @author Eason(bo.chenb)
 * @description
 * @date 2020-03-21
 **/
public class Date2StringFunction implements Function<String, Date2StringFunctionParam> {

    @Override
    public String convert(Date2StringFunctionParam param) throws FunctionConvertException {
        Date value = param.getValue();

        if(value == null){
            return param.defaultValue();
        }

        DateTime dateTime = new DateTime(param.getValue());

        return dateTime.toString(param.getFormat());
    }

    @Override
    public FunctionType type() {
        return FunctionType.date2String;
    }
}
