package junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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

import bank.Bank;
import bo.Konto;

public class TestBankBeanCRUD {

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
		System.out.println(po);

		assertTrue(po instanceof Bank);
		bank = (Bank) po;
		assertNotNull(bank);
	}

	/*
	 * Test Bedingungen: Konto: account mit 4 Datensätzen
	 * 
	 * sql-files > createAccountTable.sql
	 */

	@Test
	// Testet die Anzahl Kontos
	public void getAllKontos() {
		List<Konto> kontos = bank.getAllKontos();
		assertEquals(4, kontos.size());
	}

	@Test
	// Testet das Hinzufügen eines Kontos
	public void saveKonto() {
		Konto k = new Konto("120-9", "TestKonto", 100.0F, "EUR");
		// Konto hinzufügen
		bank.createKonto(k);
		Konto savedKonto = bank.getKontoByNr("120-9");
		assertEquals("120-9", savedKonto.getNr());
		// ein Konto mehr in der Liste
		List<Konto> kontos_1 = bank.getAllKontos();
		assertEquals(5, kontos_1.size());
		// Konto wieder löschen
		bank.removeKonto(savedKonto);
		// wieder 4 Konti in der Liste
		List<Konto> kontos_2 = bank.getAllKontos();
		assertEquals(4, kontos_2.size());
	}

	@Test
	// Testet das Modifizieren eines bestehenden Kontos
	public void updateKonto() {
		// Konto holen
		Konto konto = bank.getKontoByNr("120-1");
		String name = konto.getName();
		float saldo = konto.getSaldo();
		// Attribute modifizieren
		konto.setName("NewName");
		konto.setSaldo(konto.getSaldo() + 123.456f);
		// Konto update
		bank.updateKonto(konto);
		// test new Name/Saldo
		Konto kontoWithNewNameAndSaldo = bank.getKontoByNr("120-1");
		assertEquals(konto.getName(), kontoWithNewNameAndSaldo.getName());
		assertEquals(konto.getSaldo(), kontoWithNewNameAndSaldo.getSaldo(), 0.1f);
		// reset to original
		konto.setName(name);
		konto.setSaldo(saldo);
		bank.updateKonto(konto);
	}

	@Test
	// testet das Löschen eines bestehenden Kontos
	public void removeKonto() {
		Konto konto = bank.getKontoByNr("120-1");
		bank.removeKonto(konto);
		// ein Konto weniger in der Liste
		List<Konto> kontos = bank.getAllKontos();
		assertEquals(3, kontos.size());
		// Konto wieder hinzufügen
		bank.createKonto(konto);
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