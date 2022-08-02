package cn.pheker.regex.generator.core.parser.nodes;

import cn.pheker.regex.generator.core.parser.abstracts.Leaf;
import cn.pheker.regex.generator.core.parser.abstracts.NonLeaf;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/7/27 20:50
 * @desc
 */
public class Single extends Leaf {
    
    
    public Single(NonLeaf parent) {
        super(parent);
    }
    
    /**
     * 叶子节点解析不会失败, 简化处理
     *
     * @param parent 父节点
     */
    public static Single of(NonLeaf parent) {
        Single single = new Single(parent);
        single.parse();
        return single;
    }
}
