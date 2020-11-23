package com.cbmiddleware.detector.sql.ddl.model;


import com.alibaba.druid.util.StringUtils;
import com.cbmiddleware.detector.sql.ddl.rules.Type;

import java.io.Serializable;

/**
 * Class Name is ColumnModel
 * 字段模型
 * @author LiJun
 * Created on 2020/10/30 10:13 上午
 */
public class ColumnModel implements Serializable {

    /**
     * 字段名称
     */
    private String columnName;
    /**
     * 重命名字段名称,当不为空时执行rename操作
     */
    private String renameColumnName;

    /**
     * 字段类型
     */
    private Type columnType;

    /**
     * 默认值
     */
    private Object defaultValue;

    /**
     * 是否是主键
     */
    private Boolean pKey = false;

    /**
     * 是否可空
     */
    private Boolean nullable = true;

    private Boolean autoIncrement = false;

    /**
     * 备注
     */
    private String comment;

    /**
     * check约束
     */
    private String check;

    /**
     * 尾部拓展字段
     */
    private String tail;

    /**
     * 长度
     */
    private int length = 255;

    private int precision = 0;

    private int scale = 0;


    private boolean unique = false;
    /**
     * 长度
     */
    private Integer[] lengths = new Integer[]{};


    public ColumnModel(String columnName) {
        this.columnName = columnName;
    }

    public ColumnModel pk() {
        this.pKey = true;
        return this;
    }

    public ColumnModel uq() {
        this.unique = true;
        return this;
    }

    public boolean isUnique() {
        return this.unique;
    }

    public String getColumnName() {
        return columnName;
    }

    public ColumnModel setColumnName(String columnName) {
        if (StringUtils.isEmpty(columnName)) {
            throw new IllegalArgumentException("columnName can not be null or empty");
        }
        this.columnName = columnName;
        return this;
    }

    public Type getColumnType() {
        return columnType;
    }


    public Object getDefaultValue() {
        return defaultValue;
    }

    public ColumnModel setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public ColumnModel notNull() {
        this.nullable = false;
        return this;
    }

    public Boolean getPKey() {
        return pKey;
    }

    public boolean isPkey() {
        return null != pKey && pKey;
    }

    public ColumnModel setPKey(Boolean pKey) {
        this.pKey = pKey;
        return this;
    }

    public boolean getNullable() {
        return nullable == null || nullable;
    }

    public ColumnModel setNullable(Boolean nullable) {
        this.nullable = nullable;
        return this;
    }


    public ColumnModel LONG(int length) {
        this.columnType = Type.BIGINT;
        this.lengths = new Integer[]{length};
        return this;
    }

    public ColumnModel BOOL() {
        this.columnType = Type.BOOLEAN;
        return this;
    }

    public ColumnModel DOUBLE() {
        this.columnType = Type.DOUBLE;
        return this;
    }

    public ColumnModel FLOAT(Integer... lengths) {
        this.columnType = Type.FLOAT;
        this.lengths = lengths;
        return this;
    }

    public ColumnModel INTEGER(int length) {
        this.columnType = Type.INTEGER;
        this.lengths = new Integer[]{length};
        return this;
    }

    public ColumnModel SHORT(int length) {
        this.columnType = Type.SMALLINT;
        this.lengths = new Integer[]{length};
        return this;
    }

    public ColumnModel BIGDECIMAL(Integer precision, Integer scale) {
        this.columnType = Type.NUMERIC;
        this.lengths = new Integer[]{precision, scale};
        return this;
    }

    public ColumnModel STRING(int length) {
        this.columnType = Type.VARCHAR;
        this.lengths = new Integer[]{length};
        return this;
    }

    public ColumnModel DATE() {
        this.columnType = Type.DATE;
        return this;
    }

    public ColumnModel DATETIME() {
        this.columnType = Type.DATETIME;
        return this;
    }

    public ColumnModel TIME() {
        this.columnType = Type.TIME;
        return this;
    }


    public ColumnModel TIMESTAMP() {
        this.columnType = Type.TIMESTAMP;
        return this;
    }

    public ColumnModel BIGINT(Integer length) {
        this.columnType = Type.BIGINT;
        this.lengths = new Integer[]{length};
        return this;
    }

