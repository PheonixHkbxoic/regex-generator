package cn.pheker.regex.generator.core.parser.model;

import cn.pheker.regex.generator.core.parser.MetaInfo;
import cn.pheker.regex.generator.core.parser.abstracts.Leaf;
import cn.pheker.regex.generator.core.parser.abstracts.NonLeaf;
import cn.pheker.regex.generator.core.parser.interfaces.Node;
import cn.pheker.regex.generator.core.parser.nodes.Branches;
import cn.pheker.regex.generator.core.parser.nodes.Empty;
import cn.pheker.regex.generator.core.parser.nodes.Pair;
import cn.pheker.regex.generator.core.parser.nodes.Sequence;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/8/2 22:04
 * @desc 模型合并器
 */
@Slf4j
public class ModelMerger {
    private Model model;
    private Branches container;
    
    public ModelMerger(Model model) {
        this.model = model;
        this.container = new Branches(null);
        // 空节点处理
        if (!model.root.children().isEmpty()) {
            this.container.add(model.root);
        }
    }
    
    /**
     * 合并其他模型节点到本模型节点
     *
     * @param other 其他模型节点
     */
    public void merge(Sequence other) {
        // 空节点处理
        if (container.children().isEmpty()) {
            container.add(other);
            model.root = container;
            return;
        }
    
        // 容器肯定不为空了, 开始合并操作
        this.doMerge(container, other);
        model.root = container;
    }
    
    /**
     * 合并其他节点树到本节点树中
     * 由于文本差异较大,节点树差异也会大,程序无法处理所有情况,以致无法决定如何合并
     * 故我们假设如下:
     * 1.绝大部分文本结构相同
     * 2.结构不同的部分很有可能是错误文本
     * 3.结构不同的部分将另起分支,即容错处理
     * 结构相同的结点树合并规则:
     * 1.
     * 2.
     * 3.
     *
     * @param first  第一个节点树, 内部可能含有分支
     * @param second 第二个节点树, 内部不可能含有分支
     */
    private boolean doMerge(Node first, Node second) {
//        if (!second.isExtendsOf(Empty.class)) {
//            first.getMetaInfo().incrLeaf();
//        }
    
        // 合并结构不同的结点
        if (!isStructSame(first, second)) {
            // 分支 + Sequence/Leaf
            if (first.isExtendsOf(Branches.class)) {
                Branches branches = (Branches) first;
                // 查找结构类似的分支
                boolean mergeSuccess = false;
                List<Node> children = branches.children();
                //  fori+size不会导致并发修改异常
                for (int i = 0; i < children.size(); i++) {
                    Node branch = children.get(i);
                    if (isStructSame(branch, second)) {
                        mergeSuccess = this.doMerge(branch, second);
                        if (mergeSuccess) {
                            break;
                        }
                    
                    }
                }
            
                // 结构都不类似,则作为新分支
                if (!mergeSuccess) {
                    branches.add(second);
                }
                return true;
            }
        
            // Sequence     + Leaf
            // Leaf         + Sequence
            mergeAsBranches(first, second);
            return true;
        }
    
        // 相同类型节点合并
        // Pair + Pair
        if (first.isExtendsOf(Pair.class)) {
            Pair firstPair = (Pair) first;
            Pair secondPair = (Pair) second;
            // TODO Strings
            // 不同类型的Pair,如:<div>与(abc)
            if (!firstPair.getStart().equals(secondPair.getStart())) {
                mergeAsBranches(first, second);
                // 相同类型的Pair
            } else {
                mergePair(firstPair, secondPair);
            }
            // Sequence + Sequence
        } else if (first.isExtendsOf(Sequence.class)) {
            return mergeSequenceExceptPair((Sequence) first, (Sequence) second);
            // Leaf + Leaf
        } else if (first.isExtendsOf(Leaf.class)) {
            mergeLeaf(((Leaf) first), ((Leaf) second));
        } else {
            // TODO 未处理的相同类型节点合并
            throw new RuntimeException("");
        }
        return true;
    }
    
