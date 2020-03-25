package com.cbmiddleware.detector.entity;

/**
 * @author Eason(bo.chenb)
 * @description 阀值参数
 *    满足其一调用回调监听器
 * @date 2020-03-13
 **/
public class Threshold {

    /**
     * 表数据量阀值
     */
    private Integer tableCount;
    /**
     * 列数据量阀值
     */
    private Integer columnCount;

    public Threshold() {
    }

    public Threshold(Integer tableCount, Integer cloumnCount) {
        this.tableCount = tableCount;
        this.columnCount = cloumnCount;
    }

    public Integer getTableCount() {
        return tableCount;
    }

    public void setTableCount(Integer tableCount) {
        this.tableCount = tableCount;
    }

    public Integer getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(Integer columnCount) {
        this.columnCount = columnCount;
    }
}
