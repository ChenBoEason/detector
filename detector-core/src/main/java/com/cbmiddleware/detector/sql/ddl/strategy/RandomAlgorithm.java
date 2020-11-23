package com.cbmiddleware.detector.sql.ddl.strategy;

/**
 * @author Eason(bo.chenb)
 * @email chenboeason@gmail.com
 * @date 2020/11/2
 * @description 随机算法
 **/
public enum RandomAlgorithm {
    /**
     * uuid
     */
    uuid,
    /**
     * 雪花算法
     */
    snowflake,
    ;
}
