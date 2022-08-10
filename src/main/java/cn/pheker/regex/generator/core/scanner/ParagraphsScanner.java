package cn.pheker.regex.generator.core.scanner;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cn.pheker
 * @version 1.0
 * @date 2022/8/10 10:14
 * 从文件读取每个段落作为一个独立的扫描单元
 */
public class ParagraphsScanner implements FileScanner, Paragraphs, MultiScanner {
    String txtPath;
    BufferedReader reader;
    List<String> paragraphs;
    int index = 0;
    boolean eof = false;

    final int bufLen = 1024;
    final char[] buf = new char[bufLen];

    boolean start = true;
    /**
     * buf实际字符数量
     */
    int len = 0;
    /**
     * 跨buf 分隔符+号 数量
     */
    int plus = 0;

    /**
     * 本次读取时 buf的偏移位置, 如果上一次读取还有剩余,则offset不会为0
     */
    int offset = 0;

    /**
     * 正在处理分隔符后面的空白字符
     **/
    boolean pendingAfterPlus = false;


    public ParagraphsScanner(String txtPath) {
        setFilePath(txtPath);
    }

    @Override
    public void setFilePath(String path) {
        this.txtPath = path;
        init();
    }

    @Override
    public int read() {
        throw new RuntimeException("ParagraphsScanner did not support read method");
    }

    private void init() {
        try {
            final FileReader reader = new FileReader(txtPath);
            this.reader = new BufferedReader(reader);

            this.paragraphs = new ArrayList<>();
            this.index = 0;
            this.eof = false;

            this.start = true;
            this.len = 0;
            this.plus = 0;
            this.offset = 0;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private int offsetSpace(int len) {
        int offset = 0;
        for (int i = 0; i < len; i++) {
            final char c = buf[i];
            if (c == ' ' || c == '\t' || c == '\r' || c == '\n') {
                offset++;
                continue;
            }
            break;
        }
        return offset;
    }

    private int offsetSpaceAfterPlus(int i, int len) {
        this.pendingAfterPlus = true;
        while (i < len) {
            char c = buf[i];
            if (c == '\n') {
                this.pendingAfterPlus = false;
                return ++i;
            }else{
                i++;
            }
        }
        return i;
    }


    @Override
    public String readParagraph() {
        return paragraphs.get(index);
    }

    @Override
    public List<String> readParagraphs() {
        while (!eof) {
            doReadParagraph();
        }
        return paragraphs;
    }

    public void doReadParagraph() {
        StringBuilder paragraph = new StringBuilder();

        try {
            OUTER:
            while (true) {
                if (offset == 0) {
                    len = reader.read(buf, 0, bufLen);
                    // 跳过文件开头的空白字符
                    if (start) {
                        offset = offsetSpace(len);
                        if (offset == len) {
                            offset = 0;
                            continue;
                        }
                        start = false;
                    }else if (pendingAfterPlus){
                        offset = offsetSpaceAfterPlus(0, len);
                    }
                }

                // 分隔符 分割为段落
                int bufPlus = 0;
                for (int i = offset; i < len; i++) {
                    char c = buf[i];
                    if (c == '+') {
                        plus++;
                        bufPlus++;
                    } else if (plus >= 5) {
                        paragraph.append(buf, offset, i - bufPlus - offset);
                        paragraphs.add(paragraph.toString());
                        paragraph = new StringBuilder();
                        i = offsetSpaceAfterPlus(i, len);
                        plus = 0;
                        offset = i;
                        break OUTER;
                    } else {
                        plus = 0;
                        bufPlus = 0;
                    }
                }


                plus = 0;
                bufPlus = 0;
                final int restLen = len - bufPlus - offset;
                if (restLen > 0) {
                    paragraph.append(buf, offset, restLen);
                }

                // 缓存没读满 说明文件已结束
                if (len < bufLen) {
                    eof = true;
                    break;
                }
            }

            if (paragraph.length() > 0) {
                paragraphs.add(paragraph.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean hasNext() {
        if (!eof) {
            doReadParagraph();
        }
        return index < paragraphs.size();
    }

    @Override
    public Scanner next() {
        final String p = readParagraph();
        index++;
        return new StringScanner(p);
    }

}
