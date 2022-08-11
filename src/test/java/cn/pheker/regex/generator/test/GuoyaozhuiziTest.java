package cn.pheker.regex.generator.test;

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

import java.util.List;

/**
 * @author cn.pheker
 * @version 1.0
 * @date 2022/8/4 11:47
 */
@Slf4j
@RunWith(JUnit4.class)
public class GuoyaozhuiziTest {

    @Test
    public void testGuoyaozhuizi() {
        final String path = ResourceUtil.getResourcePath("guoyaozhuizi.txt");
        LinesScanner scanner = new LinesScanner(path);
        final Model model = ModelBuilder.of(scanner).build();
        log.info("testGuoyaozhuizi-model: {}", model.format());
        final List<String> regexList = Generator.of(model).generate();
        log.info("testGuoyaozhuizi-regexList: {}", regexList);
    }

    @Test
    public void testQueryWordsLib() {
        final String path = ResourceUtil.getResourcePath("query-words-lib.txt");
        LinesScanner scanner = new LinesScanner(path);
        final Model model = ModelBuilder.of(scanner).build();
        log.info("testQueryWordsLib-model: {}", model.format());
        GeneratorConfig config = new GeneratorConfig();
        config.setMode(Mode.Generate);
        final List<String> regexList = Generator.of(model).generate();
        log.info("testQueryWordsLib-regexList: {}", regexList);
    }

}
