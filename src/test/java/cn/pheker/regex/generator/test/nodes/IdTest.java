package cn.pheker.regex.generator.test.nodes;

import cn.pheker.regex.generator.core.parser.model.Model;
import cn.pheker.regex.generator.core.parser.model.ModelBuilder;
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
        final String text = rg.generate();
        final Model model = ModelBuilder.of(text).build();
        log.info("testId-text: {}, 模型如下:{}", text, model.format());
    }

}
