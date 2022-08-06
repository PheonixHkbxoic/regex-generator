package cn.pheker.regex.generator.core.parser;

import cn.pheker.regex.generator.core.parser.other.Mode;
import lombok.Data;

/**
 * @author cn.pheker
 * @version 1.0
 * @date 2022/8/3 14:11
 * 生成器配置
 */
@Data
public class GeneratorConfig {
    /**
     * 模式
     */
    private Mode mode = Mode.Generate;
    
    /**
     * 容错率
     */
    private float faultRate = 0.1f;
    
    /**
     * 最小升级分支数量
     * 即分支数量达到levelUpBranchNum时考虑升级
     */
    private int levelUpBranchNum = 3;
}
