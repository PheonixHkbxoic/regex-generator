package cn.pheker.regex.generator.core.parser.nodes;

import cn.pheker.regex.generator.core.lexer.Token;
import cn.pheker.regex.generator.core.lexer.TokenType;
import cn.pheker.regex.generator.core.parser.MetaInfo;
import cn.pheker.regex.generator.core.parser.abstracts.AbstractComposite;
import cn.pheker.regex.generator.core.parser.abstracts.Leaf;
import cn.pheker.regex.generator.core.parser.abstracts.NonLeaf;
import cn.pheker.regex.generator.core.parser.interfaces.Node;
import cn.pheker.regex.generator.exception.TooManyRegex;
import cn.pheker.regex.generator.misc.IntTuple;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/7/27 21:52
 * @desc
 */
public class Sequence extends AbstractComposite {
    
    
    public Sequence(NonLeaf parent) {
        super(parent);
    }
    
    @Override
    public String toString() {
        return "Sequence{" + children + '}';
    }
    
    @Override
    public List<String> generateRegex() {
        List<String> regex = null;
        boolean containEmpty = children().removeIf(c -> c.isExtendsOf(Empty.class));
        if (containEmpty) {
            this.add(new Empty(this));
        }
        for (Node child : children()) {
            // 判断mode
            MetaInfo meta = child.getMetaInfo();
            List<String> res = child.generateRegex();
            // accurate
            // mode = 0
    
            int mode = 1;
            // generate
            if (mode == 1) {
                MetaInfo metaInfo = this.getMetaInfo();
                IntTuple minMaxTimes = metaInfo.getMinMaxTimes();
                // 固定不变
                if (minMaxTimes.same() && minMaxTimes.getM() > 1) {
            
                } else {
                    if (this.isLeaf()) {
                        Token token = ((Leaf) child).getToken();
                        res = Collections.singletonList(levelUp(token));
                    } else if (children().stream().allMatch(n -> n.isExtendsOf(Leaf.class))) {
                        res = levelCompress(res);
                    }
                }
                // extreme
            } else if (mode == 2) {
        
            }
            regex = cartesian(regex, res);
            if (regex.size() > 10000) {
                throw new TooManyRegex();
            }
        }
        return regex;
    }
    
    private List<String> levelCompress(List<String> res) {
        return res.stream()
                .map(re -> {
                    String re2 = re;
                    if (re.contains("|")) {
                        Set<String> set = Arrays.stream(re.split("|"))
                                .collect(Collectors.toSet());
                        re2 = String.join("", set);
                    }
                    return re2.replaceAll("\\d+", "\\\\d+")
                            .replaceAll("(?:\\\\[A-Z\\\\])+", "[A-Z]+")
                            .replaceAll("(?:\\\\[a-z\\\\])+", "[a-z]+")
                            .replaceAll("(?:\\\\[\\\\x4e00-\\\\x9fa5\\\\])+", "[\\\\x4e00-\\\\x9fa5]+");
                })
                .collect(Collectors.toList());
    }
    
    public String levelUp(Token token) {
        String re = token.getTok();
        if (token.isTokenType(TokenType.DIGIT)) {
            re = "\\d";
        } else if (token.isTokenType(TokenType.Upper)) {
            re = "[A-Z]";
        } else if (token.isTokenType(TokenType.Lower)) {
            re = "[a-z]";
        } else if (token.isTokenType(TokenType.Other)) {
            re = "[\\x4e00-\\x9fa5]";
        }
        return re;
    }
}
