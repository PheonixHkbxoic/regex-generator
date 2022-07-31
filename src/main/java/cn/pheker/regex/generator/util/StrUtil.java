package cn.pheker.regex.generator.util;

import com.sun.istack.internal.NotNull;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/7/26 22:18
 * @desc
 */
public class StrUtil {
    
    public static String times(int i, @NotNull String sep, @NotNull String str) {
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < i; j++) {
            sb.append(str).append(sep);
        }
        sb.delete(sb.length() - sep.length(), sb.length());
        return sb.toString();
    }
    
    public static String times(int i) {
        return times(i, "    ");
    }
    
    public static String times(int i, @NotNull String str) {
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < i; j++) {
            sb.append(str);
        }
        return sb.toString();
    }
}
