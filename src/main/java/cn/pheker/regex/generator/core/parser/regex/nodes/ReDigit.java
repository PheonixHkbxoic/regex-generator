package cn.pheker.regex.generator.core.parser.regex.nodes;

import cn.pheker.regex.generator.core.parser.other.Mode;
import cn.pheker.regex.generator.core.parser.regex.ReRange;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/8/15 22:49
 * @desc
 */
public class ReDigit extends ReRange {
    public ReDigit() {
        super('0', '9');
        this.alias = "\\d";
        this.mode = Mode.Low;
    }
}
