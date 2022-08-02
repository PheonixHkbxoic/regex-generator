package cn.pheker.regex.generator.core.parser.model;

import cn.pheker.regex.generator.core.lexer.Lexer;
import cn.pheker.regex.generator.core.lexer.TokenType;
import cn.pheker.regex.generator.core.parser.interfaces.Node;
import cn.pheker.regex.generator.core.scanner.Scanner;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/7/29 22:18
 * @desc 模型上下文, 用于模型构建
 */
public class ModelContext {
    
    /**
     * 记法解析器
     */
    Lexer lexer;
    
    /**
     * 记录 解析某个Pair时是否已经遇到了某种开始标识
     * 如: "或'
     */
    protected Map<Node, Set<TokenType>> starts = new HashMap<>();
    
    public static ModelContext of(Scanner scanner) {
        return of(new Lexer(scanner));
    }
    
    public static ModelContext of(Lexer lexer) {
        ModelContext mc = new ModelContext();
        mc.lexer = lexer;
        return mc;
    }
    
}
