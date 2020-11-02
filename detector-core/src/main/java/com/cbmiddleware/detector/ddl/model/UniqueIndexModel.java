package com.cbmiddleware.detector.ddl.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class Name is UniqueIndexModel
 *
 * @author LiJun
 * Created on 2020/10/30 10:22 上午
 */
public class UniqueIndexModel implements Serializable {

    private String name;
    private List<String> columnsList;

    public String getName() {
        return name;
    }

    public UniqueIndexModel setName(String name) {
        this.name = name;
        return this;
    }

    public List<String> getColumnsList() {
        return columnsList;
    }

    public UniqueIndexModel setColumnsList(List<String> columnsList) {
        this.columnsList = columnsList;
        return this;
    }

    public UniqueIndexModel addColumn(String column){

        if (null == this.columnsList){
            this.columnsList = new ArrayList<>();
        }
        this.columnsList.add(column);
        return this;
    }

    public String[] getColumnsArray(){
        return this.columnsList.toArray(new String[0]);
    }
}
