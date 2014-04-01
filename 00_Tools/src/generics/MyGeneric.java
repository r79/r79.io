package generics;

public class MyGeneric<T, U> {

	public static <E, T> String sayHello(E pre, T post) {
		return (pre.toString() + " hello " + post.toString());
	}

	private T key;
	private U value;

	public MyGeneric(T key, U value) {
		this.key = key;
		this.value = value;
	}

	public T getKey() {
		return key;
	}

	public U getValue() {
		return value;
	}

}
