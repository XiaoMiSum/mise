package xyz.migoo.mise.exception;

/**
 * @author yacheng.xiao
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