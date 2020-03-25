package com.cbmiddleware.detector.function.param;

import com.cbmiddleware.detector.constant.FunctionType;

import java.util.Date;

/**
 * @author Eason(bo.chenb)
 * @description
 * @date 2020-03-21
 **/
public class Date2StringFunctionParam implements FunctionParam<String> {

    /**
     * string 日期的格式
     */
    private String format;
    /**
     * 真实值-待转换的参数值
     */
    private Date value;


    private String defaultValue;

    public Date2StringFunctionParam() {
    }

    public Date2StringFunctionParam(String format, Date value) {
        this.format = format;
        this.value = value;
    }

    public Date2StringFunctionParam(String format, Date value, String defaultValue) {
        this.format = format;
        this.value = value;
        this.defaultValue = defaultValue;
    }

    @Override
    public String defaultValue() {
        return this.defaultValue;
    }

    @Override
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public FunctionType type() {
        return FunctionType.date2String;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Date getValue() {
        return value;
    }

    public void setValue(Date value) {
        this.value = value;
    }
}
