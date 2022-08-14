package cn.pheker.regex.generator.core.parser.regex.abstracts;

import java.util.List;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/8/14 21:45
 * @desc
 */
public class ReMulti<T> extends ReNode {
    protected List<T> children;

    public List<T> getChildren() {
        return children;
    }

    public void setChildren(List<T> children) {
        this.children = children;
    }
}
