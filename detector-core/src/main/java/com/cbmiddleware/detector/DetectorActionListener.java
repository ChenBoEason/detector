package com.cbmiddleware.detector;

/**
 * @author Eason(bo.chenb)
 * @description 探测器行为监听器
 * @date 2020-03-13
 **/
public interface DetectorActionListener<Response> {


    /**
     * 监听响应
     * @param response
     */
    void onResponse(Response response);

    /**
     * 执行异常
     * @param e
     */
    void onFailure(Exception e);
}
