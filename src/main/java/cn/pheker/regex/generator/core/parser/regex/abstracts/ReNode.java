package cn.pheker.regex.generator.core.parser.regex.abstracts;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/8/14 21:36
 * @desc
 */
public abstract class ReNode {
    protected int min;
    protected int max;
    protected GroupType groupType;

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public GroupType getGroupType() {
        return groupType;
    }

    public void setGroupType(GroupType groupType) {
        this.groupType = groupType;
    }
}
