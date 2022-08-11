package cn.pheker.regex.generator.test.nodes;

import cn.pheker.regex.generator.core.parser.Generator;
import cn.pheker.regex.generator.core.parser.GeneratorConfig;
import cn.pheker.regex.generator.core.parser.model.Model;
import cn.pheker.regex.generator.core.parser.model.ModelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * @author cn.pheker
 * @version 1.0
 * @date 2022-08-11 17:31:04
 */
@Slf4j
@RunWith(JUnit4.class)
public class BlankTest {


    @Test
    public void testBlank() {
        String[] lines = new String[]{
                " ",
                "  ",
                "\t\t",
                "\r\n\r\n"
        };
        final Model model = ModelBuilder.of().buildModel();
        for (String line : lines) {
            model.proofread(line);
        }
        log.info("testBlank-model: {}", model.format());

        final GeneratorConfig config = new GeneratorConfig();
        config.setConvertBlankBranch(true);
        final Generator gen = Generator.of(model, config);
        log.info("testBlank-gen: {}", gen.generate());
    }

}
