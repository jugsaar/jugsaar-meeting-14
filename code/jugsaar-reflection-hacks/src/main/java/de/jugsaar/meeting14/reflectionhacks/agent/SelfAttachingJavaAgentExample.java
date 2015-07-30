package de.jugsaar.meeting14.reflectionhacks.agent;

import com.sun.tools.attach.VirtualMachine;
import com.sun.xml.internal.ws.org.objectweb.asm.*;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

/**
 * Created by tom on 27.07.15.
 *
 * Run with -noverify as JVM arguments!
 */
public class SelfAttachingJavaAgentExample {

    public static class Greeter {

        public void greet() {
            System.out.println("Hello: JUGSaar!");
        }
    }

    public static void main(String[] args) throws Exception {

        Greeter greeter = new Greeter();

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> greeter.greet(), 0, 1, TimeUnit.SECONDS);

        dynamicAgentAttachDemo();
    }

    private static void dynamicAgentAttachDemo() throws Exception {

        TimeUnit.SECONDS.sleep(4);
        System.out.println("Attaching Profiling Agent");

        Class<?>[] agentClasses = {ProfilingAgent.class};

        File agentJar = createAgentJarFileFrom(agentClasses);

        VirtualMachine vm = VirtualMachine.attach(getJvmPid());
        vm.loadAgent(agentJar.getAbsolutePath());
        System.out.println("Agent ready!");

        System.out.println("Profiling...");
        TimeUnit.SECONDS.sleep(7);
        System.out.println("Profiling finished! Rolling back bytecode modifications...");
        ProfilingAgent.rollback();

        System.out.println("Reverting back to original code...");
        vm.detach();

        System.out.println("Running plain code again...");
        TimeUnit.SECONDS.sleep(7);

        System.out.println("Reattaching profiler agent...");

        vm = VirtualMachine.attach(getJvmPid());
        vm.loadAgent(agentJar.getAbsolutePath());

        TimeUnit.SECONDS.sleep(7);
        vm.detach();
    }

    private static File createAgentJarFileFrom(Class<?>[] agentClasses) throws Exception {

        File agentJar = File.createTempFile("agent", ".jar");
        agentJar.deleteOnExit();

        Manifest manifest = new Manifest();
        Attributes mainAttributes = manifest.getMainAttributes();
        mainAttributes.put(Attributes.Name.MANIFEST_VERSION, "1.0");
        mainAttributes.put(new Attributes.Name("Agent-Class"), ProfilingAgent.class.getName());
        mainAttributes.put(new Attributes.Name("Can-Retransform-Classes"), "true");
        mainAttributes.put(new Attributes.Name("Can-Redefine-Classes"), "true");
        JarOutputStream jos = new JarOutputStream(new FileOutputStream(agentJar), manifest);

        for (Class<?> clazz : agentClasses) {

            JarEntry agent = new JarEntry(clazz.getName().replace('.', '/') + ".class");

            jos.putNextEntry(agent);
            jos.write(getClassBytes(clazz));
            jos.closeEntry();
        }

        jos.close();
        return agentJar;
    }

    static byte[] getClassBytes(Class<?> cls) throws Exception {

        InputStream classBytesStream = cls.getClassLoader().getResourceAsStream(cls.getName().replace('.', '/') + ".class");
        byte[] classBytes = new byte[classBytesStream.available()];
        classBytesStream.read(classBytes);

        return classBytes;
    }


    static String getJvmPid() {

        String jvm = ManagementFactory.getRuntimeMXBean().getName();
        return jvm.substring(0, jvm.indexOf('@'));
    }

    public static class ProfileClassAdapter extends ClassAdapter {

        private String className;

        public ProfileClassAdapter(ClassVisitor visitor, String theClass) {
            super(visitor);
            this.className = theClass;
        }

        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {

            MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);

            return new ProfileMethodAdapter(mv, className, name);
        }
    }

    public static class ProfileMethodAdapter extends MethodAdapter {

        private String className;
        private String methodName;
        private String profileUtilInternalName;

        public ProfileMethodAdapter(MethodVisitor visitor, String className, String methodName) {
            super(visitor);
            this.className = className;
            this.methodName = methodName;
            this.profileUtilInternalName = Type.getInternalName(ProfileUtil.class);
            System.out.println("Profiled " + methodName + " in class " + className + ".");
        }

        public void visitCode() {

            this.visitLdcInsn(this.className);
            this.visitLdcInsn(this.methodName);
            this.visitMethodInsn(Opcodes.INVOKESTATIC, profileUtilInternalName, "start", "(Ljava/lang/String;Ljava/lang/String;)V");

            super.visitCode();
        }

        public void visitInsn(int inst) {

            switch (inst) {
                case Opcodes.ARETURN:
                case Opcodes.DRETURN:
                case Opcodes.FRETURN:
                case Opcodes.IRETURN:
                case Opcodes.LRETURN:
                case Opcodes.RETURN:
                case Opcodes.ATHROW:
                    this.visitLdcInsn(this.className);
                    this.visitLdcInsn(this.methodName);
                    this.visitMethodInsn(Opcodes.INVOKESTATIC, profileUtilInternalName, "end", "(Ljava/lang/String;Ljava/lang/String;)V");
                default:
                    break;
            }

            super.visitInsn(inst);
        }
    }
}
