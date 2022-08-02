package cn.pheker.regex.generator.core.parser.model;

import cn.hutool.core.util.StrUtil;
import cn.pheker.regex.generator.core.parser.model.abstracts.NonLeaf;
import cn.pheker.regex.generator.core.parser.model.interfaces.Node;
import cn.pheker.regex.generator.core.parser.model.nodes.Root;
import cn.pheker.regex.generator.core.scanner.Scanner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/7/27 0:17
 * @desc
 */
public class Model implements Iterable<String>{
    Root root = new Root();
    
    /**
     * 应用过的模型上下文列表
     */
    List<ModelContext> contextList = new ArrayList<>();
    /**
     * 当前模型上下文
     */
    ThreadLocalModelContext tlmc;
    
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
        this.tlmc = ThreadLocalModelContext.of(mc);
        this.root.setContext(tlmc);
        boolean flag = root.parse();
        if (flag) {
            this.contextList.add(mc);
            this.tlmc = null;
        }
        return flag;
    }
    
    @Override
    public String toString() {
        return String.format("\n=================================== 模型 ===================================\n" +
                        "%s" +
                        "============================================================================",
                root.printFormatted());
    }


    @Override
    public Iterator<String> iterator() {
        return new ModelIterator(root);
    }

    public Node search(String nodeId) {
        if (StrUtil.isEmpty(nodeId)) {
            return null;
        }
        final List<Integer> indices = Arrays.asList(nodeId.split("-")).stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList());
        Integer index = indices.remove(0);
        if (indices.isEmpty()) {
            return root;
        }
        Node curr = root;
        while (!indices.isEmpty()) {
            index = indices.remove(0);
            if (curr.isExtendsOf(NonLeaf.class)) {
                curr = ((NonLeaf) curr).children().get(index);
            }else {
                curr = null;
                break;
            }
        }

        // 非法nodeId
        if (curr == null) {
            return null;
        }
        return curr;
    }

}
