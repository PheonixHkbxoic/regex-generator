package cn.pheker.regex.generator.core.parser.model;

import cn.pheker.regex.generator.core.parser.model.interfaces.Node;

import java.util.Iterator;
import java.util.List;

/**
 * @author wanghj
 * @version 1.0
 * @date 2022/8/2 10:19
 * 模型迭代器
 */
public class ModelIterator implements Iterator<String>{
    private Node node;
    private List<String> regex;
    private String errorMsg;
    private int cursor = 0;

    public ModelIterator(Node node) {
        this.node = node;
        try {
            this.regex = this.node.generateRegex();
        } catch (Exception e) {
            this.errorMsg = e.getMessage();
        }
    }

    @Override
    public boolean hasNext() {
        return regex != null && cursor < regex.size();
    }

    @Override
    public String next() {
        return regex.get(cursor++);
    }

    public List<String> getRegex() {
        return regex;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
