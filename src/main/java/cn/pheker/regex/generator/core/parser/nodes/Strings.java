package cn.pheker.regex.generator.core.parser.nodes;

import cn.pheker.regex.generator.core.parser.abstracts.NonLeaf;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/7/30 18:02
 * @desc
 */
public class Strings extends Pair {
    protected boolean doubleQuote;
    
    public Strings(NonLeaf parent, boolean doubleQuote) {
        super(parent);
        this.doubleQuote = doubleQuote;
    }
    
    public boolean isDoubleQuote() {
        return doubleQuote;
    }
}
