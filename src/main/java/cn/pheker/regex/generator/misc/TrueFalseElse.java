package cn.pheker.regex.generator.misc;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/7/30 19:03
 * @desc 三元表达式
 * true,false,else
 */
public class TrueFalseElse extends Ternary {
    
    protected TrueFalseElse(int flag) {
        super(flag);
    }
    
    public static TrueFalseElse ofTrue() {
        return new TrueFalseElse(1);
    }
    
    public static TrueFalseElse ofFalse() {
        return new TrueFalseElse(0);
    }
    
    public static TrueFalseElse ofElse() {
        return new TrueFalseElse(-1);
    }
    
    public boolean isTrue() {
        return isPositive();
    }
    
    public boolean isFalse() {
        return isZero();
    }
    
    public boolean isElse() {
        return isNegative();
    }
    
}
