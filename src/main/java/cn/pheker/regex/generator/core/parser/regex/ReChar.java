package cn.pheker.regex.generator.core.parser.regex;

import cn.pheker.regex.generator.core.parser.regex.abstracts.ReOne;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/8/14 21:56
 * @desc 单字符节点
 */
public class ReChar extends ReOne {
    protected int ch;
    protected boolean escape;
    protected boolean empty;

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

    public boolean isEmpty() {
        return ch == 0;
    }

    @Override
    public boolean intersect(ReOne reOne) {
        if (reOne instanceof ReChar) {
            return ch == ((ReChar) reOne).ch;
        }
        return reOne.intersect(this);
    }

    @Override
    public boolean contains(int ch) {
        return this.ch == ch;
    }

    /**
     * 当前字符是否在指定字符区间内[from, to]
     *
     * @param from 开始字符
     * @param to   结束字符
     * @return 当前字符是否在指定字符区间内
     */
    public boolean between(int from, int to) {
        for (int ch = from; ch <= to; ch++) {
            if (contains(ch)) {
                return true;
            }
        }
        return false;
    }
}
