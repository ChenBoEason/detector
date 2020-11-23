package com.cbmiddleware.detector.mysql.sql.ddl;

import com.bqmiddleware.detector.constant.DataBaseType;
import com.bqmiddleware.detector.sql.ddl.AbstractRdbmsDDLDialect;
import com.bqmiddleware.detector.sql.ddl.constants.ColumnOperationTypeEnum;
import com.bqmiddleware.detector.sql.ddl.model.ColumnModel;
import com.bqmiddleware.detector.sql.ddl.model.IndexModel;
import com.bqmiddleware.detector.sql.ddl.model.TableModel;
import com.bqmiddleware.detector.sql.ddl.rules.Type;
import com.bqmiddleware.detector.utils.ConcatUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.bqmiddleware.detector.mysql.common.constant.MysqlRdbmsDDLDialectConstant.*;
import static com.bqmiddleware.detector.sql.constant.ConstVal.*;
import static com.bqmiddleware.detector.sql.ddl.constants.RdbmsDDLDialectConstant.*;

/**
 * Class Name is MysqlDDLDialect
 *
 * @author LiJun
 * Created on 2020/10/30 5:45 下午
 */
public class MysqlDDLDialect extends AbstractRdbmsDDLDialect {

    public MysqlDDLDialect() {
        super(DataBaseType.mysql, MysqlDDLDialect.class);
    }

    @Override
    protected void initTypeMappings() {
        typeMappings.putAll(MYSQL_TYPE_MAPPING);
    }

    @Override
    protected String createTableSql(TableModel table, boolean isNotSplit) {

        // 选择是否在建表语句中加 if not exists 执行完后 语句create table [if not exists] 表名 (
        StringBuilder createSqlPrefix = new StringBuilder(String.format(table.isIfNotExistsCreate() ?
                        CREATE_TABLE_IF_NOT_EXISTS_PREFIX : CREATE_TABLE_PREFIX,
                table.getTableName()))
                .append(LEFT_BRACKET);

        // 记录table (lineStatements) 中的语句
        List<String> lineStatements = new ArrayList<>(table.getColumns().size());
        //记录那些字段为主键
        StringBuilder primaryKey = null;
        // 字段添加
        for (ColumnModel column : table.getColumns()) {
            // 列的建表语句
            StringBuilder columnStatement = new StringBuilder();
            // 记录联合主键字段，可能存在多个主键情况
            if (column.isPkey()) {
                if (null == primaryKey) {
                    primaryKey = new StringBuilder();
                    primaryKey.append(column.getColumnName());
                } else {
                    primaryKey.append(COMMA).append(column.getColumnName());
                }
                column.notNull();
            }

            columnStatement.append("\n\t").append(column.getColumnName()).append(SPACE);
            columnStatement.append(translateToDDLType(column.getColumnType(), column.getLengths()));

            //mysql 支持 name varchar(32) unique 语法
            if (column.isUnique()) {
                columnStatement.append(UNIQUE_BODY);
            }

            if (null != column.getDefaultValue()) {
                columnStatement.append(COLUMN_DEFAULT_BODY);
                if (Type.stringType().contains(column.getColumnType())) {
                    columnStatement.append(SINGLE_QUOTES).append(column.getDefaultValue()).append(SINGLE_QUOTES);
                } else {
                    columnStatement.append(SPACE).append(column.getDefaultValue());
                }
            }
            columnStatement.append(!column.getNullable() ? NOT_NULL_BODY : NULL_BODY);
            // 是否为自增
            if (column.getAutoIncrement()) {
                columnStatement.append(AUTO_INCREMENT_BODY);
            }

            if (StringUtils.isNotBlank(column.getCheck())) {
                if (supportsColumnCheck) {
                    columnStatement.append(" check(").append(column.getCheck()).append(")");
                } else {
                    log.warn("Ignore unsupported check setting on column[{}] at table:[{}] with value:[{}]", column.getColumnName(), table.getTableName(), column.getCheck());
                }
            }

            if (StringUtils.isNotBlank(column.getComment())) {
                columnStatement.append(String.format(COLUMN_COMMENT_BODY, column.getComment()));
            }

            if (StringUtils.isNotBlank(column.getTail())) {
                columnStatement.append(SPACE).append(column.getTail());
            }
            columnStatement.append(COMMA);
            lineStatements.add(columnStatement.toString());
        }

        if (primaryKey != null) {
            lineStatements.add(new StringBuilder("\n\tprimary key(").append(primaryKey).append(RIGHT_BRACKET).append(COMMA).toString());
        }
        if (StringUtils.isNotBlank(table.getCheck())) {
            if (supportsTableCheck) {
                lineStatements.add(new StringBuilder(" check(").append(table.getCheck()).append(RIGHT_BRACKET).append(COMMA).toString());
            } else {
                log.warn("Ignore unsupported check setting on table:[{}] with value:[{}]", table.getTableName(), table.getCheck());
            }
        }

        // 记录索引
        List<String> indexList = new LinkedList<>();
        for (IndexModel index : table.getIndices()) {
            String indexName = index.getName();
            if (StringUtils.isBlank(indexName)) {
                indexName = table.getTableName() + UNDERLINE + ConcatUtils.joinStringList(index.getColumnList(), UNDERLINE);
            }

            if (isNotSplit) {
                String uniqueStr = index.isUnique() ? "unique " : "";
                lineStatements.add(new StringBuilder("\n\t").append(uniqueStr).append(String.format(INDEX_TEMPLATE, indexName, ConcatUtils.joinStringList(index.getColumnList(), COMMA_SPACE))).toString());
            } else {
                String uniqueStr = index.isUnique() ? UNIQUE_BODY : "";
                String createUniqueIndexStatement = String.format(
                        CREATE_TABLE_INDEX_PREFIX, uniqueStr, indexName, table.getTableName(), ConcatUtils.joinStringList(index.getColumnList(), COMMA_SPACE)
                );
                indexList.add(createUniqueIndexStatement);
            }
        }
        // 建表语句
        StringBuilder createTableStatement = new StringBuilder(createSqlPrefix);

        for (int index = 0; index < lineStatements.size(); index++) {
            String lineStr = lineStatements.get(index);
            if (index == lineStatements.size() -1) {
                createTableStatement.append(lineStr.substring(0, lineStr.length() -1));
            } else {
                createTableStatement.append(lineStr);
            }
        }

        createTableStatement.append("\n)");

        // 记录table () 括号外的语句 备注、引擎
        // engine eg:mysql engine=InnoDB
        if (StringUtils.isNotBlank(table.getEngine())) {
            createTableStatement.append(SPACE).append("engine=").append(table.getEngine());
        } else {
            createTableStatement.append(TABLE_TYPE_BODY);
        }

        createTableStatement.append(String.format(TABLE_CHARSET_BODY, table.getCharset()));

        if (StringUtils.isNotBlank(table.getComment())) {
            createTableStatement.append(String.format(TABLE_COMMENT_BODY, table.getComment()));
        }
        createTableStatement.append(SEMICOLON);

        for (String indexStatement : indexList) {
            createTableStatement.append("\n").append(indexStatement).append(SEMICOLON);
        }
        return createTableStatement.toString();
    }


