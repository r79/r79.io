package junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import generics.Datentyp;
import generics.MyGeneric;
import hello.HelloWorld;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


import org.junit.Test;

import pojo.Person;
import annotations.Datenfeld;

public class TestUtils<K, V> {

	@Test
	public void testMyAnnotation() throws Exception {
		Person p = new Person();
		Field[] fields = p.getClass().getDeclaredFields();
		assertEquals(4, fields.length);
		assertEquals("Nummer", p.getClass().getDeclaredField("nr")
				.getAnnotation(Datenfeld.class).bezeichnung());
		assertEquals("Name", p.getClass().getDeclaredField("name")
				.getAnnotation(Datenfeld.class).bezeichnung());
		assertEquals("", p.getClass().getDeclaredField("beschreibung")
				.getAnnotation(Datenfeld.class).bezeichnung());

		assertEquals(Datentyp.NUMMER, p.getClass().getDeclaredField("nr")
				.getAnnotation(Datenfeld.class).datentyp());
		assertEquals(Datentyp.TEXT, p.getClass().getDeclaredField("name")
				.getAnnotation(Datenfeld.class).datentyp());
		assertEquals(
				Datentyp.TEXT,
				p.getClass().getDeclaredField("beschreibung")
						.getAnnotation(Datenfeld.class).datentyp());
	}

	@Test
	public void testDepricated() {
		HelloWorld hw = new HelloWorld();
		Method[] meth = hw.getClass().getDeclaredMethods();
		for (Method m : meth) {
			if (m.getName().equals("sayHello")) {
				assertNotNull(m.getAnnotation(Deprecated.class));
			}
		}
	}

	@Test
	public void testSayHello() {
		assertEquals("generic hello world",
				MyGeneric.sayHello("generic", new StringBuffer("world")));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testMyGeneric() {

		K key = (K) "test";
		V value = (V) (new Integer(123));
		MyGeneric<K, V> myGeneric = new MyGeneric<K, V>(key, value);

		assertEquals(key, myGeneric.getKey());
		assertEquals(value, myGeneric.getValue());
	}

}
