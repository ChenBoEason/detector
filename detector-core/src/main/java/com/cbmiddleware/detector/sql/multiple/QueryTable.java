package com.cbmiddleware.detector.sql.multiple;

import com.cbmiddleware.detector.sql.constant.TableRelation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eason(bo.chenb)
 * @description 查询表信息
 * @date 2020-03-18
 **/
public class QueryTable {


    /**
     * 表名
     */
    private String tableName;
    /**
     * 表的别名
     */
    private String tableAlias;
    /**
     * 表关联关系 left/right join
     */
    private TableRelation tableRelation;
    /**
     * 关联的表查询 最外层的表为基表
     */
    private List<QueryTable> queryTables;

    /**
     * 条件信息 where 后面的字段条件
     */
    private ConditionInfo conditionInfo;

    public QueryTable() {
    }

    public QueryTable(String tableName, String tableAlias) {
        this.tableName = tableName;
        this.tableAlias = tableAlias;
    }

    public QueryTable(String tableName, String tableAlias, TableRelation tableRelation, List<QueryTable> queryTables) {
        this.tableName = tableName;
        this.tableAlias = tableAlias;
        this.tableRelation = tableRelation;
        this.queryTables = queryTables;
    }

    public QueryTable(String tableName, String tableAlias, TableRelation tableRelation, QueryTable queryTable) {
        this.tableName = tableName;
        this.tableAlias = tableAlias;
        this.tableRelation = tableRelation;
        if (this.queryTables == null) {
            this.queryTables = new ArrayList<>();
        }
        this.queryTables.add(queryTable);
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableAlias() {
        return tableAlias;
    }

    public void setTableAlias(String tableAlias) {
        this.tableAlias = tableAlias;
    }

    public TableRelation getTableRelation() {
        return tableRelation;
    }

    public void setTableRelation(TableRelation tableRelation) {
        this.tableRelation = tableRelation;
    }

    public List<QueryTable> getQueryTables() {
        return queryTables;
    }

    public void setQueryTables(List<QueryTable> queryTables) {
        if (this.queryTables != null) {
            this.queryTables.addAll(queryTables);
        } else {
            this.queryTables = queryTables;
        }

    }

    public void setQueryTables(QueryTable queryTable) {
        if (this.queryTables == null) {
            this.queryTables = new ArrayList<>();
        }

        this.queryTables.add(queryTable);
    }

    public ConditionInfo getConditionInfo() {
        return conditionInfo;
    }

    public void setConditionInfo(ConditionInfo conditionInfo) {
        this.conditionInfo = conditionInfo;
    }
}
