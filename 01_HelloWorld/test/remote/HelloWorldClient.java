package remote;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import server.HelloWorld;
import server.NoEchoException;

public class HelloWorldClient {
	public static void main(String[] args) {
		try {
			// Business Interface
			HelloWorld hello = null;

			// Properties for Initial Context
			final Hashtable<String, String> jndiProperties = new Hashtable<String, String>();
			jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			
			// Initial Context
			final Context context = new InitialContext(jndiProperties);

			/* The JNDI lookup name for a stateless session bean has the syntax of:
			   ejb:<appName>/<moduleName>/<distinctName>/<beanName>!<viewClassName>
			 
			   "MyEAR"             <appName> application name of the EAR (without the .ear) or blank
			   "HelloWorld"        <moduleName> module name of the EJB JAR file (without the .jar suffix)
			   ""                  <distinctName> (optional) distinct name or blank
			   "HelloWorldBean"    <beanName> name of the session been to be invoked
			   "server.HelloWorld" <viewClassName> fully qualified classname (with package) of the remote interface.
    */
			
			// Lookup Server Proxy Object "HelloWorld" 
			Object o = context.lookup("ejb:MyEAR/MyEJB/HelloWorldBean!server.HelloWorld");
				
			// ODER:  durch Reflektion
			//Object o = context.lookup("ejb:MyEAR/MyEJB//HelloWorldBean!" + HelloWorld.class.getName());
   			
			// Instanziierung des Business Interface durch narrowing		
			hello = (HelloWorld) PortableRemoteObject.narrow(o, HelloWorld.class);

			// ODER: Instanziierung des Business Interface durch Type-Casting des Proxy
			hello = (HelloWorld) o;
						
			// Business Methoden
			try {
				System.out.println(hello.echo("Hello EJB3 World"));
				System.out.println(hello.echo(null));
			} catch (NoEchoException e) {
				System.out.println("NO ECHO FROM SERVER");
			}

			// zeigt Proxyklasse und Interface
			System.out.print("class > " + hello.getClass().getName());
			@SuppressWarnings("rawtypes")
			Class[] interfaces = hello.getClass().getInterfaces();
			for (int i = 0; i < interfaces.length; ++i) {
				System.out.println("\timplements > " + interfaces[i].getName());
			}

		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
}