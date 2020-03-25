package com.cbmiddleware.detector.entity;

/**
 * @author Eason(bo.chenb)
 * @description 字段信息
 * @date 2020-03-05
 **/
public class ColumnInfo {

    /**
     * 数据库名
     */
    private String project;

    /**
     * 数据库表名
     */
    private String tableName;
    /**
     * 列名
     */
    private String columnName;

    /**
     * 序数位置
     */
    private Integer ordinalPosition;

    /**
     * 列的默认值
     */
    private String columnDefault;
    /**
     * 是否允许为空
     */
    private Boolean allowedNull = true;
    /**
     * 数据类型
     */
    private String dataType;
    /**
     * 字符型允许最大长度  MYSQL中为CHARACTER_MAXIMUM_LENGTH
     *  属性CHARACTER_OCTET_LENGTH为最大八字节长度 = CHARACTER_MAXIMUM_LENGTH*4
     */
    private String characterMaxLength;
    /**
     * 字符集名称 如utf8mb4 MYSQL中为CHARACTER_SET_NAME
     */
    private String charsetName;
    /**
     * 列所属字符属性 如 utf8mb4_general_ci MYSQL中为COLLATION_NAME
     */
    private String charsetValue;

    /**
     * 列数据类型 如varchar(20) MYSQL中为COLUMN_TYPE
     */
    private String columnType;
    /**
     * 列所居属性如 索引，外键、主键  MYSQL中为COLUMN_KEY
     */
    private String columnKey;
    /**
     * 有哪些规则 如 自增  MYSQL中为EXTRA
     */
    private String extra;
    /**
     * 备注  MYSQL中为COLUMN_COMMENT
     */
    private String columnComment;

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

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Integer getOrdinalPosition() {
        return ordinalPosition;
    }

    public void setOrdinalPosition(Integer ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }

    public String getColumnDefault() {
        return columnDefault;
    }

    public void setColumnDefault(String columnDefault) {
        this.columnDefault = columnDefault;
    }

    public Boolean getAllowedNull() {
        return allowedNull;
    }

    public void setAllowedNull(Boolean allowedNull) {
        this.allowedNull = allowedNull;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getCharacterMaxLength() {
        return characterMaxLength;
    }

    public void setCharacterMaxLength(String characterMaxLength) {
        this.characterMaxLength = characterMaxLength;
    }

    public String getCharsetName() {
        return charsetName;
    }

    public void setCharsetName(String charsetName) {
        this.charsetName = charsetName;
    }

    public String getCharsetValue() {
        return charsetValue;
    }

    public void setCharsetValue(String charsetValue) {
        this.charsetValue = charsetValue;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getColumnKey() {
        return columnKey;
    }

    public void setColumnKey(String columnKey) {
        this.columnKey = columnKey;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }
}
