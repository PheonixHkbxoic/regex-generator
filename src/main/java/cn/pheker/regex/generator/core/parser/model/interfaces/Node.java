package cn.pheker.regex.generator.core.parser.model.interfaces;

import cn.pheker.regex.generator.core.lexer.TokenType;
import cn.pheker.regex.generator.core.parser.model.ThreadLocalModelContext;
import cn.pheker.regex.generator.core.parser.model.abstracts.Leaf;
import cn.pheker.regex.generator.core.parser.model.abstracts.NonLeaf;
import com.sun.istack.internal.NotNull;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/7/27 0:17
 * @desc
 */
public interface Node {
    /**
     * 获取父节点
     *
     * @return 父节点, 如果是根节点返回null
     */
    NonLeaf getParent();
    
    /**
     * 设置父节点
     *
     * @param parent 父节点
     */
    void setParent(NonLeaf parent);
    
    /**
     * 是否是根节点(根节点没有父节点,即parent为null)
     *
     * @return true是根节点, false不是根节点
     */
    boolean isRoot();
    
    /**
     * 设置上下文
     *
     * @param tlmc 线程内模型上下文
     */
    void setContext(ThreadLocalModelContext tlmc);
    
    /**
     * 获取上下文
     *
     * @return 上下文
     */
    ThreadLocalModelContext getContext();
    
    /**
     * 获取节点深度
     *
     * @return 节点深度
     */
    int getDeep();
    
    /**
     * 解析
     *
     * @return 是否解析成功
     */
    boolean parse();
    
    /**
     * 当解析失败时, 要手动调用此方法进行回溯
     */
    void recall();

    /**
     * 判断当前对象是否是某一类节点
     **/
    default boolean isExtendsOf(Class<? extends Node> clzz){
        return clzz.isAssignableFrom(this.getClass());
    }
    
    /**
     * 判断是否是叶子节点且tokenType为types之一
     *
     * @param types 可能的tokenType类型
     * @return 是叶子节点且tokenType相同返回true, 否则返回false
     */
    default boolean isLeafTokenType(@NotNull TokenType... types) {
        if (!isLeaf()) {
            return false;
        }
        Leaf $ = (Leaf) this;
        for (TokenType type : types) {
            if ($.isTokenType(type)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 判断是否是叶子节点
     *
     * @return 是否是叶子节点
     */
    default boolean isLeaf() {
        return this instanceof Leaf;
    }
    
    /**
     * 判断当前节点是否解析成功
     *
     * @return 默认成功, 如,但是有状态的节点如Pair需要重写,自行判断
     */
    boolean parseSuccess();
    
    String printFormatted();
}
