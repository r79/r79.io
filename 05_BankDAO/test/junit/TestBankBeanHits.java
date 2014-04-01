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
import org.junit.Ignore;
import org.junit.Test;

import bank.Bank;

public class TestBankBeanHits {

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

	// Testet die Anzahl Hits
	// kein sinvoller Unit-Test!
	@Test
	@Ignore
	public void testGetHits() {
		assertEquals(1, bank.tellHits());
	}

	// Besser
	@Test
	public void logHits(){
		System.out.println("Anzahl Hits > " + bank.tellHits());
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