package com.cbmiddleware.detector.detector;

import com.alibaba.fastjson.JSON;
import com.cbmiddleware.detector.exception.DetectorException;
import com.cbmiddleware.detector.mysql.sql.MysqlQuerySqlGenerator;
import com.cbmiddleware.detector.sql.constant.ColumnRelation;
import com.cbmiddleware.detector.sql.constant.ConditionType;
import com.cbmiddleware.detector.sql.constant.TableRelation;
import com.cbmiddleware.detector.sql.function.ColumnCaseWhen;
import com.cbmiddleware.detector.sql.multiple.*;
import org.junit.Test;

import java.util.*;

/**
 * @author Eason(bo.chenb)
 * @description
 * @date 2020-03-19
 **/
public class MysqlQuerySqlGeneratorTest {


    @Test
    public void threeTableJoin() {

        String sql = "SELECT\n" +
                "\tt.id AS id,\n" +
                "\tt.project AS project,\n" +
                "\tt.table_name AS tableName,\n" +
                "\tc.column_comment AS columnComment,\n" +
                "\tc.column_name AS columnName,\n" +
                "\ts.url as jdbcUrl,\n" +
                "\ts.username as dbUsername,\n" +
                "\tem.data_element_column_name as elementColmnName,\n" +
                "\tNULL AS emptyColumn \n" +
                "FROM\n" +
                "\tmed_detect_table t\n" +
                "\tINNER JOIN med_detect_table_column c ON c.table_name = t.name\n" +
                "\tLEFT JOIN med_data_source s on  t.db_code = s.db_code\n" +
                "\tLEFT JOIN med_data_element_mapping_info em on em.column_name = c.column_name and em.name = 'chenbo'\n" +
                "WHERE\n" +
                "\tt.db_code = 'xBbGIw2C' \n" +
                "\tAND t.creator = 'SYSTEM'\n" +
                "\tAND t.gmt_create <= NOW()";

        GenerateSqlConfInfo generateSqlConf = new GenerateSqlConfInfo();

        ExtraCondition extraCondition = new ExtraCondition();
        extraCondition.setTableName("med_data_source");
        extraCondition.setConditionType(ConditionType.gt);
        extraCondition.setColumnName("time");
        extraCondition.setValue("%s");
        extraCondition.setRelation(ColumnRelation.and);

        System.out.println(JSON.toJSONString(extraCondition));

        /**
         * 构建 med_detect_table 表配置
         */
        QueryTable detectTable = new QueryTable("med_detect_table", "t");
        /* gmt_create 条件 */
        ConditionInfo gmtContidion = new ConditionInfo("gmt_create", ConditionType.lte, new Date(), ColumnRelation.and);
        ConditionInfo creatorCondition = new ConditionInfo("creator", ConditionType.eq, "SYSTEM", ColumnRelation.and, gmtContidion);
        ConditionInfo dbCodeCondition = new ConditionInfo("db_code", ConditionType.eq,"xBbGIw2C",null, creatorCondition);


        /* 设置基表where条件 */
        generateSqlConf.setWhere(dbCodeCondition);

        /**
         * 构建med_detect_table_column表配置
         */
        QueryTable columnTable = new QueryTable("med_detect_table_column", "c");
        /* 与med_detect_table inner join */
        columnTable.setTableRelation(TableRelation.inner_join);
        columnTable.setConditionInfo(new ConditionInfo("table_name", ConditionType.eq, "t", "name"));

        /**
         * med_data_source
         */
        QueryTable datasourceTable = new QueryTable("med_data_source", "s");
        /* 与med_detect_table left join */
        datasourceTable.setTableRelation(TableRelation.left_join);
        datasourceTable.setConditionInfo(new ConditionInfo("db_code", ConditionType.eq, "t", "db_code"));

        /**
         * med_data_element_mapping_info
         */
        QueryTable dataElementTable = new QueryTable("med_data_element_mapping_info", "em");
        /* 与med_detect_table_column left join */
        dataElementTable.setTableRelation(TableRelation.left_join);
        dataElementTable.setConditionInfo(new ConditionInfo("column_name", ConditionType.eq, "c", "column_name"));


        List<QueryTable> queryTables = new ArrayList<>();
        /* med_detect_table_column 和med_data_element_mapping_info与med_detect_table关联 */
        queryTables.add(columnTable);
        //columnTable.setQueryTables(datasourceTable);
       // queryTables.add(datasourceTable);
        queryTables.add(datasourceTable);
        detectTable.setQueryTables(queryTables);

        /* med_data_element_mapping_info 与构建med_detect_table_column表配置关联 */
        List<QueryTable> columnQueryTables = new ArrayList<>();
        columnQueryTables.add(dataElementTable);
        detectTable.setQueryTables(columnQueryTables);

        generateSqlConf.setQueryTable(detectTable);

        /**
         * 返回字段
         */
        List<QueryColumn> returnColumns = new ArrayList<>();

        returnColumns.add(new QueryColumn("t", "id", "id"));
        returnColumns.add(new QueryColumn("t", "project", "project"));
        returnColumns.add(new QueryColumn("t", "table_name", "tableName"));
        returnColumns.add(new QueryColumn("c", "column_comment", "columnComment"));


        QueryColumn queryColumn = new QueryColumn("c", "column_name", "columnName");


        ColumnCaseWhen columnCaseWhen = new ColumnCaseWhen();
        columnCaseWhen.setTableAlias("c");
        columnCaseWhen.setColumnName("column_name");

        /**
         * case when
         */
        /*List<CaseWhenCondition> conditions = new ArrayList<>();
        columnCaseWhen.setConditions(conditions);

        conditions.add(new CaseWhenCondition("eason", ConditionType.eq, "chenbo"));

        conditions.add(new CaseWhenCondition(1, ConditionType.eq, "one"));

        queryColumn.setFunctionParam(columnCaseWhen);*/
        returnColumns.add(queryColumn);



        returnColumns.add(new QueryColumn(null, null, "columnName"));

        returnColumns.add(new QueryColumn("s", "url", "jdbcUrl"));
        returnColumns.add(new QueryColumn("s", "username", "dbUsername"));
        returnColumns.add(new QueryColumn("em", "data_element_column_name", "elementColmnName"));

        generateSqlConf.setReturnColumns(returnColumns);

        QuerySqlGenerator querySqlGenerator = new MysqlQuerySqlGenerator();

        /*try {
            querySqlGenerator.join(extraCondition, generateSqlConf.getQueryTable(), 0, 50);
        } catch (DetectorException e) {
            e.printStackTrace();
        }*/

        String generateSql = null;
        try {
            System.out.println(JSON.toJSONString(generateSqlConf));
            generateSql = querySqlGenerator.generate(generateSqlConf);
        } catch (DetectorException e) {
            e.printStackTrace();
        }

        /*Map<String, Object> sqlConfMap = new HashMap<>(2);
        sqlConfMap.put("extraCondition", extraCondition);
        sqlConfMap.put("sqlConfInfo", generateSqlConf);
        //System.out.println(JSON.toJSONString(sqlConfMap));
        System.out.println(JSON.toJSONString(extraCondition));
        System.out.println(JSON.toJSONString(generateSqlConf));*/
    }

