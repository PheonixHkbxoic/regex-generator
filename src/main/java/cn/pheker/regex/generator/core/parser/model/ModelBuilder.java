package cn.pheker.regex.generator.core.parser.model;

import cn.pheker.regex.generator.core.scanner.MultiScanner;
import cn.pheker.regex.generator.core.scanner.Scanner;
import cn.pheker.regex.generator.core.scanner.StringLinesScanner;
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
        ModelBuilder builder = new ModelBuilder();
        builder.model = new Model();
        return builder;
    }
    
    public static ModelBuilder of(String text) {
        return of(new StringLinesScanner(text));
    }
    
    public static ModelBuilder of(Scanner scanner) {
        ModelBuilder builder = new ModelBuilder();
        builder.model = new Model();
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
