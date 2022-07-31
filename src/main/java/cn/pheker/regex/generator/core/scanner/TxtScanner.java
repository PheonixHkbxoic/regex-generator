package cn.pheker.regex.generator.core.scanner;

import java.util.List;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/7/25 21:28
 * @desc
 */
public interface TxtScanner extends FileScanner {
    
    String readLine();
    
    List<String> readAll();
    
}
