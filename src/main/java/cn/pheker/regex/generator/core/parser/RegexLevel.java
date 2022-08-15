package cn.pheker.regex.generator.core.parser;

import cn.pheker.regex.generator.core.parser.other.Mode;
import cn.pheker.regex.generator.core.parser.regex.ReChar;
import cn.pheker.regex.generator.core.parser.regex.ReRange;
import cn.pheker.regex.generator.core.parser.regex.abstracts.ReOne;
import cn.pheker.regex.generator.core.parser.regex.nodes.*;

/**
 * @author cn.pheker
 * @version 1.0
 * @date 2022/8/15 18:29
 */
public class RegexLevel {
    public static ReChar of(int ch) {
        return new ReChar(ch);
    }

    /**
     * 将单字符节点升级到目标级别, 如果单字符节点的级别不低于目标级别,则不会升级
     *
     * @param one        单字符节点
     * @param targetMode 目标级别
     * @return 升级后的单字符节点
     */
    public ReOne levelUp(ReOne one, Mode targetMode) {
        ReOne lowerNode = one;
        while (lowerNode.getMode().isLowerThan(targetMode)) {
            if (one instanceof ReChar) {
                lowerNode = this.levelUpChar(((ReChar) one));
            } else {
                lowerNode = this.levelUpRange(((ReRange) one));
            }
        }

        return lowerNode;
    }

    private ReOne levelUpChar(ReChar reChar) {
        Mode fromMode = reChar.getMode();
        if (fromMode == Mode.Accurate) {
            return this.levelUpFromAccurate(reChar);
        }

        if (fromMode == Mode.Low) {
            return this.levelUpFromLow(reChar);
        }

        if (fromMode == Mode.Middle) {
            return new ReDot();
        }

        return new ReAll();
    }

    private ReOne levelUpRange(ReRange reRange) {
        if (reRange instanceof ReExtend) {
            return new ReDot();
        }
        return new ReWord();
    }

    private ReOne levelUpFromLow(ReChar reChar) {
        if (reChar instanceof ReBlank) {
            if (((ReBlank) reChar).isNonCrLf()) {
                return new ReAll();
            }
            return new ReDot();
        }
        return new ReWord();
    }

    private ReOne levelUpFromAccurate(ReChar reChar) {
        ReOne reNode = null;
        if (reChar.containsAny(' ', '\t', '\r', '\n')) {
            reNode = new ReBlank(reChar.getCh());
        } else if (reChar.between('0', '9')) {
            reNode = new ReDigit();
        } else if (reChar.between('a', 'z')) {
            reNode = new ReLower();
        } else if (reChar.between('A', 'Z')) {
            reNode = new ReUpper();
        } else if (reChar.between(0x80, 0xff)) {
            reNode = new ReExtend();
        } else if (reChar.between(0x4e00, 0x9fa5)) {
            reNode = new ReChinese();
        }

        if (reNode != null) {
            return reNode;
        }

        if (reChar.contains('_')) {
            reNode = new ReWord();
            return reNode;
        }

        reNode = new ReDot();
        return reNode;
    }


}
