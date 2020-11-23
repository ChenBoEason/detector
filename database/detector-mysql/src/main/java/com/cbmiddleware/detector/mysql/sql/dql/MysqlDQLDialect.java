package com.cbmiddleware.detector.mysql.sql.dql;

import com.bqmiddleware.detector.constant.DataBaseType;
import com.bqmiddleware.detector.sql.dql.AbstractRdbmsDQLDialect;
import org.jetbrains.annotations.NotNull;

/**
 * Class Name is MysqlDQLDialect
 *
 * @author LiJun
 * Created on 2020/11/13 2:05 下午
 */
public class MysqlDQLDialect extends AbstractRdbmsDQLDialect {
    public MysqlDQLDialect() {
        super(DataBaseType.mysql, MysqlDQLDialect.class);
    }

    @Override
    public @NotNull String transferPageSqlByTemplate(@NotNull String sql, int pageIndex, int pageSize) {
        return sql + " LIMIT " + (pageIndex - 1)*pageSize + ", " + pageSize;
    }
}
