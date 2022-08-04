package cn.pheker.regex.generator.test.nodes;

import cn.pheker.regex.generator.core.parser.model.Model;
import cn.pheker.regex.generator.core.parser.model.ModelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cn.pheker
 * @version 1.0
 * @date 2022/8/1 15:37
 */
@Slf4j
@RunWith(JUnit4.class)
public class DotXTest {


    @Test
    public void testDotX() {
        for (String str : targets()) {
            ModelBuilder builder = ModelBuilder.of(str);
            Model model = builder.buildModel();
            log.info("model: {}", model.format());
        }
    }

    private List<String> targets() {
        List<String> targets = new ArrayList<>();
//        targets.add("bin.get()");
        targets.add("bin.get2()");
        targets.add("bin.2get2()");
        targets.add("bin.set(1)");
        targets.add("123.456abc");
        return targets;
    }

}
