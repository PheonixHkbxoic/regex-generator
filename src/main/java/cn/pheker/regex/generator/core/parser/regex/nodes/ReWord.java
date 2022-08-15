package cn.pheker.regex.generator.core.parser.regex.nodes;

import cn.pheker.regex.generator.core.parser.other.Mode;
import cn.pheker.regex.generator.core.parser.regex.ReChar;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/8/15 22:20
 * @desc
 */
public class ReWord extends ReChar {

    public ReWord() {
        super(0);
        this.alias = "\\w";
        this.mode = Mode.Middle;
    }

}
