package com.cbmiddleware.detector.sql.ddl.model;

import com.alibaba.druid.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;

/**
 * Class Name is TableModel
 * 表模型
 *
 * @author LiJun
 * Created on 2020/10/28 9:42 上午
 */
public class TableModel implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(TableModel.class);
    /**
     * 表名
     */
    private String tableName;
    /**
     * check 约束
     */
    private String check;
    /**
     * 表备注
     */
    private String comment;
    /**
     * 表引擎
     */
    private String engine;
    /**
     * 字符集
     */
    private String charset = "utf8";
    /**
     * 表不存在创建
     */
    private boolean ifNotExistsCreate = true;
    /**
     * 表字段内容
     */
    private Map<String, ColumnModel> columnsHashMap = new LinkedHashMap<>();
    /**
     * 表索引信息
     */
    private List<IndexModel> indices = new LinkedList<>();
    /**
     * 预留字段，用于类解析成字段和表信息
     */
    private Class<?> entityClass;
    /**
     * 表别名 -- 预留字段
     */
    private String alias;

    public TableModel(String tableName) {
        if (StringUtils.isEmpty(tableName)) {
            throw new IllegalArgumentException("tableName cannot be null!");
        }
        this.tableName = tableName;
    }

    public TableModel addColumns(ColumnModel... columns) {
        for (ColumnModel column : columns) {
            addColumn(column);
        }
        return this;
    }

    public TableModel addColumn(ColumnModel column) {
        if (this.columnsHashMap.containsKey(column.getColumnName())) {
            throw new IllegalArgumentException(String.format("TableModel:[%s], Column:[%s], duplicate columns are not allowed in the table!", this.tableName, column.getColumnName()));
        }
        this.columnsHashMap.put(column.getColumnName(), column);
        return this;
    }

    public TableModel removeColumn(String columnName) {
        if (!this.columnsHashMap.containsKey(columnName)) {
            log.warn("table:[{}] cannot found column:[{}]", this.tableName, columnName);
            return this;
        }
        this.columnsHashMap.remove(columnName);
        return this;
    }

    public TableModel addIndices(IndexModel... indices) {
        this.indices.addAll(Arrays.asList(indices));
        return this;
    }

    public String getTableName() {
        return tableName;
    }

    public TableModel setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }


    public boolean isIfNotExistsCreate() {
        return ifNotExistsCreate;
    }

    public TableModel setIfNotExistsCreate(boolean ifNotExistsCreate) {
        this.ifNotExistsCreate = ifNotExistsCreate;
        return this;
    }

    public String getCheck() {
        return check;
    }

    public TableModel setCheck(String check) {
        this.check = check;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public TableModel setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public String getEngine() {
        return engine;
    }

    public TableModel setEngine(String engine) {
        this.engine = engine;
        return this;
    }

    public Collection<ColumnModel> getColumns() {
        return columnsHashMap.values();
    }

    public List<IndexModel> getIndices() {
        return indices;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public TableModel setEntityClass(Class<?> entityClass) {
        this.entityClass = entityClass;
        return this;
    }

    public String getAlias() {
        return alias;
    }

    public TableModel setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    public TableModel charset(String charsetName) {
        this.charset = charsetName;
        return this;
    }

    public String getCharset() {
        return charset;
    }

    public ColumnModel getColumn(String column) {
        return this.columnsHashMap.get(column);
    }
}
