package cn.pheker.regex.generator.core.parser.model;

import cn.pheker.regex.generator.core.lexer.Lexer;
import cn.pheker.regex.generator.core.lexer.TokenType;
import cn.pheker.regex.generator.core.parser.interfaces.Node;

import java.util.HashSet;
import java.util.Set;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/7/29 22:35
 * @desc 线程内模型上下文, 线程内代理上下文
 */
public class ThreadLocalModelContext {
    
    ThreadLocal<ModelContext> threadCurrModelContext;
    
    public ThreadLocalModelContext(ThreadLocal<ModelContext> threadCurrModelContext) {
        this.threadCurrModelContext = threadCurrModelContext;
    }
    
    public static ThreadLocalModelContext of(ModelContext mc) {
        ThreadLocal<ModelContext> tl = new ThreadLocal<>();
        tl.set(mc);
        return new ThreadLocalModelContext(tl);
    }
    
    public Lexer getLexer() {
        ModelContext mc = threadCurrModelContext.get();
        return mc.lexer;
    }
    
    public boolean contains(Node pair, TokenType type) {
        Set<TokenType> types = threadCurrModelContext.get().starts.get(pair);
        return types != null && types.contains(type);
    }
    
    public void add(Node pair, TokenType type) {
        if (!threadCurrModelContext.get().starts.containsKey(pair)) {
            threadCurrModelContext.get().starts.put(pair, new HashSet<>());
        }
        Set<TokenType> types = threadCurrModelContext.get().starts.get(pair);
        types.add(type);
    }
    
    public void remove(Node pair, TokenType type) {
        if (!threadCurrModelContext.get().starts.containsKey(pair)) {
            return;
        }
        Set<TokenType> types = threadCurrModelContext.get().starts.get(pair);
        if (types != null) {
            types.remove(type);
        }
    }
    
}
