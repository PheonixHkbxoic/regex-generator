package cn.pheker.regex.generator.test;

import cn.pheker.regex.generator.core.parser.model.Model;
import cn.pheker.regex.generator.core.parser.model.ModelBuilder;
import cn.pheker.regex.generator.core.scanner.StringScanner;
import cn.pheker.regex.generator.core.scanner.TxtLinesScanner;
import cn.pheker.regex.generator.util.ResourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/7/28 0:06
 * @desc
 */
@Slf4j
public class ModelBuilderTest {
    
    @Test
    public void testTargets() {
        for (String str : targets()) {
            StringScanner scanner = new StringScanner(str);
            ModelBuilder builder = ModelBuilder.of(scanner);
            Model model = builder.buildModel();
            log.info("model: {}", model);
        }
    }
    
    private List<String> targets() {
        List<String> targets = new ArrayList<>();
//        targets.add("<div>");
//        targets.add("<div>abc</div>");
//        targets.add("<div>abc<p>123</p></div>");
//        targets.add("<type:<node>, text:<abc123@#$>>");
//        targets.add("'user'");
//        targets.add("<div id='user'>");
//        targets.add("<yes<no>abc");
//        targets.add("<div id = abc > 123");
//        targets.add("\"abc'123\"");
//        targets.add("'name':'zhangsan'");
//        targets.add("'name':'zhangsan',abc");
//        targets.add("'name': zhangsan,abc");
        targets.add("{a:{b:1}");
        return targets;
    }
    
    
    @Test
    public void testFullHtml() {
        String path = ResourceUtil.getResourcePath("google_index.html");
        TxtLinesScanner scanner = new TxtLinesScanner();
        scanner.setFilePath(path);
    
        ModelBuilder builder = ModelBuilder.of(scanner);
        Model model = builder.buildModel();
        log.info("model: {}", model);
    }
    
    @Test
    public void testFullJson() {
        String path = ResourceUtil.getResourcePath("runoob.json");
        TxtLinesScanner scanner = new TxtLinesScanner();
        scanner.setFilePath(path);
    
        ModelBuilder builder = ModelBuilder.of(scanner);
        Model model = builder.buildModel();
        log.info("model: {}", model);
    }
    
}
