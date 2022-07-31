package cn.pheker.regex.generator.core.parser;

import cn.pheker.regex.generator.core.parser.model.Model;
import cn.pheker.regex.generator.core.parser.strategy.Strategy;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/7/27 0:15
 * @desc
 */
public class Generator {
    Model model;
    Strategy strategy;
    
    public static Generator of(Model model) {
        return new Generator();
    }
    
    public String gen() {
        return null;
    }
    
    
}
