package com.cbmiddleware.detector.constant;

/**
 * @author Eason(bo.chenb)
 * @description mysql系统语句，查询系统数据相关的sql语句
 * @date 2020-03-06
 **/
public class OdpsSystemStatement {

    public static final String TEST_SQL = "select getdate();";


    public static final String COUNT_SQL = "select count(*) as table_rows from %s;";
}
