package com.cbmiddleware.detector.function.param;

/**
 * @author Eason(bo.chenb)
 * @description
 * @date 2020-03-21
 **/
public class Range<V> {

    /**
     * 满足条件返回的值
     */
    private Object value;
    /**
     * 最小值域
     */
    private V min;
    /**
     * 最大值域
     */
    private V max;

    public Range() {
    }

    public Range(Object value, V min, V max) {
        this.value = value;
        this.min = min;
        this.max = max;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }


    public V getMin() {
        return min;
    }

    public void setMin(V min) {
        this.min = min;
    }

    public V getMax() {
        return max;
    }

    public void setMax(V max) {
        this.max = max;
    }
}
