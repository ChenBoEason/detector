package com.cbmiddleware.detector;

import java.util.List;
import java.util.Map;

/**
 * @author Eason(bo.chenb)
 * @description
 * @date 2020-03-10
 **/
public class OdpsDetectExecuteSqlResponse extends AbstractDetectResponse {

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
