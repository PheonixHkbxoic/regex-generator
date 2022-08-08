package cn.pheker.regex.generator.test.other;

import cn.pheker.regex.generator.core.parser.Generalizer;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/8/8 23:04
 * @desc
 */
@Slf4j
@RunWith(JUnit4.class)
public class DistinctTest {
    
    @Test
    public void testDistinctList() {
        List<String> arr = Arrays.asList("[a-z]", "[A-Z]");
        List<String> list = new ArrayList<>(arr);
        Generalizer.Wrapper.distinct(list);
        log.info("list: {}", list);
    }
    
    
    @Test
    public void testDistinctList2() {
        List<String> arr = Arrays.asList("[a-z]", "[a-z]", "[a-z]", "[A-Z]", "[A-Z]", "\\d");
        List<String> list = new ArrayList<>(arr);
        Generalizer.Wrapper.distinct(list);
        log.info("list2: {}", list);
    }
}
