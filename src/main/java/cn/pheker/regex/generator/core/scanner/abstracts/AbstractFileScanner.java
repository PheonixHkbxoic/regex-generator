package cn.pheker.regex.generator.core.scanner.abstracts;

/**
 * @author cn.pheker
 * @version 1.0
 * @date 2022/8/12 10:54
 */
public abstract class AbstractFileScanner extends AbstractScanner implements FileScanner {
    protected String path;

    @Override
    public void setFilePath(String path) {
        this.path = path;
    }
}
