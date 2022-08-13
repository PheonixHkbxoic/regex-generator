package cn.pheker.regex.generator.test;

import cn.pheker.regex.generator.core.parser.Generator;
import cn.pheker.regex.generator.core.parser.model.Model;
import cn.pheker.regex.generator.core.parser.model.ModelBuilder;
import cn.pheker.regex.generator.core.parser.model.ModelInterceptor;
import cn.pheker.regex.generator.core.parser.nodes.Sequence;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022-08-13 20:06:57
 * @desc 模型拦截器测试
 */
@Slf4j
@RunWith(JUnit4.class)
public class ModelInterceptorTest {
    
    @Test
    public void testGeneralizer() {
        String text = "pheker java 78\n" +
                "pheker english 55\n" +
                "pheker english 55\n" +
                "pheker golang 88\n" +
                "pheker golang 88\n" +
                "pheker golang 88\n" +
                "pheker golang 88\n" +
                "zj java 15\n" +
                "zj php 95\n" +
                "93 zj js";
        
        final Model model = ModelBuilder.of(text, new ModelInterceptor() {
            @Override
            public boolean before(boolean parseSuccess, Model model, Sequence proofread) {
                log.info("before: parseSuccess: {}, model: {}",
                        parseSuccess, model.format());
                return true;
            }
            
            @Override
            public void after(Model model) {
            
            }
        })
                .build();
        log.info("model: {}", model.format());
        
        Generator gen = Generator.of(model);
        log.info("gen: {}", gen.generate());
    }
    
}
