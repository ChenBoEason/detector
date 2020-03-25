package com.cbmiddleware.detector.util;

import com.alibaba.druid.sql.SQLUtils;
import com.cbmiddleware.detector.constant.DataBaseType;
import com.cbmiddleware.detector.constant.RangeType;
import com.cbmiddleware.detector.exception.DetectorException;
import com.cbmiddleware.detector.function.param.Range;
import com.cbmiddleware.detector.sql.function.CaseWhenCondition;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Eason(bo.chenb)
 * @description 数据源信息获取工具，连接数据源时未进行连接池的搭建，因为获取数据源的操作并不频繁
 * @date 2020-03-06
 **/
public class DataSourceUtils {


    public static String concatTableNames(List<String> tableNames) {
        StringBuilder builder = new StringBuilder();
        tableNames.forEach(name -> {
            builder.append("'").append(name.trim()).append("',");
        });

        return builder.substring(0, builder.length() - 1);
    }


    /**
     * 格式化sql
     *
     * @param sql    sql语句
     * @param dbType 数据库类型
     * @return
     */
    public static String formatSql(String sql, String dbType) {
        return SQLUtils.format(sql, dbType);
    }


    public static void getValue(Object value, StringBuilder conditionSql, DataBaseType dataBaseType) throws DetectorException {

        if (value instanceof String) {
            conditionSql.append("'").append(value.toString()).append("'");
            return;
        }

        if (value instanceof Double || value instanceof Float || value instanceof Integer || value instanceof Long) {
            conditionSql.append(value.toString());
            return;
        }

        if (value instanceof Boolean) {
            Boolean flag = (Boolean) value;
            conditionSql.append(flag ? 1 : 0);
            return;
        }

        if (value instanceof Date) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateValue = sdf.format((Date) value);
            switch (dataBaseType) {
                case mysql:
                    conditionSql.append("'").append(dateValue).append("'");
                    return;
                case oracle:
                    conditionSql.append("TO_DATE('").append(dateValue).append("',").append("'yyyy-MM-dd HH24:MI:SS')");
                    return;
                case odps:
                    conditionSql.append("to_date('").append(dateValue).append("',").append("'yyyy-mm-dd hh:mi:ss')");
                    return;
                default:
                    return;
            }

        }


        throw new DetectorException(String.format("Unsupported type [%s], supported type[String、Double、Float、Integer、Long、Boolean、Date]", value.getClass()));
    }


    /**
     * string to date
     *
     * @param value
     * @param format
     * @param dataBaseType
     * @param builder
     */
    public static void toDate(String value, String format, DataBaseType dataBaseType, StringBuilder builder) {
        switch (dataBaseType) {
            case mysql:
                builder.append("str_to_date(").append(value).append(",").append("'").append(format).append("')");
                return;
            case oracle:
                builder.append("TO_DATE(").append(value).append(",").append("'").append(format).append("')");
                return;
            case odps:
                builder.append("to_date(").append(value).append(",").append("'").append(format).append("')");
                return;
            default:
                return;
        }

    }

    /**
     * date to string
     *
     * @param value
     * @param format
     * @param dataBaseType
     * @param builder
     */
    public static void dateToString(String value, String format, DataBaseType dataBaseType, StringBuilder builder) {
        switch (dataBaseType) {
            case mysql:
                builder.append("date_format(").append(value).append(",").append("'").append(format).append("')");
                return;
            case oracle:
                builder.append("to_char(").append(value).append(",").append("'").append(format).append("')");
                return;
            case odps:
                builder.append("to_char(").append(value).append(",").append("'").append(format).append("')");
                return;
            default:
                return;
        }
    }

    /**
     * 区间
     *
     * @param value
     * @param ranges
     * @param rangeType
     * @param dataBaseType
     * @param builder
     */
    public static void range(String value, List<Range> ranges, RangeType rangeType, DataBaseType dataBaseType, StringBuilder builder) throws DetectorException {
        builder.append("CASE\n");

        for (Range range : ranges) {
            StringBuilder leftConditionValue = new StringBuilder();
            getValue(range.getMin(), leftConditionValue, dataBaseType);

            StringBuilder rightConditionValue = new StringBuilder();
            getValue(range.getMin(), rightConditionValue, dataBaseType);

            StringBuilder thenValue = new StringBuilder();
            getValue(range.getValue(), thenValue, dataBaseType);

            builder.append("WHEN ").append(value);
            switch (rangeType) {
                case loro:
                    builder.append(" > ").append(leftConditionValue)
                            .append(" and ")
                            .append(value).append(" < ").append(rightConditionValue);
                    break;
                case lorc:
                    builder.append(" > ").append(leftConditionValue)
                            .append(" and ")
                            .append(value).append(" <= ").append(rightConditionValue);
                    break;
                case lcro:
                    builder.append(" >= ").append(leftConditionValue)
                            .append(" and ")
                            .append(value).append(" < ").append(rightConditionValue);
                    break;
                case lcrc:
                    builder.append(" >= ").append(leftConditionValue)
                            .append(" and ")
                            .append(value).append(" <= ").append(rightConditionValue);
                    break;
                default:
                    break;
            }

            builder.append(" THEN ").append(thenValue)
                    .append("\n");
        }
        builder.append("END");
    }

    /**
     * case when 也适用于小数据量的字典查询
     *
     * @param value
     * @param conditions
     * @param dataBaseType
     * @param builder
     */
    public static void casewhen(String value, List<CaseWhenCondition> conditions, DataBaseType dataBaseType, StringBuilder builder) throws DetectorException {
        builder.append("CASE\n");
        for (CaseWhenCondition condition : conditions) {
            StringBuilder conditionValue = new StringBuilder();
            getValue(condition.getConditionValue(), conditionValue, dataBaseType);

            StringBuilder thenValue = new StringBuilder();
            getValue(condition.getValue(), thenValue, dataBaseType);

            builder.append("WHEN ").append(value).append(" ")
                    .append(condition.getConditionType().getSymbol())
                    .append(" ").append(conditionValue.toString())
                    .append(" THEN ").append(thenValue)
                    .append("\n");
        }
        builder.append("END");
    }
}
