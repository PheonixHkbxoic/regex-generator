package cn.pheker.regex.generator.core.scanner.abstracts;

import java.util.List;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/7/25 21:28
 * @desc 按行读取
 */
public interface Lines {

    /**
     * 读取一行
     **/
    String readLine();

    /**
     * 读取所有行
     **/
    List<String> readLines();

}
