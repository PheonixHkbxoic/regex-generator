package cn.pheker.regex.generator.core.parser.nodes;

import cn.pheker.regex.generator.core.parser.MetaInfo;
import cn.pheker.regex.generator.core.parser.abstracts.AbstractComposite;
import cn.pheker.regex.generator.core.parser.abstracts.NonLeaf;
import cn.pheker.regex.generator.core.parser.interfaces.Node;
import cn.pheker.regex.generator.exception.TooManyRegex;

import java.util.List;

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
        return "Sequence{" + children + '}';
    }
    
    @Override
    public List<String> generateRegex() {
        List<String> regex = null;
        boolean containEmpty = children().removeIf(c -> c.isExtendsOf(Empty.class));
        if (containEmpty) {
            this.add(new Empty(this));
        }
        for (Node child : children()) {
            // 判断mode
            
            MetaInfo meta = child.getMetaInfo();
            regex = cartesian(regex, child.generateRegex());
            if (regex.size() > 10000) {
                throw new TooManyRegex();
            }
        }
        return regex;
    }
}
