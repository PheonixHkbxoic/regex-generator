package cn.pheker.regex.generator.core.parser.nodes;

import cn.pheker.regex.generator.core.parser.abstracts.AbstractComposite;
import cn.pheker.regex.generator.core.parser.abstracts.NonLeaf;
import cn.pheker.regex.generator.core.parser.interfaces.Node;
import cn.pheker.regex.generator.exception.TooManyRegex;

import java.util.*;
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
        Set<String> reAll = new HashSet<>();
        for (Node child : children()) {
            List<String> res = child.generateRegex();
            reAll.addAll(res);
            if (reAll.size() > 10000) {
                throw new TooManyRegex();
            }
        }
        List<String> regex = new ArrayList<>(reAll);
    
        if (regex.size() == 1) {
            return regex;
        } else if (regex.size() == 2 && regex.contains("")) {
            String reCanBeEmpty = regex.get(0);
            if (reCanBeEmpty.length() == 0) {
                reCanBeEmpty = regex.get(1);
            }
            if (reCanBeEmpty.length() > 1) {
                reCanBeEmpty = "(?:" + reCanBeEmpty + ")?";
            } else {
                reCanBeEmpty += "?";
            }
            return Collections.singletonList(reCanBeEmpty);
        }
    
        // one char, multi char
        List<String> multi = compressOneChar(regex);
        if (multi.size() == 1) {
            return multi;
        }
        String group = multi.stream().collect(Collectors.joining("|", "(?:", ")"));
        return Collections.singletonList(group);
    }
    
    private List<String> compressOneChar(List<String> regex) {
        List<String> one = regex.stream()
                .filter(r -> r.length() == 1)
                .collect(Collectors.toList());
        List<String> multi = new ArrayList<>();
        if (one.size() != regex.size()) {
            multi = regex.stream()
                    .filter(r -> r.length() > 1)
                    .collect(Collectors.toList());
        }
        if (!one.isEmpty()) {
            String oneCompressed;
            if (one.size() == 1) {
                oneCompressed = one.get(0);
            } else {
                oneCompressed = one.stream().collect(Collectors.joining("", "[", "]"));
            }
            multi.add(0, oneCompressed);
        }
        return multi;
    }
}
