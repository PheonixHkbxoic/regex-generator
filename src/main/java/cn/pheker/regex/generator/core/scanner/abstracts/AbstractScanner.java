package cn.pheker.regex.generator.core.scanner.abstracts;

/**
 * @author cn.pheker
 * @version 1.0
 * @date 2022/8/12 10:45
 */
public abstract class AbstractScanner implements Scanner {
    protected int offset;
    protected int offsetRow;

    @Override
    public int offset() {
        return offset;
    }

    @Override
    public int offsetRow() {
        return offsetRow;
    }
}
