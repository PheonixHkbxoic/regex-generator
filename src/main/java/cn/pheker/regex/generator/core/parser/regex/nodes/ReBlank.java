package cn.pheker.regex.generator.core.parser.regex.nodes;

import cn.pheker.regex.generator.core.parser.other.Mode;
import cn.pheker.regex.generator.core.parser.regex.ReChar;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/8/15 22:20
 * @desc
 */
public class ReBlank extends ReChar {

    public ReBlank(int ch) {
        super(ch);
        this.alias = "\\s";
        this.mode = Mode.Low;
    }

    public boolean isCr() {
        return ch == '\r';
    }

    public boolean isLf() {
        return ch == '\n';
    }

    public boolean isNonCrLf() {
        return !isCr() && !isLf();
    }
}