    /**
     * 合并两个叶子节点
     *
     * @param first  叶子节点1
     * @param second 叶子节点2
     */
    private void mergeLeaf(Leaf first, Leaf second) {
        if (leafEquals(first, second)) {
            MetaInfo metaInfo = first.getMetaInfo();
            metaInfo.incrLeaf();
        } else if (first.getParent().isExtendsOf(Branches.class)) {
            first.getParent().add(second);
        } else {
            final Branches branches = new Branches(first.getParent());
            final List<Node> children = branches.children();
            children.add(first);
            children.add(second);
            this.replaceInParent(first, branches);
        }
    }
    
    /**
     * 合并两个非Pair类型的Sequence节点
     *
     * @param s1 Sequence节点1
     * @param s2 Sequence节点2
     */
    private boolean mergeSequenceExceptPair(Sequence s1, Sequence s2) {
        // 结构不同, s2作为新分支
        if (!isStructSame(s1.children().get(0), s2.children().get(0))
                && s1.getParent().isExtendsOf(Branches.class)) {
            return false;
        }
        
        // div + p/pi/didc/di/dip 分支如下
        // div
        // p^2i
        // d^3i^3 branches(empty, p)
        final int firstEndIndex = s1.size();
        final int secondEndIndex = s2.size();
        int minLen = Math.min(firstEndIndex, secondEndIndex);
        for (int i = 0; i < minLen; i++) {
            Node firstSub = s1.children().get(i);
            Node secondSub = s2.children().get(i);
    
            if (isStructSame(firstSub, secondSub)) {
                this.doMerge(firstSub, secondSub);
                continue;
            }
            // 发现这是个分支节点 那么这个分支节点一定是最后一个节点
            if (firstSub.isExtendsOf(Branches.class)) {
                List<Node> nodes = s2.children().subList(i, s2.size());
                this.doMerge(firstSub, newSequence(nodes));
                return true;
            }
    
            // 因为如果节点不同的话  会合并成一个Branches节点
            List<Node> nodes1 = removeFrom(s1.children(), i, firstEndIndex);
            Node firstBranch = nodes1.isEmpty() ? new Empty(null) : newSequence(nodes1);
            List<Node> nodes2 = removeFrom(s2.children(), i, secondEndIndex);
            Node secondBranch = nodes2.isEmpty() ? new Empty(null) : newSequence(nodes2);
            Branches branches = newBranches(s1, firstBranch, secondBranch);
            s1.add(branches);
            return true;
        }
        
        // second还有剩余节点
        if (firstEndIndex != secondEndIndex) {
    
            List<Node> longer;
            if (firstEndIndex > secondEndIndex) {
                longer = removeFrom(s1.children(), minLen, firstEndIndex);
            } else {
                longer = removeFrom(s2.children(), minLen, secondEndIndex);
            }
            Node empty = new Empty(null);
            Node branch = newSequence(longer);
            Branches branches = newBranches(s1, branch, empty);
            branches.setParent(s1);
            // 注意是添加到最后
            s1.add(branches);
        }
        return true;
    }
    
