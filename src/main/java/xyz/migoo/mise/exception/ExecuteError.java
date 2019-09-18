package xyz.migoo.mise.exception;

/**
 * @author xiaomi
 * @date 2019-08-04 09:22
 */
public class ExecuteError extends Error {

    public ExecuteError(String message) {
        super(message);
    }

    public ExecuteError(String message, Throwable cause) {
        super(message, cause);
    }
}
