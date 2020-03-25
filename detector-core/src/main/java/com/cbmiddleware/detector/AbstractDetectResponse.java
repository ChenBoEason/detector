package com.cbmiddleware.detector;

import com.cbmiddleware.detector.entity.TableInfo;

import java.util.Map;

/**
 * @author Eason(bo.chenb)
 * @description
 * @date 2020-03-06
 **/
public abstract class AbstractDetectResponse implements DetectResponse {

    /**
     * 表结构信息
     */
    private Map<String, TableInfo> tableInfos;


    public Map<String, TableInfo> getTableInfos() {
        return tableInfos;
    }

    public void setTableInfos(Map<String, TableInfo> tableInfos) {
        this.tableInfos = tableInfos;
    }
}