    @Test
    public void twoTableJoin() {

        QuerySqlGenerator querySqlGenerator = new MysqlQuerySqlGenerator();


        String sql = "SELECT\n" +
                "\tt.id as id,\n" +
                "\tt.project as project,\n" +
                "\tt.table_name as tableName,\n" +
                "\tc.column_comment as columnComment,\n" +
                "\tc.column_name as columnName\n" +
                "FROM\n" +
                "\tmed_detect_table t\n" +
                "\tINNER JOIN med_detect_table_column c ON c.table_name = t.table_name \n" +
                "WHERE\n" +
                "\tt.db_code = 'xBbGIw2C' \n" +
                "\tAND t.creator = 'SYSTEM'";

        GenerateSqlConfInfo generateSqlConf = new GenerateSqlConfInfo();

        /**
         * 构建 med_detect_table 表配置
         */
        QueryTable detectTable = new QueryTable("med_detect_table", "t");

        ConditionInfo dbCodeCondition = new ConditionInfo("db_code", ConditionType.eq,
                "xBbGIw2C",
                null,
                new ConditionInfo("creator", ConditionType.eq, "SYSTEM", ColumnRelation.and));

        generateSqlConf.setWhere(dbCodeCondition);

        /**
         * 构建med_detect_table_column表配置
         */
        QueryTable columnTable = new QueryTable("med_detect_table_column", "c");
        columnTable.setTableRelation(TableRelation.inner_join);
        columnTable.setConditionInfo(new ConditionInfo("table_name", ConditionType.eq, "t", "table_name"));

        /**
         * 两表关联
         */
        List<QueryTable> queryTables = new ArrayList<>();
        queryTables.add(columnTable);
        detectTable.setQueryTables(queryTables);

        generateSqlConf.setQueryTable(detectTable);

        /**
         * 字段设置
         */
        List<QueryColumn> returnColumns = new ArrayList<>();

        returnColumns.add(new QueryColumn("t", "id", "id"));
        returnColumns.add(new QueryColumn("t", "project", "project"));
        returnColumns.add(new QueryColumn("t", "table_name", "tableName"));
        returnColumns.add(new QueryColumn("c", "column_comment", "columnComment"));
        returnColumns.add(new QueryColumn("c", "column_name", "columnName"));
        returnColumns.add(new QueryColumn(null, null, "columnName"));

        generateSqlConf.setReturnColumns(returnColumns);


        String generateSql = null;
        try {
            generateSql = querySqlGenerator.generate(generateSqlConf);
        } catch (DetectorException e) {
            e.printStackTrace();
        }
        //System.out.println(generateSql);

        //System.out.println(JSON.toJSONString(generateSqlConf));
    }


}
