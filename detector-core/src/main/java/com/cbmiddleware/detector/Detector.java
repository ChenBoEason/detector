package com.cbmiddleware.detector;

import com.cbmiddleware.detector.constant.DataBaseType;
import com.cbmiddleware.detector.constant.DetectorType;
import com.cbmiddleware.detector.entity.Threshold;
import com.cbmiddleware.detector.exception.DetectorException;

import java.util.List;

/**
 * @author Eason(bo.chenb)
 * @description 探测器
 * @date 2020-03-06
 **/
public interface Detector {
    /**
     * 探测
     *
     * @param detectRequest 探测请求参数
     * @return
     * @throws DetectorException
     */
    DetectResponse detect(DetectRequest detectRequest) throws DetectorException;

    /**
     * 同步回调探测
     *
     * @param detectRequest
     * @param threshold     探测参数回调阀值 如 100，当探测到100张表或列数达到1000时调用回调监听器
     * @param listener
     * @return
     * @throws DetectorException
     */
    void detect(DetectRequest detectRequest, Threshold threshold, DetectorActionListener<DetectResponse> listener) throws DetectorException;

    /**
     * 异步回调阀值探测
     *
     * @param detectRequest 探测请求参数
     * @param listener      回调监听器
     * @return
     * @throws DetectorException
     */
    void detectAsync(DetectRequest detectRequest, Threshold threshold, DetectorActionListener<DetectResponse> listener) throws DetectorException;

    /**
     * 测试连通性
     *
     * @param detectRequest
     * @return
     */
    boolean isConnect(DetectRequest detectRequest) throws DetectorException;

    /**
     * 探测表大小（存储大小，数据量）
     *
     * @param detectRequest
     * @return
     * @throws DetectorException
     */
    DetectResponse detectTableSize(DetectRequest detectRequest, List<String> tableNames) throws DetectorException;

    /**
     * 执行sql
     *
     * @param detectRequest
     * @param sql
     * @return
     * @throws DetectorException
     */
    DetectResponse executeSql(DetectRequest detectRequest, String sql) throws DetectorException;

    /**
     * 探测器类型
     *
     * @return
     */
    DetectorType detectorType();

    /**
     * 探测数据库类型
     *
     * @return
     */
    DataBaseType databaseType();
}
