package cn.pheker.regex.generator.test;

import cn.pheker.regex.generator.core.parser.Generator;
import cn.pheker.regex.generator.core.parser.model.Model;
import cn.pheker.regex.generator.core.parser.nodes.Branches;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * @author wanghaijun
 * @version 1.0.0
 * @date 2022/8/2 21:46
 * @desc 测试模型修正
 */
@Slf4j
@RunWith(JUnit4.class)
public class ModelProofreadTest {
    Model model = new Model();
    
    @Before
    public void buildModel() {
        String text = "<div>abc</div>";
        model.proofread(text);
        log.info("before: {}", model.format());
    }

    @Test
    public void testProofread() {
        String text = "<p>abc</p>";
        boolean proofread = model.proofread(text);
        log.info("after: {}", model.format());
        final Generator gen = Generator.of(model);
        log.info("after regex: {}", gen.generate());
    }
    
    
}
