package cn.pheker.regex.generator.core.parser.model;

import cn.pheker.regex.generator.core.parser.model.nodes.Root;
import cn.pheker.regex.generator.core.scanner.Scanner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/7/27 0:17
 * @desc
 */
public class Model {
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
    
    
}
