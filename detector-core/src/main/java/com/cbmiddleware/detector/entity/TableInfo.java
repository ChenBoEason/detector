package com.cbmiddleware.detector.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Eason(bo.chenb)
 * @description
 *      表信息
 * @date 2020-03-05
 **/
public class TableInfo implements Serializable {

    private static final long serialVersionUID = 1931888606471146647L;
    /**
     * 表所属用户
     */
    private String tableOwner;

    /**
     * 所属项目
     */
    private String project;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 表类型
     */
    private String tableType;

    /**
     * 表行数
     */
    private Long tableRows;
    /**
     * 表存储大小
     */
    private Long tableStorageSize;

    /**
     * 表所占磁盘的物理大小
     */
    private Long tablePhysicalSize;
    /**
     * 存储单位 byte
     */
    private String storageUnit;

    /**
     * 表创建时间
     */
    private Date tableCreateTime;

    /**
     * 表修改时间
     */
    private Date tableUpdateTime;

    /**
     * 表的字符集
     */
    private String tableCharsetName;

    /**
     * 表的备注
     */
    private String remark;

    /**
     * 字段信息
     */
    private List<ColumnInfo> fieldInfos;

    /**
     * 分区字段信息
     */
    private List<ColumnInfo> partitionFieldInfos;

    /**
     * 字段配置信息  如 odps的分区信息，mysql oracle 字段索引信息
     */
    private Map<String, Object> tableConf;


    public String getTableOwner() {
        return tableOwner;
    }

    public void setTableOwner(String tableOwner) {
        this.tableOwner = tableOwner;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Date getTableCreateTime() {
        return tableCreateTime;
    }

    public void setTableCreateTime(Date tableCreateTime) {
        this.tableCreateTime = tableCreateTime;
    }

    public String getTableCharsetName() {
        return tableCharsetName;
    }

    public void setTableCharsetName(String tableCharsetName) {
        this.tableCharsetName = tableCharsetName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<ColumnInfo> getFieldInfos() {
        return fieldInfos;
    }

    public void setFieldInfos(List<ColumnInfo> fieldInfos) {
        this.fieldInfos = fieldInfos;
    }

    public List<ColumnInfo> getPartitionFieldInfos() {
        return partitionFieldInfos;
    }

    public void setPartitionFieldInfos(List<ColumnInfo> partitionFieldInfos) {
        this.partitionFieldInfos = partitionFieldInfos;
    }

    public Date getTableUpdateTime() {
        return tableUpdateTime;
    }

    public void setTableUpdateTime(Date tableUpdateTime) {
        this.tableUpdateTime = tableUpdateTime;
    }

    public Long getTableRows() {
        return tableRows;
    }

    public void setTableRows(Long tableRows) {
        this.tableRows = tableRows;
    }

    public Long getTableStorageSize() {
        return tableStorageSize;
    }

    public void setTableStorageSize(Long tableStorageSize) {
        this.tableStorageSize = tableStorageSize;
    }

    public Long getTablePhysicalSize() {
        return tablePhysicalSize;
    }

    public void setTablePhysicalSize(Long tablePhysicalSize) {
        this.tablePhysicalSize = tablePhysicalSize;
    }

    public String getStorageUnit() {
        return storageUnit;
    }

    public void setStorageUnit(String storageUnit) {
        this.storageUnit = storageUnit;
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public Map<String, Object> getTableConf() {
        return tableConf;
    }

    public void setTableConf(Map<String, Object> tableConf) {
        this.tableConf = tableConf;
    }
}
