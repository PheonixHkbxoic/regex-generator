package cn.pheker.regex.generator.core.parser.abstracts;

import cn.pheker.regex.generator.core.parser.interfaces.Node;
import cn.pheker.regex.generator.core.parser.model.ThreadLocalModelContext;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/7/28 21:01
 * @desc
 */
public abstract class AbstractNode implements Node {
    protected NonLeaf parent;
    
    public AbstractNode(NonLeaf parent) {
        this.setParent(parent);
    }
    
    @Override
    public void setParent(NonLeaf parent) {
        this.parent = parent;
        if (isRoot()) {
            this.deep = 0;
        } else {
            this.deep = parent.getDeep() + 1;
            this.context = parent.getContext();
        }
    }
    
    /**
     * 上下文
     */
    protected ThreadLocalModelContext context;
    
    /**
     * 系数
     */
    protected float coefficient = 0.5f;
    
    /**
     * 节点深度
     */
    protected int deep = 0;
    
    public float getCoefficient() {
        return coefficient;
    }
    
    public void setCoefficient(float coefficient) {
        this.coefficient = coefficient;
    }
    
    @Override
    public NonLeaf getParent() {
        return parent;
    }
    
    @Override
    public boolean isRoot() {
        return parent == null;
    }
    
    @Override
    public ThreadLocalModelContext getContext() {
        return context;
    }
    
    @Override
    public void setContext(ThreadLocalModelContext tlmc) {
        this.context = tlmc;
    }
    
    @Override
    public int getDeep() {
        return deep;
    }
    
    /**
     * 默认解析true
     * 对于有状态的节点, 需要重写,自行判断
     *
     * @return true
     */
    @Override
    public boolean parseSuccess() {
        return false;
    }
}
