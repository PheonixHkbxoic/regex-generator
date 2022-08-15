package cn.pheker.regex.generator.core.parser.regex.nodes;

import cn.pheker.regex.generator.core.parser.other.Mode;
import cn.pheker.regex.generator.core.parser.regex.ReRange;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/8/15 22:49
 * @desc
 */
public class ReExtend extends ReRange {
    public ReExtend() {
        super(0x80, 0xff);
        this.alias = "\\x80-\\xff";
        this.mode = Mode.Low;
    }
}
