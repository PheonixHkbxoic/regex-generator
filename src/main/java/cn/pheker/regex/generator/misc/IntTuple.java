package cn.pheker.regex.generator.misc;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/8/7 18:08
 * @desc
 */
public class IntTuple extends Tuple<Integer, Integer> {
    
    public IntTuple(Integer m, Integer n) {
        super(m, n);
    }
    
    public boolean same() {
        return m != null && m.equals(n);
    }
}
