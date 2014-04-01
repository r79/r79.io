package junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
import bo.Konto;

public class TestBankBeanWithZins {

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
		po = ic.lookup("ejb:MyEAR/MyEJB//BankBean!" + Bank.class.getName());
		assertNotNull(po);
		System.out.println(po);

		assertTrue(po instanceof Bank);
		bank = (Bank) po;
		assertNotNull(bank);
	}

	// Testet den Zinssatz
	@Test
	public void tellRate() {
		assertEquals(1.25, bank.tellRate(), 0.05);
	}

	// Testet die Zinsberechnung
	@Test
	public void calculateRate() {
		Konto newKonto = new Konto("120-1", "TestKonto", 100.0f, "CHF");
		bank.createKonto(newKonto);
				
		Konto savedKonto = bank.getKontoByNr("120-1");
		assertEquals(101.25, bank.calculateRate(savedKonto, 1), 0.01);
		assertEquals(106.40, bank.calculateRate(savedKonto, 5), 0.01);
		assertEquals(109.08, bank.calculateRate(savedKonto, 7), 0.01);
		
		bank.removeKonto(newKonto);
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