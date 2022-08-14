package cn.pheker.regex.generator.test;

import cn.pheker.regex.generator.core.parser.Generator;
import cn.pheker.regex.generator.core.parser.model.Model;
import cn.pheker.regex.generator.core.parser.model.ModelBuilder;
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
 * @desc 测试元数据
 */
@Slf4j
@RunWith(JUnit4.class)
public class MetaInfoTest {

    @Test
    public void testMetaInfo() {
        List<String> lines = Arrays.asList(
            "div"
            , "di"
            , "dicp"
            , "cps"
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
