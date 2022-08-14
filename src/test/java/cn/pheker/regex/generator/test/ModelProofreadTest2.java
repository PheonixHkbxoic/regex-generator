package cn.pheker.regex.generator.test;

import cn.pheker.regex.generator.core.parser.Generator;
import cn.pheker.regex.generator.core.parser.GeneratorConfig;
import cn.pheker.regex.generator.core.parser.model.Model;
import cn.pheker.regex.generator.core.parser.model.ModelBuilder;
import cn.pheker.regex.generator.core.parser.other.Mode;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.List;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/8/2 21:46
 * @desc 测试模型修正2
 */
@Slf4j
@RunWith(JUnit4.class)
public class ModelProofreadTest2 {

    @Test
    public void testProofread2() {
        List<String> lines = Arrays.asList(
            "div",
            "dc",
            "dicp",
            "divp",
            "cp",
            "cip",
            "civp",
            "faq"
        );
        Model model = ModelBuilder.of().build();
        for (String line : lines) {
            boolean proofread = model.proofread(line);
        }
        log.info("after: {}", model.format());
        GeneratorConfig config = new GeneratorConfig();
        config.setMode(Mode.Extreme);
        final Generator gen = Generator.of(model, config);
        log.info("after regex: {}", gen.generate());
    }


    @Test
    public void testProofread3() {
        List<String> lines = Arrays.asList(
            "div",
//                "di",
            "divc"
        );
        Model model = ModelBuilder.of().build();
        for (String line : lines) {
            boolean proofread = model.proofread(line);
        }
        log.info("after: {}", model.format());
        final Generator gen = Generator.of(model);
        log.info("after regex: {}", gen.generate());
    }

    @Test
    public void testProofread4() {
        List<String> lines = Arrays.asList(
            "di",
            "cp",
            "cv",
            "dx"
        );
        Model model = ModelBuilder.of().build();
        for (String line : lines) {
            boolean proofread = model.proofread(line);
        }
        log.info("after: {}", model.format());
        final Generator gen = Generator.of(model);
        log.info("after regex: {}", gen.generate());
    }


}
