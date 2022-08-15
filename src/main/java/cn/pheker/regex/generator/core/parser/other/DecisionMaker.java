package cn.pheker.regex.generator.core.parser.other;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author cn.pheker
 * @version 1.0
 * @date 2022/8/15 14:17
 * 决策者: 判断是否应该泛化子节点还是由本节点处理
 * 1.每种模式会根据BranchesDeep决定是否泛化子节点
 * 2.根据最大深度maxBranchesDeep,动态生成每种模式对应的BranchesDeep
 * 3.规律: 深度越深(范围越大)收缩越快, 深度越浅(范围越小)收缩越慢
 */
public class DecisionMaker {

    private int maxBranchesDeep;
    private Map<Mode, Integer> modeBranches;

    private DecisionMaker(int maxBranchesDeep) {
        this.maxBranchesDeep = maxBranchesDeep;
    }

    public static DecisionMaker of(int maxBranchesDeep) {
        final DecisionMaker dm = new DecisionMaker(maxBranchesDeep);
        dm.init();
        return dm;
    }

    private void init() {
        this.modeBranches = new HashMap<>(4);
        int max = maxBranchesDeep;
        for (Mode mode : Mode.values()) {
            final double rate = max > 100 ? 0.382
                : max > 20 ? 0.5f
                : max > 10 ? 0.618f
                : max > 7 ? 0.73
                : max > 5 ? 0.82
                : max > 3 ? 0.89
                : 0.95;
            max = (int) Math.floor(max * rate);
            modeBranches.put(mode, max);
        }
    }

    /**
     * 动态决策 当前深度时, 在mode模式下是否应该泛化子节点
     *
     * @param mode 当前模式
     * @param deep 当前深度
     * @return true泛化子节点, false不泛化子节点(由本节点处理)
     **/
    public boolean make(Mode mode, int deep) {
        return make(mode, deep, false);
    }

    /**
     * 决策 当前深度时, 在mode模式下是否应该泛化子节点
     *
     * @param mode 当前模式
     * @param deep 当前深度
     * @param fix  是否采用固定决策
     * @return true泛化子节点, false不泛化子节点(由本节点处理)
     **/
    public boolean make(Mode mode, int deep, boolean fix) {
        if (deep == 0) {
            return true;
        }

        if (fix) {
            return Mode.Full == mode && deep < 2
                || Mode.High == mode && deep < 4
                || Mode.Low == mode && deep < 6
                || Mode.Accurate == mode && deep < 8;
        }

        return deep <= modeBranches.get(mode);
    }

    @Override
    public String toString() {
        return "DynamicMode{" +
            "maxBranchesDeep=" + maxBranchesDeep +
            ", modeBranches=" + modeBranches.entrySet().stream()
            .sorted(Comparator.comparingInt(e -> e.getKey().ordinal()))
            .collect(Collectors.toList()) +
            '}';
    }
}
