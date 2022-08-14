package cn.pheker.regex.generator.core.lexer;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/7/25 22:32
 * @desc
 */
public enum TokenType {
    EOF(""),

    // 控制字符 不可见
    // 0-8 11-12 13-31 127
    CONTROL(""),
    // 9
    HT("\t"),
    // 10
    LF("\n"),
    // 13
    CR("\r"),


    // 可打印字符 可见
    // 32
    SPACE(" "),
    ExclamationMark("!"),
    DoubleQuote("\""),
    Hash("#"),
    Dollar("$"),
    Percent("%"),
    Ampersand("&"),
    Apostrophe("'"),
    OpenParenthesis("("),
    CloseParenthesis(")"),
    Asterisk("*"),
    Plus("+"),
    Comma(","),
    Minus("-"),
    Dot("."),
    Slash("/"),
    // 48-57
    DIGIT("0123456789"),
    Colon(":"),
    Semicolon(";"),
    LessThan("<"),
    Equals("="),
    GreaterThan(">"),
    QuestionMark("?"),
    At("@"),

    // 65-90
    Upper("ABCDEFGHIJKLMNOPQRSTUVWXYZ"),
    OpenBracket("["),
    Backslash("\\"),
    CloseBracket("]"),
    Caret("^"),
    Underscore("_"),
    BackQuote("`"),

    // 97-122
    Lower("abcdefghijklmnopqrstuvwxyz"),
    OpenBrace("{"),
    VerticalBar("|"),
    CloseBrace("}"),
    Tilde("~"),

    // 扩展字符
    // 128-255
    Extended(""),

    Other("");

    String chs;

    TokenType(String chs) {
        this.chs = chs;
    }

    public static TokenType of(char c) {
        for (TokenType type : values()) {
            if (type.chs.length() > 0 && type.chs.charAt(0) == c) {
                return type;
            }
        }
        return null;
    }

    public String getChs() {
        return chs;
    }
}
