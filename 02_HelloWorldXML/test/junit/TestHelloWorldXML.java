package junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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

import server.HelloWorldXML;
import server.NoEchoException;

public class TestHelloWorldXML {

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
	final String beanName = "HelloWorldXMLBean";
	// oder: final String beanName = HelloWorldBean.class.getSimpleName();

	// the remote view fully qualified class name
	final String viewClassName = "server.HelloWorldXML";
	// oder: final String viewClassName = HelloWorld.class.getName();

	// initialer Kontext, um nach einem Namen zu suchen
	private static InitialContext initialContext;

	// Stellvertreter der Business Schnittstelle
	private static Object proxy;

	// Business Schnittstelle mit den Geschäftsmethoden
	private HelloWorldXML hello;

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
		assertTrue(proxy instanceof HelloWorldXML);
		hello = (HelloWorldXML) proxy;
		assertNotNull(hello);
	}

	// testet Echo des EJB
	@Test
	public void testEJBEcho() throws NamingException, NoEchoException {
		assertEquals("Server Echo : Hello EJB World", hello.echo("Hello EJB World"));
	}

	// testet ungültiges Echo des EJB (echo = null)
	@Test(expected = NoEchoException.class)
	public void testNoEJBEcho() throws NamingException, NoEchoException {
		hello.echo(null);
	}

	// testet "not bound" -> erwartet NamingException
	@Test(expected = NamingException.class)
	public void noEcho() throws NamingException {
		proxy = initialContext.lookup("not bound");
		assertNull(proxy);
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
