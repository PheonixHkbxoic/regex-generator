package cn.pheker.regex.generator.core.parser.model.abstracts;

import cn.pheker.regex.generator.core.parser.model.interfaces.Last;
import cn.pheker.regex.generator.core.parser.model.interfaces.Node;
import cn.pheker.regex.generator.util.StrUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/7/27 22:30
 * @desc
 */
public abstract class NonLeaf extends AbstractNode implements Iterator<Node>, Last<Node> {
    protected List<Node> children;
    protected int cursor;
    
    public NonLeaf(NonLeaf parent) {
        super(parent);
        this.children = new ArrayList<>();
    }
    
    
    @Override
    public void recall() {
        throw new RuntimeException("sub class must rewrite this method");
    }
    
    public void add(Node child) {
        children.add(child);
    }
    
    public Node getNode(int index) {
        return children.get(index);
    }
    
    @Override
    public boolean hasNext() {
        return cursor < children.size();
    }
    
    @Override
    public Node next() {
        return children.get(cursor++);
    }
    
    @Override
    public void remove() {
        children.remove(cursor - 1);
    }
    
    public int size() {
        return children.size();
    }
    
    @Override
    public Node getLast() {
        return children == null || children.isEmpty() ? null
                : children.get(size() - 1);
    }
    
    public List<Node> children() {
        return children == null ? new ArrayList<>() : children;
    }
    
    @Override
    public String toString() {
        return "NonLeaf{" + children + '}';
    }
    
    @Override
    public String printFormatted() {
        StringBuilder sb = new StringBuilder();
        sb.append(StrUtil.times(getDeep()))
                .append(this.getClass().getSimpleName())
                .append("{")
                .append('\n');
        
        for (Node child : children) {
            sb.append(child.printFormatted());
        }
        
        sb.append(StrUtil.times(getDeep()))
                .append("}")
                .append('\n');
        return sb.toString();
    }
}
