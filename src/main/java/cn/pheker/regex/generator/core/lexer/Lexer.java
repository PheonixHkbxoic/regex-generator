package cn.pheker.regex.generator.core.lexer;

import cn.pheker.regex.generator.core.scanner.abstracts.Scanner;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/7/25 21:21
 * @desc
 */
@Slf4j
public class Lexer {

    Scanner scanner;
    // 便于合并同类型token使用
    Token curr, next;

    // token缓存, 便于向前/后读取
    List<Token> tokens = new ArrayList<>();
    int cursor = 0;

    // 记录坐标相关信息
    int offset;
    int offsetRow;
    int row;
    int col;

    public Lexer(Scanner scanner) {
        this.scanner = scanner;
        this.offset = scanner.offset();
        this.offsetRow = scanner.offsetRow();
    }

    public boolean hasNext() {
        return read() != Token.EOF;
    }

    public Token next() {
        Token token = read();
        if (token.getTok().length() == 1) {
            token.pos = Pos.of(offset++, offsetRow + row, col++);
            if (token.isTokenType(TokenType.LF)) {
                this.row++;
                this.col = 0;
            }
        } else {
            // 合并的token
            // 暂不支持
        }
        advance();
        return token;
    }

    /**
     * 从缓存中读取,不前进
     * 缓存中没有时才去真正读取并放到缓存中
     *
     * @return token
     */
    public Token read() {
        if (cursor < tokens.size()) {
            return this.tokens.get(cursor);
        }
        return this.readMergeCache();
    }

    /**
     * 往前至少多读一个字符, 直到遇到不同类型的字符
     * 便于合并相同类型的字符 成 一个token
     *
     * @return token
     */
    private Token readMergeCache() {
        // 同类型token合并
        // pre-read next token, must return curr
        while ((next = doRead()) != Token.EOF) {
            if (curr == null) {
                curr = next;
                continue;
            }

            // 相关类型token合并, 目前暂不支持合并
            if (next.type == curr.type && needMerge(curr)) {
                curr.tok += next.tok;
                continue;
            }
            this.tokens.add(curr);
            curr = next;
            return this.tokens.get(cursor);
        }

        // 读完后, 再读就只能返回结束了
        if (curr == null) {
            return Token.EOF;
        }

        // the last token
        this.tokens.add(curr);
        curr = next;
        // EOF
        this.tokens.add(curr);

        // empty
        curr = null;
        next = null;
        return this.tokens.get(cursor);
    }

    private boolean needMerge(Token curr) {
        return false;
//        return curr.type == TokenType.DIGIT
//                || curr.type == TokenType.Lower
//                || curr.type == TokenType.Upper;
    }

    /**
     * 读取当前字符并转换成token
     *
     * @return token
     */
    private Token doRead() {
        int ch;
        if ((ch = scanner.read()) != -1) {
            char c = (char) ch;
            if (c < 256) {
                if (c > 127) {
                    return Token.of(TokenType.Extended, c);
                } else if (c == '\t') {
                    return Token.of(TokenType.HT, c);
                } else if (c == '\r') {
                    return Token.of(TokenType.CR, c);
                } else if (c == '\n') {
                    return Token.of(TokenType.LF, c);
                } else if (c == 127 || c < 9 || 10 < c && c < 13 || 13 < c && c < 32) {
                    return Token.of(TokenType.CONTROL, c);
                } else if (TokenType.DIGIT.chs.contains(c + "")) {
                    return Token.of(TokenType.DIGIT, c);
                } else if (TokenType.Upper.chs.contains(c + "")) {
                    return Token.of(TokenType.Upper, c);
                } else if (TokenType.Lower.chs.contains(c + "")) {
                    return Token.of(TokenType.Lower, c);
                } else {
                    return Token.of(TokenType.of(c), c);
                }
            }

            // non-ascii
            return Token.of(TokenType.Other, c);
        }
        return Token.EOF;
    }

    public void advance() {
        if (cursor >= tokens.size()) {
            return;
        }
        cursor++;
    }

    public void back() {
        cursor--;
    }

    @Override
    public String toString() {
        return tokens.toString();
    }
}
