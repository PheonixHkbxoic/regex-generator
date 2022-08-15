package cn.pheker.regex.generator.core.parser.other;

/**
 * @author cn.pheker
 * @version 1.0
 * @date 2022/8/3 14:26
 * 生成模式:
 * 泛化程度由低到高依次为 精确模式、低级模式、中级模式、高级模式、完全模式
 * 泛化取决于 模式、分支深度、分支数量、节点类型
 */
public enum Mode {
    /**
     * 精确模式
     **/
    Accurate,

    /**
     * 低级模式
     **/
    Low,

    /**
     * 中等模式
     **/
    Middle,

    /**
     * 高级模式
     **/
    High,

    /**
     * 完全模式
     **/
    Full;


    /**
     * 选择两个模式中更高级的模式
     *
     * @param mode1 模式1
     * @param mode2 模式2
     * @return 更高级的模式
     */
    public static Mode higherMode(Mode mode1, Mode mode2) {
        if (mode1.ordinal() > mode2.ordinal()) {
            return mode1;
        }
        return mode2;
    }

    /**
     * 返回泛化程度更高的模式,最高为{@link Mode#Full}
     *
     * @param mode 低级模式
     * @return 更高级模式
     */
    public static Mode higherMode(Mode mode) {
        return from(mode.ordinal() + 1);
    }

    /**
     * 通过ordinal反向查询Mode
     *
     * @param mode 模式对应的ordinal,范围[0-3]
     * @return ordinal对应的模式
     * mode小于0则返回{@link Mode#Accurate}
     * mode大于3则返回{@link Mode#Full}
     */
    private static Mode from(int mode) {
        for (Mode l : values()) {
            if (l.ordinal() == mode) {
                return l;
            }
        }
        return mode > Full.ordinal() ? Full : Accurate;
    }
}
