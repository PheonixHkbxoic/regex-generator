package cn.pheker.regex.generator.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/7/30 21:07
 * @desc
 */
public class ObjectUtil {
    
    public static <T> T ofNullable(T t) {
        return t;
    }
    
    public static <K, V> Map<K, V> ofNotNullMap(Map<K, V> map) {
        if (map == null) {
            map = new HashMap<>(3);
        }
        return map;
    }
    
    public static <T> List<T> ofNotNullList(List<T> list) {
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }
    
    
}
