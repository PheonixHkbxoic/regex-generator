package cn.pheker.regex.generator.core.scanner;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author cn.pheker
 * @version 1.0
 * @date 2022/8/10 10:14
 * 从文本读取每个段落作为一个独立的扫描单元
 */
public class StringParagraphsScanner implements Paragraphs, MultiScanner {
    List<String> paragraphs;
    int index = 0;

    public StringParagraphsScanner(String text) {
        splitParagraphs(text, true);
    }

    public StringParagraphsScanner(String text, boolean trim) {
        splitParagraphs(text, trim);
    }

    public void splitParagraphs(String text, boolean trim) {
        final String[] paragraphs = text.split("\\+{5,}\\s*?\n");
        if (paragraphs.length > 0) {
            paragraphs[0] = paragraphs[0].replaceFirst("^[\\s]+", "");
        }
        Stream<String> stream = Arrays.stream(paragraphs);
        if (trim) {
            stream = stream.map(s -> s.trim());
        }
        this.paragraphs = stream.collect(Collectors.toList());
    }

    @Override
    public String readParagraph() {
        return paragraphs.get(index);
    }

    @Override
    public List<String> readParagraphs() {
        return paragraphs;
    }

    @Override
    public boolean hasNext() {
        return index < paragraphs.size();
    }

    @Override
    public Scanner next() {
        final String p = readParagraph();
        index++;
        return new StringScanner(p);
    }

    @Override
    public int read() {
        throw new RuntimeException("StringParagraphsScanner did not support read method");
    }
}
