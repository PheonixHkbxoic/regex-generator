package cn.pheker.regex.generator.core.scanner;

import cn.pheker.regex.generator.core.scanner.abstracts.AbstractScanner;
import cn.pheker.regex.generator.core.scanner.abstracts.MultiScanner;
import cn.pheker.regex.generator.core.scanner.abstracts.Paragraphs;
import cn.pheker.regex.generator.core.scanner.abstracts.Scanner;
import cn.pheker.regex.generator.misc.IntTuple;
import cn.pheker.regex.generator.util.StrUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cn.pheker
 * @version 1.0
 * @date 2022/8/10 10:14
 * 从文本读取每个段落作为一个独立的扫描单元
 */
public class StringParagraphsScanner extends AbstractScanner implements Paragraphs, MultiScanner {
    final char SEP = '+';
    final int SEP_MIN_LEN = 5;
    
    boolean trim;
    
    List<Seg> segList;
    int index = 0;
    
    public StringParagraphsScanner(String text) {
        this(text, true);
    }
    
    public StringParagraphsScanner(String text, boolean trim) {
        this.trim = trim;
        this.segList = splitParagraphs(text);
    }
    
    
    @Override
    public String readParagraph() {
        return segList.get(index).text;
    }
    
    @Override
    public List<String> readParagraphs() {
        return segList.stream()
                .filter(seg -> seg.type == SegType.PARAGRAPH)
                .map(seg -> seg.text)
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean hasNext() {
        return index < segList.size();
    }
    
    @Override
    public Scanner next() {
        String p = readParagraph();
        int offset = 0, offsetRow = 0;
        for (int i = 0; i < index; i++) {
            offset += segList.get(i).text.length();
            offsetRow += segList.get(i).rows;
        }
        
        // 查找下一个段落索引
        int index = this.index;
        this.index = segList.size();
        for (int i = index + 1; i < segList.size(); i++) {
            if (segList.get(i).type == SegType.PARAGRAPH) {
                this.index = i;
                break;
            }
        }
        
        if (trim) {
            final Integer m = segList.get(index).valid.getM();
            if (m > 0) {
                final String blankHead = p.substring(0, m);
                for (char c : blankHead.toCharArray()) {
                    if (c == '\n') {
                        offsetRow++;
                    }
                }
            }
            p = p.trim();
        }
        return new StringScanner(offset, offsetRow, p);
    }
    
    @Override
    public int read() {
        throw new RuntimeException("StringParagraphsScanner did not support read method");
    }
    
    
    /**
     * 段
     **/
    static class Seg {
        /* 段类型  */
        SegType type;
        /* 文本 */
        String text;
        /* 有效偏移 不包括开头与结束空白字符的 起止位置 */
        IntTuple valid;
        int rows;
    }
    
    /**
     * 段类型
     **/
    enum SegType {
        /**
         * 段落
         **/
        PARAGRAPH,
        
        /**
         * 分隔符
         **/
        SEPARATOR
    }
    
    public List<Seg> splitParagraphs(String text) {
        final int len = text.length();
        
        List<Seg> segList = new ArrayList<>();
        Seg seg = new Seg();
        
        int plus = 0;
        boolean foundLineValidChar = false;
        int blankHead = 0;
        int blankTail = 0;
        StringBuilder segText = new StringBuilder();
        int rows = 0;
        for (int i = 0; i < len; i++) {
            final char c = text.charAt(i);
            if (c == SEP) {
                plus++;
            } else if (plus >= SEP_MIN_LEN) {
                plus++;
                if (c == '\n') {
                    if (segText.length() != 0) {
                        seg.type = SegType.PARAGRAPH;
                        seg.text = segText.toString();
                        seg.valid = new IntTuple(blankHead, segText.length() - blankTail);
                        seg.rows = rows;
                        rows = 0;
                        segList.add(seg);
                        seg = new Seg();
                        segText = new StringBuilder();
                        blankHead = 0;
                        blankTail = 0;
                    }
    
                    seg.type = SegType.SEPARATOR;
                    seg.text = text.substring(i + 1 - plus, i + 1);
                    seg.rows = 1;
                    plus = 0;
                    segList.add(seg);
                    seg = new Seg();
                    foundLineValidChar = false;
                }
            } else {
                if (c == ' ' || c == '\t' || c == '\r' || c == '\n') {
                    if (!foundLineValidChar) {
                        blankHead += 1;
                    } else {
                        blankTail += 1;
                    }
                    if (c == '\n') {
                        rows++;
                    }
                } else {
                    foundLineValidChar = true;
                    blankTail = 0;
                }
                segText.append(c);
            }
        }
        
        // 收尾
        if (plus >= SEP_MIN_LEN) {
            if (segText.length() != 0) {
                seg.type = SegType.PARAGRAPH;
                seg.text = segText.toString();
                seg.valid = new IntTuple(blankHead, segText.length() - blankTail);
                seg.rows = rows;
                segList.add(seg);
                seg = new Seg();
            }
    
            seg.text = text.substring(len - plus, len);
            seg.type = SegType.SEPARATOR;
            seg.rows = 1;
            segList.add(seg);
        } else if (plus > 0) {
            segText.append(StrUtil.times(plus, "+"));
        }
        
        rows++;
        if (segText.length() != 0) {
            seg.type = SegType.PARAGRAPH;
            seg.text = segText.toString();
            seg.valid = new IntTuple(blankHead, segText.length() - blankTail);
            seg.rows = rows;
            segList.add(seg);
        }
        
        return segList;
    }
}