    public ColumnModel BINARY(Integer... lengths) {
        this.columnType = Type.BINARY;
        this.lengths = lengths;
        return this;
    }

    public ColumnModel BIT() {
        this.columnType = Type.BIT;
        return this;
    }

    public ColumnModel BLOB(Integer... lengths) {
        this.columnType = Type.BLOB;
        this.lengths = lengths;
        return this;
    }

    public ColumnModel CHAR(Integer... lengths) {
        this.columnType = Type.CHAR;
        this.lengths = lengths;
        return this;
    }

    public ColumnModel CLOB(Integer... lengths) {
        this.columnType = Type.CLOB;
        this.lengths = lengths;
        return this;
    }

    public ColumnModel DECIMAL(Integer... lengths) {
        this.columnType = Type.DECIMAL;
        this.lengths = lengths;
        return this;
    }

    public ColumnModel JAVA_OBJECT() {
        this.columnType = Type.JAVA_OBJECT;
        return this;
    }

    public ColumnModel LONGNVARCHAR(Integer length) {
        this.columnType = Type.LONGNVARCHAR;
        this.lengths = new Integer[]{length};
        return this;
    }

    public ColumnModel LONGVARBINARY(Integer... lengths) {
        this.columnType = Type.LONGVARBINARY;
        this.lengths = lengths;
        return this;
    }

    public ColumnModel LONGVARCHAR(Integer... lengths) {
        this.columnType = Type.LONGVARCHAR;
        this.lengths = lengths;
        return this;
    }

    public ColumnModel NCHAR(Integer length) {
        this.columnType = Type.NCHAR;
        this.lengths = new Integer[]{length};
        return this;
    }

    public ColumnModel NCLOB() {
        this.columnType = Type.NCLOB;
        return this;
    }

    public ColumnModel NUMERIC(Integer... lengths) {
        this.columnType = Type.NUMERIC;
        this.lengths = lengths;
        return this;
    }

    public ColumnModel NVARCHAR(Integer length) {
        this.columnType = Type.NVARCHAR;
        this.lengths = new Integer[]{length};
        return this;
    }

    public ColumnModel OTHER(Integer... lengths) {
        this.columnType = Type.OTHER;
        this.lengths = lengths;
        return this;
    }

    public ColumnModel REAL() {
        this.columnType = Type.REAL;
        return this;
    }

    public ColumnModel SMALLINT(Integer length) {
        this.columnType = Type.SMALLINT;
        this.lengths = new Integer[]{length};
        return this;
    }

    public ColumnModel TINYINT(Integer length) {
        this.columnType = Type.TINYINT;
        this.lengths = new Integer[]{length};
        return this;
    }

    public ColumnModel VARBINARY(Integer... lengths) {
        this.columnType = Type.VARBINARY;
        this.lengths = lengths;
        return this;
    }

    public ColumnModel VARCHAR(Integer length) {
        this.columnType = Type.VARCHAR;
        this.lengths = new Integer[]{length};
        return this;
    }

    public ColumnModel VARCHAR2(Integer length) {
        this.columnType = Type.VARCHAR2;
        this.lengths = new Integer[]{length};
        return this;
    }

    public String getComment() {
        return comment;
    }

    public int getLength() {
        return length;
    }

    public int getPrecision() {
        return precision;
    }

    public int getScale() {
        return scale;
    }

    public Integer[] getLengths() {
        return lengths;
    }

    public String getCheck() {
        return check;
    }

    public ColumnModel setCheck(String check) {
        this.check = check;
        return this;
    }

    public String getTail() {
        return tail;
    }

    public ColumnModel setTail(String tail) {
        this.tail = tail;
        return this;
    }

    public ColumnModel setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public String getRenameColumnName() {
        return renameColumnName;
    }

    public ColumnModel setRenameColumnName(String renameColumnName) {
        this.renameColumnName = renameColumnName;
        return this;
    }

    public ColumnModel setUnique(boolean unique) {
        this.unique = unique;
        return this;
    }

    public Boolean getAutoIncrement() {
        return autoIncrement;
    }

    public ColumnModel autoIncrement() {
        this.autoIncrement = true;
        return this;
    }
}
