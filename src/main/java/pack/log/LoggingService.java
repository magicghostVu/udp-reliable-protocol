package pack.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Fresher on 21/03/2018.
 */
public class LoggingService {


    private Logger logger;

    private static LoggingService ourInstance = new LoggingService();

    public static LoggingService getInstance() {
        return ourInstance;
    }

    private LoggingService() {

        logger = LogManager.getLogger("RollingFileLog");

    }

    public Logger getLogger() {
        return logger;
    }
}
