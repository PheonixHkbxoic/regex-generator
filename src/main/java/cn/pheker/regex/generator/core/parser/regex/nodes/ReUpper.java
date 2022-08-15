package cn.pheker.regex.generator.core.parser.regex.nodes;

import cn.pheker.regex.generator.core.parser.other.Mode;
import cn.pheker.regex.generator.core.parser.regex.ReRange;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/8/15 22:49
 * @desc
 */
public class ReUpper extends ReRange {
    public ReUpper() {
        super('A', 'Z');
        this.alias = "A-Z";
        this.mode = Mode.Low;
    }
}
