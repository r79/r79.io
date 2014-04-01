package server;

import javax.ejb.Remote;

@Remote
public interface HelloWorld {
		public String echo(String echo) throws NoEchoException;
}
