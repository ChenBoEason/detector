package com.cbmiddleware.detector.constant;

/**
 * @author Eason(bo.chenb)
 * @description 关系型数据库常量池
 * @date 2020-03-10
 **/
public class RdsCoreKey {
    /**
     * 表所属用户
     */
    public static final String table_owner = "OWNER";

    /**
     * 表所属项目
     */
    public static final String project = "PROJECT";

    /**
     * 表名
     */
    public static final String table_name = "TABLENAME";

    /**
     * 表备注
     */
    public static final String table_remark = "REMARK";

    /**
     * 表创建时间
     */
    public static final String table_create_time = "TABLECREATETIME";

    /**
     * 表最后更新时间
     */
    public static final String table_update_time = "TABLEUPDATEETIME";


    /**
     * 表字符集
     */
    public static final String table_char_name = "TABLECHARNAME";

    /**
     * 表行数
     */
    public static final String table_rows = "TABLEROWS";

    /**
     * 表存储大小
     */
    public static final String table_storage_size = "TABLESTORAGESIZE";

    /**
     * 表物理存储大小
     */
    public static final String table_physical_size = "TABLEPHYSICALSIZE";

    /**
     * 表类型
     */
    public static final String table_type = "TABLETYPE";

    public static final int FETCH_SIZE = 1000;

    public static final int QUERY_TIMEOUT = 172800;
}
