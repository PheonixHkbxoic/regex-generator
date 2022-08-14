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
        List<String> arr = Arrays.asList("[a-z]", "[a-z]", "[A-Z]");
        List<String> list = new ArrayList<>(arr);
        Generalizer.Wrapper.distinct(list, true);
        log.info("list branch: {}", list);

        List<String> list2 = new ArrayList<>(arr);
        Generalizer.Wrapper.distinct(list2, false);
        log.info("list sequence: {}", list2);
    }


    @Test
    public void testDistinctListComplex() {
        List<String> arr = Arrays.asList("[a-z]", "[a-z]{2,4}", "[A-Z]", "\\d{2,4}", "\\d{3,6}");
        List<String> list = new ArrayList<>(arr);
        Generalizer.Wrapper.distinct(list, true);
        log.info("list branch: {}", list);

        List<String> list2 = new ArrayList<>(arr);
        Generalizer.Wrapper.distinct(list2, false);
        log.info("list sequence: {}", list2);
    }


    @Test
    public void testDistinctListGroup() {
        List<String> arr = Arrays.asList("\\d(?:\\d{7}|0{2})", "\\d{8}", "\\d{8}", "\\d{8}");
        List<String> list = new ArrayList<>(arr);
        Generalizer.Wrapper.distinct(list, true);
        log.info("list branch: {}", list);

        List<String> list2 = new ArrayList<>(arr);
        Generalizer.Wrapper.distinct(list2, false);
        log.info("list sequence: {}", list2);
    }

}
