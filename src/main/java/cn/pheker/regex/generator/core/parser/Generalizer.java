package cn.pheker.regex.generator.core.parser;

import cn.pheker.regex.generator.core.parser.abstracts.Leaf;
import cn.pheker.regex.generator.core.parser.abstracts.NonLeaf;
import cn.pheker.regex.generator.core.parser.interfaces.Node;
import cn.pheker.regex.generator.core.parser.nodes.Branches;
import cn.pheker.regex.generator.core.parser.nodes.Id;
import cn.pheker.regex.generator.core.parser.nodes.Numbers;
import cn.pheker.regex.generator.core.parser.nodes.Sequence;
import cn.pheker.regex.generator.misc.IntTuple;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/8/7 13:03
 * @desc 泛化器
 */
@Slf4j
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
            Empty, "\\t", "\\n", Empty, Blank, "\\r", Empty, Empty,
            // 16-23
            Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty,
            // 24-31
            Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty,
            // 32-39
            Empty, "!", "\"", "#", "\\$", "%", "&", "'",
            // 40-47
            "\\(", "\\)", "\\*", "\\+", ",", "\\-", "\\.", "\\/",
            // 48-55
            Digit, Digit, Digit, Digit, Digit, Digit, Digit, Digit,
            // 56-63
            Digit, Digit, ":", ";", "<", "=", ">", "\\?",
            // 64-71
            "@", Upper, Upper, Upper, Upper, Upper, Upper, Upper,
            // 72-79
            Upper, Upper, Upper, Upper, Upper, Upper, Upper, Upper,
            // 80-87
            Upper, Upper, Upper, Upper, Upper, Upper, Upper, Upper,
            // 88-95
            Upper, Upper, Upper, "\\[", "\\\\", "\\]", "^", "_",
            // 96-103
            "`", Lower, Lower, Lower, Lower, Lower, Lower, Lower,
            // 104-111
            Lower, Lower, Lower, Lower, Lower, Lower, Lower, Lower,
            // 112-119
            Lower, Lower, Lower, Lower, Lower, Lower, Lower, Lower,
            // 120-127
            Lower, Lower, Lower, "\\{", "\\|", "\\}", "~", Empty
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
        levelTree.put("\t", "\\t");
        levelTree.put("\r", "\\r");
        levelTree.put("\n", "\\n");
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

        Level rootLevel = wrapper.item.level;
        Level defaultLevel = Level.from(config.getMode().ordinal());
        Level level = Level.selectHigherLevel(rootLevel, defaultLevel);
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
                // 查找父级链里最近的Branches
                Wrapper curr = this;
                while (!curr.node.isExtendsOf(Branches.class)
                        && (curr = curr.getParent()) != null) {
                }

                // 特殊情况 叶子节点如果在低级分支中占比较高时 不泛化
                if (curr != null && curr != this) {
                    // Single在Branches中占比大于0.8时 不进行泛化
                    final Integer times = curr.getItem().getTimes().getM();
                    final Integer leafTimes = this.getItem().getTimes().getN();
                    if (leafTimes * 1.0f / times >= 0.7) {
                        // 即使不泛化, 但元字符还是要转义的
                        final String escapeSingle = levelTree.get(((Leaf) node).getToken().getTok());
                        if (escapeSingle != null && escapeSingle.length() > 0) {
                            item.regex = escapeSingle;
                        }
                        return;
                    }
                }

                // 字符泛化
                item.charLevelUp(currLevel);
                return;
            }

            // 剪枝
            this.prune();

            // 子节点泛化
            for (Wrapper wc : children) {
                if (!wc.useRegex) {
                    wc.generalize(Level.selectHigherLevel(currLevel, wc.item.level));
                }
            }

            // Branches或Sequence的子节点合并
            // 分支通过|合并,且可能会用(?:)或()包裹
            if (node.isExtendsOf(Branches.class)) {
                List<String> list = this.children.stream()
                        .map(Wrapper::getItem)
                        .map(Item::getRegex)
                        .collect(Collectors.toList());
                distinct(list, true);
                // 只有一个节点时 不使用(?:)或()
                final String first = list.get(0);
                if (list.size() == 1) {
                    item.regex = first;
                } else {
                    // 存在空分支Empty 则正则加? 表示可以没有
                    if (list.contains(Empty)) {
                        list.remove(Empty);
                        // 移到末尾
                        if (!config.isConvertBlankBranch()) {
                            list.add(Empty);
                        }

                        String groupPrefix = config.isUseCapturedGroup() ? "(" : "(?:";
                        item.regex = list.stream()
                                .collect(Collectors.joining("|", groupPrefix, ")"));
                        // 单分支 单字符 不用组
                        if (list.size() == 1
                                && (first.length() == 1 || first.length() == 2 && first.charAt(0) == '\\')
                        ) {
                            item.regex = first;
                        }
                        if (config.isConvertBlankBranch()) {
                            item.regex += "?";
                        }
                        // 位于最外层时 不使用(?:)或()
                    } else if (parent == null) {
                        item.regex = String.join("|", list);
                    } else {
                        String groupPrefix = config.isUseCapturedGroup() ? "(" : "(?:";
                        item.regex = list.stream()
                                .collect(Collectors.joining("|", groupPrefix, ")"));
                    }
                }
            } else {
                // Sequence 串连子节点即可
                if (node.isExtendsOf(Id.class)) {
                    IntTuple lens = item.getLens();
                    item.regex = wildcard(Word, lens);
                    this.useRegex = true;
                }
                if (node.isExtendsOf(Numbers.class)) {
                    IntTuple lens = item.getLens();
                    item.regex = wildcard(Digit, lens);
                    this.useRegex = true;
                } else {
                    List<String> list = this.children.stream()
                            .map(Wrapper::getItem)
                            .map(Item::getRegex)
                            .collect(Collectors.toList());
                    distinct(list, false);
                    if (this.children.size() == 1) {
                        item.regex = list.get(0);
                    } else {
                        item.regex = String.join("", list);
                    }
                }
            }
        }

        /**
         * 对Branches剪枝
         * 条件:
         * 1.分支仅出现一次, 且占所有分支出现次数比率小于容错率(一般为0.2)
         * 2.分支的有子节点,且子节点不能全部为叶子节点
         **/
        private void prune() {
            if (!this.node.isExtendsOf(Branches.class)) {
                return;
            }
            final Integer max = item.times.getN();
            final Iterator<Wrapper> ite = children.iterator();
            while (ite.hasNext()) {
                final Wrapper w = ite.next();
                final int branchMax = w.getItem().getTimes().getN();
                if (branchMax < 2 && branchMax * 1.0f / max <= config.getFaultRate()) {
                    // 分支不是Sequence节点,或者Sequence分支的所有子节点不全部为叶子节点
                    if (!w.node.isExtendsOf(Sequence.class)
                            || !((Sequence) w.node).children().stream().allMatch(Node::isLeaf)) {
                        ite.remove();
                        log.info("prune-wrapper: {}", w);
                    }
                }
            }
        }

        public static String wildcard(String re, IntTuple lens) {
            Integer min = lens.getM(), max = lens.getN();
            if (lens.same()) {
                if (min == 1) {
                    return re;
                }
                return String.format("%s{%d}", re, min);
            } else {
                char wildcard = min == 0 ? '*' : min == 1 ? '+' : '\0';
                if (wildcard != '\0' && max - min > (config == null ? 3 : config.getWildcardMinInterval())) {
                    return String.format("%s%s", re, wildcard);
                } else {
                    return String.format("%s{%d,%d}", re, min, max);
                }
            }
        }

        /**
         * 对list中相似的正则 压缩合并
         *
         * @param isBranch 是否是分支,
         *                 如果是分支
         *                 相同的会忽略 如:\d{2} \d{2}会取其一
         *                 相似的取最大范围 如:\d{2,4} \d{3,6}会变成\d{2,6}
         *                 如果是Sequence
         *                 相同的会累加 如:\d{2} \d{2}会变成\d{4}
         *                 相似的会累加后取最大范围 如:\d{2,4} \d{3,6}会变成\d{5,10}
         **/
        public static void distinct(List<String> list, boolean isBranch) {
            int index = 0;
            String curr = null;
            int min = 1, max = 1;

            while (index < list.size()) {
                // 跳过组()和(?:) 以防止按{}分割时 处理错乱
                if (list.get(index).contains("(")) {
                    if (curr != null) {
                        list.add(index, wildcard(curr, new IntTuple(min, max)));
                        index += 2;
                        curr = null;
                        min = 1;
                        max = 1;
                    } else {
                        index++;
                    }
                    continue;
                }

                if (curr == null) {
                    String tmpStr = list.remove(index);
                    final String[] segs = tmpStr.split("[{}]");
                    IntTuple times = segs.length > 1 ? range(segs[1]) : new IntTuple(1, 1);
                    min = times.getM();
                    max = times.getN();
                    curr = segs[0];
                    continue;
                }

                String next = list.remove(index);
                final String[] tmpSegs = next.split("[{}]");
                String nextTmp = tmpSegs[0];
                final IntTuple tmpTimes = tmpSegs.length > 1 ? range(tmpSegs[1]) : new IntTuple(1, 1);
                // 不同类型 则保存并开始新一轮合并去重 如: [a-z]{3}与\\d{4}
                if (!nextTmp.equals(curr)) {
                    list.add(index, wildcard(curr, new IntTuple(min, max)));
                    curr = nextTmp;
                    index++;
                    min = tmpTimes.getM();
                    max = tmpTimes.getN();
                    continue;
                }

                // branches [a-z]{3} [a-z]{6} == [a-z]{3,6}
                if (isBranch) {
                    // 相似的取最大范围 如:\d{2,4} \d{3,6}会变成\d{2,6}
                    min = Math.min(min, tmpTimes.getM());
                    max = Math.max(max, tmpTimes.getN());
                } else {
                    // 相似的会累加后取最大范围 如:\d{2,4} \d{3,6}会变成\d{5,10}
                    min = min + tmpTimes.getM();
                    max = max + tmpTimes.getN();
                }
            }

            // 处理最后的
            if (curr != null) {
                list.add(index, wildcard(curr, new IntTuple(min, max)));
            }
        }

        /**
         * 解析数字区间字符串
         * 如: 5或5,6
         *
         * @return 返回数字区间, 如: [5,5]或[5,6]
         **/
        public static IntTuple range(String range) {
            final String[] minMax = range.split(",");
            int min = Integer.parseInt(minMax[0]), max = min;
            if (minMax.length == 2) {
                max = Integer.parseInt(minMax[1]);
            }
            return new IntTuple(min, max);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + "{node=" + this.node + ",item=" + this.item + "}";
        }
    }

    @Data
    static class Item {
        private Class<?> clzz;
        private Level level;
        private String regex;
        private IntTuple times;
        public IntTuple lens;

        // 128
        private static final int MAX_ASCII = 0x80;
        // 256
        private static final int MAX_ASCII_EXTEND = 0xFF;

        public void charLevelUp(Level currLevel) {
            String chs = regex;
            if (level.ordinal() >= currLevel.ordinal()) {
                return;
            }
            if (level.ordinal() < currLevel.ordinal() - 1) {
                charLevelUp(Level.higherLevel(level));
            }
            switch (currLevel) {
                case LEVEL_0:
                    if (chs != null && chs.length() > 0) {
                        final char c = chs.charAt(0);
                        if (c <= MAX_ASCII) {
                            regex = table[c];
                        }
                    }
                    break;
                case LEVEL_1:
                    // 如 a-z
                    if (chs != null && chs.length() > 0) {
                        final char c = chs.charAt(0);
                        if (c <= MAX_ASCII) {
                            regex = table[c];
                        } else if (c <= MAX_ASCII_EXTEND) {
                            regex = Extend;
                        } else if (c >= 0x4e00 && c <= 0x9fa5) {
                            regex = Chinese;
                        }
                    }

                    // [a-z]
                    if (chs != null) {
                        String rr = levelTree.get(chs);
                        if (rr != null && rr.length() > 0 && !chs.equals(rr)) {
                            if (rr.contains(chs)) {
                                regex = rr;
                            } else {
                                if (chs.equals(Lower) && rr.equals(Upper)) {
                                    regex = Letter;
                                } else if (chs.equals(Digit) || chs.contains("_")) {
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
                    ", lens=" + lens +
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
