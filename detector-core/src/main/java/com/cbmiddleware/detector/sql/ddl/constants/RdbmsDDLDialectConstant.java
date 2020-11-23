package com.cbmiddleware.detector.sql.ddl.constants;


/**
 * @author Eason(bo.chenb)
 * @email chenboeason@gmail.com
 * @date 2020/11/3
 * @description
 *     公共语句常量
 **/
public class RdbmsDDLDialectConstant {

    /**
     * 修改表统一前缀
     *  alter table 表名
     */
    public static final String ALTER_TABLE_PREFIX = "alter table %s";
    /**
     * 表字段更新关键字
     */
    public static final String MODIFY_COLUMN_BODY = " modify";
    /**
     * 表字段删除统一模版
     *  alter table 表名 drop column 字段名
     *  适用于 mysql、oracle
     */
    public static final String DEL_TABLE_COLUMN_TEMPLATE = "alter table %s drop column %s;";

    /**
     * 建表前缀
     */
    public static final String CREATE_TABLE_PREFIX = "create table %s ";
    /**
     * 创建索引
     *  "create 是否为唯一索引 index 索引名 on 表名 (字段名);
     */
    public static final String CREATE_TABLE_INDEX_TEMPLATE = "create%s index %s on %s (%s);";
    /**
     * 创建索引
     *  "create 是否为唯一索引 index 索引名 on 表名 (字段名);
     */
    public static final String CREATE_TABLE_INDEX_PREFIX = "create%s index %s on %s (%s)";

    /**
     * 创建表主键
     * alter table 表名 add constraint 主键名称 primary key (字段);
     */
    public static final String CREATE_TABLE_PRIMARY_KEY_TEMPLATE = "alter table %s add constraint %s primary key (%s);";
    /**
     * 表主键body
     */
    public static final String CREATE_TABLE_PRIMARY_KEY_BODY = " constraint %s primary key";

    /**
     * 添加外键
     * alter table 当前表 add constraint 外键名 foreign key (当前表字段) references 关联表名(关联表字段);
     */
    public static final String CREATE_TABLE_FOREIGN_KEY_TEMPLATE = "alter table %s add constraint %s foreign key (%s) references %s(%s);";

    /**
     * 删除表主键
     * alter table 表名 drop primary key;
     */
    public static final String DEL_TABLE_PRIMARY_KEY_TEMPLATE = "alter table %s drop primary key;";

    /**
     * 删表
     * drop table 表名;
     */
    public static final String DEL_TABLE_TEMPLATE = "drop table %s;";
    /**
     * 删除外键
     * alter table 表名 drop foreign key 外键key;
     */
    public static final String DEL_TABLE_FOREIGN_KEY_TEMPLATE = "alter table %s drop foreign key %s;";


    public static final String UNIQUE_BODY = " unique";

    public static final String TABLE_CHARSET_BODY = " DEFAULT CHARSET=%s";

    public static final String NOT_NULL_BODY = " not null";

    public static final String NULL_BODY = " null";

}
