package com.cbmiddleware.detector.database.model;

import java.io.Serializable;
import java.util.List;

/**
 * Class Name is PrimaryKeyModel
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
}
