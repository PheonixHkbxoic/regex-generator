package cn.pheker.regex.generator.core.parser.abstracts;

import cn.pheker.regex.generator.core.parser.interfaces.Last;
import cn.pheker.regex.generator.core.parser.interfaces.Node;
import cn.pheker.regex.generator.exception.TooManyRegex;
import cn.pheker.regex.generator.util.StrUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

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
        return children == null ? 0 : children.size();
    }
    
    @Override
    public Node getLast() {
        return children == null || children.isEmpty() ? null
                : children.get(size() - 1);
    }

    @Override
    public boolean removeLast(){
        if (children == null || children.isEmpty()) {
            return false;
        }
        children.remove(children.size() - 1);
        return true;
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

    @Override
    public List<String> generateRegex() {
        List<String> regex = null;
        for (Node child : children()) {
            regex = cartesian(regex, child.generateRegex());
            if (regex.size() > 10000) {
                throw new TooManyRegex();
            }
        }
        return regex;
    }

    /**
     * 迪卡尔积
     * @return 迪卡尔积
     **/
    protected List<String> cartesian(List<String> first, List<String> second) {
        if (first == null || first.isEmpty()) {
            return second;
        }
        return first.stream()
                .flatMap(prefix -> second
                        .stream().map(prefix::concat))
                .collect(Collectors.toList());
    }
}
