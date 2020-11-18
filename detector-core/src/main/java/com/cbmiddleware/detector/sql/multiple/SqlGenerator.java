package com.cbmiddleware.detector.sql.multiple;

import com.cbmiddleware.detector.exception.DetectorException;

/**
 * @author Eason(bo.chenb)
 * @description 查询sql生成器
 * @date 2020-03-19
 **/
public interface SqlGenerator {

    /**
     * sql生成
     * @param sqlConfInfo  sql生成配置信息
     * @return
     * @throws DetectorException
     */
    String generate(GenerateSqlConfInfo sqlConfInfo) throws DetectorException;

    void join(ExtraCondition extraCondition, QueryTable queryTable) throws DetectorException;


    /**
     * @param extraCondition 额外的条件（待合并的参数）
     * @param queryTable     原始配置
     * @param dfsCount       当前dfs数
     * @param expectCount    期望dfs调用次数
     * @throws DetectorException
     */
    void join(ExtraCondition extraCondition, QueryTable queryTable, long dfsCount, final long expectCount) throws DetectorException;
}
