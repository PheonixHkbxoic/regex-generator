package cn.pheker.regex.generator.test;

import cn.pheker.regex.generator.core.lexer.Lexer;
import cn.pheker.regex.generator.core.lexer.Token;
import cn.pheker.regex.generator.core.scanner.Scanner;
import cn.pheker.regex.generator.core.scanner.TxtScanner;
import cn.pheker.regex.generator.util.ResourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/7/25 22:07
 * @desc
 */
@Slf4j
@RunWith(JUnit4.class)
public class LexerPosTest {
    Lexer lexer;
    
    @Before
    public void initScanner() {
        final String path = ResourceUtil.getResourcePath("runoob.json");
        Scanner scanner = new TxtScanner(path);
        lexer = new Lexer(scanner);
    }
    
    @Test
    public void testPos() {
        Token token;
        while (lexer.hasNext()) {
            token = lexer.next();
            log.info("token: {}", token);
        }
        Token next = lexer.next();
        log.info("next: {}", next);
        next = lexer.next();
        log.info("next: {}", next);
    }
    
}
