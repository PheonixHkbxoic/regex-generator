package cn.pheker.regex.generator.core.parser.interfaces;

/**
 * @author wanghj
 * @version 1.0
 * @date 2022/8/4 16:55
 * 深度格式化, 一般用于树形结构 结点格式化
 */
public interface DeepFormat {

    /**
     * 按深度格式化
     * @return 格式化后的字符串
     **/
    String format();
}
