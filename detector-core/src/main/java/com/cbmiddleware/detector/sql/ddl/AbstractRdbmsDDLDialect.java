package com.cbmiddleware.detector.sql.ddl;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlPrimaryKey;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OraclePrimaryKey;
import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.fastjson.JSON;
import com.bqmiddleware.detector.constant.DataBaseType;
import com.bqmiddleware.detector.exception.Assert;
import com.bqmiddleware.detector.exception.UnsupportedException;
import com.bqmiddleware.detector.sql.AbstractRdbmsDialect;
import com.bqmiddleware.detector.sql.ddl.constants.ColumnOperationTypeEnum;
import com.bqmiddleware.detector.sql.ddl.model.ColumnModel;
import com.bqmiddleware.detector.sql.ddl.model.IndexModel;
import com.bqmiddleware.detector.sql.ddl.model.PrimaryKeyModel;
import com.bqmiddleware.detector.sql.ddl.model.TableModel;
import com.bqmiddleware.detector.sql.ddl.rules.Type;
import com.bqmiddleware.detector.utils.ConcatUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.bqmiddleware.detector.sql.constant.ConstVal.*;
import static com.bqmiddleware.detector.sql.ddl.constants.RdbmsDDLDialectConstant.*;

/**
 * Class Name is AbstractRdbmsDDLDialect
 *
 * @author LiJun
 * Created on 2020/10/30 10:26 上午
 */
public abstract class AbstractRdbmsDDLDialect extends AbstractRdbmsDialect implements DDL {
    /**
     * 需要初始化
     */
    protected final Map<Type, String> typeMappings = new EnumMap<>(Type.class);

    /**
     * 是否支持字段check约束
     */
    protected boolean supportsColumnCheck = false;
    /**
     * 是否支持表check约束
     */
    protected boolean supportsTableCheck = false;

    public AbstractRdbmsDDLDialect(DataBaseType dbType, Class<?  extends AbstractRdbmsDDLDialect> clazz)  {
        super(dbType, clazz);
        initTypeMappings();
    }

    @Override
    public @NotNull String[] createTable(boolean isNotSplit, TableModel... tables) {
        List<String> result = new LinkedList<>();

        for (TableModel table : tables) {
            String createTableSql = createTableSql(table, isNotSplit);
            result.add(createTableSql);
        }
        if (isLog) {
            log.warn("createTable the final sql is [{}]", JSON.toJSONString(result));
        }
        return result.toArray(new String[0]);
    }

    /**
     * 建表语句
     *
     * @param table      建表原数据
     * @param isNotSplit 索引等信息是否需要和建表语句分开
     * @return
     */
    protected abstract String createTableSql(TableModel table, boolean isNotSplit);

    @Override
    public @NotNull String[] createTable(TableModel... tables) {
        return createTable(true, tables);
    }

    /**
     * 初始化typeMapping
     */
    protected abstract void initTypeMappings();

    @Override
    public @NotNull String[] deleteColumn(@NotNull String tableName, String... columns) {
        Assert.isNotNull("tableName", tableName);
        List<String> result = new ArrayList<>();
        if (null != columns) {
            if (isDruid) {
                SQLExprTableSource table = table(tableName);
                for (String column : columns) {
                    SQLAlterTableStatement statement = new SQLAlterTableStatement();
                    statement.setTableSource(table);
                    SQLAlterTableDropColumnItem dropColumnItem = new SQLAlterTableDropColumnItem();
                    dropColumnItem.addColumn(new SQLIdentifierExpr(column));
                    statement.addItem(dropColumnItem);
                    result.add(SQLUtils.toSQLString(statement, dbType) + SEMICOLON);
                }
            } else {
                for (String column : columns) {
                    result.add(String.format(DEL_TABLE_COLUMN_TEMPLATE, tableName, column));
                }
            }

        }
        if (isLog) {
            log.debug("deleteColumn the final sql is [{}]", JSON.toJSONString(result));
        }
        return result.toArray(new String[0]);
    }

