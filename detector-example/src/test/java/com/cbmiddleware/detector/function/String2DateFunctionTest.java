package com.cbmiddleware.detector.function;

import com.cbmiddleware.detector.exception.FunctionConvertException;
import com.cbmiddleware.detector.function.param.String2DateFunctionParam;
import org.junit.Test;

import java.util.Date;

/**
 * @author Eason(bo.chenb)
 * @description
 * @date 2020-03-21
 **/
public class String2DateFunctionTest {


    @Test
    public void convert(){
        String2DateFunction string2DateFunction = new String2DateFunction();
        String2DateFunctionParam param = new String2DateFunctionParam("yyyy-MM-dd HH:mm:ss", "2020-03-21 13:13:13111", new Date());

        try {
            Date date = string2DateFunction.convert(param);
            System.out.println(date);
        } catch (FunctionConvertException e) {
            e.printStackTrace();
        }
    }
}
