package cn.pheker.regex.generator.core.scanner;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/7/25 21:21
 * @desc
 */
public interface Scanner {
    int EOF = -1;
    
    int read();
    
}
