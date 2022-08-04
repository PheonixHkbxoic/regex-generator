package cn.pheker.regex.generator.core.scanner;

import cn.pheker.regex.generator.exception.FileException;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/7/25 21:28
 * @desc
 */
public class TxtScanner implements FileScanner {
    
    String txtPath;
    protected BufferedReader reader;
    // cache line
    private String cacheLine;
    private int cursor = 0;
    private boolean end = false;
    
    // cache array
    private char[] cbuf = new char[1024];
    int c = 0;
    int e = 0;

    public TxtScanner(String txtPath) {
        this.setFilePath(txtPath);
    }

    private void init() {
        File file = new File(txtPath);
        try {
            FileReader reader = new FileReader(file);
            this.reader = new BufferedReader(reader);
        } catch (FileNotFoundException e) {
            throw new FileException("文件不存在");
        }
    }
    
    @Override
    public void setFilePath(String path) {
        this.txtPath = path;
        init();
    }
    
    @Override
    public int read() {
        return readAndCache();
    }
    
    private int readAndCache() {
        if (c < e) {
            return cbuf[c++];
        }
        try {
            c = 0;
            e = reader.read(cbuf, 0, 1024);
            if (c < e) {
                return cbuf[c++];
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
