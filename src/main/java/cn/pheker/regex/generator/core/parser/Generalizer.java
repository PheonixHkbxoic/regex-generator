package cn.pheker.regex.generator.core.parser;

import cn.pheker.regex.generator.core.parser.abstracts.Leaf;
import cn.pheker.regex.generator.core.parser.abstracts.NonLeaf;
import cn.pheker.regex.generator.core.parser.interfaces.Node;
import cn.pheker.regex.generator.core.parser.nodes.Branches;
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

    static {
        Map<String, String> levelTree = new HashMap<>();
        levelTree.put(Digit, "\\d");
        levelTree.put(Lower, "[a-z]");
        levelTree.put(Upper, "[A-Z]");
        levelTree.put("_", "[a-zA-z]");
        levelTree.put(Blank, "\\s");
        levelTree.put("", ".");
        for (String c : ".\\?*+|{}()[]".split("")) {
            levelTree.put(c, "\\" + c);
        }
    }


    public static Generalizer of(Node node, GeneratorConfig config) {
        return new Generalizer(node, config);
    }

    private Generalizer(Node node, GeneratorConfig config) {
        this.node = node;
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
        wrapper.setLevel(Level.LEVEL_1);
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
            item.times = node.getMetaInfo().getMinMaxTimes();
            item.level = Level.LEVEL_0;
            w.item = item;
            if (w.parent != null) {
                w.parent.children.add(w);
            }

            if (node.isLeaf()) {
                item.regex = ((Leaf) node).getToken().getTok();
                w.generalize(item.level);
            } else if (node.isExtendsOf(Branches.class)) {
                // calc children's item
                item.level = Level.LEVEL_1;
                w.generalize(item.level);
                if (w.children.size() == 1) {
                    item.regex = w.children.get(0).item.regex;
                } else {
                    item.regex = w.children.stream()
                            .map(Wrapper::getItem)
                            .map(Item::getRegex)
                            .collect(Collectors.joining("|", "(?:", ")"));
                }
            } else {
                // Sequence
                item.level = item.times.same() ? Level.LEVEL_0 : Level.LEVEL_1;
                w.generalize(item.level);
                if (w.children.size() == 1) {
                    item.regex = w.children.get(0).item.regex;
                } else {
                    item.regex = w.children.stream()
                            .map(Wrapper::getItem)
                            .map(Item::getRegex)
                            .collect(Collectors.joining());
                }
            }
        }

        return wrapper.getItem().regex;
    }


    @Data
    static class Wrapper {
        private Node node;
        private Level level;
        private Wrapper parent;
        private List<Wrapper> children;
        private Item item;
        private int curr;
        private int size;

        public static Wrapper of(Node node, Wrapper parent) {
            final Wrapper wrapper = new Wrapper();
            wrapper.node = node;
            wrapper.level = Level.LEVEL_0;
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
                wc.generalize(currLevel);
            }
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + "{" + this.item + "}";
        }
    }

    @Data
    static class Item {
        private Level level;
        private String regex;
        private IntTuple times;

        public void charLevelUp(Level currLevel) {
            String ch = regex;
            if (level.ordinal() >= currLevel.ordinal()) {
                return;
            }
            if (level.ordinal() < currLevel.ordinal() - 1) {
                charLevelUp(Level.higherLevel(level));
            }
            switch (level) {
                case LEVEL_0:
                    regex = regex == null || regex.length() == 0 ? "" : table[regex.charAt(0)];
                    break;
                case LEVEL_1:
                    if (ch.contains(Digit) || ch.contains(Lower)
                            || ch.contains(Upper) || ch.contains("_")) {
                        regex = "\\w";
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
                    ", regex=" + regex  +
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
