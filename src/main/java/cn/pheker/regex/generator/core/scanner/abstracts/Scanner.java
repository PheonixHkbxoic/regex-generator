package cn.pheker.regex.generator.core.scanner.abstracts;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/7/25 21:21
 * @desc 扫描器 用于扫描文本字符
 */
public interface Scanner {
    int EOF = -1;

    /**
     * 读取一个字符
     *
     * @return 符号unicode, 如果文件结束返回EOF, 即-1
     **/
    int read();

    /**
     * 整体偏移字符数, 默认为0
     *
     * @return 偏移字符数
     */
    int offset();

    /**
     * 整体偏移行数
     *
     * @return 偏移行数
     **/
    int offsetRow();
}
