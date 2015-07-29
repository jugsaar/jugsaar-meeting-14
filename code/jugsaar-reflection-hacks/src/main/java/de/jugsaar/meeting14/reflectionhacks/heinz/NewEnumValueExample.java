package de.jugsaar.meeting14.reflectionhacks.heinz;

/**
 * View via javap
 * <p>
 * Whole class: (-p also private members, -v verbose: show bytecode)
 * javap -p -cp .\target\classes reflect.madness.Hck006_NewEnumValueExample
 * <p>
 * javap -p -v -cp .\target\classes reflect.madness.Hck006_NewEnumValueExample
 * <p>
 * Just the enum:
 * javap -p -cp .\target\classes reflect.madness.Hck006_NewEnumValueExample$HumanState
 * <p>
 * javap -p -v -cp .\target\classes reflect.madness.Hck006_NewEnumValueExample$HumanState
 * <p>
 * Just Human:
 * javap -v -p -cp .\target\classes reflect.madness.Hck006_NewEnumValueExample$Human
 *
 * http://www.javaspecialists.eu/archive/Issue161.html
 */
public class NewEnumValueExample {

    static {
        hackEnum();
    }

    public static void main(String[] args) {

        Human bill = new Human();

        for (HumanState state : HumanState.values()) {
            bill.respondToHowAreYou(state);
        }
    }

    static enum HumanState {HAPPY, SAD}

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


    private static void hackEnum() {

        EnumBuster<HumanState> buster =
                new EnumBuster<HumanState>(HumanState.class,
                        Human.class);

        HumanState MELLOW = buster.make("MELLOW");
        buster.addByValue(MELLOW);

    }
}
