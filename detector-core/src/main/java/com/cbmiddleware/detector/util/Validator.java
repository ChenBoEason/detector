package com.cbmiddleware.detector.util;

/**
 * @author Eason(bo.chenb)
 * @description
 * @date 2020-03-18
 **/
public class Validator {


    public static Integer validatInteger(Integer value, Integer defaultValue) {

        if (value == null || value <= 0) {
            return defaultValue;
        }

        return value;
    }
}
