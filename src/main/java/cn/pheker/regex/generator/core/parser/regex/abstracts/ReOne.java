package cn.pheker.regex.generator.core.parser.regex.abstracts;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/8/14 22:02
 * @desc
 */
public abstract class ReOne extends ReNode {
    protected String alias;

    /**
     * 与其它节点是否存在交集
     *
     * @param reOne 其它节点
     * @return true存在交集, false不存在交集
     */
    public abstract boolean intersect(ReOne reOne);

    /**
     * 是否包含指定字符
     *
     * @param ch 指定字符
     * @return 是否包含指定字符
     */
    public abstract boolean contains(int ch);

    /**
     * 是否包含任意一个指定字符
     *
     * @param chs 指定字符列表
     * @return 是否包含任意一个指定字符
     */
    public boolean containsAny(int... chs) {
        for (int ch : chs) {
            if (contains(ch)) {
                return true;
            }
        }
        return false;
    }

}
