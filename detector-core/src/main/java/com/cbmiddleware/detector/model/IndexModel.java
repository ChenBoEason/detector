package com.cbmiddleware.detector.model;

import com.bqmiddleware.detector.dialect.rules.IndexType;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class Name is IndexModel
 *
 * @author LiJun
 * Created on 2020/10/30 10:20 上午
 */
public class IndexModel implements Serializable {

    private String name;

    private boolean unique;

    private StringBuilder definition;

    private int pages;

    private IndexType indexType;

    private Set<String> columnList = new HashSet<>();


    public IndexModel addColumns(String... columns) {
        this.columnList.addAll(Arrays.asList(columns));
        return this;
    }


    public IndexModel addColumns(ColumnModel... columns) {
        this.columnList.addAll(Arrays.stream(columns).map(ColumnModel::getColumnName).collect(Collectors.toSet()));
        return this;
    }

    public IndexModel setUnique(){
        this.unique = true;
        return this;
    }

    public String getName() {
        return name;
    }

    public IndexModel setName(String name) {
        this.name = name;
        return this;
    }

    public boolean isUnique() {
        return unique;
    }

    public IndexModel setUnique(boolean unique) {
        this.unique = unique;
        return this;
    }

    public StringBuilder getDefinition() {
        return definition;
    }

    public IndexModel setDefinition(StringBuilder definition) {
        this.definition = definition;
        return this;
    }

    public int getPages() {
        return pages;
    }

    public IndexModel setPages(int pages) {
        this.pages = pages;
        return this;
    }

    public IndexType getIndexType() {
        return indexType;
    }

    public IndexModel setIndexType(IndexType indexType) {
        this.indexType = indexType;
        return this;
    }

    public Set<String> getColumnList() {
        return columnList;
    }
}
