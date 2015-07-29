package de.jugsaar.meeting14.reflectionhacks;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import sun.reflect.ReflectionFactory;

/**
 *  View via javap
 *
 *  Whole class: (-p also private members, -v verbose: show bytecode)
 *  javap -p -cp target/classes de.jugsaar.meeting13.reflectionhacks.Hck006_NewEnumValueExample
 *  javap -p -v -cp target/classes de.jugsaar.meeting13.reflectionhacks.Hck006_NewEnumValueExample
 *
 *  Just the enum:
 *  javap -p -cp target/classes de.jugsaar.meeting13.reflectionhacks.Hck006_NewEnumValueExample.HumanState
 *  javap -p -v -cp target/classes de.jugsaar.meeting13.reflectionhacks.Hck006_NewEnumValueExample.HumanState
 *
 *  Just Human:
 *  javap -p -cp target/classes de.jugsaar.meeting13.reflectionhacks.Hck006_NewEnumValueExample.Human
 *  javap -v -p -cp target/classes de.jugsaar.meeting13.reflectionhacks.Hck006_NewEnumValueExample.Human
 *
 *  Show the actual enum structure via jad: http://varaneckas.com/jad/
 *  /Applications/dev/tools/jad -p target/classes/de/jugsaar/meeting13/reflectionhacks/Hck006_NewEnumValueExample\$HumanState.class
 */
public class Hck006_NewEnumValueExample {

    static {
        hackEnum();
    }

    enum HumanState {HAPPY, SAD}

	static class Human {

		public void respondToHowAreYou(HumanState state) {
			switch (state) {
				case HAPPY:
					System.out.println("fine");
					break;
				case SAD:
					System.out.println("Don't ask...");
					break;
				default:
					throw new IllegalStateException("Invalid state: " + state);
			}
		}
	}

    public static void main(String[] args) {

        Human bill = new Human();

        for(HumanState state : HumanState.values()){
            bill.respondToHowAreYou(state);
        }
    }







































    private static void hackEnum() {

        try {

            //Construct new enum value via ReflectionFactory
            Constructor ctor = HumanState.class.getDeclaredConstructor(String.class, int.class);
            ReflectionFactory reflection = ReflectionFactory.getReflectionFactory();
            Enum newEnumValue = (Enum)reflection.newConstructorAccessor(ctor).newInstance(new Object[]{"UNKNOWN", 3});

            //extract the internal values field that contains the actual enum values
            Field enumValuesField = HumanState.class.getDeclaredField("$VALUES");
            enumValuesField.setAccessible(true);

            //remove final flag from static final enum values field
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.set(enumValuesField, ((int) modifiersField.get(enumValuesField)) & ~Modifier.FINAL);

            //Add new enum value to interal enum values array
            Object[] oldValues = (Object[])enumValuesField.get(null);
            Object[] newValues = Arrays.copyOf(oldValues, oldValues.length + 1);
            newValues[oldValues.length] = newEnumValue;

            //set ordinal of new enum value to the last position
            Field ordinalField = Enum.class.getDeclaredField("ordinal");
            ordinalField.setAccessible(true);
            ordinalField.set(newEnumValue, oldValues.length);

            //set new enum values field
            enumValuesField.set(null, newValues);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Collection<Field> findRelatedSwitchFields(
            Class[] switchUsers, Class<?> clazz) {
        Collection<Field> result = new ArrayList<Field>();
        try {
            for (Class switchUser : switchUsers) {
                Class[] clazzes = switchUser.getDeclaredClasses();
                for (Class suspect : clazzes) {
                    Field[] fields = suspect.getDeclaredFields();
                    for (Field field : fields) {

                        System.out.println(field);
                        if (field.getName().startsWith("$SwitchMap$" +
                                clazz.getSimpleName())) {
                            field.setAccessible(true);
                            result.add(field);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Could not fix switch", e);
        }
        return  result;
    }

}
