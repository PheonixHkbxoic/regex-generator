package cn.pheker.regex.generator.core.parser;

import cn.pheker.regex.generator.core.parser.abstracts.Leaf;
import cn.pheker.regex.generator.core.parser.abstracts.NonLeaf;
import cn.pheker.regex.generator.core.parser.interfaces.Node;
import cn.pheker.regex.generator.core.parser.nodes.Branches;
import cn.pheker.regex.generator.core.parser.nodes.Empty;
import cn.pheker.regex.generator.misc.IntTuple;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author cn.pheker
 * @version 1.0
 * @date 2022/8/3 14:12
 */
@Data
public class MetaInfo {
    private Node node;
    
    /**
     * 长度:出现次数 map
     * 叶子节点: 长度1, 出现次数默认1, 累加
     * NonLeaf:
     * - Branches:
     */
    protected Map<Integer, Integer> lenTimes;
    
    
    public MetaInfo(Node node) {
        this.node = node;
        incrLeaf();
    }
    
    public void statistics() {
        if (node.isExtendsOf(Leaf.class)) {
            return;
        }
        
        List<Node> children = ((NonLeaf) node).children();
        // Branches
        if (node.isExtendsOf(Branches.class)) {
            lenTimes = mergeMetaInfoByChildLen(children);
            return;
        }
        
        // Sequence
        this.lenTimes = new HashMap<>(children.size());
        for (Node child : children) {
            Map<Integer, Integer> childLenTimes = child.getMetaInfo().getLenTimes();
            this.lenTimes = cartesian(this.lenTimes, childLenTimes);
        }
    }
    
    /**
     * 笛卡尔积
     *
     * @return 笛卡尔积
     **/
    protected Map<Integer, Integer> cartesian(final Map<Integer, Integer> first,
                                              final Map<Integer, Integer> second) {
        if (first == null || first.isEmpty()) {
            return second.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }
        Map<Integer, Integer> tmp = new HashMap<>(first.size() * second.size());
        first.forEach((key, value) -> {
            int len = key, times = value;
            second.forEach((key1, value1) -> {
                int tmpLen = key1, tmpTimes = value1;
                tmp.put(len + tmpLen, Math.max(times, tmpTimes));
            });
        });
        return tmp;
    }
    
    /**
     * 按len合并子节点MetaInfo信息
     *
     * @param children 子节点
     * @return lenTimes
     */
    private Map<Integer, Integer> mergeMetaInfoByChildLen(List<Node> children) {
        int lenTypes = (int) (children.stream()
                .filter(n -> n.isExtendsOf(NonLeaf.class))
                .count()
                + 1);
        Map<Integer, Integer> union =
                new HashMap<>(lenTypes);
        for (Node child : children) {
            Map<Integer, Integer> lenTimes = child.getMetaInfo().lenTimes;
            lenTimes.forEach((len, times) ->
                    union.put(len, union.getOrDefault(len, 0) + times)
            );
        }
        return union;
    }
    
    public void incrLeaf() {
        int len = 1;
        if (node.isExtendsOf(Empty.class)) {
            len = 0;
        }
        if (lenTimes == null) {
            lenTimes = new HashMap<>(1);
        }
        Integer oldTimes = this.lenTimes.getOrDefault(len, 0);
        this.lenTimes.put(len, ++oldTimes);
    }
    
    @Override
    public String toString() {
        return "MetaInfo{" +
                lenTimes.entrySet().stream()
                        .map(e -> String.format("(%d,%d)", e.getKey(), e.getValue()))
                        .collect(Collectors.joining(",", "[", "]")) +
                '}';
    }
    
    
    public boolean isMultiLen() {
        return lenTimes.size() > 1;
    }
    
    public IntTuple getMinMaxLen() {
        Integer min = null, max = null;
        for (Integer len : lenTimes.keySet()) {
            if (min == null) {
                min = len;
            }
            if (max == null) {
                max = len;
            }
    
            if (min > len) {
                min = len;
            }
            if (max < len) {
                max = len;
            }
        }
        return new IntTuple(min, max);
    }
    
    public IntTuple getMinMaxTimes() {
        Integer min = null, max = null;
        for (Integer len : lenTimes.values()) {
            if (min == null) {
                min = len;
            }
            if (max == null) {
                max = len;
            }
            
            if (min > len) {
                min = len;
            }
            if (max < len) {
                max = len;
            }
        }
        return new IntTuple(min, max);
    }
}
