package pojo;

import generics.Datentyp;
import lombok.Data;

import org.apache.log4j.Logger;

import annotations.Datenfeld;

@SuppressWarnings("serial")
@Data
public class Person implements java.io.Serializable {
	@Datenfeld(bezeichnung = "Nummer", datentyp = Datentyp.NUMMER)
	private int nr;
	@Datenfeld(bezeichnung = "Name", datentyp = Datentyp.TEXT)
	private String name;
	@Datenfeld
	private String beschreibung;

	@Deprecated
	public String sayHello() {
		return "Hello World";
	}

	@Override
	public String toString() {
		return "Hello World";
	}
	
	  static Logger logger = Logger.getLogger(Person.class);

	  public Person(){
	   logger.info("Person konstructed!");
	  }
	  
	  public void doIt() {
	    logger.warn("did it again!");
	  }
}


