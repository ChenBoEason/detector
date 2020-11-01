package com.cbmiddleware.detector.dialect.model;

import com.alibaba.druid.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;

/**
 * Class Name is TableModel
 *
 * @author LiJun
 * Created on 2020/10/28 9:42 上午
 */
public class TableModel implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(TableModel.class);

    private String tableName;
    private String check;
    private String comment;
    private String engine;
    private Map<String, ColumnModel> columnsHashMap = new HashMap<>();
    private List<IndexModel> indices = new LinkedList<>();
    private List<UniqueIndexModel> uniqueIndex = new LinkedList<>();
    private Class<?> entityClass;
    private String alias;

    private Set<String> indexSet = new HashSet<>();
    private Set<String> uniqueIndexSet = new HashSet<>();

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

    public TableModel addUnique(UniqueIndexModel uniqueIndex) {
        this.uniqueIndex.add(uniqueIndex);
        return this;
    }

    public String getTableName() {
        return tableName;
    }

    public TableModel setTableName(String tableName) {
        this.tableName = tableName;
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

    public List<UniqueIndexModel> getUniqueIndex() {
        return uniqueIndex;
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

    public ColumnModel getColumn(String column) {
        return this.columnsHashMap.get(column);
    }
}
