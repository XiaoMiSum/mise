package xyz.migoo.mise.framework.assertion;

/**
 * @author xiaomi
 * @date 2017/7/26 17:23
 */
public class ExecuteError extends Error  {

    public ExecuteError(String message) {
        super(message);
    }

    public ExecuteError(String message, Throwable cause) {
        super(message, cause);
    }

}
