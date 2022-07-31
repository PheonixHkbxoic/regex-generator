package cn.pheker.regex.generator.misc;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/7/30 18:56
 * @desc 三元表达式
 * true,false,break
 */
public class ReturnBreak extends TrueFalseElse {
    protected ReturnBreak(int flag) {
        super(flag);
    }
    
    public static ReturnBreak ofBreak() {
        return new ReturnBreak(-1);
    }
    
    public static ReturnBreak ofReturn(boolean bool) {
        return bool ? ofTrue() : ofFalse();
    }
    
    public static ReturnBreak ofFalse() {
        return new ReturnBreak(0);
    }
    
    public static ReturnBreak ofTrue() {
        return new ReturnBreak(1);
    }
    
    public boolean isBreak() {
        return isElse();
    }
    
    public boolean isReturn() {
        return !isNegative();
    }
    
}
