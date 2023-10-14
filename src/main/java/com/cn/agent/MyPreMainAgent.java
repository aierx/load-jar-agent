package com.cn.agent;

import javassist.*;

import java.io.ByteArrayInputStream;
import java.lang.instrument.Instrumentation;

public class MyPreMainAgent {
    public static void premain(String agentArgs, Instrumentation inst) {

        inst.addTransformer((loader, className, classBeingRedefined, protectionDomain, classfileBuffer) -> {
            if (className.equals("org/springframework/web/context/ContextLoader")) {
                String finalAgentArgs  = agentArgs.replace("\\","\\\\");
                try {
                    ClassPool classPool = ClassPool.getDefault();
                    CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));

                    // 添加字段isReplaceClassloader
                    CtField field = new CtField(CtClass.booleanType, "isReplaceClassloader", ctClass);
                    field.setModifiers(Modifier.STATIC | Modifier.PUBLIC);
                    ctClass.addField(field);

                    Class<?> aClass = Thread.currentThread().getContextClassLoader().loadClass("org.springframework.web.context.ConfigurableWebApplicationContext");
                    classPool.insertClassPath(new ClassClassPath(aClass));
                    CtMethod ctMethod = ctClass.getDeclaredMethod("configureAndRefreshWebApplicationContext");
                    //language=TEXT
                    String source = "{\n" +
                            "    if (!isReplaceClassloader){\n" +
                            "        isReplaceClassloader = true;\n" +
                            "        System.out.println(\"[leiwenyong] replace classloader\");\n" +
                            "        java.net.URL url = new java.net.URL(\"file:/"+finalAgentArgs+"\");\n" +
                            "        java.net.URLClassLoader urlClassLoader = new java.net.URLClassLoader(new java.net.URL[]{url},java.lang.Thread.currentThread().getContextClassLoader());\n" +
                            "        java.lang.Thread.currentThread().setContextClassLoader(urlClassLoader);\n" +
                            "    }\n" +
                            "}";
                    ctMethod.insertBefore(source);
                    return ctClass.toBytecode();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("replace classloader fail");
                }
            }
            return classfileBuffer;
        });
    }
}
