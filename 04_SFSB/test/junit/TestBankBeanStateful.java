package junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import bank.Bank;

public class TestBankBeanStateful {

	// initialer Kontext, um nach einem Namen zu suchen
	private static InitialContext ic;

	// Stellvertreter der Business Schnittstelle
	private static Object po;

	// Business Schnittstelle mit den Geschäftsmethoden
	private Bank bank;

	// definiert den InitialContaxt -> einmal am Anfang
	@BeforeClass
	public static void setUp() throws NamingException {
		Properties properties = new Properties();
		properties.setProperty(Context.URL_PKG_PREFIXES,
				"org.jboss.ejb.client.naming");

		ic = new InitialContext(properties);
		assertNotNull(ic);
	}

	// holt das Server Proxy Objekt -> vor jedem Test
	@Before
	public void getEJBProxy() throws NamingException {

		/*
		 * For stateless beans: ejb:<app-name>/ <module-name>/ <distinct-name>/
		 * <bean-name>!<fully-qualified-classname-of-the-remote-interface>
		 * 
		 * For stateful beans: ejb:<app-name>/ <module-name>/ <distinct-name>/
		 * <bean-name>!<fully-qualified-classname-of-the-remote-interface>?stateful
		 */

		// instatziiert den Stellvertreter
		po = ic.lookup("ejb:MyEAR/MyEJB//BankBean!" + Bank.class.getName()
				+ "?stateful");
		assertNotNull(po);

		assertTrue(po instanceof Bank);
		bank = (Bank) po;
		assertNotNull(bank);
		System.out.println("BankProxy : " + bank);
	}

	// neues PortFolio erstellen
	@Test
	public void getAndSaveProxy() throws IOException, ClassNotFoundException {
		// PortFolio Attribut setzen
		bank.setPortFolio("my Portfolio");
		// Handle holen und speichern
		saveBank(bank.getRemoteReference());
	}

	// neue "bank" Instanzen erzeugen
	@Test
	public void instantiateMoreProxies() {
		bank.setPortFolio("my Portfolio 1");
		bank.setPortFolio("my Portfolio 2");
		bank.setPortFolio("my Portfolio 3");
		bank.setPortFolio("my Portfolio 4");
	}

	// Statefull PortFolio testen
	@Test
	public void loadAndTestStatefulProxy() throws ClassNotFoundException,
			IOException {
		// Handle holen
		Bank statefullBankProxy = loadBank();
  // Test PortFolio Name
		assertEquals("my Portfolio", statefullBankProxy.getPortFolio());
	}

	private void saveBank(Object bank) throws IOException {
		// write "bank" Object Reference to serialized File
		FileOutputStream fos = new FileOutputStream("Bank_Reference.ser");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(bank);
		oos.close();
	}

	private Bank loadBank() throws IOException, ClassNotFoundException {
		ObjectInputStream ois = null;
		try {
			// read "bank" Object Reference from File
			FileInputStream fis = new FileInputStream("Bank_Reference.ser");
			ois = new ObjectInputStream(fis);
			return (Bank) ois.readObject();
		} finally {
			ois.close();
		}
	}

	// löst den Stellvertreter
	@After
	public void resetEJBProxy() {
		po = null;
	}

	// Aufräumen (Resscourcen lösen / löschen)
	@AfterClass
	public static void clanUp() {
		try {
			ic.close();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
}