package cn.pheker.regex.generator.core.parser;

import cn.hutool.core.bean.BeanUtil;
import cn.pheker.regex.generator.core.parser.interfaces.Node;
import cn.pheker.regex.generator.core.parser.model.Model;
import cn.pheker.regex.generator.core.parser.model.ModelIterator;

import java.util.*;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/7/27 0:15
 * @desc 生成器
 */
public class Generator implements Iterable<String> {
    private final Model model;
    private GeneratorConfig config;

    private final Map<String, List<String>> nodeRegexListCache = new HashMap<>(1);

    public static Generator of(Model model) {
        return of(model, null);
    }

    public static Generator of(Model model, GeneratorConfig config) {
        return new Generator(model, config);
    }

    public Generator(Model model, GeneratorConfig config) {
        this.model = model;
        this.config = config;
        if (config == null) {
            this.config = new GeneratorConfig();
        }
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
            // TODO 正则生成
            // 1.节点复制
            Node cpNode = BeanUtil.toBean(iteNode, iteNode.getClass());
            // 2.提取公共后缀
            // 3.节点泛化
            Generalizer generalizer = Generalizer.of(cpNode, config);
            String regex = generalizer.generalize();
            List<String> regexList = Collections.singletonList(regex);
            // 再生成正则
//            List<String> regexList = cpNode.generateRegex();
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
