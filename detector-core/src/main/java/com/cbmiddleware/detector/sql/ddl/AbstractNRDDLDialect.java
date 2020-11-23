package com.cbmiddleware.detector.sql.ddl;

import com.bqmiddleware.detector.exception.UnsupportedException;
import com.bqmiddleware.detector.sql.ddl.model.ColumnModel;
import com.bqmiddleware.detector.sql.ddl.model.IndexModel;
import com.bqmiddleware.detector.sql.ddl.model.PrimaryKeyModel;
import com.bqmiddleware.detector.sql.ddl.model.TableModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Class Name is AbstractNRDDLDialect
 * 给关系型数据库的抽象实现层
 *
 * @author LiJun
 * Created on 2020/11/2 5:44 下午
 */
public abstract class AbstractNRDDLDialect implements DDL {

    @Override
    public @NotNull String[] deleteColumn(@NotNull String tableName, String... columns) {
        throw new UnsupportedException("invalid DDL method deleteColumn");
    }

    @Override
    public @NotNull String[] deleteColumn(@NotNull String tableName, ColumnModel... columns) {
        throw new UnsupportedException("invalid DDL method deleteColumn");
    }

    @Override
    public @NotNull String[] alterColumn(@NotNull String tableName, ColumnModel... columns) {
        throw new UnsupportedException("invalid DDL method alterColumn");
    }

    @Override
    public @NotNull String[] addColumn(@NotNull String tableName, ColumnModel... columns) {
        throw new UnsupportedException("invalid DDL method addColumn");
    }

    @Override
    public @NotNull String[] addIndex(@NotNull String tableName, IndexModel... indices) {
        throw new UnsupportedException("invalid DDL method addIndex");
    }

    @Override
    public @NotNull String[] deleteIndex(@NotNull String tableName, String... indices) {
        throw new UnsupportedException("invalid DDL method deleteIndex");
    }

    @Override
    public @NotNull String addPrimaryKey(@NotNull String tableName, @NotNull PrimaryKeyModel primaryKey) {
        throw new UnsupportedException("invalid DDL method addPrimaryKey");
    }

    @Override
    public @NotNull String[] deletePrimaryKey(String... tableName) {
        throw new UnsupportedException("invalid DDL method deletePrimaryKey");
    }

    @Override
    public @NotNull String[] dropTable(String... tables) {
        throw new UnsupportedException("invalid DDL method dropTable");
    }

    @Override
    public @NotNull String[] createTable(boolean isNotSplit, TableModel... tables) {
        throw new UnsupportedException("invalid DDL method createTable");
    }

    @Override
    public @NotNull String[] createTable(TableModel... tables) {
        throw new UnsupportedException("invalid DDL method createTable");
    }

    @Override
    public @NotNull String[] dropAndCreateTable(boolean isNotSplit, TableModel... tables) {
        throw new UnsupportedException("invalid DDL method dropAndCreateTable");
    }

    @Override
    public @NotNull String[] dropAndCreateTable(TableModel... tables) {
        throw new UnsupportedException("invalid DDL method dropAndCreateTable");
    }

    @Override
    public @NotNull String[] dropForeignKey(@NotNull String tableName, String... foreignKeys) {
        throw new UnsupportedException("invalid DDL method dropForeignKey");
    }

    @Override
    public @NotNull String[] dropDatabase(@Nullable String... databases) {
        throw new UnsupportedException("invalid DDL method dropDatabase");
    }

    @Override
    public @NotNull String[] createDatabase(@Nullable String... databases) {
        throw new UnsupportedException("invalid DDL method createDatabase");
    }
}
