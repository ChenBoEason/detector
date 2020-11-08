package com.cbmiddleware.detector.sql.multiple;

import com.alibaba.fastjson.JSON;
import com.cbmiddleware.detector.constant.DataBaseType;
import com.cbmiddleware.detector.exception.DetectorException;
import com.cbmiddleware.detector.constant.FunctionType;
import com.cbmiddleware.detector.sql.constant.ColumnRelation;
import com.cbmiddleware.detector.sql.constant.ConditionType;
import com.cbmiddleware.detector.sql.constant.TableRelation;
import com.cbmiddleware.detector.sql.function.*;
import com.cbmiddleware.detector.util.DataSourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eason(bo.chenb)
 * @description
 * @date 2020-03-19
 **/
public abstract class AbstractSqlGenerator implements SqlGenerator {


    private static final Logger logger = LoggerFactory.getLogger(AbstractSqlGenerator.class);

    @Override
    public String generate(GenerateSqlConfInfo sqlConfInfo) throws DetectorException {
        logger.info("sql conf:{}", JSON.toJSONString(sqlConfInfo));

        /* 翻译列语句（查询字段） */
        List<String> columnSentences = translateColumn(sqlConfInfo.getReturnColumns());

        QueryTable queryTable = sqlConfInfo.getQueryTable();

        /* 基表若有条件，取出来放到where条件中 */
        ConditionInfo conditionInfo = queryTable.getConditionInfo();
        queryTable.setConditionInfo(null);


        List<String> tableSentences = new ArrayList<>();
        /* 翻译表语句 */
        translateTable(queryTable, tableSentences);

        /* 构建sql语句 */
        StringBuilder sql = new StringBuilder("SELECT \n");

        for (int i = 0; i < columnSentences.size() - 1; i++) {
            sql.append(columnSentences.get(i)).append(", \n");
        }

        sql.append(columnSentences.get(columnSentences.size() - 1)).append(" \nFROM \n");

        for (int i = 0; i < tableSentences.size() - 1; i++) {
            sql.append(tableSentences.get(i)).append(" \n");
        }
        sql.append(tableSentences.get(tableSentences.size() - 1));

        /* where条件 */
        ConditionInfo where = sqlConfInfo.getWhere();

        if (conditionInfo != null) {
            if (where == null) {
                /* 第一个条件不需要加任何关系 */
                conditionInfo.setColumnRelation(null);
                where = conditionInfo;
            } else {
                appendWhere(where, conditionInfo, 0, 50);
            }
        }


        if (where != null) {
            StringBuilder builder = new StringBuilder();
            translateCondition(queryTable.getTableAlias(), where, builder);
            if (!StringUtils.isEmpty(builder.toString())) {
                sql.append(" \nWHERE ").append(builder.toString());
            }
        }

        sql.append(";");
        logger.info("sql info\n{}", sql.toString());
        return sql.toString();
    }

    private static void appendWhere(ConditionInfo where, ConditionInfo conditionInfo, long dfsCount, final long expectCount) throws DetectorException{

        if (++dfsCount >= expectCount) {
            throw new DetectorException(String.format("self invocation more than %s", expectCount));
        }

        /* 当下一个条件为空时将该条件加入进去 */
        if (where.getConditionInfo() == null) {

            where.setConditionInfo(conditionInfo);
            return;
        }

        appendWhere(where.getConditionInfo(), conditionInfo, dfsCount, expectCount);
    }


