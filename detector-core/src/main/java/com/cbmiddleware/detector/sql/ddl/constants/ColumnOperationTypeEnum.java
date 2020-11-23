package com.cbmiddleware.detector.sql.ddl.constants;

/**
 * @author Eason(bo.chenb)
 * @email chenboeason@gmail.com
 * @date 2020/11/3
 * @description 列操作类型
 **/
public enum ColumnOperationTypeEnum {

    /**
     * 修改字段
     */
    modify,
    /**
     * 新增字段
     */
    add,

    /**
     * 字段重命名
     */
    rename,
    ;
}
