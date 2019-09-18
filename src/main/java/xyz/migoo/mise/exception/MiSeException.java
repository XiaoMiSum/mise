package xyz.migoo.mise.exception;

/**
 * @author xiaomi
 * @date 2019/8/14 9:24
 */
public class MiSeException extends RuntimeException {

    public MiSeException(String message){
        super(message);
    }
    public MiSeException(String message, Throwable t){
        super(message, t);
    }
}