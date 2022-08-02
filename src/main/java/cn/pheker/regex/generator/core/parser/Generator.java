package cn.pheker.regex.generator.core.parser;

import cn.pheker.regex.generator.core.parser.model.Model;
import cn.pheker.regex.generator.core.parser.model.ModelIterator;
import cn.pheker.regex.generator.core.parser.model.interfaces.Node;
import cn.pheker.regex.generator.core.parser.strategy.Strategy;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/7/27 0:15
 * @desc 生成器
 */
public class Generator implements Iterable<String> {
    private Model model;
    Strategy strategy;
    private Map<String, List<String>> nodeRegexListCache = new HashMap<>(1);

    public static Generator of(Model model) {
        return new Generator(model);
    }

    public Generator(Model model) {
        this.model = model;
    }


    public List<String> generate() {
        return generate("0");
    }

    public List<String> generate(String nodeId) {
        doGeneratorIfAbsent(nodeId);
        return nodeRegexListCache.get(nodeId);
    }

    private void doGeneratorIfAbsent(String nodeId) {
        if (!nodeRegexListCache.containsKey(nodeId)) {
            final Node iteNode = model.search(nodeId);
            List<String> regexList = iteNode.generateRegex();
            nodeRegexListCache.put(nodeId, regexList);
        }
    }


    @Override
    public Iterator<String> iterator() {
        return this.iterator("0");
    }

    public Iterator<String> iterator(String nodeId) {
        try {
            doGeneratorIfAbsent(nodeId);
            return new ModelIterator(nodeRegexListCache.get(nodeId), null);
        } catch (Exception e) {
            return new ModelIterator(null, e.getMessage());
        }
    }
}
