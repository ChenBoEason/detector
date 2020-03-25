package com.cbmiddleware.detector.function.param;

import com.cbmiddleware.detector.constant.FunctionType;

import java.util.Date;

/**
 * @author Eason(bo.chenb)
 * @description
 * @date 2020-03-20
 **/
public class String2DateFunctionParam implements FunctionParam<Date> {

    /**
     * string 日期的格式
     */
    private String format;
    /**
     * 真实值-待转换的参数值
     */
    private String value;


    private Date defaultValue;

    public String2DateFunctionParam() {
    }

    public String2DateFunctionParam(String format, String value) {
        this.format = format;
        this.value = value;
    }

    public String2DateFunctionParam(String format, String value, Date defaultValue) {
        this.format = format;
        this.value = value;
        this.defaultValue = defaultValue;
    }

    @Override
    public Date defaultValue() {
        return defaultValue;
    }

    @Override
    public void setDefaultValue(Date defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public FunctionType type() {
        return FunctionType.string2date;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
