package com.cbmiddleware.detector.sql.ddl.strategy;

/**
 * @author Eason(bo.chenb)
 * @email chenboeason@gmail.com
 * @date 2020/11/2
 * @description
 *  生成策略
 *
 **/
public interface GenerateStrategy<T> {

    /**
     * 生成
     * @return
     */
    <T> T generate() throws Exception;


}
