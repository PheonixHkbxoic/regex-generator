package cn.pheker.regex.generator.core.lexer;

import cn.pheker.regex.generator.core.annotation.NotNull;
import lombok.Getter;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/7/25 21:21
 * @desc
 */
@Getter
public class Token {
    public static Token EOF = of(TokenType.EOF, "");
    
    TokenType type;
    String tok;
    
    public static Token of(TokenType type, char ch) {
        return of(type, String.valueOf(ch));
    }
    
    public static Token of(TokenType type, String tok) {
        Token token = new Token();
        token.type = type;
        token.tok = tok;
        return token;
    }
    
    /**
     * 判断token类型是否是预期的类型
     *
     * @param types 预期类型列表
     * @return true是预期类型, false不是预期类型
     */
    public boolean isTokenType(@NotNull TokenType... types) {
        for (TokenType type : types) {
            if (this.type == type) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String toString() {
        return String.format("(%s, \"%s\")",
                type, escapeIfNeed(this));
    }
    
    /**
     * 对特殊token进行转义处理
     *
     * @param token token
     * @return 转义后的tok
     */
    public static String escapeIfNeed(Token token) {
        switch (token.type) {
            case CR:
                return "\\n";
            case LF:
                return "\\r";
            case Backslash:
                return "\\\\";
            case DoubleQuote:
                return token.tok.replaceAll("\"", "\\\\\"");
        }
        
        return token.tok;
    }
    
}
