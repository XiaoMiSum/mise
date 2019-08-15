package xyz.migoo.mise.exception;

/**
 * @author yacheng.xiao
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