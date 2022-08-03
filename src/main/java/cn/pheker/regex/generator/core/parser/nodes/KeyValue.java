package cn.pheker.regex.generator.core.parser.nodes;

import cn.pheker.regex.generator.core.lexer.Lexer;
import cn.pheker.regex.generator.core.lexer.Token;
import cn.pheker.regex.generator.core.lexer.TokenType;
import cn.pheker.regex.generator.core.parser.abstracts.AbstractComposite;
import cn.pheker.regex.generator.core.parser.abstracts.NonLeaf;
import cn.pheker.regex.generator.core.parser.interfaces.Node;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static cn.pheker.regex.generator.core.lexer.TokenType.*;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/7/30 18:29
 * @desc
 */
@Slf4j
public class KeyValue extends Sequence {
    protected Node key;
    protected Node value;
    protected Token separator;
    
    public KeyValue(NonLeaf parent, Token separator) {
        super(parent);
        this.separator = separator;
    }
    
    public Token getSeparator() {
        return separator;
    }
    
    @Override
    public boolean parse() {
        Lexer lexer = context.getLexer();
        Token token;
        OUTER:
        while ((token = lexer.read()) != Token.EOF) {
            //log.debug("token: {}", token);
            TokenType type = token.getType();
            switch (type) {
                case LessThan:
                case OpenParenthesis:
                case OpenBrace:
                case OpenBracket:
                case DoubleQuote:
                case Apostrophe:
                    // 入口交给父类处理
                    if (super.parse()) {
                        value = this.getLast();
                    }
                    // 无论成功或失败 都是出口
                    break OUTER;
                
                case Upper:
                case Lower:
                case DIGIT:
                    // 出口
                    value = Single.of(this);
                    this.add(value);
                    break OUTER;
                case CR:
                case LF:
                case SPACE:
                    // 空白节点
                    this.add(Single.of(this));
                    break;
                default:
                    // 其它无效节点
                    // 出口
                    break OUTER;
            }
        }
        
        // 未发现Value, 解析失败
        if (value == null) {
            return false;
        }
        
        // 在父节点中查找Key节点,
        // 没找到 则失败
        // 找到并转移到本节点下 则成功
        return takeKeyFromParent();
    }
    
    @Override
    public boolean parseSuccess() {
        return key != null && value != null;
    }
    
    /**
     * 将父节点中最后一个非叶子节点(Key节点)及后面的节点拿到本节点下
     *
     * @return false父节点中不存在Key节点, true父节点中存在Key节点且拿到本节点下
     */
    private boolean takeKeyFromParent() {
        List<Node> pChildren = parent.children();
        int keyNodeIndex = -1;
        // 查找Key节点位置
        for (int i = pChildren.size() - 1; i >= 0; i--) {
            Node node = pChildren.get(i);
            if (isNotBlankNode(node)) {
                keyNodeIndex = i;
                break;
            }
        }
        
        if (keyNodeIndex == -1) {
            return false;
        }
        
        // 前提是已查找到Key节点,不然过早的拿取会有问题
        // 倒序拿取
        for (int i = pChildren.size() - 1; i >= keyNodeIndex; i--) {
            Node lastNode = pChildren.remove(i);
            // 变更节点的父节点为自己
            lastNode.setParent(this);
            // 前插法
            children.add(0, lastNode);
        }
        
        key = children.get(0);
        return true;
    }
    
    /**
     * 判断 不是空白节点
     *
     * @return true不是空白节点, false是空白节点
     */
    protected boolean isNotBlankNode(Node node) {
        return node instanceof NonLeaf
                || node.isLeafTokenType(Lower, Upper, DIGIT);
    }
}
