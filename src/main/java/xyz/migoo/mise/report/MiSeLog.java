package xyz.migoo.mise.report;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xiaomi
 * @date 2019/8/15 15:03
 */
public class MiSeLog {

    private static final Logger LOGGER = LoggerFactory.getLogger("MiSe-Logger");

    public static void log(String message) {
        LOGGER.info(message);
    }

    public static void log(String message, Object... msg) {
        LOGGER.info(message, msg);
    }

    public static void log(String message, Throwable t) {
        LOGGER.error(message, t);
    }

    public static void debug(String message) {
        LOGGER.debug(message);
    }

    public static void debug(String message, Object... msg) {
        LOGGER.debug(message, msg);
    }
}