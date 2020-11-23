package com.cbmiddleware.detector.sql.ddl.strategy;

import com.cbmiddleware.detector.util.DetectorSnowFlakeUtils;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * @author Eason(bo.chenb)
 * @email chenboeason@gmail.com
 * @date 2020/11/2
 * @description 随机字符串生成策略
 *              生成策略分为 uuid和雪花算法
 *
 **/
public class RandomStringGenerateStrategy implements GenerateStrategy<String> {

    /**
     * 生成算法
     */
    private RandomAlgorithm algorithm;
    /**
     * 所需字符长度
     */
    private int length;

    public RandomStringGenerateStrategy() {
        this.algorithm = RandomAlgorithm.uuid;
        this.length = 32;
    }

    public RandomStringGenerateStrategy(RandomAlgorithm algorithm, int length) {

        if (length < 0) {
            throw new IllegalArgumentException("length must be greater than 0");
        }

        this.algorithm = algorithm;
        this.length = length;
    }

    @Override
    public String generate() throws Exception {

        switch (algorithm) {
            case uuid:
                return RandomStringUtils.random(length, true, true);
            case snowflake:
                return DetectorSnowFlakeUtils.generate(length);
            default:
                throw new IllegalArgumentException(algorithm.name()+" algorithm not supported");
        }

    }
}
