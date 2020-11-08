package com.cbmiddleware.detector.sql.multiple;


import java.util.List;

/**
 * @author Eason(bo.chenb)
 * @description
 *    sql生成配置信息
 * @date 2020-03-18
 **/
public class GenerateSqlConfInfo {

    /**
     * 返回列信息
     */
    private List<QueryColumn> returnColumns;
    /**
     * 查询表信息
     */
    private QueryTable queryTable;

    /**
     * 条件信息 where 后面的字段条件
     */
    private ConditionInfo where;


    public List<QueryColumn> getReturnColumns() {
        return returnColumns;
    }

    public void setReturnColumns(List<QueryColumn> returnColumns) {
        this.returnColumns = returnColumns;
    }

    public QueryTable getQueryTable() {
        return queryTable;
    }

    public void setQueryTable(QueryTable queryTable) {
        this.queryTable = queryTable;
    }

    public ConditionInfo getWhere() {
        return where;
    }

    public void setWhere(ConditionInfo where) {
        this.where = where;
    }
}
