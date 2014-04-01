package remote;

import java.util.Hashtable;

import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import bank.Bank;

public class BankClient {
	public static void main(String[] args) {
		try {
			// Business Interface
			Bank bank = null;

			// Properties for Initial Context
			final Hashtable<String, String> jndiProperties = new Hashtable<String, String>();
			jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");

			// Initial Context
			final Context context = new InitialContext(jndiProperties);

			System.out.println("context" + context);
			
			/*
			 * For stateless beans: ejb:<app-name>/ <module-name>/ <distinct-name>/
			 * <bean-name>!<fully-qualified-classname-of-the-remote-interface>
			 * 
			 * For stateful beans: ejb:<app-name>/ <module-name>/ <distinct-name>/
			 * <bean-name>!<fully-qualified-classname-of-the-remote-interface>?stateful
			 */

			// Lookup Server Proxy Object "Bank"
			Object o = context.lookup("ejb:MyEAR/MyEJB/BankBean!bank.Bank?stateful");

			System.out.println("proxy" + o);
			
			// Instanziierung des Business Interface durch narrowing
			bank = (Bank) PortableRemoteObject.narrow(o, Bank.class);

			// Business Methoden
			try {
				System.out.println(bank.tellRate());

			} catch (EJBException e) {
				e.printStackTrace();
			}

		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
}