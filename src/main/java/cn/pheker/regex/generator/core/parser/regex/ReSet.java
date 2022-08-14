package cn.pheker.regex.generator.core.parser.regex;

import cn.pheker.regex.generator.core.parser.regex.abstracts.ReMulti;
import cn.pheker.regex.generator.core.parser.regex.abstracts.ReOne;

import java.util.Iterator;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/8/14 22:01
 * @desc
 */
public class ReSet extends ReMulti<ReOne> {
    private boolean opposite;

    public boolean isOpposite() {
        return opposite;
    }

    public void setOpposite(boolean opposite) {
        this.opposite = opposite;
    }

    public void merge(ReOne one) {
        ReOne exist = null;
        Iterator<ReOne> ite = children.iterator();
        while (ite.hasNext()) {
            ReOne child = ite.next();
            if (child.intersect(one)) {
                ite.remove();
                exist = child;
                break;
            }
        }

        // 合并量词
        this.setMin(Math.min(min, one.getMin()));
        this.setMax(Math.max(max, one.getMax()));

        // 添加
        if (exist == null) {
            children.add(one);
            return;
        }

        // 合并
        if (exist instanceof ReChar && one instanceof ReChar) {
            children.add(exist);
        } else if (exist instanceof ReRange && one instanceof ReRange) {
            ReRange first = (ReRange) exist;
            ReRange second = (ReRange) one;
            int from = Math.min(first.getFrom(), second.getFrom());
            int to = Math.max(first.getTo(), second.getTo());
            ReRange merged = new ReRange(from, to);
            children.add(merged);
        } else {
            children.add(exist instanceof ReRange ? exist : one);
        }
    }
}
