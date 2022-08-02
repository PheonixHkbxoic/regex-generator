package cn.pheker.regex.generator.core.parser.model.nodes;

import cn.pheker.regex.generator.core.lexer.Lexer;
import cn.pheker.regex.generator.core.lexer.Token;
import cn.pheker.regex.generator.core.lexer.TokenType;
import cn.pheker.regex.generator.core.parser.model.abstracts.AbstractComposite;
import cn.pheker.regex.generator.core.parser.model.abstracts.NonLeaf;
import lombok.extern.slf4j.Slf4j;

/**
 * @author cn.pheker
 * @version 1.0
 * @date 2022/8/1 13:37
 *
 */
@Slf4j
public class Id extends AbstractComposite {
    public Id(NonLeaf parent) {
        super(parent);
    }

    @Override
    public boolean parse() {
        Lexer lexer = context.getLexer();
        Token token;
        OUTER:
        while ((token = lexer.read()) != Token.EOF) {
            if (log.isDebugEnabled()) {
                log.debug("token: {}", token);
            }
            TokenType type = token.getType();
            switch (type) {
//                case Dollar:
                case Underscore:
                case Upper:
                case Lower:
                case DIGIT:
                    // 出口
                    this.add(Single.of(this));
                    break;
                default:
                    // 出口
                    break OUTER;
            }
        }

        return this.parseSuccess();
    }

}
