package cn.pheker.regex.generator.core.parser.model;

import cn.pheker.regex.generator.core.parser.MetaInfo;
import cn.pheker.regex.generator.core.parser.abstracts.Leaf;
import cn.pheker.regex.generator.core.parser.abstracts.NonLeaf;
import cn.pheker.regex.generator.core.parser.interfaces.Node;
import cn.pheker.regex.generator.core.parser.nodes.Branches;
import cn.pheker.regex.generator.core.parser.nodes.Root;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author wanghaijun
 * @version 1.0.0
 * @date 2022/8/2 22:04
 * @desc 模型合并器
 */
@Slf4j
public class ModelMerger {
    private Model origin;

    public ModelMerger(Model origin) {
        this.origin = origin;
    }


    /**
     * 合并其他模型节点到本模型节点
     *
     * @param other 其他模型节点
     * @return 合并是否成功
     */
    public boolean merge(Root other) {
        if (origin.root.children().isEmpty()) {
            origin.root = other;
            return true;
        }

        this.doMerge(origin.root, other);
        return true;
    }

    /**
     * 合并其他节点树到本节点树中
     * 合并规则:
     * 1.
     * 2.
     * 3.
     *
     * @param second 其他节点树
     * @return 合并是否成功
     */
    private Node doMerge(Node first, Node second) {
        log.info("second: {}", second);
        // 相同类型节点合并
        if (first.getName().equals(second)) {
            // (NonLeaf,NonLeaf)

            // (Leaf,Leaf)
            if (first.isExtendsOf(Leaf.class)) {
                if (leafEquals((Leaf) first, (Leaf) second)) {
                    final MetaInfo metaInfo = first.getMetaInfo();
                    metaInfo.setCount(metaInfo.getCount());
                }else {
                    final Branches branches = new Branches(first.getParent());
                    final List<Node> children = branches.children();
                    children.add(first);
                    children.add(second);
                    this.replaceInParent(first, branches);
                }
            }
            return first;
            // 父子类型节点合并
        } else {
            // 叶子节点
            if (first.isLeaf()) {
                final Branches branches = new Branches(first.getParent());
                final List<Node> children = branches.children();
                children.add(first);
                children.add(second);
                this.replaceInParent(first, branches);
            } else {

            }
        }


        return first;
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
