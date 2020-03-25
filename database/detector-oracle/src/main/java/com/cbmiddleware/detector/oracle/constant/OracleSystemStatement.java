package com.cbmiddleware.detector.oracle.constant;

import com.cbmiddleware.detector.constant.RdsCoreKey;

/**
 * @author Eason(bo.chenb)
 * @description mysql系统语句，查询系统数据相关的sql语句
 * @date 2020-03-06
 **/
public class OracleSystemStatement {

    public static final String TEST_SQL = "select sysdate from dual";

    /**
     * 查询表中字段信息
     */
    public static final String TABLE_INFO = "select * from all_tab_columns where Table_Name in (%s)";

    /**
     * 查询项目下所有表信息
     */
    public static final String TABLES = new StringBuilder("select \n")
            .append("t.owner as ").append(RdsCoreKey.table_owner).append(", \n")
            .append("t.tablespace_name as ").append(RdsCoreKey.project).append(", \n")
            .append("t.table_name as ").append(RdsCoreKey.table_name).append(", \n")
            .append("t.num_rows as ").append(RdsCoreKey.table_rows).append(", \n")
            .append("c.comments as ").append(RdsCoreKey.table_remark).append(", \n")
            .append("c.table_type as ").append(RdsCoreKey.table_type).append(", \n")
            .append("o.created as ").append(RdsCoreKey.table_create_time).append(", \n")
            .append("o.last_ddl_time as ").append(RdsCoreKey.table_update_time).append(", \n")
            .append("s.bytes as ").append(RdsCoreKey.table_storage_size).append(",\n")
            .append("s.bytes as ").append(RdsCoreKey.table_physical_size).append(",\n")
            .append("null as ").append(RdsCoreKey.table_char_name).append(" \n")
            .append("from dba_tables t \n")
            .append("inner join all_tab_comments c on c.table_name = t.table_name\n")
            .append("inner join all_objects o on o.object_name = t.table_name and o.object_type = 'TABLE'\n")
            .append("inner join dba_segments s on s.segment_name = t.table_name and s.segment_type='TABLE'\n")
            .append("where t.tablespace_name = '%s'")
            .toString();

    /**
     * 查询项目下部分表信息
     */
    public static final String SOME_TABLES = new StringBuilder("select \n")
            .append("t.owner as ").append(RdsCoreKey.table_owner).append(", \n")
            .append("t.tablespace_name as ").append(RdsCoreKey.project).append(", \n")
            .append("t.table_name as ").append(RdsCoreKey.table_name).append(", \n")
            .append("t.num_rows as ").append(RdsCoreKey.table_rows).append(", \n")
            .append("c.comments as ").append(RdsCoreKey.table_remark).append(", \n")
            .append("c.table_type as ").append(RdsCoreKey.table_type).append(", \n")
            .append("o.created as ").append(RdsCoreKey.table_create_time).append(", \n")
            .append("o.last_ddl_time as ").append(RdsCoreKey.table_update_time).append(", \n")
            .append("s.bytes as ").append(RdsCoreKey.table_storage_size).append(",\n")
            .append("s.bytes as ").append(RdsCoreKey.table_physical_size).append(",\n")
            .append("null as ").append(RdsCoreKey.table_char_name).append(" \n")
            .append("from dba_tables t \n")
            .append("inner join all_tab_comments c on c.table_name = t.table_name\n")
            .append("inner join all_objects o on o.object_name = t.table_name and o.object_type = 'TABLE'\n")
            .append("inner join dba_segments s on s.segment_name = t.table_name and s.segment_type='TABLE'\n")
            .append("where t.tablespace_name = '%s'\n")
            .append("and t.table_name in (%s)")
            .toString();

    public static final String TABLE_SIZE = new StringBuilder("select \n")
            .append("t.tablespace_name as ").append(RdsCoreKey.project).append(", \n")
            .append("t.table_name as ").append(RdsCoreKey.table_name).append(", \n")
            .append("t.num_rows as ").append(RdsCoreKey.table_rows).append(", \n")
            .append("s.bytes as ").append(RdsCoreKey.table_storage_size).append(",\n")
            .append("s.bytes as ").append(RdsCoreKey.table_physical_size).append("\n")
            .append("from dba_tables t \n")
            .append("inner join dba_segments s on s.segment_name = t.table_name and s.segment_type='TABLE'\n")
            .append("where t.tablespace_name = '%s'\n")
            .append("and t.table_name in (%s)")
            .toString();


}
