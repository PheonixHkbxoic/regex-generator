package cn.pheker.regex.generator.misc;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/7/30 18:49
 * @desc 三元表达式
 */
public class Ternary {
    protected int flag;
    
    public Ternary(int flag) {
        this.flag = flag;
    }
    
    public static Ternary ofNegative() {
        return new Ternary(-1);
    }
    
    public static Ternary ofZero() {
        return new Ternary(0);
    }
    
    public static Ternary ofPositive() {
        return new Ternary(0);
    }
    
    public boolean isNegative() {
        return flag < 0;
    }
    
    public boolean isZero() {
        return flag == 0;
    }
    
    public boolean isPositive() {
        return flag > 0;
    }
    
    
}
