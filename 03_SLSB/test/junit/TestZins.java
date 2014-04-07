package junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
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

import zins.Zins;

@RunWith(value = Parameterized.class)
public class TestZins {

	private double amount;
	private int years;
	private double rate;
	private double result;
	private double resultGlobalRate;

	public TestZins(double amount, int years, double rate, double result,
			double resultGlobalRate) {
		this.amount = amount;
		this.years = years;
		this.rate = rate;
		this.result = result;
		this.resultGlobalRate = resultGlobalRate;
	}

	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] { { 100.00, 1, 5.0, 105.00, 101.25 },
				{ 100.00, 2, 5.0, 110.25, 102.50 }, { 100.00, 3, 5.0, 115.75, 103.80 },
				{ 100.00, 4, 5.0, 121.55, 105.10 } };
		return Arrays.asList(data);
	}

	// initialer Kontext, um nach einem Namen zu suchen
	private static InitialContext ic;

	// Stellvertreter der Business Schnittstelle
	private static Object po;

	// Business Schnittstelle mit den Geschäftsmethoden
	private Zins zins;

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
	public void testEJBProxy() throws NamingException {

		// instatziiert den Stellvertreter
		po = ic.lookup("ejb:MyEAR/MyEJB//RateBean!" + Zins.class.getName());

		assertNotNull(po);
		assertTrue(po instanceof Zins);
		zins = (Zins) po;
		assertNotNull(zins);
	}

	// testet die Methode calculate(amount, rate, years) mit dem Runner
	// Parameterized.class
	@Test
	public void calculateZins() throws Exception {
		assertEquals(result, zins.calculate(amount, rate, years), 0.05);
	}

	// testet die Methode calculate(amount, years) mit dem Runner
	// Parameterized.class
	// Rate ist Serverseitig gemäss ejb-jar.xml
	// <env-entry-value>1.25</env-entry-value>
	@Test
	public void calculateGlobalZins() throws Exception {
		assertEquals(resultGlobalRate, zins.calculate(amount, years), 0.1);
	}

	// löst den Stellvertreter
	@After
	public void resetEJBProxy() {
		po = null;
	}

	// Aufräumen (Resscourcen lösen / löschen)
	@AfterClass
	public static void cleanUp() {
		try {
			ic.close();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
}
