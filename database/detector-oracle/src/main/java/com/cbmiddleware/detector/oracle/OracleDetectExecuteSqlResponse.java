package com.cbmiddleware.detector.oracle;

import com.cbmiddleware.detector.AbstractDetectResponse;

import java.util.List;
import java.util.Map;

/**
 * @author Eason(bo.chenb)
 * @description
 * @date 2020-03-21
 **/
public class OracleDetectExecuteSqlResponse extends AbstractDetectResponse {

    /**
     * 执行结果
     */
    private List<Map<String, Object>> records;


    public List<Map<String, Object>> getRecords() {
        return records;
    }

    public void setRecords(List<Map<String, Object>> records) {
        this.records = records;
    }

}
