package cn.pheker.regex.generator.core.parser.nodes;

import cn.pheker.regex.generator.core.lexer.Token;
import cn.pheker.regex.generator.core.parser.abstracts.Leaf;
import cn.pheker.regex.generator.core.parser.abstracts.NonLeaf;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/8/4 21:33
 * @desc 空节点
 */
public class Empty extends Leaf {
    public Empty(NonLeaf parent) {
        super(parent);
        this.token = Token.EOF;
    }
    
}
