package cn.pheker.regex.generator.core.parser.nodes;

import cn.pheker.regex.generator.util.StrUtil;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/7/27 21:20
 * @desc
 */
public class Root extends Sequence {
    
    public Root() {
        super(null);
    }
    
    @Override
    public String toString() {
        return StrUtil.times(deep) + "Root{" + children + "}";
    }
}
