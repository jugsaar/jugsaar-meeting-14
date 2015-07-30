package de.jugsaar.meeting14.reflectionhacks;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 *  Show decompiled version via jad:
 *  /Applications/dev/tools/jad -p target/classes/de/jugsaar/meeting13/reflectionhacks/Hck004_ChangeStaticFinalFieldExample.class
 */
public class Hck004_ChangeStaticFinalFieldExample {

	static class Example {

		private static final CharSequence value = "gugu";

		public static void printValue() {
			System.out.println(value);
		}
	}

	public static void main(String[] args) throws Exception {

		Example.printValue();

		hack();

		Example.printValue();
	}


























	private static void hack() throws Exception {
		setStaticFinalField(Example.class.getDeclaredField("value"), "gaga");
	}

	public static void setStaticFinalField(Field field, Object newValue) throws Exception {

		field.setAccessible(true);

		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		modifiersField.set(field, field.getModifiers() & ~Modifier.FINAL);

		field.set(null, newValue);
	}
}
