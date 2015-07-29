package de.jugsaar.meeting14.reflectionhacks;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Ex006_DynamicProxyExample {

    interface BusinessService {

        void businessMethod(String arg);
    }

    static class DefaultBusinessService implements BusinessService {

        @Override
        public void businessMethod(String arg) {
            System.out.println("\tbusiness " + arg);
        }
    }


    public static void main(String[] args) {

        BusinessService service = new DefaultBusinessService();

        System.out.printf("%n%s%n%n", "Direct method invocation");
        service.businessMethod("foo");

        //Construct proxy
        ClassLoader cl = BusinessService.class.getClassLoader();
        Class[] interfaces = {BusinessService.class};
        InvocationHandler invocationHandler = new TracingInterceptor(service);

        BusinessService proxy = (BusinessService) Proxy.newProxyInstance(cl, interfaces, invocationHandler);

        System.out.printf("%n%s%n%n", "Proxied method invocation");
        proxy.businessMethod("foo");

        System.out.println("");

        //Some proxy details
        //Proxy.... gives us access to the invocationHandler can be useful
        InvocationHandler handler = Proxy.getInvocationHandler(proxy);
        System.out.println("InvocationHandler: " + handler);
        System.out.println("Proxy class: " + proxy.getClass());
        System.out.printf("Proxy is proxy class: %s%n", Proxy.isProxyClass(proxy.getClass()));

        //you can look at the generated via
        // -Dsun.misc.ProxyGenerator.saveGeneratedFiles=true
        // /home/tom/dev/ws/idea/training/de/jugsaar/meeting14/reflectionhacks
    }

    static class TracingInterceptor implements InvocationHandler {

        private final Object subject;

        TracingInterceptor(Object subject) {
            this.subject = subject;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            System.out.printf("Before %s%n", method);
            try {
                return method.invoke(subject, args);
            } finally {
                System.out.printf("After %s%n", method);
            }
        }
    }
}
