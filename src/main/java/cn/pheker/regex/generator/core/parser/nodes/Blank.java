package cn.pheker.regex.generator.core.parser.nodes;

import cn.pheker.regex.generator.core.lexer.Lexer;
import cn.pheker.regex.generator.core.lexer.Token;
import cn.pheker.regex.generator.core.lexer.TokenType;
import cn.pheker.regex.generator.core.parser.abstracts.NonLeaf;

import static cn.pheker.regex.generator.core.lexer.Token.EOF;
import static cn.pheker.regex.generator.core.lexer.TokenType.*;

/**
 * @author cn.pheker
 * @version 1.0
 * @date 2022/8/11 16:50
 * 空白字符
 */
public class Blank extends Sequence {
    /**
     * 是否存在回车\r
     **/
    private boolean carryReturn;

    /**
     * 是否存在换行\n
     **/
    private boolean lineFeed;

    /**
     * 是否存在水平制表符HT
     **/
    private boolean horizontalTab;

    public Blank(NonLeaf parent) {
        super(parent);
    }

    public boolean containsCarryReturn() {
        return carryReturn;
    }

    public boolean containsLineFeed() {
        return lineFeed;
    }

    public boolean containsHorizontalTab() {
        return horizontalTab;
    }

    /**
     * 是否存在回车或换行
     *
     * @return true存在回车或换行或两者皆在, false两者皆不存在
     **/
    public boolean containsCrLf() {
        return carryReturn || lineFeed;
    }

    @Override
    public boolean parse() {
        final Lexer lexer = context.getLexer();
        Token token;
        OUTER:
        while ((token = lexer.read()) != EOF) {
            final TokenType type = token.getType();
            switch (type) {
                case CR:
                case LF:
                case SPACE:
                case HT:
                    if (token.isTokenType(CR)) {
                        this.carryReturn = true;
                    } else if (token.isTokenType(LF)) {
                        this.lineFeed = true;
                    } else if (token.isTokenType(HT)) {
                        this.horizontalTab = true;
                    }
                    this.add(Single.of(this));
                    break;
                default:
                    break OUTER;
            }
        }
        return this.parseSuccess();
    }

}
