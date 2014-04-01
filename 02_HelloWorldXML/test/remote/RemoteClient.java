package remote;

import java.util.Properties;

import org.jboss.ejb.client.ContextSelector;
import org.jboss.ejb.client.EJBClientConfiguration;
import org.jboss.ejb.client.EJBClientContext;
import org.jboss.ejb.client.PropertiesBasedEJBClientConfiguration;
import org.jboss.ejb.client.StatelessEJBLocator;
import org.jboss.ejb.client.remoting.ConfigBasedEJBClientContextSelector;

import server.HelloWorldXML;
import server.NoEchoException;

public class RemoteClient {
	@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
	public static void main(String[] args) {
		// Business Interface
		HelloWorldXML hello = null;

		Properties properties = new Properties();

		properties.put("endpoint.name", "client-endpoint");
		properties.put(
				"remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED",
				"false");
		properties.put("remote.connections", "default");
		properties.put("remote.connection.default.port", "4447");
		properties.put("remote.connection.default.host", "localhost");
		properties.put("remote.connection.default.username", "user");
		properties.put("remote.connection.default.password", "Password12345");

		// utility classes to perform a lookup without any configuration file
		// provides the configurations that will be used for creating EJB receivers
		// and managing the EJB client context
		EJBClientConfiguration cc = new PropertiesBasedEJBClientConfiguration(
				properties);

		// This constructor creates a org.jboss.ejb.client.EJBClientContext and uses
		// the passed ejbClientConfiguration to create and associate EJB receivers to
		// that context
		final ContextSelector<EJBClientContext> ejbClientContextSelector = new ConfigBasedEJBClientContextSelector(

		cc);

		// Now let's setup the EJBClientContext to use this selector
		final ContextSelector<EJBClientContext> previousSelector = EJBClientContext
				.setSelector(ejbClientContextSelector);

		// StatelessEJBLocator(Class<T> viewType, String appName, String moduleName,
		// String beanName, String distinctName)
		StatelessEJBLocator<HelloWorldXML> locator = new StatelessEJBLocator(
				HelloWorldXML.class, "MyEAR", "MyEJB", "HelloWorldXMLBean", "");

		// Finally, a proxy for the ejb is created by passing an instance of the
		// StatelessEJBLocator (or of the StatefulEJBLocator if you are running
		// against a Stateful EJB) class to the EJBClient class:
		hello = org.jboss.ejb.client.EJBClient.createProxy(locator);

		// Business Methoden
		try {
			System.out.println(hello.echo("Hello EJB3 World"));
			System.out.println(hello.echo(null));
		} catch (NoEchoException e) {
			System.out.println("NO ECHO FROM SERVER");
		}

		// zeigt Proxyklasse und Interface
		System.out.print("class > " + hello.getClass().getName());
		Class[] interfaces = hello.getClass().getInterfaces();
		for (int i = 0; i < interfaces.length; ++i) {
			System.out.println("\timplements > " + interfaces[i].getName());
		}
	}
}