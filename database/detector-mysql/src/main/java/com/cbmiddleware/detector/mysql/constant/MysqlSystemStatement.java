package com.cbmiddleware.detector.mysql.constant;

import com.cbmiddleware.detector.constant.RdsCoreKey;

/**
 * @author Eason(bo.chenb)
 * @description mysql系统语句，查询系统数据相关的sql语句
 * @date 2020-03-06
 **/
public class MysqlSystemStatement {

    public static final String TEST_SQL = "select current_date";

    /**
     * 查询表中字段信息
     */
    public static final String TABLE_INFO = "select * from information_schema.columns where table_name in (%s) and table_schema = '%s'";

    /**
     * mysql 查询项目下所有表信息
     */
    public static final String TABLES = new StringBuilder()
            .append("SELECT \n")
            .append("null as ").append(RdsCoreKey.table_owner).append(",\n")
            .append("t.table_schema as ").append(RdsCoreKey.project).append(",\n")
            .append("t.table_name as ").append(RdsCoreKey.table_name).append(",\n")
            .append("t.table_comment as ").append(RdsCoreKey.table_remark).append(",\n")
            .append("t.create_time as ").append(RdsCoreKey.table_create_time).append(",\n")
            .append("t.table_collation as ").append(RdsCoreKey.table_char_name).append(",\n")
            .append("t.table_type as ").append(RdsCoreKey.table_type).append(",\n")
            .append("t.table_rows as ").append(RdsCoreKey.table_rows).append(",\n")
            .append("t.data_length as ").append(RdsCoreKey.table_storage_size).append(",\n")
            .append("t.data_length as ").append(RdsCoreKey.table_physical_size).append(",\n")
            .append("t.update_time as ").append(RdsCoreKey.table_update_time).append("\n")
            .append("from information_schema.TABLES t\n")
            .append("WHERE table_schema = '%s'")
            .toString();

    /**
     * mysql 查询项目下部分表信息
     */
    public static final String SOME_TABLES = new StringBuilder()
            .append("SELECT \n")
            .append("null as ").append(RdsCoreKey.table_owner).append(",\n")
            .append("t.table_schema as ").append(RdsCoreKey.project).append(",\n")
            .append("t.table_name as ").append(RdsCoreKey.table_name).append(",\n")
            .append("t.table_comment as ").append(RdsCoreKey.table_remark).append(",\n")
            .append("t.create_time as ").append(RdsCoreKey.table_create_time).append(",\n")
            .append("t.table_collation as ").append(RdsCoreKey.table_char_name).append(",\n")
            .append("t.table_type as ").append(RdsCoreKey.table_type).append(",\n")
            .append("t.table_rows as ").append(RdsCoreKey.table_rows).append(",\n")
            .append("t.data_length as ").append(RdsCoreKey.table_storage_size).append(",\n")
            .append("t.data_length as ").append(RdsCoreKey.table_physical_size).append(",\n")
            .append("t.update_time as ").append(RdsCoreKey.table_update_time).append("\n")
            .append("from information_schema.TABLES t\n")
            .append("WHERE table_schema = '%s'\n")
            .append("and t.table_name in (%s)")
            .toString();

    public static final String TABLE_SIZE = new StringBuilder()
            .append("SELECT \n")
            .append("t.table_schema as ").append(RdsCoreKey.project).append(",\n")
            .append("t.table_name as ").append(RdsCoreKey.table_name).append(",\n")
            .append("t.table_rows as ").append(RdsCoreKey.table_rows).append(",\n")
            .append("t.data_length as ").append(RdsCoreKey.table_storage_size).append(",\n")
            .append("t.data_length as ").append(RdsCoreKey.table_physical_size).append("\n")
            .append("from information_schema.TABLES t\n")
            .append("WHERE table_schema = '%s'\n")
            .append("and t.table_name in (%s)")
            .toString();
}
