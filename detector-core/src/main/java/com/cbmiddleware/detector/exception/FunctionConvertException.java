package com.cbmiddleware.detector.exception;

/**
 * @author Eason(bo.chenb)
 * @description
 * @date 2020-03-20
 **/
public class FunctionConvertException extends Exception {

    public FunctionConvertException(Throwable e) {
        super(e);
    }

    public FunctionConvertException(String e) {
        super(e);
    }

    public FunctionConvertException(String msg, Throwable e) {
        super(msg, e);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
