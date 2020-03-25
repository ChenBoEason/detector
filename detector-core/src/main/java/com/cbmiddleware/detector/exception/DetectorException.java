package com.cbmiddleware.detector.exception;

/**
 * @author Eason(bo.chenb)
 * @description
 * @date 2020-03-06
 **/
public class DetectorException extends Exception {

    public DetectorException(Throwable e) {
        super(e);
    }

    public DetectorException(String e) {
        super(e);
    }

    public DetectorException(String msg, Throwable e) {
        super(msg, e);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
