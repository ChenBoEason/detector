package com.cbmiddleware.detector.function;

import com.cbmiddleware.detector.constant.FunctionType;
import com.cbmiddleware.detector.exception.FunctionConvertException;
import com.cbmiddleware.detector.function.param.DictionaryFunctionParam;

import java.util.Map;

/**
 * @author Eason(bo.chenb)
 * @description
 * @date 2020-03-21
 **/
public class DictionaryFunction implements Function<String, DictionaryFunctionParam> {


    @Override
    public String convert(DictionaryFunctionParam param) throws FunctionConvertException {

        String value = param.getValue();

        if (value == null || value.length() == 0) {
            return param.defaultValue();
        }
        /* 映射关系 */
        Map<String, String> mappingRelation = param.getMappingRelation();

        String result = mappingRelation.get(value);

        if (result == null) {
            return param.defaultValue();
        }

        return result;
    }

    @Override
    public FunctionType type() {
        return FunctionType.dictionary;
    }
}
