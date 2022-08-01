package cn.pheker.regex.generator.core.annotation;

import java.lang.annotation.*;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/7/26 22:23
 * @desc 不能为空 只是标识,没有任务作用
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
public @interface NotNull {
}
