package cn.pheker.regex.generator.core.lexer;

import lombok.Data;

/**
 * @author cn.pheker
 * @version 1.0
 * @date 2022/8/12 10:14
 * 位置坐标范围 [start, end]
 */
@Data
public class Range {
    private Pos start;
    private Pos end;

    public int lines() {
        return end.getRow() - start.getRow() + 1;
    }

    public int lens() {
        return end.getOffset() - start.getOffset() + 1;
    }
}
