package de.jugsaar.meeting14.reflectionhacks;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Uc003_GenericObjectAdapterExample {

    interface BusinessService {

        void businessMethod(String arg);

        void otherBusinessMethod(String arg);
    }

    static class DefaultBusinessService implements BusinessService {

        @Override
        public void businessMethod(String arg) {
            System.out.println(arg);
        }

        @Override
        public void otherBusinessMethod(String arg) {
            System.out.println("original other business method called with: " + arg);
        }
    }

    public static void main(String[] args) {

        BusinessService service = new DefaultBusinessService();

        BusinessService newService = DynamicObjectAdapterFactory.adapt(service, BusinessService.class, new Object() {

            public void businessMethod(String arg) {
                System.out.println("Adapted method called: " + arg);
                System.out.println("-> delegating to original method...");
                service.businessMethod(arg);
            }
        });

        newService.businessMethod("Foo");
        newService.otherBusinessMethod("Bar");
    }

    /**
     * http://www.javaspecialists.eu/archive/Issue108.html
     */
    public static class DynamicObjectAdapterFactory {

        public static <T> T adapt(Object adaptee, Class<T> target, Object adapter) {
            return (T) Proxy.newProxyInstance(target.getClassLoader(), new Class[]{target}, new AdaptingInvocationHandler(adapter, adaptee));
        }

        private static class MethodIdentifier {

            private final String name;
            private final Class[] parameters;

            public MethodIdentifier(Method m) {
                this.name = m.getName();
                this.parameters = m.getParameterTypes();
            }

            // we can save time by assuming that we only compare against
            // other MethodIdentifier objects
            public boolean equals(Object o) {

                MethodIdentifier that = (MethodIdentifier) o;
                return this.name.equals(that.name) && Arrays.equals(this.parameters, that.parameters);
            }

            public int hashCode() {
                return name.hashCode();
            }
        }

        private static class AdaptingInvocationHandler implements InvocationHandler {

            private final Object adapter;
            private final Object adaptee;
            private final Map<MethodIdentifier, Method> adaptedMethods;

            public AdaptingInvocationHandler(Object adapter, Object adaptee) {

                this.adapter = adapter;
                this.adaptee = adaptee;
                this.adaptedMethods = new HashMap<>();

                Method[] methods = adapter.getClass().getDeclaredMethods();
                for (Method m : methods) {
                    adaptedMethods.put(new MethodIdentifier(m), m);
                }
            }

            public Object invoke(Object proxy, Method originalMethod, Object[] args) throws Throwable {

                try {

                    Method adapterMethod = adaptedMethods.get(new MethodIdentifier(originalMethod));

                    if (adapterMethod != null) {
                        return adapterMethod.invoke(adapter, args);
                    }

                    return originalMethod.invoke(adaptee, args);

                } catch (InvocationTargetException e) {
                    throw e.getTargetException();
                }
            }
        }
    }
}
