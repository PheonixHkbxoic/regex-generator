package cn.pheker.regex.generator.core.parser;

import cn.pheker.regex.generator.core.parser.other.Mode;
import lombok.Data;

/**
 * @author wanghj
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

}
