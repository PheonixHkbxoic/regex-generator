package cn.pheker.regex.generator.core.parser.nodes;

import cn.pheker.regex.generator.core.parser.abstracts.AbstractComposite;
import cn.pheker.regex.generator.core.parser.abstracts.NonLeaf;
import cn.pheker.regex.generator.util.StrUtil;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/7/27 21:52
 * @desc
 */
public class Sequence extends AbstractComposite {
    
    
    public Sequence(NonLeaf parent) {
        super(parent);
    }
    
    @Override
    public String toString() {
        return StrUtil.times(this.getDeep()) + "Sequence{" + children + '}';
    }
    
}
