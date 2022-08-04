package cn.pheker.regex.generator.core.parser;

import lombok.Data;

/**
 * @author wanghj
 * @version 1.0
 * @date 2022/8/3 14:12
 */
@Data
public class MetaInfo {
    protected int count = 1;
    protected int len = 1;

    public void incrCount(){
        this.count++;
    }

    @Override
    public String toString() {
        return "MetaInfo{" +
                "count=" + count +
                ", len=" + len +
                '}';
    }
}
