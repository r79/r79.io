package log;

import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class MyLogger {

	// private static Logger logger = Logger.getRootLogger();
	static Logger logger = Logger.getLogger(MyLogger.class);

	private MyLogger() {
		try {
			PatternLayout layout = new PatternLayout(
					"%d{ISO8601} %-5p [%t] %c: %m%n");
			ConsoleAppender consoleAppender = new ConsoleAppender(layout);
			logger.addAppender(consoleAppender);
			FileAppender fileAppender = new FileAppender(layout,
					"log/mylog.log", true);
			logger.addAppender(fileAppender);
			// ALL | DEBUG | INFO | WARN | ERROR | FATAL | OFF:
			logger.setLevel(Level.ERROR);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}
}
