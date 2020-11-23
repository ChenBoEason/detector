package com.cbmiddleware.detector.mysql.sql.dml;

import com.bqmiddleware.detector.constant.DataBaseType;
import com.bqmiddleware.detector.sql.dml.AbstractRdbmsDMLDialect;


/**
 * Class Name is MysqlDMLDialect
 *
 * @author LiJun
 * Created on 2020/11/2 9:33 上午
 */
public class MysqlDMLDialect extends AbstractRdbmsDMLDialect {
    public MysqlDMLDialect() {
        super(DataBaseType.mysql, MysqlDMLDialect.class);
    }
}