    /**
     * 将配置翻译成sql语句 (dfs 递归调用)
     *
     * @param queryTable
     * @return
     */
    public void translateTable(QueryTable queryTable, List<String> sqlSentence) throws DetectorException {
        ConditionInfo conditionInfo = queryTable.getConditionInfo();
        List<QueryTable> queryTables = queryTable.getQueryTables();
        String tableAlias = queryTable.getTableAlias();
        String tableName = queryTable.getTableName();
        TableRelation tableRelation = queryTable.getTableRelation();

        if (StringUtils.isEmpty(tableAlias)) {
            tableAlias = tableName;
        }

        StringBuilder sql = new StringBuilder();

        if (tableRelation != null) {
            switch (tableRelation) {
                case inner_join:
                    sql.append("inner join ");
                    sql.append(tableName);
                    sql.append(" ").append(tableAlias);

                    if (conditionInfo != null) {
                        sql.append(" on ");
                    }
                    break;
                case union:
                    break;
                case left_join:
                    sql.append("left join ");
                    sql.append(tableName);
                    sql.append(" ").append(tableAlias);
                    if (conditionInfo != null) {
                        sql.append(" on ");
                    }
                    break;
                case right_join:
                    sql.append("right join ");
                    sql.append(tableName);
                    sql.append(" ").append(tableAlias);
                    if (conditionInfo != null) {
                        sql.append(" on ");
                    }
                    break;
                default:
                    break;
            }
        } else {
            sql.append(tableName).append(" ").append(tableAlias).append(" ");
        }

        /* 设置条件 */
        if (conditionInfo != null) {
            StringBuilder conditionSql = new StringBuilder();
            translateCondition(tableAlias, conditionInfo, conditionSql);
            sql.append(conditionSql.toString());
        }

        sqlSentence.add(sql.toString());
        if (queryTables == null) {
            return;
        }

        /* 遍历同级以及下一级内容 */
        for (QueryTable table : queryTables) {
            translateTable(table, sqlSentence);
        }
    }

    /**
     * 将条件配置翻译成sql条件语句
     *
     * @param tableAlias
     * @param conditionInfo
     * @param conditionSql
     */
    public void translateCondition(final String tableAlias, ConditionInfo conditionInfo, StringBuilder conditionSql) throws DetectorException {

        String name = conditionInfo.getName();
        ConditionType conditionType = conditionInfo.getConditionType();
        Object value = conditionInfo.getValue();

        String conditionTableAlias = conditionInfo.getConditionTableAlias();
        String conditionColumnName = conditionInfo.getConditionColumnName();

        ColumnRelation columnRelation = conditionInfo.getColumnRelation();

        /* 连接字段条件类型 and/or */
        if (columnRelation != null) {
            conditionSql.append(" ").append(columnRelation.name()).append(" ");
        }

        /* t.column = 'xxx' | t.column = 12 */
        if (value != null) {
            conditionSql.append(tableAlias).append(".").append(name)
                    .append(" ").append(conditionType.getSymbol())
                    .append(" ");
            DataSourceUtils.getValue(value, conditionSql, this.dataBaseType());
        }

        /*t.column = c.column*/
        if (conditionTableAlias != null && conditionColumnName != null) {
            conditionSql.append(tableAlias).append(".").append(name)
                    .append(" ").append(conditionType.getSymbol())
                    .append(" ").append(conditionTableAlias).append(".").append(conditionColumnName);
        }

        if (conditionInfo.getConditionInfo() == null) {
            return;
        }
        /* 进入下一层 */
        translateCondition(tableAlias, conditionInfo.getConditionInfo(), conditionSql);
    }


    /**
     * @param queryColumns
     * @return
     */
    public List<String> translateColumn(List<QueryColumn> queryColumns) throws DetectorException {
        List<String> columnSentence = new ArrayList<>(queryColumns.size());

        for (QueryColumn returnColumn : queryColumns) {

            if (StringUtils.isEmpty(returnColumn.getTableAlias())) {
                returnColumn.setTableAlias(returnColumn.getTableName());
            }

            StringBuilder sentence;
            /* 如果为空则输入null */
            if (StringUtils.isEmpty(returnColumn.getTableAlias()) || StringUtils.isEmpty(returnColumn.getOriginalColumnName())) {
                sentence = new StringBuilder("NULL");
            } else {

                if (returnColumn.getFunctionParam() != null) {
                    sentence = buildFunction(returnColumn.getFunctionParam(), this.dataBaseType());
                } else {
                    sentence = new StringBuilder(returnColumn.getTableAlias())
                            .append(".").append(returnColumn.getOriginalColumnName());
                }


            }

            columnSentence.add(sentence.append(" AS ").append(returnColumn.getColumnAlias()).toString());
        }

        return columnSentence;
    }


