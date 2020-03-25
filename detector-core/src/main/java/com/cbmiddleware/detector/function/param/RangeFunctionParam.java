package com.cbmiddleware.detector.function.param;

import com.cbmiddleware.detector.constant.RangeType;
import com.cbmiddleware.detector.constant.FunctionType;

import java.util.List;

/**
 * @author Eason(bo.chenb)
 * @description V  传入值的类型
 * R  区间值的类型
 * @date 2020-03-21
 **/
public class RangeFunctionParam<V> implements FunctionParam<Object> {

    /**
     * 传入进来的值类型
     */
    private V value;
    /**
     * 区间范围
     */
    private List<Range<V>> ranges;

    /**
     * 区间类型
     */
    private RangeType rangeType;

    private Object defaultValue;


    public RangeFunctionParam() {
    }

    public RangeFunctionParam(V value, List<Range<V>> ranges, RangeType rangeType) {
        this.value = value;
        this.ranges = ranges;
        this.rangeType = rangeType;
    }

    public RangeFunctionParam(V value, List<Range<V>> ranges, RangeType rangeType, Object defaultValue) {
        this.value = value;
        this.ranges = ranges;
        this.rangeType = rangeType;
        this.defaultValue = defaultValue;
    }

    @Override
    public Object defaultValue() {
        return this.defaultValue;
    }

    @Override
    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public FunctionType type() {
        return FunctionType.range;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public List<Range<V>> getRanges() {
        return ranges;
    }

    public void setRanges(List<Range<V>> ranges) {
        this.ranges = ranges;
    }

    public RangeType getRangeType() {
        return rangeType;
    }

    public void setRangeType(RangeType rangeType) {
        this.rangeType = rangeType;
    }
}
