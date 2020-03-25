package com.cbmiddleware.detector.elasticsearch;

import com.cbmiddleware.detector.AbstractTableDetector;
import com.cbmiddleware.detector.DetectRequest;
import com.cbmiddleware.detector.DetectResponse;
import com.cbmiddleware.detector.DetectorActionListener;
import com.cbmiddleware.detector.constant.DataBaseType;
import com.cbmiddleware.detector.constant.DetectorType;
import com.cbmiddleware.detector.entity.Threshold;
import com.cbmiddleware.detector.exception.DetectorException;

import java.util.List;

/**
 * @author Eason(bo.chenb)
 * @description es 索引结构字段探测器
 * @date 2020-03-13
 **/
public class ElasticsearchTableDetector extends AbstractTableDetector {

    @Override
    public DetectResponse detect(DetectRequest detectRequest) throws DetectorException {
        return super.detect(detectRequest);
    }

    @Override
    public void detect(DetectRequest detectRequest, Threshold threshold, DetectorActionListener<DetectResponse> listener) throws DetectorException {
        super.detect(detectRequest, threshold, listener);
    }

    @Override
    public void detectAsync(DetectRequest detectRequest, Threshold threshold, DetectorActionListener<DetectResponse> listener) throws DetectorException {
        super.detectAsync(detectRequest, threshold, listener);
    }

    @Override
    public boolean isConnect(DetectRequest detectRequest) throws DetectorException {
        return super.isConnect(detectRequest);
    }

    @Override
    public DetectResponse detectTableSize(DetectRequest detectRequest, List<String> tableNames) throws DetectorException {
        return super.detectTableSize(detectRequest, tableNames);
    }

    @Override
    public DetectResponse executeSql(DetectRequest detectRequest, String sql) throws DetectorException {
        return super.executeSql(detectRequest, sql);
    }

    @Override
    public DetectorType detectorType() {
        return null;
    }

    @Override
    public DataBaseType databaseType() {
        return null;
    }
}
