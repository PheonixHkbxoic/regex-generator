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
    
    /**
     * 分支默认使用捕获组;
     */
    private boolean useCapturedGroup = false;
    
    /**
     * 不定长字符, 最大长度与最小长度差大于wildcardMinInterval时
     * 使用通配符*或+代替, 否则使用范围长度如:{5},{2,8}
     * 当然,前提是最小长度为0时 才可能使用*,为1时才可能使用+
     */
    private int wildcardMinInterval = 3;
}
