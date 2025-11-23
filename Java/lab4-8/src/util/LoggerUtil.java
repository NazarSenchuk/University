package util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerUtil {
    private static final Logger logger = LogManager.getLogger(LoggerUtil.class);
    
    public static void logError(String message, Throwable throwable) {
        logger.error(message, throwable);
    }
    
    public static void logError(String message) {
        logger.error(message);
    }
    
    public static void logInfo(String message) {
        logger.info(message);
    }
    
    public static void logWarn(String message) {
        logger.warn(message);
    }
    
    public static void logDebug(String message) {
        logger.debug(message);
    }
    
    public static Logger getLogger(Class<?> clazz) {
        return LogManager.getLogger(clazz);
    }
}