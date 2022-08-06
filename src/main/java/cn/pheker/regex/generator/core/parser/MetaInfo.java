package cn.pheker.regex.generator.core.parser;

import cn.pheker.regex.generator.core.parser.abstracts.Leaf;
import cn.pheker.regex.generator.core.parser.abstracts.NonLeaf;
import cn.pheker.regex.generator.core.parser.interfaces.Node;
import cn.pheker.regex.generator.core.parser.nodes.Branches;
import cn.pheker.regex.generator.core.parser.nodes.Empty;
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
    }
    
    public void statistics() {
        if (this.lenTimes != null) {
            return;
        }
        if (node.isExtendsOf(Leaf.class)) {
            lenTimes = new HashMap<>(1);
            lenTimes.put(node.isExtendsOf(Empty.class) ? 0 : 1, 1);
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
        Integer oldTimes = this.lenTimes.getOrDefault(1, 1);
        this.lenTimes.put(1, ++oldTimes);
    }
    
    @Override
    public String toString() {
        return "MetaInfo{" +
                lenTimes.entrySet().stream()
                        .map(e -> String.format("(%d,%d)", e.getKey(), e.getValue()))
                        .collect(Collectors.joining(",", "[", "]")) +
                '}';
    }
}
