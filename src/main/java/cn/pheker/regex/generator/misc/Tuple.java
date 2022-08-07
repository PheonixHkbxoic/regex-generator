package cn.pheker.regex.generator.misc;

import lombok.Getter;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/8/7 18:06
 * @desc
 */
@Getter
public abstract class Tuple<M, N> {
    protected M m;
    protected N n;
    
    public Tuple(M m, N n) {
        this.m = m;
        this.n = n;
    }
}
