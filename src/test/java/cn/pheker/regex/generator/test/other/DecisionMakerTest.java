package cn.pheker.regex.generator.test.other;

import cn.pheker.regex.generator.core.parser.other.DecisionMaker;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * @author cn.pheker
 * @version 1.0
 * @date 2022/8/15 15:13
 */
@Slf4j
@RunWith(JUnit4.class)
public class DecisionMakerTest {

    @Test
    public void testDecisionMaker() {
        for (int i = 0; i < 30; i++) {
            final DecisionMaker dm = DecisionMaker.of(i);
            log.info("testDecisionMaker-dm: \n[{}]: {}", String.format("%02d", i), dm);
        }
    }
}
