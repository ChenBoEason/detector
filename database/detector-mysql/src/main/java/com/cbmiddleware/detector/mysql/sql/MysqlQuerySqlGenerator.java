package com.cbmiddleware.detector.mysql.sql;

import com.cbmiddleware.detector.constant.DataBaseType;
import com.cbmiddleware.detector.exception.DetectorException;
import com.cbmiddleware.detector.sql.multiple.AbstractQuerySqlGenerator;
import com.cbmiddleware.detector.sql.multiple.GenerateSqlConfInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Eason(bo.chenb)
 * @description mysql生成器
 * @date 2020-03-19
 **/
public class MysqlQuerySqlGenerator extends AbstractQuerySqlGenerator {

    private static final Logger logger = LoggerFactory.getLogger(MysqlQuerySqlGenerator.class);


    @Override
    public String generate(GenerateSqlConfInfo sqlConfInfo) throws DetectorException {
        return super.generate(sqlConfInfo);
    }


    @Override
    public DataBaseType dataBaseType() {
        return DataBaseType.mysql;
    }
}