    @Override
    public @NotNull String[] deleteColumn(@NotNull String tableName, ColumnModel... columns) {
        if (null == columns) {
            return new String[0];
        }
        String[] result = Arrays.stream(columns).map(ColumnModel::getColumnName).toArray(String[]::new);
        return deleteColumn(tableName, result);
    }

    /**
     * 删除表索引模版
     *
     * @param tableName
     * @param index
     * @return sql statement
     */
    public abstract String delTableIndexStatement(String tableName, String index);

    @Override
    public @NotNull String[] deleteIndex(@NotNull String tableName, String... indices) {
        Assert.isNotNull("tableName", tableName);

        if (null == indices) {
            return new String[0];
        }

        List<String> result = new ArrayList<>();
        if (isDruid) {
            SQLExprTableSource table = table(tableName);
            for (String index : indices) {
                SQLAlterTableStatement alterTableStatement = new SQLAlterTableStatement();
                alterTableStatement.setTableSource(table);
                SQLAlterTableDropIndex sqlAlterTableDropIndex = new SQLAlterTableDropIndex();
                sqlAlterTableDropIndex.setIndexName(new SQLIdentifierExpr(index));
                alterTableStatement.addItem(sqlAlterTableDropIndex);
                result.add(SQLUtils.toSQLString(alterTableStatement, dbType) + SEMICOLON);
            }
        } else {
            for (String index : indices) {
                result.add(delTableIndexStatement(tableName, index));
            }
        }

        if (isLog) {
            log.debug("deleteIndex the final sql is [{}]", JSON.toJSONString(result));
        }

        return result.toArray(new String[0]);
    }

    @Override
    public @NotNull String[] addIndex(@NotNull String tableName, IndexModel... indices) {
        Assert.isNotNull("tableName", tableName);

        if (null == indices) {
            return new String[0];
        }

        List<String> result = new ArrayList<>();

        if (isDruid) {
            SQLExprTableSource table = table(tableName);
            for (IndexModel index : indices) {
                SQLCreateIndexStatement indexStatement = new SQLCreateIndexStatement();
                indexStatement.setTable(table);
                indexStatement.setName(new SQLIdentifierExpr(getIndexName(tableName, index)));
                for (String column : index.getColumnList()) {
                    indexStatement.addItem(new SQLSelectOrderByItem(new SQLIdentifierExpr(column)));
                }
                result.add(SQLUtils.toSQLString(indexStatement, dbType) + SEMICOLON);
            }
        } else {
            for (IndexModel index : indices) {
                String sqlStatement = String.format(CREATE_TABLE_INDEX_TEMPLATE, index.isUnique() ? UNIQUE_BODY : "",
                        getIndexName(tableName, index), tableName,
                        ConcatUtils.joinStringList(index.getColumnList(), COMMA + SPACE));
                result.add(sqlStatement);
            }
        }

        if (isLog) {
            log.debug("addIndex the final sql is [{}]", JSON.toJSONString(result));
        }

        return result.toArray(new String[0]);
    }


    @Override
    public @NotNull String addPrimaryKey(@NotNull String tableName, @NotNull PrimaryKeyModel primaryKey) {
        Assert.isNotContainNull("tableName, primaryKey", tableName, primaryKey);

        String name = primaryKey.getName();

        if (StringUtils.isBlank(name)) {
            name = tableName + "_pk";
            primaryKey.setName(name);
        }

        String sqlStatement = isDruid ? addPrimaryKeyDruid(tableName, primaryKey) : String.format(CREATE_TABLE_PRIMARY_KEY_TEMPLATE, tableName, name, ConcatUtils.joinStringList(primaryKey.getColumns(), COMMA + SPACE));
        if (isLog) {
            log.debug("addPrimaryKey the final sql is [{}]", sqlStatement);
        }
        return sqlStatement;
    }

