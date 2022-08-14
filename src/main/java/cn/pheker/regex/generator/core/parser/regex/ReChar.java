package cn.pheker.regex.generator.core.parser.regex;

import cn.pheker.regex.generator.core.parser.regex.abstracts.ReOne;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/8/14 21:56
 * @desc
 */
public class ReChar extends ReOne {
    protected int ch;
    protected boolean escape;

    public ReChar(int ch) {
        this.ch = ch;
    }

    public int getCh() {
        return ch;
    }

    public void setCh(int ch) {
        this.ch = ch;
    }

    public boolean isEscape() {
        return escape;
    }

    public void setEscape(boolean escape) {
        this.escape = escape;
    }

    @Override
    public boolean intersect(ReOne reOne) {
        if (reOne instanceof ReChar) {
            return ch == ((ReChar) reOne).ch;
        }
        return reOne.intersect(this);
    }
}
