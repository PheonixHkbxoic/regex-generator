package cn.pheker.regex.generator.test.other;

import cn.hutool.core.io.FileUtil;
import cn.pheker.regex.generator.core.parser.Generator;
import cn.pheker.regex.generator.core.parser.GeneratorConfig;
import cn.pheker.regex.generator.core.parser.model.Model;
import cn.pheker.regex.generator.core.parser.model.ModelBuilder;
import cn.pheker.regex.generator.core.scanner.ParagraphsScanner;
import cn.pheker.regex.generator.core.scanner.StringParagraphsScanner;
import cn.pheker.regex.generator.util.ResourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * @author cn.pheker
 * @version 1.0
 * @date 2022/8/10 16:03
 */
@Slf4j
@RunWith(JUnit4.class)
public class ParagraphsScannerTest {

    @Test
    public void testParagraphsScanner() {
        final String path = ResourceUtil.getResourcePath("paragraphs.txt");
        final ParagraphsScanner scanner = new ParagraphsScanner(path);
        final Model model = ModelBuilder.of(scanner).build();
        log.info("testParagraphsScanner-model: {}", model.format());
        final GeneratorConfig config = new GeneratorConfig();
        final Generator gen = Generator.of(model, config);
        log.info("testParagraphsScanner-gen: {}", gen.generate());
    }


    @Test
    public void testStringParagraphsScanner() {
        final String path = ResourceUtil.getResourcePath("paragraphs.txt");
        final byte[] bytes = FileUtil.readBytes(path);
        String text = new String(bytes);
        final StringParagraphsScanner scanner = new StringParagraphsScanner(text);
        final Model model = ModelBuilder.of(scanner).build();
        log.info("testStringParagraphsScanner-model: {}", model.format());
        final GeneratorConfig config = new GeneratorConfig();
        final Generator gen = Generator.of(model, config);
        log.info("testStringParagraphsScanner-gen: {}", gen.generate());
    }
}
