package cn.pheker.regex.generator.core.scanner;

import cn.pheker.regex.generator.core.scanner.abstracts.AbstractFileScanner;
import cn.pheker.regex.generator.exception.FileException;

import java.io.*;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/7/25 21:28
 * @desc
 */
public class TxtScanner extends AbstractFileScanner {
    protected BufferedReader reader;
    private String cacheLine;
    private int cursor = 0;
    private boolean end = false;

    private final char[] buf = new char[1024];
    int c = 0;
    int e = 0;

    public TxtScanner(String path) {
        this.setFilePath(path);
    }

    private void init() {
        File file = new File(path);
        try {
            FileReader reader = new FileReader(file);
            this.reader = new BufferedReader(reader);
        } catch (FileNotFoundException e) {
            throw new FileException("文件不存在");
        }
    }
    
    @Override
    public void setFilePath(String path) {
        super.setFilePath(path);
        init();
    }


    @Override
    public int read() {
        return readAndCache();
    }
    
    private int readAndCache() {
        if (c < e) {
            return buf[c++];
        }
        try {
            c = 0;
            e = reader.read(buf, 0, 1024);
            if (c < e) {
                return buf[c++];
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return EOF;
    }
    
    private int readLineAndCache() {
        if (cacheLine != null && cursor < cacheLine.length()) {
            return cacheLine.charAt(cursor++);
        }
        
        doReadLineIfAbsent();
        
        if (end || cacheLine == null) {
            this.end = true;
            return EOF;
        }
        
        return cacheLine.charAt(cursor++);
    }
    
    private void doReadLineIfAbsent() {
        int i = 0;
        while (!end && (cacheLine == null
                || cacheLine.length() == 0
                || cursor == cacheLine.length()
        ) && i++ < 10) {
            try {
                cacheLine = reader.readLine();
            } catch (EOFException e) {
                this.end = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            cursor = 0;
        }
    }
}
