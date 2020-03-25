package com.cbmiddleware.detector.function;

import com.cbmiddleware.detector.constant.FunctionType;
import com.cbmiddleware.detector.exception.FunctionConvertException;
import com.cbmiddleware.detector.function.param.String2DateFunctionParam;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * @author Eason(bo.chenb)
 * @description
 * @date 2020-03-20
 **/
public class String2DateFunction implements Function<Date, String2DateFunctionParam> {



    @Override
    public Date convert(String2DateFunctionParam param) throws FunctionConvertException {
        String value = param.getValue();

        if (value == null || value.length() == 0) {
            return param.defaultValue();
        }


        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(param.getFormat());

        DateTime dateTime = dateTimeFormatter.parseDateTime(value);

        return dateTime.toDate();
    }

    @Override
    public FunctionType type() {
        return FunctionType.string2date;
    }


}
