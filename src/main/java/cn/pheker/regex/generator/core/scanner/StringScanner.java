package cn.pheker.regex.generator.core.scanner;

import cn.pheker.regex.generator.core.scanner.abstracts.AbstractScanner;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/7/27 21:04
 * @desc
 */
public class StringScanner extends AbstractScanner {
    private String str;
    private int cursor;

    public StringScanner(String str) {
        this.str = str;
    }

    public StringScanner(int offset, int offsetRow, String str) {
        this.offset = offset;
        this.offsetRow = offsetRow;
        this.str = str;
    }

    @Override
    public int read() {
        if (str == null || cursor >= str.length()) {
            return EOF;
        }
        return str.charAt(cursor++);
    }
}
