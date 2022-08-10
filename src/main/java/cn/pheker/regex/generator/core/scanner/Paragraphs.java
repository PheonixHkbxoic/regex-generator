package cn.pheker.regex.generator.core.scanner;

import java.util.List;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/7/25 21:28
 * @desc 按段落读取
 */
public interface Paragraphs {

    /**
     * 读取一个段落
     **/
    String readParagraph();

    /**
     * 读取所有段落
     **/
    List<String> readParagraphs();
    
}
