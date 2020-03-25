package com.cbmiddleware.detector.function;

import com.cbmiddleware.detector.constant.RangeType;
import com.cbmiddleware.detector.exception.FunctionConvertException;
import com.cbmiddleware.detector.function.param.Range;
import com.cbmiddleware.detector.function.param.RangeFunctionParam;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eason(bo.chenb)
 * @description
 * @date 2020-03-21
 **/
public class RangeTest {


    @Test
    public void stringTest(){
        Function function = new RangeFunction<String>();

        List<Range<String>> ranges = new ArrayList<>();

        Range<String> range1 = new Range<>();
        range1.setMin("10");
        range1.setMax("10.9");
        range1.setValue("在10到11之间");

        Range<String> range2 = new Range<>();
        range2.setMin("11");
        range2.setMax("11.9");
        range2.setValue("在11到12之间");

        ranges.add(range1);
        ranges.add(range2);

        RangeFunctionParam<String> param = new RangeFunctionParam<>("9", ranges, RangeType.lcrc,"未知区间");

        try {
            Object convert = function.convert(param);
            System.out.println(convert);
        } catch (FunctionConvertException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void longTest(){
        Function function = new RangeFunction<String>();

        List<Range<Long>> ranges = new ArrayList<>();

        Range<Long> range1 = new Range<>();
        range1.setMin(10L);
        range1.setMax(11L);
        range1.setValue("在10到11之间");

        Range<Long> range2 = new Range<>();
        range2.setMin(11L);
        range2.setMax(12L);
        range2.setValue("在11到12之间");

        ranges.add(range1);
        ranges.add(range2);

        RangeFunctionParam<Long> param = new RangeFunctionParam<>(11L, ranges, RangeType.lcro,"未知区间");

        try {
            Object convert = function.convert(param);
            System.out.println(convert);
        } catch (FunctionConvertException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void doubleTest(){
        Function function = new RangeFunction<String>();

        List<Range<Double>> ranges = new ArrayList<>();

        Range<Double> range1 = new Range<>();
        range1.setMin(10.0);
        range1.setMax(11.0);
        range1.setValue("在10到11之间");

        Range<Double> range2 = new Range<>();
        range2.setMin(11.0);
        range2.setMax(12.0);
        range2.setValue("在11到12之间");

        ranges.add(range1);
        ranges.add(range2);

        RangeFunctionParam<Double> param = new RangeFunctionParam<>(12.0D, ranges, RangeType.lcro,"未知区间");

        try {
            Object convert = function.convert(param);
            System.out.println(convert);
        } catch (FunctionConvertException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void test(){
        System.out.println("11".compareTo("11"));
    }
}
