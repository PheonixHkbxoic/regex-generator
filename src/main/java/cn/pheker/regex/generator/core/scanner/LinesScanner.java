package cn.pheker.regex.generator.core.scanner;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/7/25 21:28
 * @desc
 */
public class LinesScanner extends TxtScanner implements Lines {

    public LinesScanner(String txtPath) {
        super(txtPath);
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

}
