package cn.pheker.regex.generator.core.parser.regex;

import cn.pheker.regex.generator.core.parser.regex.abstracts.ReOne;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/8/14 21:59
 * @desc 范围 单字符节点
 */
public class ReRange extends ReOne {
    private int from;
    private int to;

    public ReRange(int from, int to) {
        this.from = from;
        this.to = to;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    @Override
    public boolean intersect(ReOne reOne) {
        if (reOne instanceof ReChar) {
            ReChar reChar = (ReChar) reOne;
            return reChar.ch >= from && reChar.ch <= to;
        }

        ReRange reRange = (ReRange) reOne;
        return !(to < reRange.from || reRange.to < from);
    }

    @Override
    public boolean contains(int ch) {
        return from <= ch && ch <= to;
    }
}
