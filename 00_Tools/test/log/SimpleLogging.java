package log;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import pojo.Person;

public class SimpleLogging {
	// defines static Logger
	// "SimpleLogging" references Logger Instance
	static Logger logger = Logger.getLogger(SimpleLogging.class.getName());

	public static void main(String[] args) {
		// configures Logger from log4j.properties
		PropertyConfigurator.configure("properties/log4j.properties");

		// configures Logger from log4j.xml
		PropertyConfigurator.configure("log4j/log4j.xml");

		logger.info("entering Application");
		Person p = new Person();
		p.doIt();
		logger.info("leaving Application");
	}
}