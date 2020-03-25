package com.cbmiddleware.detector.sql.function;

import com.cbmiddleware.detector.constant.RangeType;
import com.cbmiddleware.detector.constant.FunctionType;
import com.cbmiddleware.detector.function.param.Range;

import java.util.List;

/**
 * @author Eason(bo.chenb)
 * @description
 * @date 2020-03-21
 **/
public class ColumnRange<V> extends AbstractSqlFunctionParam{


    /**
     * 区间范围
     */
    private List<Range<V>> ranges;

    /**
     * 区间类型
     */
    private RangeType rangeType;


    @Override
    public FunctionType type() {
        return FunctionType.range;
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
