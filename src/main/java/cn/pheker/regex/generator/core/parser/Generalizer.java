package cn.pheker.regex.generator.core.parser;

import cn.pheker.regex.generator.core.parser.abstracts.Leaf;
import cn.pheker.regex.generator.core.parser.abstracts.NonLeaf;
import cn.pheker.regex.generator.core.parser.interfaces.Node;
import cn.pheker.regex.generator.core.parser.nodes.Branches;
import cn.pheker.regex.generator.core.parser.nodes.Id;
import cn.pheker.regex.generator.core.parser.nodes.Numbers;
import cn.pheker.regex.generator.misc.IntTuple;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/8/7 13:03
 * @desc 泛化器
 */
public class Generalizer {
    private Node node;
    private static GeneratorConfig config;
    
    private static String Empty = "";
    private static String Digit = "\\d";
    private static String Upper = "A-Z";
    private static String Lower = "a-z";
    private static String Blank = "\\s";
    static String[] table = new String[]{
            // 0-7
            Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty,
            // 8-15
            Empty, Blank, Blank, Empty, Blank, Empty, Empty, Empty,
            // 16-23
            Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty,
            // 24-31
            Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty,
            // 32-39
            Empty, "!", "\"", "#", "$", "%", "&", "'",
            // 40-47
            "(", ")", "*", "+", ",", "-", ".", "/",
            // 48-55
            Digit, Digit, Digit, Digit, Digit, Digit, Digit, Digit,
            // 56-63
            Digit, Digit, ":", ";", "<", "=", ">", "?",
            // 64-71
            "@", Upper, Upper, Upper, Upper, Upper, Upper, Upper,
            // 72-79
            Upper, Upper, Upper, Upper, Upper, Upper, Upper, Upper,
            // 80-87
            Upper, Upper, Upper, Upper, Upper, Upper, Upper, Upper,
            // 88-95
            Upper, Upper, Upper, "[", "\\", "]", "^", "_",
            // 96-103
            "`", Lower, Lower, Lower, Lower, Lower, Lower, Lower,
            // 104-111
            Lower, Lower, Lower, Lower, Lower, Lower, Lower, Lower,
            // 112-119
            Lower, Lower, Lower, Lower, Lower, Lower, Lower, Lower,
            // 120-127
            Lower, Lower, Lower, "{", "|", "}", "~", Empty
    };
    
    static String Letter = "[a-zA-z]";
    static String Word = "\\w";
    static String Extend = "[\\x80-\\xff]";
    static String Chinese = "[\\x4e00-\\x9fa5]";
    static String Line = ".";
    static String All = "[\\s\\S]";
    
    static Map<String, String> levelTree = new HashMap<>();
    
    static {
        levelTree.put(Digit, "\\d");
        levelTree.put(Lower, "[a-z]");
        levelTree.put(Upper, "[A-Z]");
        levelTree.put(Blank, "\\s");
        for (String c : ".\\?*+|{}()[]".split("")) {
            levelTree.put(c, "\\" + c);
        }
    }
    
    
    public static Generalizer of(Node node, GeneratorConfig config) {
        return new Generalizer(node, config);
    }
    
    private Generalizer(Node node, GeneratorConfig config) {
        this.node = node;
        Generalizer.config = config;
    }
    
    private Stack<Wrapper> stack = new Stack<>();
    
    /**
     * 泛化
     *
     * @return 泛化后的正则
     **/
    public String generalize() {
        return this.doGeneralize(this.node);
    }
    
    private String doGeneralize(Node target) {
        final Wrapper wrapper = Wrapper.of(target, null);
        stack.push(wrapper);
        
        while (!stack.isEmpty()) {
            final Wrapper w = stack.pop();
            if (!w.isFinished()) {
                stack.push(w);
                final Node child = w.getChild();
                stack.push(Wrapper.of(child, w));
                continue;
            }
            
            final Node node = w.getNode();
            final Item item = new Item();
            item.clzz = node.getClass();
            item.times = node.getMetaInfo().getMinMaxTimes();
            item.lens = node.getMetaInfo().getMinMaxLen();
            item.level = Level.LEVEL_0;
            w.item = item;
            if (w.parent != null) {
                w.parent.children.add(w);
            }
            
            if (node.isLeaf()) {
                item.regex = ((Leaf) node).getToken().getTok();
            } else if (node.isExtendsOf(Branches.class)) {
                // calc children's item
                if (w.children.size() > config.getLevelUpBranchNum()) {
                    item.level = Level.LEVEL_1;
                }
                if (item.times.same()) {
                    item.level = Level.LEVEL_0;
                }
            }
        }
        
        Level level = wrapper.item.level;
        wrapper.generalize(level);
        return wrapper.getItem().regex;
    }
    
    
    @Data
    public static class Wrapper {
        private Node node;
        private Wrapper parent;
        private List<Wrapper> children;
        private Item item;
        private int curr;
        private int size;
        /**
         * 直接使用item.regex,而不是通过generalize方法计算获取item.regex
         */
        private boolean useRegex = false;
        
        public static Wrapper of(Node node, Wrapper parent) {
            final Wrapper wrapper = new Wrapper();
            wrapper.node = node;
            wrapper.parent = parent;
            wrapper.children = new ArrayList<>();
            if (!node.isLeaf()) {
                wrapper.size = ((NonLeaf) node).children().size();
            }
            return wrapper;
        }
        
        public boolean isRoot() {
            return parent == null;
        }
        
        public boolean isFinished() {
            return curr >= size;
        }
        
