package cn.pheker.regex.generator.core.parser.regex.nodes;

import cn.pheker.regex.generator.core.parser.other.Mode;
import cn.pheker.regex.generator.core.parser.regex.ReRange;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/8/15 22:49
 * @desc
 */
public class ReChinese extends ReRange {
    public ReChinese() {
        super(0x4e00, 0x9fa5);
        this.alias = "\\x4e00-\\x9fa5";
        this.mode = Mode.Low;
    }
}
