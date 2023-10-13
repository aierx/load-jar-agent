package com.cn.agent;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.Principal;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Properties;

public class MyPreMainAgent {
    public static void premain(String agentArgs, Instrumentation inst) {

        System.out.println("leiwneyong " +agentArgs);
        agentArgs = agentArgs.replace("\\","\\\\");
        System.setProperty("java.class.path", System.getProperty("java.class.path")+";"+"C:\\Users\\aleiw\\Desktop\\untitled\\src\\processor-1.0-SNAPSHOT.jar");
        System.out.println(System.getProperty("java.class.path"));
        String finalAgentArgs = agentArgs;
        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                                    ProtectionDomain protectionDomain, byte[] classfileBuffer) {
                String targetPath = "\""+ finalAgentArgs +"\"";
                if (className.equals("org/apache/catalina/mbeans/MBeanFactory")) {
                    // tomcat server
                    System.out.println("tomcat server");
                    try {
                        ClassPool classPool = ClassPool.getDefault();
                        CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));
                        CtClass[] parameterTypes = new CtClass[]{classPool.get("java.lang.String"), classPool.get(
                                "java.lang.String"), classPool.get("java.lang.String")};
                        CtMethod ctMethod = ctClass.getDeclaredMethod("createStandardContext", parameterTypes);
                        ctMethod.insertBefore("{ \n" +
                                "        String sourcePath = " + targetPath + ";\n" +
                                "        String targetPath = $3 + \"\\\\WEB-INF\\\\lib\\\\load-jar-agent-1.0-SNAPSHOT.jar\";\n" +
                                "        java.io.File file = new java.io.File(targetPath);\n" +
                                "        if (!file.exists()) {\n" +
                                "            java.io.File sourceFile = new java.io.File(sourcePath);\n" +
                                "            java.io.File targetFile = new java.io.File(targetPath);\n" +
                                "            try {\n" +
                                "                java.io.FileInputStream inputStream = new java.io.FileInputStream(sourceFile);\n" +
                                "                java.io.FileOutputStream outputStream = new java.io.FileOutputStream(targetFile);\n" +
                                "                byte[] buffer = new byte[1024];\n" +
                                "                int length;\n" +
                                "                while ((length = inputStream.read(buffer)) > 0) {\n" +
                                "                    outputStream.write(buffer, 0, length);\n" +
                                "                }\n" +
                                "                outputStream.flush();\n" +
                                "                inputStream.close();\n" +
                                "                outputStream.close();\n" +
                                "                System.out.println(\"复制jar到tomcat的web-inf下\");\n" +
                                "            } catch (Exception e) {\n" +
                                "                System.out.println(\"fail\");\n" +
                                "            }\n" +
                                "        }\n" +
                                "}");
                        return ctClass.toBytecode();
                    } catch (Exception e) {
                        System.out.println("copy jar file fail");
                    }
                } else if (className.equals("org/eclipse/jetty/deploy/AppLifeCycle")) {
                    try {
                        // jetty server
                        ClassPool classPool = ClassPool.getDefault();
                        System.out.println("jetty server");
                        String jettyHome = System.getProperty("jetty.home");
                        File file = new File(jettyHome + "/lib");
                        File[] files = file.listFiles();
                        for (File file1 : files) {
                            if (file1.getName().endsWith(".jar")) {
                                classPool.insertClassPath(file1.getAbsolutePath());
                            }
                        }
                        CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));
                        CtMethod ctMethod = ctClass.getDeclaredMethod("runBindings");
                        ctMethod.insertBefore(
                                "{\n" +
                                        "        Object o = $2;\n" +
                                        "        String[] path = \"_context._baseResource.path.path\".split(\"\\\\.\");\n" +
                                        "        for (int i = 0; i < path.length; i++) {\n" +
                                        "            java.lang.reflect.Field field = null;\n" +
                                        "            java.lang.Class clz = o.getClass();\n" +
                                        "            do {\n" +
                                        "                try {\n" +
                                        "                    field = clz.getDeclaredField(path[i]);\n" +
                                        "                } catch (NoSuchFieldException e) {\n" +
                                        "                    clz = clz.getSuperclass();\n" +
                                        "                }\n" +
                                        "            } while (field == null && clz != null);\n" +
                                        "            field.setAccessible(true);\n" +
                                        "            try {\n" +
                                        "                o = field.get(o);\n" +
                                        "            }catch (Exception e){\n" +
                                        "                e.printStackTrace();\n" +
                                        "            }\n" +
                                        "            if (o == null) {\n" +
                                        "                break;\n" +
                                        "            }\n" +
                                        "        }\n" +
                                        "        String sourcePath = " + targetPath + ";\n" +
                                        "        String targetPath = o + \"\\\\WEB-INF\\\\lib\\\\load-jar-agent-1.0-SNAPSHOT.jar\";\n" +
                                        "        java.io.File file = new java.io.File(targetPath);\n" +
                                        "        if (o != null && !file.exists()) {\n" +
                                        "            java.io.File sourceFile = new java.io.File(sourcePath);\n" +
                                        "            java.io.File targetFile = new java.io.File(targetPath);\n" +
                                        "            try {\n" +
                                        "                java.io.FileInputStream inputStream = new java.io.FileInputStream(sourceFile);\n" +
                                        "                java.io.FileOutputStream outputStream = new java.io.FileOutputStream(targetFile);\n" +
                                        "                byte[] buffer = new byte[1024];\n" +
                                        "                int length;\n" +
                                        "                while ((length = inputStream.read(buffer)) > 0) {\n" +
                                        "                    outputStream.write(buffer, 0, length);\n" +
                                        "                }\n" +
                                        "                outputStream.flush();\n" +
                                        "                inputStream.close();\n" +
                                        "                outputStream.close();\n" +
                                        "                System.out.println(\"复制jar到jetty的web-inf下\");\n" +
                                        "            } catch (Exception e) {\n" +
                                        "                e.printStackTrace();\n" +
                                        "                System.out.println(\"copy jar file fail\");\n" +
                                        "            }\n" +
                                        "        }\n" +
                                        "}");
                        System.out.println(ctMethod.getName());
                        return ctClass.toBytecode();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        System.out.println("copy jar file fail");
                    }
                }
                return classfileBuffer;
            }
        });
    }
}
