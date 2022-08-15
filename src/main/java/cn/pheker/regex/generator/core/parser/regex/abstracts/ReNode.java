package cn.pheker.regex.generator.core.parser.regex.abstracts;

import cn.pheker.regex.generator.core.parser.other.Mode;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/8/14 21:36
 * @desc 正则节点
 * 包含通用属性:
 * 节点级别(模式)
 * 节点可匹配字符长度
 * 节点分组类型
 */
public abstract class ReNode {
    protected Mode mode = Mode.Accurate;
    protected int min;
    protected int max;
    protected GroupType groupType;

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

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
