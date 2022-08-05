package cn.pheker.regex.generator.core.parser.nodes;

import cn.pheker.regex.generator.core.parser.abstracts.AbstractComposite;
import cn.pheker.regex.generator.core.parser.abstracts.NonLeaf;
import cn.pheker.regex.generator.core.parser.interfaces.Node;
import cn.pheker.regex.generator.exception.TooManyRegex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/7/27 22:07
 * @desc
 */
public class Branches extends AbstractComposite {
    
    public Branches(NonLeaf parent) {
        super(parent);
    }
    
    @Override
    public String toString() {
        return "Branches{" + children + '}';
    }
    
    @Override
    public List<String> generateRegex() {
        List<String> regex = new ArrayList<>();
        for (Node child : children()) {
            regex.addAll(child.generateRegex());
            if (regex.size() > 10000) {
                throw new TooManyRegex();
            }
        }
        
        if (regex.size() == 1) {
            return regex;
        } else if (regex.size() == 2 && regex.contains("")) {
            String reCanBeEmpty = regex.get(0);
            if (reCanBeEmpty.length() == 0) {
                reCanBeEmpty = regex.get(1);
            }
            if (reCanBeEmpty.length() > 1) {
                reCanBeEmpty = "(?:" + reCanBeEmpty + ")?";
            }else{
                reCanBeEmpty += "?";
            }
            return Collections.singletonList(reCanBeEmpty);
        }
        String group = regex.stream().collect(Collectors.joining("|", "(?:", ")"));
        return Collections.singletonList(group);
    }
}
