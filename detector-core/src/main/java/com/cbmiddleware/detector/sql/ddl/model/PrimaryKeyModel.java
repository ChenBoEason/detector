package com.cbmiddleware.detector.sql.ddl.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class Name is PrimaryKeyModel
 * 主键模型
 *
 * @author LiJun
 * Created on 2020/10/27 6:58 下午
 */
public class PrimaryKeyModel implements Serializable {

    private String name;

    private List<String> columns;

    public String getName() {
        return name;
    }

    public PrimaryKeyModel() {
    }

    public PrimaryKeyModel setName(String name) {
        this.name = name;
        return this;
    }

    public List<String> getColumns() {
        return columns;
    }

    public PrimaryKeyModel setColumns(List<String> columns) {
        this.columns = columns;
        return this;
    }

    public PrimaryKeyModel addColumn(String columnName){
        if(null == columns){
            columns = new ArrayList<>();
        }
        this.columns.add(columnName);
        return this;
    }
}
