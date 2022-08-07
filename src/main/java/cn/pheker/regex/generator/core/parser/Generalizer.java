package cn.pheker.regex.generator.core.parser;

import cn.pheker.regex.generator.core.parser.interfaces.Node;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * @author wanghaijun
 * @version 1.0.0
 * @date 2022/8/7 13:03
 * @desc
 */
public class Generalizer {
    private Node node;
    
    private static String Empty = "";
    private static String Digit = "\\d";
    private static String Upper = "A-Z";
    private static String Lower = "a-z";
    private static String Blank = "\\s";
    private String[] table = new String[]{
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
    
    
    public static Generalizer of(Node node) {
        return new Generalizer(node);
    }
    
    private Generalizer(Node node) {
        this.node = node;
    }
    
    private Stack<Node> stack = new Stack<>();
    
    public List<String> generalize(Node node) {
        boolean first = true;
        while (!stack.isEmpty() || first) {
            if (first) {
                first = false;
            }
            
            
        }
        
        return Collections.emptyList();
    }
}
