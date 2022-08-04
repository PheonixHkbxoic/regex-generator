package cn.pheker.regex.generator.core.parser.model;

import cn.hutool.core.util.StrUtil;
import cn.pheker.regex.generator.core.parser.abstracts.NonLeaf;
import cn.pheker.regex.generator.core.parser.interfaces.Node;
import cn.pheker.regex.generator.core.parser.nodes.Branches;
import cn.pheker.regex.generator.core.parser.nodes.Sequence;
import cn.pheker.regex.generator.core.scanner.Scanner;
import cn.pheker.regex.generator.core.scanner.StringScanner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/7/27 0:17
 * @desc
 */
public class Model {
    NonLeaf root = new Sequence(null);
    ModelMerger merger;
    
    public Model() {
        this.merger = new ModelMerger(this);
    }
    
    /**
     * 应用过的模型上下文列表
     */
    List<ModelContext> contextList = new ArrayList<>();
    
    /**
     * 通过文本校正模型
     *
     * @param text 文本
     * @return 校正成功或失败
     */
    public boolean proofread(String text) {
        return this.proofread(new StringScanner(text));
    }
    
    /**
     * 通过扫描器 校正模型
     *
     * @param scanner 扫描器
     * @return 校正成功或失败
     */
    public boolean proofread(Scanner scanner) {
        ModelContext mc = ModelContext.of(scanner);
        return this.proofread(mc);
    }
    
    /**
     * 通过上下文 校正模型
     *
     * @param mc 模型上下文
     * @return 校正成功或失败
     */
    public boolean proofread(ModelContext mc) {
        ThreadLocalModelContext tlmc = ThreadLocalModelContext.of(mc);
        Sequence other = new Sequence(null);
        other.setContext(tlmc);
        boolean success = other.parse();
        if (success) {
            merger.merge(other);
            this.contextList.add(mc);
        }
        return success;
    }
    
    
    @Override
    public String toString() {
        return String.format("\n=================================== 模型 ===================================\n" +
                        "%s" +
                        "============================================================================",
                root.printFormatted());
    }
    
    
    /**
     * 根据节点id查找节点
     *
     * @return 对应的节点
     **/
    public Node search(String nodeId) {
        if (StrUtil.isEmpty(nodeId)) {
            return null;
        }
        final List<Integer> indices = Arrays.stream(nodeId.split("-"))
                .map(Integer::valueOf)
                .collect(Collectors.toList());
        indices.remove(0);
        if (indices.isEmpty()) {
            return root;
        }
        Node curr = root;
        Integer index;
        while (!indices.isEmpty()) {
            index = indices.remove(0);
            if (curr.isExtendsOf(NonLeaf.class)) {
                curr = ((NonLeaf) curr).children().get(index);
            } else {
                curr = null;
                break;
            }
        }
    
        return curr;
    }
    
}
