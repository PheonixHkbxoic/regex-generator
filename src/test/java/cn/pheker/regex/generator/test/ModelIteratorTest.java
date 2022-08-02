package cn.pheker.regex.generator.test;

import cn.pheker.regex.generator.core.parser.model.Model;
import cn.pheker.regex.generator.core.parser.model.ModelBuilder;
import cn.pheker.regex.generator.core.parser.model.ModelIterator;
import cn.pheker.regex.generator.core.scanner.StringScanner;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Iterator;

/**
 * @author wanghj
 * @version 1.0
 * @date 2022/8/2 15:49
 */

@RunWith(JUnit4.class)
@Slf4j
public class ModelIteratorTest {

    @Test
    public void testModelIterator() {
        final String text = "<div>abc</div>";
        StringScanner scanner = new StringScanner(text);
        ModelBuilder builder = ModelBuilder.build(scanner);
        Model model = builder.buildModel();
        log.info("model: {}", model);
        final Iterator<String> regexIte = model.iterator();
        if (!regexIte.hasNext()) {
            log.error("testModelIterator-errorMsg: {}", ((ModelIterator) regexIte).getErrorMsg());
            return;
        }

        // print regex list;
        int i = 0;
        while (regexIte.hasNext()) {
            final String regex = regexIte.next();
            log.info("[{}] regex: {}", String.format("%04d", ++i), regex);
        }
    }

}
