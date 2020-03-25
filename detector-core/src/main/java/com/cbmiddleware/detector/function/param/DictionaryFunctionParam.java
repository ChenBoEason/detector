package com.cbmiddleware.detector.function.param;

import com.cbmiddleware.detector.constant.FunctionType;

import java.util.Map;

/**
 * @author Eason(bo.chenb)
 * @description
 * @date 2020-03-21
 **/
public class DictionaryFunctionParam implements FunctionParam<String> {

    /**
     * 真实值-待转换的参数值
     */
    private String value;
    /**
     * 映射关系
     */
    private Map<String, String> mappingRelation;

    private String defaultValue;


    public DictionaryFunctionParam() {
    }

    public DictionaryFunctionParam(String value, Map<String, String> mappingRelation) {
        this.value = value;
        this.mappingRelation = mappingRelation;
    }

    public DictionaryFunctionParam(String value, Map<String, String> mappingRelation, String defaultValue) {
        this.value = value;
        this.mappingRelation = mappingRelation;
        this.defaultValue = defaultValue;
    }

    @Override
    public String defaultValue() {
        return this.defaultValue;
    }

    @Override
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public FunctionType type() {
        return FunctionType.dictionary;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Map<String, String> getMappingRelation() {
        return mappingRelation;
    }

    public void setMappingRelation(Map<String, String> mappingRelation) {
        this.mappingRelation = mappingRelation;
    }
}
