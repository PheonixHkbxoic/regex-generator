package cn.pheker.regex.generator.util;

/**
 * @author cn.pheker
 * @version 1.0.0
 * @date 2022/7/25 22:11
 * @desc
 */
public class ResourceUtil {
    
    /**
     * 通过resources目录下的相对路径, 返回其绝对路径
     *
     * @param resource 文件相对于resources的相对路径
     * @return resource的绝对路径
     */
    public static String getResourcePath(String resource) {
        String path = ResourceUtil.class.getClassLoader().getResource("").getPath();
        if (path.endsWith("test-classes/")) {
            path = path.substring(0, path.length() - 13) + "/classes/" + resource;
        }
        return path;
    }
}
