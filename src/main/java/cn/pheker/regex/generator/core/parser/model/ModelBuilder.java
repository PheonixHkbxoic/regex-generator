package cn.pheker.regex.generator.core.parser.model;

import cn.pheker.regex.generator.core.scanner.Scanner;
import cn.pheker.regex.generator.core.scanner.StringScanner;
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
    
    public Model buildModel() {
        return model;
    }
    
    public static ModelBuilder of(String text) {
        ModelBuilder builder = new ModelBuilder();
        builder.model = new Model();
        return builder.build(new StringScanner(text));
    }
    
    public static ModelBuilder of(Scanner scanner) {
        ModelBuilder builder = new ModelBuilder();
        builder.model = new Model();
        return builder.build(scanner);
    }
    
    private ModelBuilder build(Scanner scanner) {
        ModelContext mc = ModelContext.of(scanner);
        return build(model, mc);
    }
    
    private ModelBuilder build(ModelContext context) {
        return build(model, context);
    }
    
    private ModelBuilder build(Model model, ModelContext mc) {
        if (!model.proofread(mc)) {
            throw new ParseException("解析失败");
        }
        return this;
    }
    
}
