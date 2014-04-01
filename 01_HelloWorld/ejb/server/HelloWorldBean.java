package server;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

@Interceptors(ch.zli.m223.interceptor.HelloInterceptor.class)
@Stateless
public class HelloWorldBean implements HelloWorld {
	@Override
	public String echo(String echo) throws NoEchoException {
		if(echo==null) {
			throw new NoEchoException("No Echo");
		} else {
			return "Server Echoes: " + echo;
		}
	}
}
