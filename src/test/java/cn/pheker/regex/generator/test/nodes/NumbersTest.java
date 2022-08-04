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
public class NumbersTest {


    @Test
    public void testNumbers() {
        for (String str : targets()) {
            ModelBuilder builder = ModelBuilder.of(str);
            Model model = builder.buildModel();
            log.info("model: {}", model.format());
        }
    }

    private List<String> targets() {
        List<String> targets = new ArrayList<>();
        targets.add("0");
        targets.add("+0");
        targets.add("-0");
        targets.add("0b");
        targets.add("0x0");
        targets.add("0x10");
        targets.add("0x1Z");
        targets.add("0x7c80");
        targets.add("0x7c8");
        targets.add("0x7c80Yes");
        targets.add("0b01");
        targets.add("0123");
        targets.add("0678");
        targets.add("123");
        targets.add("-123");
        targets.add("-123.456");
        targets.add("-123.456e7");
        targets.add("-123.456e-8");
        return targets;
    }

}
