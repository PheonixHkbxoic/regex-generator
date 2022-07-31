package cn.pheker.regex.generator.core.parser.model.interfaces;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/7/27 21:57
 * @desc 可选择的, 多选一
 */
public interface Alternative {
    
    /**
     * 设置为可选择的,多选一
     */
    void setAlternative();
    
    /**
     * 是否是多选一
     *
     * @return true多选一, false全部都要选
     */
    boolean isAlternative();
}