    /**
     * druid 方式添加主键
     *
     * @param tableName
     * @param primaryKey
     * @return
     */
    public String addPrimaryKeyDruid(String tableName, @NotNull PrimaryKeyModel primaryKey) {

        SQLAlterTableStatement addPrimaryKeyStatement = new SQLAlterTableStatement();
        addPrimaryKeyStatement.setTableSource(getTableExpr(tableName));

        SQLUnique sqlPrimary = null;
        if (StringUtils.equalsIgnoreCase(JdbcConstants.MYSQL, dbType)) {
            sqlPrimary = new MySqlPrimaryKey();
        } else if (StringUtils.equalsIgnoreCase(JdbcConstants.ORACLE, dbType)) {
            sqlPrimary = new OraclePrimaryKey();
        } else {
            sqlPrimary = new SQLPrimaryKeyImpl();
        }
        for (String columnName : primaryKey.getColumns()) {
            sqlPrimary.addColumn(new SQLIdentifierExpr(columnName));
        }
        sqlPrimary.setName(primaryKey.getName());
        addPrimaryKeyStatement.addItem(new SQLAlterTableAddConstraint(sqlPrimary));
        return SQLUtils.toSQLString(addPrimaryKeyStatement, dbType) + SEMICOLON;
    }

    @Override
    public @NotNull String[] addColumn(@NotNull String tableName, ColumnModel... columns) {
        Assert.isNotNull("tableName", tableName);

        if (null == columns) {
            return new String[0];
        }

        List<String> result = new ArrayList<>();
        for (ColumnModel column : columns) {
            String addColumnSql = transferColumnSql(tableName, ColumnOperationTypeEnum.add, column);
            result.add(addColumnSql);
        }
        if (isLog) {
            log.warn("addColumn the final sql is [{}]", JSON.toJSONString(result));
        }
        return result.toArray(new String[0]);
    }

    /**
     * 建表语句构建
     *
     * @param tableName     表名
     * @param operationType 操作类型
     * @param column        列信息
     */
    public abstract String transferColumnSql(String tableName, ColumnOperationTypeEnum operationType, ColumnModel column);

    @Override
    public @NotNull String[] alterColumn(@NotNull String tableName, ColumnModel... columns) {
        Assert.isNotNull("tableName", tableName);
        List<String> result = new ArrayList<>();
        if (null == columns) {
            return new String[0];
        }
        for (ColumnModel column : columns) {
            ColumnOperationTypeEnum operationType = ColumnOperationTypeEnum.modify;
            // 当renameColumnName 不为空并且和原有字段不相同时为重命名操作
            if (column.getRenameColumnName() != null && !StringUtils.equals(column.getRenameColumnName(), column.getColumnName())) {
                operationType = ColumnOperationTypeEnum.rename;
            }
            String alterColumnSql = transferColumnSql(tableName, operationType, column);
            result.add(alterColumnSql);
        }
        if (isLog) {
            log.warn("alter Column the final sql is [{}]", JSON.toJSONString(result));
        }
        return result.toArray(new String[0]);
    }

    @Override
    public @NotNull String[] deletePrimaryKey(String... tableName) {
        List<String> result = new ArrayList<>();
        if (isDruid) {
            for (String table : tableName) {
                SQLAlterTableStatement alterTableStatement = new SQLAlterTableStatement();
                alterTableStatement.setTableSource(table(table));
                alterTableStatement.addItem(new SQLAlterTableDropPrimaryKey());
                result.add(SQLUtils.toSQLString(alterTableStatement, dbType) + SEMICOLON);
            }
        } else {
            for (String table : tableName) {
                result.add(String.format(DEL_TABLE_PRIMARY_KEY_TEMPLATE, table));
            }
        }
        if (isLog) {
            log.debug("deletePrimaryKey the final sql is [{}]", JSON.toJSONString(result));
        }
        return result.toArray(new String[0]);
    }


    @Override
    public @NotNull String[] dropTable(String... tables) {
        List<String> result = new ArrayList<>();
        if (isDruid) {
            for (String table : tables) {
                result.add(SQLUtils.toMySqlString(new SQLDropTableStatement(table(table))) + SEMICOLON);
            }
        } else {
            for (String table : tables) {
                result.add(String.format(DEL_TABLE_TEMPLATE, table));
            }
        }
        if (isLog) {
            log.debug("dropTable the final sql is [{}]", JSON.toJSONString(result));
        }
        return result.toArray(new String[0]);
    }

