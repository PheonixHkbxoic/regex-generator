package cn.pheker.regex.generator.core.scanner;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/7/25 21:28
 * @desc 每行作为一个独立的扫描单元
 */
public class LinesScanner extends TxtScanner implements Lines, MultiScanner {
    String line;
    public LinesScanner(String txtPath) {
        super(txtPath);
        this.readLine();
    }

    @Override
    public String readLine() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public List<String> readLines() {
        return reader.lines().collect(Collectors.toList());
    }

    @Override
    public boolean hasNext() {
        return line != null;
    }

    @Override
    public Scanner next() {
        line = readLine();
        return new StringScanner(this.line);
    }
}
