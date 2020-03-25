package com.cbmiddleware.detector.sql.function;

import com.cbmiddleware.detector.constant.FunctionType;

/**
 * @author Eason(bo.chenb)
 * @description
 * @date 2020-03-21
 **/
public class ColumnString2Date extends AbstractSqlFunctionParam {


    /**
     * string 日期的格式
     */
    private String format;

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
}
