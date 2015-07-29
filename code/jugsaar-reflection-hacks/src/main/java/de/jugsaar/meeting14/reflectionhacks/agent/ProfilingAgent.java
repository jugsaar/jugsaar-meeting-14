package de.jugsaar.meeting14.reflectionhacks.agent;

import com.sun.xml.internal.ws.org.objectweb.asm.ClassAdapter;
import com.sun.xml.internal.ws.org.objectweb.asm.ClassReader;
import com.sun.xml.internal.ws.org.objectweb.asm.ClassWriter;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tom on 27.07.15.
 */
public class ProfilingAgent implements ClassFileTransformer {

    private static Instrumentation instrumentation;
    private static ProfilingAgent transformer;
    private static String agentTypeDescriptor = ProfilingAgent.class.getName().replace(".", "/");

    private static List<ClassDefinition> transformedClasses = new ArrayList<>();

    public static void rollback() throws Exception{

        instrumentation.removeTransformer(transformer);
        instrumentation.redefineClasses(transformedClasses.toArray(new ClassDefinition[transformedClasses.size()]));

        transformedClasses.clear();
    }

    public static void agentmain(String s, Instrumentation instrumentation) {
        System.out.println("Agent loaded!");

// initialization code:
        ProfilingAgent.transformer = new ProfilingAgent();
        ProfilingAgent.instrumentation = instrumentation;
        ProfilingAgent.instrumentation.addTransformer(transformer);
// to instrument, first revert all added bytecode:
// call retransformClasses() on all modifiable classes...
        try {
            ProfilingAgent.instrumentation.redefineClasses(new ClassDefinition(SelfAttachingJavaAgentExample.Greeter.class, SelfAttachingJavaAgentExample.getClassBytes(SelfAttachingJavaAgentExample.Greeter.class)));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to redefine class!");
        }
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        if (loader != ClassLoader.getSystemClassLoader()) {
            System.err
                    .println(className
                            + " is not using the system loader, and so cannot be loaded!");
            return classfileBuffer;
        }

        if (className.startsWith(agentTypeDescriptor)) {
            System.err
                    .println(className
                            + " is part of profiling classes. No StackOverflow for you!");
            return classfileBuffer;
        }

        byte[] result = classfileBuffer;

        transformedClasses.add(new ClassDefinition(classBeingRedefined, classfileBuffer.clone()));

        try {
//Create class reader from buffer
            ClassReader reader = new ClassReader(classfileBuffer);
//Make writer
            ClassWriter writer = new ClassWriter(0);
            ClassAdapter profiler = new SelfAttachingJavaAgentExample.ProfileClassAdapter(writer, className);
//Add the class adapter as a modifier
            reader.accept(profiler, 0);
            result = writer.toByteArray();
            System.out.println("Returning reinstrumented class: " + className);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
