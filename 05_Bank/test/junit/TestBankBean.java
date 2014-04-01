package junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import bank.Bank;
import bo.Konto;

@RunWith(value = Parameterized.class)
public class TestBankBean {

	private String nr;
	private String name;
	private Float saldo;
	private String currency;
	private double rate;

	public TestBankBean(String nr, String name, Float saldo, String currency,
			Float result, double rate) {
		this.nr = nr;
		this.name = name;
		this.saldo = saldo;
		this.currency = currency;
		this.rate = rate;
	}

	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] {
				{ "120-1", "Sparkonto", 100.50F, "CHF", 100.50F, 1.25 },
				{ "120-2", "Privatkonto", 280.20F, "CHF", 280.00F, 1.25 },
				{ "120-3", "Eurokonto", 250.00F, "EUR", 101.00F, 1.25 },
				{ "120-4", "Dollarkonto", 305.10F, "USD", 101.00F, 1.25 } };
		return Arrays.asList(data);
	}

	// The app name is the application name of the deployed EJBs. This is typically
	// the ear name without the .ear suffix.
	// However, the application name could be overridden in the application.xml of
	// the EJB deployment on the server.
	// If the application is not as a .ear, the app name then will be an empty
	// string
	final String appName = "MyEAR";

	// This is the module name of the deployed EJBs on the server.
	// This is typically the jar name of the EJB deployment, without the .jar
	// suffix, but can be overridden via the ejb-jar.xml
	final String moduleName = "MyEJB";

	// AS7 allows each deployment to have an (optional) distinct name. We haven't
	// specified a distinct name for our EJB deployment, so this is an empty string
	final String distinctName = "";

	// The EJB name which by default is the simple class name of the bean
	// implementation class
	final String beanName = "BankBean";
	// oder: final String beanName = HelloWorldBean.class.getSimpleName();

	// the remote view fully qualified class name
	final String viewClassName = "bank.Bank";
	// oder: final String viewClassName = HelloWorld.class.getName();

	// initialer Kontext, um nach einem Namen zu suchen
	private static InitialContext initialContext;

	// Stellvertreter der Business Schnittstelle
	private static Object proxy;

	// Business Schnittstelle mit den Geschäftsmethoden
	private Bank bank;

	// definiert den InitialContaxt -> einmal am Anfang
	@BeforeClass
	public static void setUp() throws NamingException {
		Properties properties = new Properties();
		properties.setProperty(Context.URL_PKG_PREFIXES,
				"org.jboss.ejb.client.naming");

		initialContext = new InitialContext(properties);
		assertNotNull(initialContext);
	}

	// holt das Server Proxy Objekt -> vor jedem Test
	@Before
	public void testEJBProxy() throws NamingException {

		// instatziiert den Stellvertrezter
		proxy = initialContext.lookup("ejb:" + appName + "/" + moduleName + "/"
				+ distinctName + "/" + beanName + "!" + viewClassName);

		assertNotNull(proxy);
		assertTrue(proxy instanceof Bank);
		bank = (Bank) proxy;
		assertNotNull(bank);
	}
	
	/*
	 * Test Bedingungen: Konto: account ohne Datensätze
	 * 
	 * sql-files > deleteAllFromAccountTable.sql
	 */
	
	// testet die Methode tellRate() mit dem Runner Parameterized.class
	@Test
	public void calculateZins() throws Exception {
		assertEquals(rate, bank.tellRate(), 0.05);
	}

	// erzeugt ein Konto mit dem Runner Parameterized.class
	@Test
	public void testCreateKonto() {
		Konto k = new Konto(nr, name, saldo, currency);
		bank.createKonto(k);
		assertEquals("Konto speichern", saldo, bank.getKontoByNr(nr).getSaldo(), 0.05);
		bank.removeKonto(k);
		assertEquals("Anzahl Kontos sollte 0 sein", 0, bank.getAllKontos().size());
	}

	// modifiziert ein Konto mit dem Runner Parameterized.class
	@Test
	public void testUpdateKonto() {
		Konto k = new Konto(nr, name, saldo, currency);
		bank.createKonto(k);

		k.setCurrency("AUD");
		bank.updateKonto(k);
		assertEquals("Konto modifizieren", "AUD", bank.getKontoByNr(nr).getCurrency());
		bank.removeKonto(k);
		assertEquals("Anzahl Kontos sollte 0 sein", 0, bank.getAllKontos().size());
	}

	// löscht alle Kontos
	@Test
	public void deleteAllKontos() throws Exception {

		Konto k = new Konto(nr, name, saldo, currency);
		bank.createKonto(k);

		List<Konto> kontos = bank.getAllKontos();
		for (Konto konto : kontos)
			bank.removeKonto(konto);
		kontos = bank.getAllKontos();
		assertEquals("Anzahl Kontos sollte 0 sein", 0, kontos.size());
	}

	// löst den Stellvertreter
	@After
	public void resetEJBProxy() {
		proxy = null;
	}

	// Aufräumen (Resscourcen lösen / löschen)
	@AfterClass
	public static void clanUp() {
		try {
			initialContext.close();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
}