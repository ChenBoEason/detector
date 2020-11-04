package com.cbmiddleware.detector.database;

import com.cbmiddleware.detector.database.model.ColumnModel;
import com.cbmiddleware.detector.database.model.IndexModel;
import com.cbmiddleware.detector.database.model.PrimaryKeyModel;
import com.cbmiddleware.detector.database.model.TableModel;
import org.jetbrains.annotations.NotNull;

/**
 * Class Name is DDL
 *
 * @author LiJun
 * Created on 2020/10/30 10:11 上午
 */
public interface DDL {

    /**
     * 删除字段信息 alter table tableName drop column columnName;
     *
     * @param tableName
     * @param columns
     * @return
     */
    @NotNull String[] deleteColumn(@NotNull String tableName, String... columns);

    /**
     * 删除字段信息 alter table tableName drop column columnName;
     *
     * @param tableName
     * @param columns
     * @return
     */
    @NotNull String[] deleteColumn(@NotNull String tableName, ColumnModel... columns);

    /**
     * 修改字段信息
     *
     * @param tableName
     * @param columns
     * @return
     */
    @NotNull String[] alterColumn(@NotNull String tableName, ColumnModel... columns);

    /**
     * 添加字段信息
     *
     * @param tableName
     * @param columns
     * @return
     */
    @NotNull String[] addColumn(@NotNull String tableName, ColumnModel... columns);

    /***
     * 添加索引
     *
     * @param tableName
     * @param indices
     * @return
     */
    @NotNull String[] addIndex(@NotNull String tableName, IndexModel... indices);

    /**
     * 删除索引
     *
     * @param tableName
     * @param indices
     * @return
     */
    @NotNull String[] deleteIndex(@NotNull String tableName, String... indices);

    /**
     * 添加主键索引
     *
     * @param tableName
     * @param primaryKey
     * @return
     */
    @NotNull String addPrimaryKey(@NotNull String tableName, @NotNull PrimaryKeyModel primaryKey);

    /***
     * 删除主键索引
     * @param tableName
     * @return
     */
    @NotNull String[] deletePrimaryKey(String... tableName);

    /**
     * 删除表
     *
     * @param tables
     * @return
     */
    @NotNull String[] dropTable(String... tables);

    /**
     * 创建表
     *
     * @param isNotSplit 索引等信息是否需要和建表语句分开
     * @param tables
     * @return
     */
    @NotNull String[] createTable(boolean isNotSplit, TableModel... tables);

    /**
     * 创建表
     *
     * @param tables
     * @return
     */
    @NotNull String[] createTable(TableModel... tables);

    /**
     * 先删表后创建表
     *
     * @param isNotSplit
     * @param tables
     * @return
     */
    @NotNull String[] dropAndCreateTable(boolean isNotSplit, TableModel... tables);

    /**
     * 先删表后创建表
     *
     * @param tables
     * @return
     */
    @NotNull String[] dropAndCreateTable(TableModel... tables);

    /**
     * 删除外键索引
     *
     * @param tableName
     * @param foreignKeys
     * @return
     */
    @NotNull String[] dropForeignKey(@NotNull String tableName, String... foreignKeys);

    /**
     * drop database
     *
     * @param databases
     * @return
     */
    @NotNull String[] dropDatabase(String... databases);

    /**
     * 创建database
     *
     * @param databases
     * @return
     */
    @NotNull String[] createDatabase(String... databases);

}
