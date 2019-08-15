package xyz.migoo.mise.framework.assertion;

/**
 * @author xiaomi
 * @date 2019-02-20 20:33
 */
public class SkippedRun extends Error {

    public SkippedRun(String message) {
        super(message);
    }

    public SkippedRun(String message, Throwable cause) {
        super(message, cause);
    }

}