package com.cbmiddleware.detector;

import com.cbmiddleware.detector.constant.DetectorType;

/**
 * @author Eason(bo.chenb)
 * @description 版本
 * @date 2020-03-06
 **/
public interface Version {

    /**
     * 数据源版本号
     * @return
     */
    String version();

    DetectorType type();
}
