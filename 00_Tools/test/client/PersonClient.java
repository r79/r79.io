package client;

import java.lang.reflect.Field;

import log.MyLogger;

import org.apache.log4j.Logger;

import pojo.Person;
import annotations.Datenfeld;

public class PersonClient {
	public static void main(String[] args) {
		final Logger logger = Logger.getLogger(MyLogger.class);

		Person p = new Person();
		Field[] fields = p.getClass().getDeclaredFields();
		for (Field field : fields) {
			Datenfeld datenfeld = field.getAnnotation(Datenfeld.class);
			if (datenfeld != null) {
				logger.info("\nannotiertes Attribut: " + field.getName());
				if (datenfeld.bezeichnung().length() != 0) {
					logger.info("\tBEZ = " + datenfeld.bezeichnung());
				}

				logger.info("\tTYP = " + datenfeld.datentyp());

			}
		}
	}
}
