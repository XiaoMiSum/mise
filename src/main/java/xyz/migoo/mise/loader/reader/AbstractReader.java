package xyz.migoo.mise.loader.reader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author xiaomi
 */
public abstract class AbstractReader {

    protected static final String CLASSPATH = "classpath://";
    protected InputStream inputStream = null;

    /**
     * 文件合法性检查，传入的文件路径不能为 null "" 文件夹
     * @param file 文件
     * @param suffix 指定的文件后缀
     */
    protected void validation(File file, String suffix) throws ReaderException {
        if (!file.exists()){
            throw new ReaderException(String.format("file not found : %s", file.getPath()));
        }
        if (!file.getName().endsWith(suffix)){
            throw new ReaderException(String.format("this file not a ' %s ' file : %s", suffix , file));
        }
        if (file.length() == 0){
            throw new ReaderException("file length == 0");
        }
    }

    protected boolean isClassPath(String path){
        return path.toLowerCase().startsWith(CLASSPATH);
    }

    protected void stream(String suffix, String path) throws ReaderException {
        try {
            if (!isClassPath(path)){
                stream(suffix, new File(path));
            }else {
                path = path.substring(CLASSPATH.length());
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                inputStream = classLoader.getResourceAsStream(path);
            }
        }catch (Exception e){
            throw new ReaderException("file read exception", e);
        }
    }

    protected void stream(String suffix, File file) throws ReaderException {
        try {
            validation(file, suffix);
            inputStream = new BufferedInputStream(new FileInputStream(file));
        }catch (Exception e){
            throw new ReaderException("file read exception: ", e);
        }
    }

    public boolean isNull(){
        return inputStream == null;
    }
}
