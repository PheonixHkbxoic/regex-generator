package cn.pheker.regex.generator.core.parser.model.nodes;

import cn.pheker.regex.generator.core.parser.model.abstracts.AbstractComposite;
import cn.pheker.regex.generator.core.parser.model.abstracts.NonLeaf;
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
    
    @Override
    public boolean parseSuccess() {
        return true;
    }
}
