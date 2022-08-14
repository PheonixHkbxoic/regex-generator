package cn.pheker.regex.generator.test.nodes;

import cn.pheker.regex.generator.core.parser.Generator;
import cn.pheker.regex.generator.core.parser.GeneratorConfig;
import cn.pheker.regex.generator.core.parser.model.Model;
import cn.pheker.regex.generator.core.parser.model.ModelBuilder;
import cn.pheker.regex.generator.core.parser.other.Mode;
import com.github.curiousoddman.rgxgen.RgxGen;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * @author cn.pheker
 * @version 1.0
 * @date 2022/8/1 14:37
 */

@RunWith(JUnit4.class)
@Slf4j
public class IdTest {
    private RgxGen rg;

    @Before
    public void gen() {
        this.rg = new RgxGen("[_$a-zA-Z][_$a-zA-Z0-9]{2,5}");
    }


    @Test
    public void testId() {
        final Model model = ModelBuilder.of().build();
        for (int i = 0; i < 10; i++) {
            String text = rg.generate();
            log.info("proofread text: {}", text);
            model.proofread(text);
        }
        log.info("testId-model:{}", model.format());

        GeneratorConfig config = new GeneratorConfig();
        config.setMode(Mode.Accurate);
        Generator gen = Generator.of(model);
        log.info("regex: {}", gen.generate());
    }

}
