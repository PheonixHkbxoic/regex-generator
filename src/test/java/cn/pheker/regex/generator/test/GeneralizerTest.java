package cn.pheker.regex.generator.test;

import cn.pheker.regex.generator.core.parser.Generator;
import cn.pheker.regex.generator.core.parser.model.Model;
import cn.pheker.regex.generator.core.parser.model.ModelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * @author wanghaijun
 * @version 1.0.0
 * @date 2022/8/7 12:28
 * @desc 泛化器测试
 */
@Slf4j
@RunWith(JUnit4.class)
public class GeneralizerTest {
    private Model model = ModelBuilder.of().buildModel();
    
    @Test
    public void testGeneralizer() {
        String text = "pheker java 78\n" +
                "pheker english 55\n" +
                "pheker english 55\n" +
                "pheker golang 88\n" +
                "pheker golang 88\n" +
                "pheker golang 88\n" +
                "zj java 15\n" +
                "zj php 95\n" +
                "93 zj js";
        String[] lines = text.split("\n");
        for (String line : lines) {
            model.proofread(line);
        }
        
        log.info("model: {}", model.format());
        Generator gen = Generator.of(model);
        log.info("gen: {}", gen.generate());
    }
    
}
