package cn.pheker.regex.generator.core.parser.model.nodes;

import cn.pheker.regex.generator.core.lexer.Lexer;
import cn.pheker.regex.generator.core.lexer.Token;
import cn.pheker.regex.generator.core.parser.model.abstracts.AbstractComposite;
import cn.pheker.regex.generator.core.parser.model.abstracts.NonLeaf;
import cn.pheker.regex.generator.core.parser.model.interfaces.Node;

import static cn.pheker.regex.generator.core.lexer.Token.EOF;
import static cn.pheker.regex.generator.core.lexer.TokenType.DIGIT;
import static cn.pheker.regex.generator.core.lexer.TokenType.Dot;

/**
 * @author cn.pheker
 * @version 1.0
 * @date 2022/8/1 15:07
 */
public class DotX extends AbstractComposite {
    public DotX(NonLeaf parent) {
        super(parent);
    }

    @Override
    public boolean parse() {
        add(Single.of(this));
        final Lexer lexer = context.getLexer();
        Token token;
        if ((token = lexer.read()) != EOF) {
            switch (token.getType()) {
//                case Dollar:
                case Underscore:
                case Upper:
                case Lower:
                case DIGIT:
                    final Node last = getLast();
//                     第一个就是数字则按小数处理
                    if (last.isLeafTokenType(Dot) && token.isTokenType(DIGIT)) {
                        super.parse();
                        break;
                    }
        
                    // 第一个非数字  按id处理
                    final Id id = new Id(this);
                    id.parse();
                    this.add(id);
                    break;
                default:
                    break;
            }
        }
        return this.parseSuccess();
    }

    @Override
    public boolean parseSuccess() {
        return size() > 1;
    }
}
