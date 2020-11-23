package com.cbmiddleware.detector.oracle;

import com.cbmiddleware.detector.constant.DataBaseType;
import com.cbmiddleware.detector.exception.DetectorException;
import com.cbmiddleware.detector.sql.multiple.AbstractQuerySqlGenerator;
import com.cbmiddleware.detector.sql.multiple.GenerateSqlConfInfo;

/**
 * @author Eason(bo.chenb)
 * @description
 * @date 2020-03-19
 **/
public class OracleQuerySqlGenerator extends AbstractQuerySqlGenerator {


    @Override
    public String generate(GenerateSqlConfInfo sqlConfInfo) throws DetectorException {
        return super.generate(sqlConfInfo);
    }

    @Override
    public DataBaseType dataBaseType() {
        return DataBaseType.oracle;
    }
}