        public Node getChild() {
            return ((NonLeaf) node).children().get(curr++);
        }
        
        private void generalize(Level currLevel) {
            if (node.isLeaf()) {
                item.charLevelUp(currLevel);
                return;
            }
            for (Wrapper wc : children) {
                if (!wc.useRegex) {
                    wc.generalize(Level.selectHigherLevel(currLevel, wc.item.level));
                }
            }
            
            if (node.isExtendsOf(Branches.class)) {
                if (this.children.size() == 1) {
                    item.regex = this.children.get(0).item.regex;
                } else {
                    List<String> list = this.children.stream()
                            .map(Wrapper::getItem)
                            .map(Item::getRegex)
                            .collect(Collectors.toList());
                    distinct(list);
                    String groupPrefix = config.isUseCapturedGroup() ? "(" : "(?:";
                    item.regex = list.stream()
                            .collect(Collectors.joining("|", groupPrefix, ")"));
                }
            } else {
                // Sequence
                if (node.isExtendsOf(Id.class)) {
                    IntTuple lens = item.getLens();
                    item.regex = wildcard(Word, lens);
                    this.useRegex = true;
                }
                if (node.isExtendsOf(Numbers.class)) {
                    IntTuple lens = item.getLens();
                    item.regex = wildcard(Digit, lens);
                    this.useRegex = true;
                } else if (this.children.size() == 1) {
                    item.regex = this.children.get(0).item.regex;
                } else {
                    List<String> list = this.children.stream()
                            .map(Wrapper::getItem)
                            .map(Item::getRegex)
                            .collect(Collectors.toList());
                    distinct(list);
                    item.regex = list.stream().collect(Collectors.joining());
                }
            }
        }
        
        private String wildcard(String re, IntTuple lens) {
            Integer min = lens.getM(), max = lens.getN();
            if (lens.same()) {
                return String.format("%s{%d}", re, min);
            } else {
                char wildcard = min == 0 ? '*' : min == 1 ? '+' : '\0';
                if (wildcard != '\0' && max - min > config.getWildcardMinInterval()) {
                    return String.format("%s%s", re, wildcard);
                } else {
                    return String.format("%s{%d,%d}", re, min, max);
                }
            }
        }
        
        public static void distinct(List<String> list) {
            if (list.isEmpty()) {
                return;
            }
            int index = 0, count = 1;
            String curr = list.remove(0);
            while (index < list.size()) {
                String next = list.remove(index);
                if (curr.equals(next)) {
                    count++;
                    continue;
                } else {
                    if (count == 1) {
                        list.add(index, curr);
                    } else {
                        list.add(index, curr + "{" + count + "}");
                    }
                    curr = next;
                    index++;
                    count = 1;
                }
            }
            if (count == 1) {
                list.add(index, curr);
            } else {
                list.add(index, curr + "{" + count + "}");
            }
        }
        
        @Override
        public String toString() {
            return this.getClass().getSimpleName() + "{" + this.item + "}";
        }
    }
    
    @Data
    static class Item {
        private Class clzz;
        private Level level;
        private String regex;
        private IntTuple times;
        public IntTuple lens;
        
        public void charLevelUp(Level currLevel) {
            String ch = regex;
            if (level.ordinal() >= currLevel.ordinal()) {
                return;
            }
            if (level.ordinal() < currLevel.ordinal() - 1) {
                charLevelUp(Level.higherLevel(level));
            }
            switch (currLevel) {
                case LEVEL_0:
                    regex = "";
                    if (regex != null && regex.length() > 0) {
                        final char c = regex.charAt(0);
                        if (c <= 127) {
                            regex = table[c];
                        }
                    }
                    break;
                case LEVEL_1:
                    // 如 a-z
                    if (regex != null && regex.length() > 0) {
                        final char c = regex.charAt(0);
                        if (c <= 127) {
                            regex = table[c];
                        } else if (c <= 256) {
                            regex = Extend;
                        } else if (c >= 0x4e00 && c <= 0x9fa5) {
                            regex = Chinese;
                        }
                    }
                    
                    // [a-z]
                    if (regex != null) {
                        String rr = levelTree.get(regex);
                        if (rr != null && rr.length() > 0 && !regex.equals(rr)) {
                            if (rr.contains(regex)) {
                                regex = rr;
                            } else {
                                if (regex.equals(Lower) && rr.equals(Upper)) {
                                    regex = Letter;
                                } else if (regex.equals(Digit) || regex.contains("_")) {
                                    regex = Word;
                                } else if (rr.contains("\r") || rr.contains("\n")) {
                                    regex = All;
                                } else {
                                    regex = Line;
                                }
                            }
                        }
                    } else {
                        regex = "";
                    }
                    break;
                default:
                    regex = ".";
                    break;
            }
        }
        
        @Override
        public String toString() {
            return "Item{" +
                    "level=" + level +
                    ", regex=" + regex +
                    ", times=" + times +
                    '}';
        }
    }
    
    enum Level {
        LEVEL_0,
        LEVEL_1,
        LEVEL_2,
        LEVEL_3,
        
        ;
        
        public static Level selectHigherLevel(Level level, Level level2) {
            if (level.ordinal() > level2.ordinal()) {
                return level;
            }
            return level2;
        }
        
        public static Level higherLevel(Level level) {
            return from(level.ordinal() + 1);
        }
        
        public static Level from(int level) {
            for (Level l : values()) {
                if (l.ordinal() == level) {
                    return l;
                }
            }
            return level > LEVEL_3.ordinal() ? LEVEL_3 : LEVEL_0;
        }
    }
}
