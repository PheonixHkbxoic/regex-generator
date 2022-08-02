package cn.pheker.regex.generator.core.parser.nodes;

import cn.pheker.regex.generator.core.parser.abstracts.AbstractComposite;
import cn.pheker.regex.generator.core.parser.abstracts.NonLeaf;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/7/27 22:07
 * @desc
 */
public class Branches extends AbstractComposite {
    
    public Branches(NonLeaf parent) {
        super(parent);
    }
    
    @Override
    public String toString() {
        return "Branches{" + children + '}';
    }
    
}
