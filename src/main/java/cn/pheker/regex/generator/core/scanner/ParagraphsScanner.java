package cn.pheker.regex.generator.core.scanner;

import cn.pheker.regex.generator.core.scanner.abstracts.AbstractFileScanner;
import cn.pheker.regex.generator.core.scanner.abstracts.MultiScanner;
import cn.pheker.regex.generator.core.scanner.abstracts.Paragraphs;
import cn.pheker.regex.generator.core.scanner.abstracts.Scanner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * @author cn.pheker
 * @version 1.0
 * @date 2022/8/10 10:14
 * 从文件读取每个段落作为一个独立的扫描单元
 */
public class ParagraphsScanner extends AbstractFileScanner implements Paragraphs, MultiScanner {
    boolean trim;
    StringParagraphsScanner sps;


    public ParagraphsScanner(String path) {
        this(path, true);
    }

    public ParagraphsScanner(String path, boolean trim) {
        this.trim = trim;
        setFilePath(path);
    }

    @Override
    public void setFilePath(String path) {
        this.path = path;
        init();
    }

    @Override
    public int read() {
        throw new RuntimeException("ParagraphsScanner did not support read method");
    }

    private void init() {
        try {
            final Path path = new File(this.path).toPath();
            String text = new String(Files.readAllBytes(path));
            this.sps = new StringParagraphsScanner(text, this.trim);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String readParagraph() {
        return sps.readParagraph();
    }

    @Override
    public List<String> readParagraphs() {
        return sps.readParagraphs();
    }

    @Override
    public boolean hasNext() {
        return sps.hasNext();
    }

    @Override
    public Scanner next() {
        return sps.next();
    }

}
