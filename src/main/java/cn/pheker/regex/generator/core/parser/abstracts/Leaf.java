package cn.pheker.regex.generator.core.parser.abstracts;

import cn.pheker.regex.generator.core.lexer.Lexer;
import cn.pheker.regex.generator.core.lexer.Token;
import cn.pheker.regex.generator.core.lexer.TokenType;
import cn.pheker.regex.generator.util.StrUtil;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/7/27 22:27
 * @desc 叶子节点, 解析不会失败
 */
public abstract class Leaf extends AbstractNode {

    protected Token token;

    public Leaf(NonLeaf parent) {
        super(parent);
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    public boolean isTokenType(TokenType type) {
        return token.getType().equals(type);
    }

    @Override
    public boolean parse() {
        Lexer lexer = getContext().getLexer();
        token = lexer.next();
        return true;
    }

    @Override
    public void recall() {
        Lexer lexer = getContext().getLexer();
        lexer.back();
    }

    @Override
    public String toString() {
        return token.toString();
    }

    /**
     * Single节点没有状态,默认解析成功
     *
     * @return true
     */
    @Override
    public boolean parseSuccess() {
        return true;
    }

    @Override
    public String format() {
        return StrUtil.times(getDeep()) +
            this.getClass().getSimpleName() +
            "  " +
            token +
            "  " + this.getMetaInfo() +
            '\n';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Leaf leaf = (Leaf) o;

        return token.equals(leaf.token);
    }

    @Override
    public int hashCode() {
        return token.hashCode();
    }

}
