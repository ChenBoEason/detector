package com.cbmiddleware.detector.function;

import com.cbmiddleware.detector.constant.FunctionType;
import com.cbmiddleware.detector.constant.RangeType;
import com.cbmiddleware.detector.exception.FunctionConvertException;
import com.cbmiddleware.detector.function.param.Range;
import com.cbmiddleware.detector.function.param.RangeFunctionParam;

import java.util.List;

/**
 * @author Eason(bo.chenb)
 * @description
 * @date 2020-03-21
 **/
public class RangeFunction<V> implements Function<Object, RangeFunctionParam<V>> {


    @Override
    public Object convert(RangeFunctionParam<V> param) throws FunctionConvertException {
        V value = param.getValue();

        Object defaultValue = param.defaultValue();
        if (value == null) {
            return defaultValue;
        }

        RangeType rangeType = param.getRangeType();

        List<Range<V>> ranges = param.getRanges();

        if (value instanceof String) {

            return rangeString((String) value, ranges, rangeType, defaultValue);
        }

        if (value instanceof Integer) {
            return rangeInteger((Integer) value, ranges, rangeType, defaultValue);
        }

        if (value instanceof Double) {
            return rangeDouble((Double) value, ranges, rangeType, defaultValue);
        }

        if (value instanceof Long) {
            return rangeLong((Long) value, ranges, rangeType, defaultValue);
        }

        return defaultValue;
    }


    @Override
    public FunctionType type() {
        return FunctionType.range;
    }

    private Object rangeLong(Long value, List<Range<V>> ranges, RangeType rangeType, Object defaultValue) {
        for (Range<V> range : ranges) {
            Long min = (Long) range.getMin();
            Long max = (Long) range.getMax();
            boolean left = false;
            boolean right = false;
            switch (rangeType) {
                /**
                 * 左闭右闭
                 */
                case lcrc:
                    left = value >= min;
                    right = value <= max;
                    break;
                /**
                 * 左闭右开
                 */
                case lcro:
                    left = value >= min;
                    right = value < max;
                    break;
                /**
                 * 左开右闭
                 */
                case lorc:
                    left = value > min;
                    right = value <= max;
                    break;

                /**
                 * 左开右开
                 */
                case loro:
                    left = value > min;
                    right = value < max;
                    break;
                default:
                    break;
            }

            if (left && right) {
                return range.getValue();
            }
        }

        return defaultValue;
    }

    private Object rangeDouble(Double value, List<Range<V>> ranges, RangeType rangeType, Object defaultValue) {
        for (Range<V> range : ranges) {
            Double min = (Double) range.getMin();
            Double max = (Double) range.getMax();
            boolean left = false;
            boolean right = false;
            switch (rangeType) {
                /**
                 * 左闭右闭
                 */
                case lcrc:
                    left = value >= min;
                    right = value <= max;
                    break;
                /**
                 * 左闭右开
                 */
                case lcro:
                    left = value >= min;
                    right = value < max;
                    break;
                /**
                 * 左开右闭
                 */
                case lorc:
                    left = value > min;
                    right = value <= max;
                    break;

                /**
                 * 左开右开
                 */
                case loro:
                    left = value > min;
                    right = value < max;
                    break;
                default:
                    break;
            }

            if (left && right) {
                return range.getValue();
            }
        }

        return defaultValue;
    }

    private Object rangeInteger(Integer value, List<Range<V>> ranges, RangeType rangeType, Object defaultValue) {
        for (Range<V> range : ranges) {
            Integer min = (Integer) range.getMin();
            Integer max = (Integer) range.getMax();
            boolean left = false;
            boolean right = false;
            switch (rangeType) {
                /**
                 * 左闭右闭
                 */
                case lcrc:
                    left = value >= min;
                    right = value <= max;
                    break;
                /**
                 * 左闭右开
                 */
                case lcro:
                    left = value >= min;
                    right = value < max;
                    break;
                /**
                 * 左开右闭
                 */
                case lorc:
                    left = value > min;
                    right = value <= max;
                    break;

                /**
                 * 左开右开
                 */
                case loro:
                    left = value > min;
                    right = value < max;
                    break;
                default:
                    break;
            }

            if (left && right) {
                return range.getValue();
            }
        }

        return defaultValue;
    }

    private Object rangeString(String value, List<Range<V>> ranges, RangeType rangeType, Object defaultValue) {

        for (Range<V> range : ranges) {
            String min = (String) range.getMin();
            String max = (String) range.getMax();
            boolean left = false;
            boolean right = false;
            switch (rangeType) {
                /**
                 * 左闭右闭
                 */
                case lcrc:
                    left = value.compareTo(min) >= 0;
                    right = value.compareTo(max) <= 0;
                    break;
                /**
                 * 左闭右开
                 */
                case lcro:
                    left = value.compareTo(min) >= 0;
                    right = value.compareTo(max) < 0;
                    break;
                /**
                 * 左开右闭
                 */
                case lorc:
                    left = value.compareTo(min) > 0;
                    right = value.compareTo(max) <= 0;
                    break;

                /**
                 * 左开右开
                 */
                case loro:
                    left = value.compareTo(min) > 0;
                    right = value.compareTo(max) < 0;
                    break;
                default:
                    break;
            }

            if (left && right) {
                return range.getValue();
            }
        }

        return defaultValue;
    }


}
