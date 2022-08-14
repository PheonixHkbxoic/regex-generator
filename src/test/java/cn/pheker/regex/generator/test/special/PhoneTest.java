package cn.pheker.regex.generator.test.special;

import cn.pheker.regex.generator.core.parser.Generator;
import cn.pheker.regex.generator.core.parser.GeneratorConfig;
import cn.pheker.regex.generator.core.parser.model.Model;
import cn.pheker.regex.generator.core.parser.model.ModelBuilder;
import cn.pheker.regex.generator.core.parser.other.Mode;
import cn.pheker.regex.generator.core.scanner.LinesScanner;
import cn.pheker.regex.generator.util.ResourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/8/9 22:35
 * @desc
 */
@Slf4j
@RunWith(JUnit4.class)
public class PhoneTest {

    @Test
    public void testPhone() {
        String path = ResourceUtil.getResourcePath("phone.txt");
        LinesScanner scanner = new LinesScanner(path);
        ModelBuilder builder = ModelBuilder.of(scanner);
        Model model = builder.build();
        log.info("model: {}", model.format());
        GeneratorConfig config = new GeneratorConfig();
        config.setMode(Mode.Generate);
        final Generator gen = Generator.of(model, config);
        log.info("regex list: {}", gen.generate());
    }
}