    @Override
    public @NotNull String[] dropAndCreateTable(TableModel... tables) {
        return dropAndCreateTable(true, tables);
    }

    @Override
    public @NotNull String[] dropAndCreateTable(boolean isNotSplit, TableModel... tables) {
        return new String[0];
    }


    @Override
    public @NotNull String[] dropForeignKey(@NotNull String tableName, String... foreignKeys) {
        Assert.isNotNull("tableName", tableName);
        List<String> result = new ArrayList<>();
        if (null != foreignKeys) {
            if (isDruid) {
                SQLExprTableSource table = table(tableName);
                for (String foreignKey : foreignKeys) {
                    SQLAlterTableStatement statement = new SQLAlterTableStatement();
                    statement.setTableSource(table);
                    SQLAlterTableDropForeignKey sqlAlterTableDropForeignKey = new SQLAlterTableDropForeignKey();
                    sqlAlterTableDropForeignKey.setIndexName(new SQLIdentifierExpr(foreignKey));
                    result.add(SQLUtils.toSQLString(statement, dbType) + SEMICOLON);
                }
            } else {
                for (String foreignKey : foreignKeys) {
                    result.add(String.format(DEL_TABLE_FOREIGN_KEY_TEMPLATE, tableName, foreignKey));
                }
            }
        }
        if (isLog) {
            log.debug("dropForeignKey the final sql is [{}]", result);
        }
        return result.toArray(new String[0]);
    }


    @Override
    public @NotNull String[] dropDatabase(@Nullable String... databases) {
        throw new UnsupportedException("invalid DDL method dropDatabase");
    }

    @Override
    public @NotNull String[] createDatabase(@Nullable String... databases) {
        throw new UnsupportedException("invalid DDL method createDatabase");
    }

    protected SQLExprTableSource table(String tableName) {
        return new SQLExprTableSource(new SQLIdentifierExpr(tableName));
    }

    protected String getIndexName(String tableName, IndexModel index) {
        return StringUtils.isNotBlank(index.getName()) ? index.getName() : "idx_" + tableName + UNDERLINE + ConcatUtils.joinStringList(index.getColumnList(), UNDERLINE);
    }


    protected String translateToDDLType(Type type, Integer... lengths) {
        String value = this.typeMappings.get(type);
        if (StringUtils.isBlank(value) || StringUtils.equalsIgnoreCase("N/A", value)) {
            throw new IllegalArgumentException("Type: [ " + type + "] is not supported! ");
        }
        if (value.contains("|")) {
            String[] mappings = StringUtils.split(value, "|");
            for (String mapping : mappings) {
                // varchar($l)<255
                if (mapping.contains("<")) {
                    String[] limitType = StringUtils.split(mapping, "<");
                    // NOSONAR
                    if (lengths.length > 0 && lengths[0] < Integer.parseInt(limitType[1])) {
                        return putParameters(type, limitType[0], lengths);
                    }
                } else {// varchar($l)
                    return putParameters(type, mapping, lengths);
                }
            }
        } else {
            if (value.contains("$")) {
                // always this order: $l, $p, $s
                return putParameters(type, value, lengths);
            } else {
                return value;
            }
        }
        return "";
    }

    protected String putParameters(Type type, String value, Integer... lengths) {
        if (lengths.length < StringUtils.countMatches(value, '$')) {
            throw new IllegalArgumentException("Type \"" + type + "\" should have "
                    + StringUtils.countMatches(value, '$') + " parameters");
        }
        int i = 0;
        String newValue = value;
        if (newValue.contains("$l")) {
            newValue = StringUtils.replace(newValue, "$l", String.valueOf(lengths[i++]));
        }
        if (newValue.contains("$p")) {
            newValue = StringUtils.replace(newValue, "$p", String.valueOf(lengths[i++]));
        }
        if (newValue.contains("$s")) {
            newValue = StringUtils.replace(newValue, "$s", String.valueOf(lengths[i]));
        }
        return newValue;
    }

    protected SQLExprTableSource getTableExpr(String tableName) {
        return new SQLExprTableSource(new SQLIdentifierExpr(tableName));
    }

}
