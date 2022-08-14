package cn.pheker.regex.generator.core.parser.model;

import cn.pheker.regex.generator.core.scanner.StringLinesScanner;
import cn.pheker.regex.generator.core.scanner.abstracts.MultiScanner;
import cn.pheker.regex.generator.core.scanner.abstracts.Scanner;
import cn.pheker.regex.generator.exception.ParseException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/7/27 20:58
 * @desc 模型建造器
 */
@Slf4j
public class ModelBuilder {
    Model model;

    public Model build() {
        return model;
    }

    public static ModelBuilder of() {
        return of(((String) null));
    }

    public static ModelBuilder of(ModelInterceptor interceptor) {
        return of(((String) null), interceptor);
    }

    public static ModelBuilder of(String text) {
        return of(text, null);
    }

    public static ModelBuilder of(String text, ModelInterceptor interceptor) {
        if (text == null) {
            return of(((Scanner) null), interceptor);
        }
        return of(new StringLinesScanner(text), interceptor);
    }

    public static ModelBuilder of(Scanner scanner) {
        return of(scanner, null);
    }

    public static ModelBuilder of(Scanner scanner, ModelInterceptor interceptor) {
        ModelBuilder builder = new ModelBuilder();
        builder.model = new Model();
        builder.model.interceptor = interceptor;
        if (scanner instanceof MultiScanner) {
            final MultiScanner ms = (MultiScanner) scanner;
            while (ms.hasNext()) {
                builder.proofread(ms.next());
            }
            return builder;
        }
        return builder.proofread(scanner);
    }

    public ModelBuilder proofread(String text) {
        return this.proofread(new StringLinesScanner(text));
    }

    public ModelBuilder proofread(Scanner scanner) {
        if (scanner == null) {
            return this;
        }

        if (scanner instanceof MultiScanner) {
            final MultiScanner ms = (MultiScanner) scanner;
            while (ms.hasNext()) {
                this.proofread(ms.next());
            }
            return this;
        }

        return proofread(model, scanner);
    }

    private ModelBuilder proofread(Model model, Scanner scanner) {
        if (!model.proofread(scanner)) {
            throw new ParseException("解析失败");
        }
        return this;
    }

}
