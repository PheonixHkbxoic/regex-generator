package cn.pheker.regex.generator.core.parser.model.nodes;

import cn.pheker.regex.generator.core.lexer.Lexer;
import cn.pheker.regex.generator.core.lexer.Token;
import cn.pheker.regex.generator.core.parser.model.abstracts.NonLeaf;
import cn.pheker.regex.generator.misc.TrueFalseElse;

import static cn.pheker.regex.generator.core.lexer.TokenType.*;

/**
 * @author cn.pheker
 * @version 1.0
 * @date 2022/8/1 16:29
 */
public class Numbers extends Sequence {
    protected NumberType numberType = NumberType.Decimal;

    /**
     * 是否有符号位
     * true: 有符号位+, false: 有符号位-, else: 无符号位
     * 如: -1,2
     **/
    protected TrueFalseElse sign;

    /**
     * 是否有小数
     * 1.23
     **/
    protected boolean hasDecimal;

    /**
     * 是否有指数
     * 如: 1.23e-5
     **/
    protected boolean hasExponential;

    public Numbers(NonLeaf parent) {
        super(parent);
    }


    @Override
    public boolean parse() {
        final Lexer lexer = context.getLexer();
        Token first = lexer.read();
        // +-0,其它数字
        switch (first.getType()) {
            case Plus:
                sign = TrueFalseElse.ofTrue();
                this.add(Single.of(this));
                break;
            case Minus:
                sign = TrueFalseElse.ofFalse();
                this.add(Single.of(this));
                break;
            default:
                sign = TrueFalseElse.ofElse();
                break;
        }

        if (first.isTokenType(EOF)) {
            return false;
        }

        // 十进制      0
        // 八进制      0123
        // 二进制      0b
        // 十六进制    0x
        if (first.getTok().equals("0")) {
            // 0
            children.add(Single.of(this));
            Token next = lexer.read();
            if (next.isTokenType(EOF)) {
                return true;
            }
            if (!next.isTokenType(Lower, Upper, DIGIT)) {
                return true;
            }

            final String tok = next.getTok();
            // 0123, 068
            if (next.isTokenType(DIGIT)) {
                numberType = NumberType.Octal;
                parseOctal();
            } else {
                if (tok.equalsIgnoreCase("b")) {
                    numberType = NumberType.Binary;
                    final Single b = Single.of(this);
                    this.add(b);
                    if (!parseBinary()) {
                        b.recall();
                        this.removeLast();
                    }
                } else if (tok.equalsIgnoreCase("x")) {
                    numberType = NumberType.Hexadecimal;
                    final Single x = Single.of(this);
                    this.add(x);
                    if (!parseHex()) {
                        x.recall();
                        this.removeLast();
                    }
                } else {
                    return true;
                }
            }
        } else {
            // 十进制
            parseDecimal();
        }
        return this.parseSuccess();
    }

    private void parseOctal() {
        final Lexer lexer = context.getLexer();
        Token curr;
        while ((curr = lexer.read()) != Token.EOF) {
            if (!checkValid(curr)) {
                return;
            }
            this.add(Single.of(this));
        }

    }

    private boolean parseHex() {
        final Lexer lexer = context.getLexer();
        final Token first = lexer.read();
        if (first.isTokenType(EOF)) {
            return false;
        }

        // 0-f
        if (!checkValid(first)) {
            return false;
        }
        final Single firstNode = Single.of(this);
        this.add(firstNode);

        Token second = lexer.read();
        if (!checkValid(second)) {
            firstNode.recall();
            this.removeLast();
            return false;
        }
        this.add(Single.of(this));

        Token third = lexer.read();
        if (!checkValid(third)) {
            return true;
        }
        final Single thirdNode = Single.of(this);
        this.add(thirdNode);

        Token forth = lexer.read();
        if (!checkValid(forth)) {
            thirdNode.recall();
            this.removeLast();
            return true;
        }
        this.add(Single.of(this));
        return true;
    }

    private boolean parseBinary() {
        final Lexer lexer = context.getLexer();
        final Token first = lexer.read();
        if (first.isTokenType(EOF)) {
            return false;
        }

        Token curr;
        while ((curr = lexer.read()) != Token.EOF) {
            if (!checkValid(curr)) {
                return true;
            }
            lexer.next();
        }


        return true;
    }

    private boolean parseDecimal() {
        final Lexer lexer = context.getLexer();
        Token curr;
        while ((curr = lexer.read()) != Token.EOF) {
            if (!checkValid(curr)) {
                return true;
            }
            this.add(Single.of(this));
        }
        return true;
    }


    private boolean checkValid(Token token) {
        if (token.isTokenType(EOF)) {
            return false;
        }
        final String tok = token.getTok();
        switch (numberType) {
            case Binary:
                return tok.matches("[01]");
            case Octal:
                return tok.matches("[0-7]");
            case Hexadecimal:
                return tok.matches("[0-9a-fA-F]");
            default:
                return tok.matches("[0-9]");
        }
    }
}
