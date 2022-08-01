package cn.pheker.regex.generator.core.parser.model.nodes;

/**
 * @author cn.pheker
 * @version 1.0
 * @date 2022/8/1 17:12
 * 数字类型, 十进制数字有小数的情况
 */
public enum NumberType {
    /**
     * 二进制
     **/
    Binary,

    /**
     * 八进制
     **/
    Octal,

    /**
     * 十进制
     **/
    Decimal,

    /**
     * 十六进制
     **/
    Hexadecimal,


    ;
}
