package com.cbmiddleware.detector;

import com.cbmiddleware.detector.entity.Threshold;
import com.cbmiddleware.detector.exception.DetectorException;

import java.util.List;

/**
 * @author Eason(bo.chenb)
 * @description
 *      表结构探测器
 * @date 2020-03-06
 **/
public abstract class AbstractTableDetector implements Detector{


    @Override
    public DetectResponse detect(DetectRequest detectRequest) throws DetectorException {
        return new AbstractDetectResponse() {
        };
    }

    @Override
    public boolean isConnect(DetectRequest detectRequest) throws DetectorException {
        return false;
    }

    @Override
    public DetectResponse detectTableSize(DetectRequest detectRequest, List<String> tableNames) throws DetectorException {
        return new AbstractDetectResponse() {
        };
    }

    @Override
    public DetectResponse executeSql(DetectRequest detectRequest, String sql) throws DetectorException {
        return new AbstractDetectResponse() {
        };
    }

    @Override
    public void detect(DetectRequest detectRequest, Threshold threshold, DetectorActionListener<DetectResponse> listener) throws DetectorException {

    }

    @Override
    public void detectAsync(DetectRequest detectRequest, Threshold threshold, DetectorActionListener<DetectResponse> listener) throws DetectorException {
    }

}
