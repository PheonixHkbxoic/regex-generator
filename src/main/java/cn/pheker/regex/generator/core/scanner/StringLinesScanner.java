package cn.pheker.regex.generator.core.scanner;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022-08-11 11:41:07
 * @desc
 */
public class StringLinesScanner implements MultiScanner, Lines {
    private List<String> lines;
    private int index;

    public StringLinesScanner(String text) {
        this.lines = Arrays.stream(text.split("(?:[\\r\\n]|\\\\r|\\\\n)+"))
                .collect(Collectors.toList());
    }

    @Override
    public int read() {
        throw new RuntimeException("StringLinesScanner did not support read method");
    }

    @Override
    public String readLine() {
        return lines.get(index);
    }

    @Override
    public List<String> readLines() {
        return lines;
    }

    @Override
    public boolean hasNext() {
        return index < lines.size();
    }

    @Override
    public Scanner next() {
        final String line = readLine();
        index++;
        return new StringScanner(line);
    }
}
