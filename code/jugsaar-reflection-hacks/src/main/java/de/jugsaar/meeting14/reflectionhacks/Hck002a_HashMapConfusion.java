package de.jugsaar.meeting14.reflectionhacks;

import java.util.HashMap;
import java.util.Map;

public class Hck002a_HashMapConfusion {

    public static void main(String[] args) throws Exception{

        Map<Integer, String> map = new HashMap<>();
        map.put(42, "Joy");

        System.out.println(map);

        Hck002_AutoBoxing_IntegerHack.hack();

        System.out.println("hacked: ");

        System.out.println(map.get(42));
        System.out.println(map.get(22));
        System.out.println(map);

        Hck002_AutoBoxing_IntegerHack.unhack();
        System.out.println("un-hacked: ");

        System.out.println(map.get(42));
        System.out.println(map.get(22));
        System.out.println(map);
    }
}
