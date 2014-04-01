package server;

public class HelloWorldXMLBean implements HelloWorldXML {
	public String echo(String echo) throws NoEchoException {
		 if (echo == null) throw new NoEchoException("No Echo");
		 else return "Server Echo : " + echo;
	}
}