    /**
     * 合并两个Pair节点
     * 处理逻辑与Sequence基本相同(只是排除start,end两个节点)
     *
     * @param firstPair  Pair节点1
     * @param secondPair Pair节点2
     */
    private void mergePair(Pair firstPair, Pair secondPair) {
//        firstPair.getStart().getMetaInfo().incrLeaf();
//        firstPair.getEnd().getMetaInfo().incrLeaf();
    
        // 相同Pair
        // <div data="123">与<p id="title">
        // 1.略过起止节点
        // 2.逐个合并相同类型前缀节点
        // 3.一次性合并不同类型后缀节点
        final int firstEndIndex = firstPair.size() - 1;
        final int secondEndIndex = secondPair.size() - 1;
        int minLen = Math.min(firstEndIndex, secondEndIndex);
        for (int i = 1; i < minLen; i++) {
            Node firstSub = firstPair.children().get(i);
            Node secondSub = secondPair.children().get(i);
    
            if (isStructSame(firstSub, secondSub)) {
                this.doMerge(firstSub, secondSub);
                continue;
            }
            // 发现这是个分支节点 那么这个分支节点一定是最后一个节点
            if (firstSub.isExtendsOf(Branches.class)) {
                List<Node> nodes = secondPair.children()
                        .subList(i, secondEndIndex);
                if (!nodes.isEmpty()) {
                    this.doMerge(firstSub, newSequence(nodes));
                }
                return;
            }
    
            // 因为如果节点不同的话  会合并成一个Branches节点
            List<Node> nodes1 = removeFrom(firstPair.children(), i, firstEndIndex);
            Node firstBranch = nodes1.isEmpty() ? new Empty(null) : newSequence(nodes1);
            List<Node> nodes2 = removeFrom(secondPair.children(), i, secondEndIndex);
            Node secondBranch = nodes2.isEmpty() ? new Empty(null) : newSequence(nodes2);
            Branches branches = newBranches(firstPair, firstBranch, secondBranch);
            branches.setParent(firstPair);
            // 注意是添加到最后一个节点End前
            firstPair.children().add(i, branches);
            return;
        }
    
        // second还有剩余节点
        if (firstEndIndex != secondEndIndex) {
            List<Node> longer;
            if (firstEndIndex > secondEndIndex) {
                longer = removeFrom(firstPair.children(), minLen, firstEndIndex);
            } else {
                longer = removeFrom(secondPair.children(), minLen, secondEndIndex);
            }
            Node branch = newSequence(longer);
            Node empty = new Empty(null);
            Branches branches = newBranches(firstPair, branch, empty);
            branches.setParent(firstPair);
            firstPair.children().add(minLen, branches);
        }
    }
    
    private Branches newBranches(NonLeaf parent, Node... nodes) {
        Branches branches = new Branches(parent);
        for (Node node : nodes) {
            branches.add(node);
        }
        return branches;
    }
    
    /**
     * 删除nodes中[i,j]位置的元素,并组成新的list
     * j越界也不会报错,会自动取最大值size
     *
     * @param nodes 节点列表
     * @param i     删除元素起始位置
     * @param j     删除元素结束位置,不包括此位置
     * @return 被删除元素 所构成的列表
     */
    private List<Node> removeFrom(List<Node> nodes, int i, int j) {
        j = Math.min(j, nodes.size());
        List<Node> list = new ArrayList<>();
        for (int k = i; k < j; k++) {
            list.add(nodes.remove(i));
        }
        return list;
    }
    
    /**
     * 创建Sequence并添加子节点
     *
     * @param children 子节点
     * @return 新Sequence
     */
    private Sequence newSequence(List<Node> children) {
        Sequence sequence = new Sequence(null);
        for (Node child : children) {
            sequence.add(child);
        }
        return sequence;
    }
    
    /**
     * 合并first、second作为Branches,
     * 并将first的父节点作为branches的父节点, branches将替换将first并占据其位置
     *
     * @param first  节点一
     * @param second 节点二
     */
    private void mergeAsBranches(Node first, Node second) {
        // 直接作为子分支
        if (first.getParent().isExtendsOf(Branches.class)) {
            first.getParent().add(second);
            return;
        }
    
        // 合并为Branches
        Branches branches = new Branches(first.getParent());
        branches.add(first);
        branches.add(second);
    
        // Branches 占据first在父类中的位置(好比夺舍后双魂共存,再认贼作父)
        replaceInParent(first, branches);
    }
    
    
    /**
     * 判断两个节点结构是否相同
     *
     * @param first  第一个节点
     * @param second 第二个节点
     * @return 结构是否相同
     */
    private boolean isStructSame(Node first, Node second) {
        if (first.isLeaf()) {
            return first.equals(second);
        }
        return first.getName().equals(second.getName());
    }
    
    private boolean leafEquals(Leaf first, Leaf second) {
        return first.getToken().equals(second.getToken());
    }
    
    
    private void replaceInParent(Node node, Node replaceNode) {
        if (node.isRoot()) {
            return;
        }
        final int index = indexInParent(node);
        final NonLeaf pNode = node.getParent();
        replaceNode.setParent(pNode);
        pNode.children().set(index, replaceNode);
    }
    
    private int indexInParent(Node node) {
        final List<Node> pChildren = node.getParent().children();
        for (int i = 0; i < pChildren.size(); i++) {
            if (pChildren.get(i) == node) {
                return i;
            }
        }
        return -1;
    }
    
}
