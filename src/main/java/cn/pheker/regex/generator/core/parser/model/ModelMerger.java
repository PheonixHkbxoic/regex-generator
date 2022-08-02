package cn.pheker.regex.generator.core.parser.model;

import cn.pheker.regex.generator.core.parser.model.nodes.Root;
import lombok.extern.slf4j.Slf4j;

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
        
        return this.doMerge(other);
    }
    
    /**
     * 合并其他节点树到本节点树中
     * 合并规则:
     * 1.
     * 2.
     * 3.
     *
     * @param other 其他节点树
     * @return 合并是否成功
     */
    private boolean doMerge(Root other) {
        
        log.info("other: {}", other);
        return false;
    }
    
    
}