    /**
     * 抽象类-实现方法
     */

    @Override
    public String delTableIndexStatement(String tableName, String index) {
        return String.format(DEL_TABLE_INDEX_TEMPLATE, tableName, index);
    }

    /**
     * 自定义方法
     */

    @Override
    public String transferColumnSql(String tableName, ColumnOperationTypeEnum operationType, ColumnModel column) {

        if (column.getColumnType() == null) {
            throw new IllegalArgumentException("Type no set on column " + column.getColumnName() + " at table" + tableName);
        }

        StringBuilder columnStatement = new StringBuilder(String.format(ALTER_TABLE_PREFIX, tableName));

        switch (operationType) {
            case add:
                columnStatement.append(ADD_COLUMN_BODY).append(SPACE).append(column.getColumnName());
                break;
            case modify:
                columnStatement.append(MODIFY_COLUMN_BODY).append(SPACE).append(column.getColumnName());
                break;
            case rename:
                columnStatement.append(RENAME_COLUMN_BODY).append(SPACE).append(column.getColumnName()).append(SPACE).append(column.getRenameColumnName());
                break;
            default:
                break;
        }

        columnStatement.append(SPACE).append(translateToDDLType(column.getColumnType(), column.getLengths()));

        // defaultValue
        Object defaultValue = column.getDefaultValue();
        if (null != defaultValue) {
            columnStatement.append(COLUMN_DEFAULT_BODY).append(SPACE);
            if (Type.stringType().contains(column.getColumnType())) {
                columnStatement.append(SINGLE_QUOTES).append(defaultValue).append(SINGLE_QUOTES);
            } else {
                columnStatement.append(defaultValue);
            }
        }
        columnStatement.append(column.getNullable() ? NULL_BODY : NOT_NULL_BODY);

        // 是否为自增
        if (column.getAutoIncrement()) {
            columnStatement.append(AUTO_INCREMENT_BODY);
        }

        if (column.isUnique()) {
            columnStatement.append(UNIQUE_BODY);
        }
        if (supportsColumnCheck && StringUtils.isNotBlank(column.getCheck())) {
            columnStatement.append("check (").append(column.getCheck()).append(")");
        }

        if (StringUtils.isNotBlank(column.getComment())) {
            columnStatement.append(String.format(COLUMN_COMMENT_BODY, column.getComment()));
        }

        if (StringUtils.isNotBlank(column.getTail())) {
            columnStatement.append(column.getTail());
        }
        return columnStatement.toString().trim() + SEMICOLON;
    }
}
