package com.cbmiddleware.detector.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * Class Name is ConcatUtils
 *
 * @author LiJun
 * Created on 2020/10/27 5:59 下午
 */
public final class ConcatUtils {

    public static String joinStringList(Collection<String> list, String joiner) {
        StringBuilder sb = new StringBuilder();
        list.forEach(item -> sb.append(item).append(joiner));
        String result = sb.toString();
        return result.substring(0, result.length() - joiner.length());
    }

    public static String joinStringList(String joiner, String... items){

        StringBuilder sb = new StringBuilder();
        for (String item : items) {
            sb.append(item).append(joiner);
        }
        String result = sb.toString();
        return result.substring(0, result.length() - joiner.length());
    }

    public static <Source> String joinStringList(Collection<Source> list, String joiner, Function<Source, String> function) {
        StringBuilder sb = new StringBuilder();
        list.forEach(item -> sb.append(function.apply(item)).append(joiner));
        String result = sb.toString();
        return result.substring(0, result.length() - joiner.length());
    }

    public static <Source> String joinStringList(Collection<Source> list, Function<Source, String> prefix, Function<Source, String> suffix, int subLen) {
        StringBuilder sb = new StringBuilder();
        list.forEach(item -> sb.append(prefix == null ? "" : prefix.apply(item)).append(null == suffix ? "" : suffix.apply(item)));
        String result = sb.toString();
        return result.substring(0, result.length() - subLen);
    }

    public static <Source> String[] joinStringList(Collection<Source> list, Function<Source, String>[]... functions) {

        int size = functions.length;

        StringBuilder[] builders = new StringBuilder[size];
        for (int i = 0; i < size; i++) {
            builders[i] = new StringBuilder();
        }
        for (Source item : list) {
            for (int i = 0, len = functions.length; i < len; i++) {
                Function<Source, String>[] function = functions[i];
                StringBuilder sb = builders[i];
                for (Function<Source, String> innerFunction : function) {
                    if (null != innerFunction) {
                        sb.append(innerFunction.apply(item));
                    }
                }
            }
        }
        String[] result = new String[size];
        for (int i = 0; i < size; i++) {
            result[i] = builders[i].toString();
        }
        return result;
    }


    public static <Source> String format(String formatString, Collection<Source> list, Concat<Source>... concat) {
        return String.format(formatString, joinStringList(list, concat));
    }

    public static <Source> String[] joinStringList(Collection<Source> list, Concat<Source>... concat) {
        int size = concat.length;

        StringBuilder[] builders = new StringBuilder[size];
        for (int i = 0; i < size; i++) {
            builders[i] = new StringBuilder();
        }
        for (Source item : list) {
            for (int i = 0, len = concat.length; i < len; i++) {
                Concat<Source> sourceConcat = concat[i];
                StringBuilder sb = builders[i];
                for (Function<Source, String> innerFunction : sourceConcat.functionList) {
                    if (null != innerFunction) {
                        sb.append(innerFunction.apply(item)).append(sourceConcat.joiner == null ? "" : sourceConcat.joiner);
                    }
                }
            }
        }

        String[] result = new String[size];
        for (int i = 0; i < size; i++) {
            String str = builders[i].toString();
            result[i] = str.substring(0, str.length() - concat[i].subLen);

        }
        return result;
    }


    public static class Concat<Source> {
        List<Function<Source, String>> functionList = new ArrayList<>();
        int subLen = 0;
        String joiner;

        public Concat<Source> function(Function<Source, String> function) {
            this.functionList.addAll(Collections.singletonList(function));
            return this;
        }

        public Concat<Source> setJoiner(String joiner) {
            this.joiner = joiner;
            this.subLen = joiner.length();
            return this;
        }

        public Concat<Source> setSubLen(int subLen) {
            this.subLen = subLen;
            return this;
        }

        public Concat<Source> setSubLen(String str) {
            this.subLen = str.length();
            return this;
        }
    }

    public static <Source> Concat<Source> builder(Class<Source> clazz) {
        return new Concat<>();
    }

    private ConcatUtils() {
    }
}
