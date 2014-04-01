package server;

@SuppressWarnings("serial")
public class NoEchoException extends Exception {
	public NoEchoException(String explanation) {
		System.out.println(explanation);
	}
}
