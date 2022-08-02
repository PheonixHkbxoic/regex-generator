package cn.pheker.regex.generator.core.parser.nodes;

import cn.pheker.regex.generator.core.parser.abstracts.NonLeaf;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/7/30 18:07
 * @desc
 */
public class Group extends Pair {
    public Group(NonLeaf parent) {
        super(parent);
    }
}
