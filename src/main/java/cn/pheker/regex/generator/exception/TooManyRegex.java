package cn.pheker.regex.generator.exception;

/**
 * @author cn.pheker
 * @version 1.0
 * @date 2022/8/2 15:41
 * 要生成的正则数据过多,超过最大上限
 */
public class TooManyRegex extends RuntimeException {
    public TooManyRegex() {
        super("too many regex to generate");
    }
}
