package xyz.migoo.mise.loader.reader;

/**
 * @author xiaomi
 * @date 2019/8/14 22:24
 */
public class ReaderException extends Exception {

    public ReaderException(String message){
        super(message);
    }
    public ReaderException(String message, Throwable t){
        super(message, t);
    }
}