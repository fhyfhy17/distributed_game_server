package com.util;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName Util
 * @Description 通用工具类
 * @Author dafeng
 * @Date 2019/1/28 11:29
 **/
public class Util {
    /**
     * 通用 Map根据Value排序，注意返回的是LinkedHashMap
     *
     * @param map 需要排序的map
     * @param <K>
     * @param <V>
     * @return 有序的map
     */
    public static <K, V extends Comparable> LinkedHashMap<K, V> mapValueSort(Map<K, V> map) {
        return (LinkedHashMap<K, V>) map.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue))
                .collect(Collectors.toMap((Map.Entry<K, V> k) -> k.getKey(), (Map.Entry<K, V> v) -> v.getValue(), (k, v) -> v, LinkedHashMap::new));

    }

    /**
     * 数组转List
     *
     * @param us  要操作的数组
     * @param <T>
     * @return List
     */
    public static <T> List<T> arrayToList(T[] us) {
        return Arrays.stream(us).collect(Collectors.toList());
    }

}
