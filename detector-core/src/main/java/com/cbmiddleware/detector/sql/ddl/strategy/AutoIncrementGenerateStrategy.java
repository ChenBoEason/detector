package com.cbmiddleware.detector.sql.ddl.strategy;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * @author Eason(bo.chenb)
 * @email chenboeason@gmail.com
 * @date 2020/11/2
 * @description 自增策略
 **/
public class AutoIncrementGenerateStrategy implements GenerateStrategy<Integer> {

    /**
     * 数据源
     */
    private DruidDataSource dataSource;
    /**
     * 自增的表名
     */
    private String tableName;

    @Override
    public Integer generate() throws Exception {

        return null;
    }
}
