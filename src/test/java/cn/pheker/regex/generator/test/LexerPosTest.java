package cn.pheker.regex.generator.test;

import cn.pheker.regex.generator.core.lexer.Lexer;
import cn.pheker.regex.generator.core.lexer.Token;
import cn.pheker.regex.generator.core.scanner.*;
import cn.pheker.regex.generator.core.scanner.abstracts.MultiScanner;
import cn.pheker.regex.generator.core.scanner.abstracts.Scanner;
import cn.pheker.regex.generator.util.ResourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/7/25 22:07
 * @desc
 */
@Slf4j
@RunWith(JUnit4.class)
public class LexerPosTest {

    @Test
    public void testTxtScanner() {
        final String path = ResourceUtil.getResourcePath("runoob.json");
        Scanner scanner = new TxtScanner(path);
        Lexer lexer = new Lexer(scanner);

        Token token;
        while (lexer.hasNext()) {
            token = lexer.next();
            log.info("token: {}", token);
        }
    }

    @Test
    public void testStringScanner() throws IOException {
        final String path = ResourceUtil.getResourcePath("runoob.json");
        final byte[] bytes = Files.readAllBytes(new File(path).toPath());
        Scanner scanner = new StringScanner(new String(bytes));
        Lexer lexer = new Lexer(scanner);

        Token token;
        while (lexer.hasNext()) {
            token = lexer.next();
            log.info("token: {}", token);
        }
    }

    @Test
    public void testLinesScanner() {
        final String path = ResourceUtil.getResourcePath("query-words-lib.txt");
        MultiScanner ms = new LinesScanner(path);
        while (ms.hasNext()) {
            final Scanner scanner = ms.next();
            final int offset = scanner.offset();
            final int offsetRow = scanner.offsetRow();
            log.info("testLinesScanner-offset: {}, offsetRow: {}", offset, offsetRow);

            Lexer lexer = new Lexer(scanner);

            Token token;
            while (lexer.hasNext()) {
                token = lexer.next();
                log.info("token: {}", token);
            }
        }
    }

    @Test
    public void testStringLinesScanner() throws IOException {
        final String path = ResourceUtil.getResourcePath("query-words-lib.txt");
        final byte[] bytes = Files.readAllBytes(new File(path).toPath());
        MultiScanner ms = new StringLinesScanner(new String(bytes));
        while (ms.hasNext()) {
            final Scanner scanner = ms.next();
            final int offset = scanner.offset();
            final int offsetRow = scanner.offsetRow();
            log.info("testStringLinesScanner-offset: {}, offsetRow: {}", offset, offsetRow);

            Lexer lexer = new Lexer(scanner);

            Token token;
            while (lexer.hasNext()) {
                token = lexer.next();
                log.info("token: {}", token);
            }
        }
    }

    @Test
    public void testParagraphsScanner() {
        final String path = ResourceUtil.getResourcePath("paragraphs.txt");
        MultiScanner ms = new ParagraphsScanner(path);
        while (ms.hasNext()) {
            final Scanner scanner = ms.next();
            final int offset = scanner.offset();
            final int offsetRow = scanner.offsetRow();
            log.info("testParagraphsScanner-offset: {}, offsetRow: {}", offset, offsetRow);

            Lexer lexer = new Lexer(scanner);

            Token token;
            while (lexer.hasNext()) {
                token = lexer.next();
                log.info("token: {}", token);
            }
        }
    }

    @Test
    public void testStringParagraphsScanner() throws IOException {
        final String path = ResourceUtil.getResourcePath("paragraphs.txt");
        final byte[] bytes = Files.readAllBytes(new File(path).toPath());
        MultiScanner ms = new StringParagraphsScanner(new String(bytes));
        while (ms.hasNext()) {
            final Scanner scanner = ms.next();
            final int offset = scanner.offset();
            final int offsetRow = scanner.offsetRow();
            log.info("testStringParagraphsScanner-offset: {}, offsetRow: {}", offset, offsetRow);

            Lexer lexer = new Lexer(scanner);

            Token token;
            while (lexer.hasNext()) {
                token = lexer.next();
                log.info("token: {}", token);
            }
        }
    }

}
