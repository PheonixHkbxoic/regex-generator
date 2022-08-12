package cn.pheker.regex.generator.core.lexer;

import lombok.Data;

/**
 * @author cn.pheker
 * @version 1.0
 * @date 2022/8/12 10:10
 * 位置坐标
 */
@Data
public class Pos {
    /**
     * 文本偏移位置
     **/
    private int offset;

    /**
     * 行 从0开始
     */
    private int row;

    /**
     * 列  从0开始
     */
    private int col;

    public Pos(int offset, int row, int col) {
        this.offset = offset;
        this.row = row;
        this.col = col;
    }

    public static Pos of(int offset, int row, int col) {
        return new Pos(offset, row, col);
    }

    @Override
    public String toString() {
        return String.format("Pos[%d,%d,%d]", offset, row, col);
    }
}
