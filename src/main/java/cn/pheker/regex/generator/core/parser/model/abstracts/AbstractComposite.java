package cn.pheker.regex.generator.core.parser.model.abstracts;

import cn.pheker.regex.generator.core.lexer.Lexer;
import cn.pheker.regex.generator.core.lexer.Token;
import cn.pheker.regex.generator.core.lexer.TokenType;
import cn.pheker.regex.generator.core.parser.model.interfaces.Alternative;
import cn.pheker.regex.generator.core.parser.model.interfaces.Node;
import cn.pheker.regex.generator.core.parser.model.nodes.*;
import cn.pheker.regex.generator.misc.ReturnBreak;
import lombok.extern.slf4j.Slf4j;

import static cn.pheker.regex.generator.core.lexer.TokenType.*;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/7/27 20:51
 * @desc 控制权在我这, 负责下钻
 * 开始 生成子类 由子类去处理继续处理, 无论成功与否 break
 * 子类接管处理
 * 1.如果结束 直接返回
 * 2.如果没结束 因为要处理其它token, 所以控制权再次交给父类, 自己无论成功与否都要return
 * 2.1父类处理其它token
 * 2.2.父类遇到结束token, 再次把控制权交给子类来处理 自己无论成功与否return
 * 2.2.1 子类来处理 结束  自己无论成功与否return
 */
@Slf4j
public abstract class AbstractComposite extends NonLeaf implements Alternative {
    /**
     * 多选一
     */
    protected boolean alternative;

    public AbstractComposite(NonLeaf parent) {
        super(parent);
    }

    @Override
    public void setAlternative() {
        this.alternative = true;
    }

    @Override
    public boolean isAlternative() {
        return alternative;
    }

    @Override
    public void setParent(NonLeaf parent) {
        super.setParent(parent);
        // 递归修正子节点的父类, 不修正的话 deep节点可能会不对
        if (children != null && !children.isEmpty()) {
            for (Node child : children) {
                child.setParent(this);
            }
        }
    }

    @Override
    public boolean parseSuccess() {
        return !children().isEmpty();
    }

    @Override
    public void recall() {
        for (int lastIndex = children.size() - 1; lastIndex >= 0; lastIndex--) {
            children.get(lastIndex).recall();
        }
    }

    @Override
    public boolean parse() {
        Lexer lexer = context.getLexer();

        Token token;
        while ((token = lexer.read()) != Token.EOF) {
            log.debug("token: {}", token);
            TokenType type = token.getType();
            switch (type) {
                case LessThan:
                    parsePair(new Tag(this));
                    break;
                case OpenParenthesis:
                    parsePair(new Group(this));
                    break;
                case OpenBrace:
                    parsePair(new Objects(this));
                    break;
                case OpenBracket:
                    parsePair(new Array(this));
                    break;

                case GreaterThan:
                case CloseParenthesis:
                case CloseBrace:
                case CloseBracket:
                    if (!this.parse()) {
                        this.add(Single.of(this));
                    }
                    return true;

                case DoubleQuote:
                case Apostrophe:
                    ReturnBreak rb = parseStrings();
                    if (rb.isBreak()) {
                        break;
                    }
                    if (rb.isFalse()) {
                        this.add(Single.of(this));
                        break;
                    }
                    return true;

                case Dollar:
                case Underscore:
                case Upper:
                case Lower:
                case DIGIT:
                    ReturnBreak id = parseId();
                    if (id.isBreak()) {
                        break;
                    }

                    // 数字
                    if (id.isFalse()) {
                        parseNumbers();
                        break;
                    }
                    return true;
                case Dot:
                    final DotX dotX = new DotX(this);
                    if (!dotX.parse()) {
                        this.add(Single.of(this));
                        break;
                    }
                    this.add(dotX);
                    break;

                case Plus:
                case Minus:
                    final ReturnBreak rbn = parseNumbers();
                    if (rbn.isReturn()) {
                        return rbn.isTrue();
                    }
                    break;
                default:
                    this.add(Single.of(this));
                    // K ::= ":"[Blank]V | "="[Blank]V
                    if (token.isTokenType(Equals, Colon)) {
                        KeyValue kv = new KeyValue(this, token);
                        if (kv.parse()) {
                            this.add(kv);
                            break;
                        }
                        kv.recall();
                    }
                    break;
            }
        }

        return this.parseSuccess();
    }

    private ReturnBreak parseNumbers() {
        final Numbers numbers = new Numbers(this);
        if (!numbers.parse()) {
            numbers.recall();
            this.add(Single.of(this));
        }else{
            this.add(numbers);
        }

        return ReturnBreak.ofBreak();
    }

    protected ReturnBreak parseId() {
        Lexer lexer = context.getLexer();
        Token token = lexer.read();
        TokenType type = token.getType();

        // 开头是数字, 则不能作为标识符
        if (!isExtendsOf(Id.class) && token.isTokenType(DIGIT)) {
            return ReturnBreak.ofFalse();
        }

        Id id = new Id(this);
        id.parse();
        this.add(id);

        return ReturnBreak.ofBreak();
    }

    private void parsePair(Node pair) {
        if (pair.parse()) {
            this.add(pair);
            return;
        }
        pair.recall();
        this.add(Single.of(this));
    }


    private ReturnBreak parseStrings() {
        Lexer lexer = context.getLexer();
        Token token = lexer.read();
        TokenType type = token.getType();
        // 提早检测  "'嵌套的情况 内层失败
        if (!this.isRoot()) {
            if (type == DoubleQuote
                    && context.contains(this.parent, Apostrophe)) {
                return ReturnBreak.ofFalse();
            }
            if (type == Apostrophe
                    && context.contains(this.parent, DoubleQuote)) {
                return ReturnBreak.ofFalse();
            }

            // 开始标志在父类
            if (context.contains(this.parent, type)) {
                this.add(Single.of(this));
                return ReturnBreak.ofTrue();
            }
        }

        if (!context.contains(this, type)) {
            context.add(this, type);
            Strings strings = new Strings(this, token.isTokenType(TokenType.DoubleQuote));
            if (strings.parse()) {
                this.add(strings);
                context.remove(this, type);
                // Strings 作为 KeyValue的结束标志
                if (strings.parent instanceof KeyValue) {
                    return ReturnBreak.ofTrue();
                }
                return ReturnBreak.ofBreak();
            }
            context.remove(this, type);
            strings.recall();
            this.add(Single.of(this));
            return ReturnBreak.ofTrue();
        }
        if (!this.parse()) {
            this.add(Single.of(this));
        }
        return ReturnBreak.ofTrue();
    }

}
