package com.cbmiddleware.detector.sql.ddl.rules;

import java.sql.DatabaseMetaData;

/**
 * Class Name is IndexType
 * 索引类型
 *
 * @author LiJun
 * Created on 2020/10/27 5:32 下午
 */

public enum IndexType {
    /**
     *
     */
    statistic(DatabaseMetaData.tableIndexStatistic),

    clustered(DatabaseMetaData.tableIndexClustered),

    hashed(DatabaseMetaData.tableIndexHashed),

    other(DatabaseMetaData.tableIndexOther);

    public static IndexType valueOf(final int id) {
        for (final IndexType type : IndexType.values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        return other;
    }

    private final int id;

    private IndexType(final int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
