package com.cbmiddleware.detector.sql.ddl.rules;

import java.util.HashSet;
import java.util.Set;

/**
 * Class Name is Type
 * 表字段类型
 *
 * @author LiJun
 * Created on 2020/10/30 10:15 上午
 */
public enum Type {

    /**
     *
     */
    BIGINT,
    BINARY,
    BIT,
    BLOB,
    BOOLEAN,
    CHAR,
    CLOB,
    DATE,
    DATETIME,
    DECIMAL,
    DOUBLE,
    FLOAT,
    INTEGER,
    JAVA_OBJECT,
    LONGNVARCHAR,
    LONGVARBINARY,
    LONGVARCHAR,
    NCHAR,
    NCLOB,
    NUMERIC,
    NVARCHAR,
    OTHER,
    REAL,
    SMALLINT,
    TIME,
    TIMESTAMP,
    TINYINT,
    VARBINARY,
    VARCHAR,
    VARCHAR2;

    public static Set<Type> stringType() {
        Set<Type> types = new HashSet<>();
        types.add(BINARY);
        types.add(BLOB);
        types.add(CHAR);
        types.add(CLOB);
        types.add(LONGNVARCHAR);
        types.add(LONGVARBINARY);
        types.add(LONGVARCHAR);
        types.add(NCHAR);
        types.add(NCLOB);
        types.add(NVARCHAR);
        types.add(VARBINARY);
        types.add(VARCHAR);
        return types;
    }

}