    @Override
    public void join(ExtraCondition extraCondition, QueryTable queryTable) throws DetectorException {
        this.join(extraCondition, queryTable, 0, 100);
    }


    /**
     * @param extraCondition 额外的条件（待合并的参数）
     * @param queryTable     原始配置
     * @param dfsCount       当前dfs数
     * @param expectCount    期望dfs调用次数
     * @throws DetectorException
     */
    @Override
    public void join(ExtraCondition extraCondition, QueryTable queryTable, long dfsCount, final long expectCount) throws DetectorException {

        if (++dfsCount >= expectCount) {
            throw new DetectorException(String.format("self invocation more than %s", expectCount));
        }

        if (extraCondition == null) {
            return;
        }

        String tableName = extraCondition.getTableName();
        String queryTableTableName = queryTable.getTableName();

        /* 表相等追加到条件后面 */
        if (com.alibaba.druid.util.StringUtils.equals(tableName, queryTableTableName)) {
            ConditionInfo conditionInfo = queryTable.getConditionInfo();

            if (conditionInfo == null) {
                queryTable.setConditionInfo(new ConditionInfo(extraCondition.getColumnName(), extraCondition.getConditionType(),
                        extraCondition.getValue(), extraCondition.getRelation()));

                return;
            }

            mergeCondition(extraCondition, conditionInfo, 0, expectCount);
            return;
        }

        List<QueryTable> queryTables = queryTable.getQueryTables();

        if (queryTables == null) {
            return;
        }

        for (QueryTable table : queryTables) {
            join(extraCondition, table, dfsCount, expectCount);
        }


    }

    private static void mergeCondition(ExtraCondition extraCondition, ConditionInfo conditionInfo, long dfsCount, final long expectCount) throws DetectorException {

        if (++dfsCount >= expectCount) {
            throw new DetectorException(String.format("self invocation more than %s", expectCount));
        }

        /* 当下一个条件为空时将该条件加入进去 */
        if (conditionInfo.getConditionInfo() == null) {

            conditionInfo.setConditionInfo(new ConditionInfo(extraCondition.getColumnName(),
                    extraCondition.getConditionType(), extraCondition.getValue(), extraCondition.getRelation()));
            return;
        }

        mergeCondition(extraCondition, conditionInfo.getConditionInfo(), dfsCount, expectCount);
    }


    private static StringBuilder buildFunction(SqlFunctionParam functionParam, DataBaseType dataBaseType) throws DetectorException {
        FunctionType functionType = functionParam.type();
        StringBuilder builder = new StringBuilder();

        switch (functionType) {
            case string2date:
                ColumnString2Date columnString2Date = (ColumnString2Date) functionParam;
                DataSourceUtils.toDate(new StringBuilder(columnString2Date.getTableAlias()).append(".").append(columnString2Date.getColumnName()).toString(),
                        columnString2Date.getFormat(), dataBaseType, builder);
                return builder;
            case range:
                ColumnRange columnRange = (ColumnRange) functionParam;
                DataSourceUtils.range(new StringBuilder(columnRange.getTableAlias()).append(".").append(columnRange.getColumnName()).toString(),
                        columnRange.getRanges(), columnRange.getRangeType(), dataBaseType, builder);
                return builder;
            case casewhen:
                ColumnCaseWhen caseWhen = (ColumnCaseWhen) functionParam;
                DataSourceUtils.casewhen(new StringBuilder(caseWhen.getTableAlias()).append(".").append(caseWhen.getColumnName()).toString(),
                        caseWhen.getConditions(), dataBaseType, builder);
                return builder;
            case date2String:
                ColumnDate2String columnDate2String = (ColumnDate2String) functionParam;
                DataSourceUtils.dateToString(new StringBuilder(columnDate2String.getTableAlias()).append(".").append(columnDate2String.getColumnName()).toString(),
                        columnDate2String.getFormat(), dataBaseType, builder);
                return builder;
            default:
                return builder.append("NULL");


        }
    }

    /**
     * 数据源类型
     *
     * @return
     */
    public abstract DataBaseType dataBaseType();
}
