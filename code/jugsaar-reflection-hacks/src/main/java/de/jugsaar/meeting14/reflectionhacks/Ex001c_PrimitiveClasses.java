package de.jugsaar.meeting14.reflectionhacks;

public class Ex001c_PrimitiveClasses {

	public static void main(String[] args) throws Exception {
//        Class.forName("int"); //not possible

		Class<?> intClass = Integer.TYPE;
		Class<?> intClass2 = int.class;

	}
}
