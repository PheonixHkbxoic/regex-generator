package cn.pheker.regex.generator.core.parser.model.nodes;

import cn.pheker.regex.generator.core.lexer.Lexer;
import cn.pheker.regex.generator.core.lexer.Token;
import cn.pheker.regex.generator.core.lexer.TokenType;
import cn.pheker.regex.generator.core.parser.model.abstracts.NonLeaf;
import cn.pheker.regex.generator.core.parser.model.interfaces.Node;
import cn.pheker.regex.generator.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import static cn.pheker.regex.generator.core.lexer.TokenType.*;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/7/27 21:35
 * @desc 成对的结构
 */
@Slf4j
public class Pair extends Sequence {
    /**
     * 开始结点
     */
    protected Node start;
    
    /**
     * 结束结点
     */
    protected Node end;
    
    public Pair(NonLeaf parent) {
        super(parent);
    }
    
    
    public Node getStart() {
        return start;
    }
    
    public Node getEnd() {
        return end;
    }
    
    public boolean isStartEqualEnd() {
        return start == end;
    }
    
    protected void setStart() {
        start = Single.of(this);
        this.add(start);
    }
    
    protected void setEnd() {
        end = Single.of(this);
        this.add(end);
    }
    
    @Override
    public boolean parse() {
        Lexer lexer = context.getLexer();
        if (start == null && lexer.hasNext()) {
            this.setStart();
        }
        Token token;
        while ((token = lexer.read()) != Token.EOF) {
            log.debug("token: {}", token);
            TokenType type = token.getType();
            switch (type) {
                case LessThan:
                case OpenParenthesis:
                case OpenBrace:
                case OpenBracket:
                    if (!super.parse()) {
                        this.add(Single.of(this));
                    }
                    break;
                case GreaterThan:
                    // 结束标识 无论成功与否 都要return 把控制权交出去
                    if (start.isLeafTokenType(LessThan)) {
                        this.setEnd();
                        return true;
                    }
                    return false;
                case CloseParenthesis:
                    if (start.isLeafTokenType(OpenParenthesis)) {
                        this.setEnd();
                        return true;
                    }
                    return false;
                case CloseBrace:
                    if (start.isLeafTokenType(OpenBrace)) {
                        this.setEnd();
                        return true;
                    }
                    return false;
                case CloseBracket:
                    if (start.isLeafTokenType(OpenBracket)) {
                        this.setEnd();
                        return true;
                    }
                    return false;
                
                case DoubleQuote:
                case Apostrophe:
                    if (start.isLeafTokenType(type)) {
                        this.setEnd();
                        return true;
                    }
                    
                    return super.parse();
                default:
                    // pair没结束, 所以无论后面处理成功与否 都要返回
                    return super.parse();
            }
        }
        
        return this.parseSuccess();
    }
    
    
    @Override
    public String toString() {
        return StrUtil.times(this.getDeep())
                + "Pair{" +
                "start=" + start +
                ", end=" + end +
                ", children=" + children +
                '}';
    }
    
    @Override
    public boolean parseSuccess() {
        return end != null;
    }
}
