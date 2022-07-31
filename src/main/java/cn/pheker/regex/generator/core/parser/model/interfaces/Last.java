package cn.pheker.regex.generator.core.parser.model.interfaces;

/**
 * @author wanghaijun
 * @version 1.0.0
 * @date 2022/7/30 21:02
 * @desc 获取最后一个元素
 */
public interface Last<T> {
    
    /**
     * 获取最后一个元素
     *
     * @return 最后一个元素
     */
    T getLast();
}
