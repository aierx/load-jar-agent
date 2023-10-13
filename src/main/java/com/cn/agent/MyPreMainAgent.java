package com.cn.agent;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class MyPreMainAgent {
    public static void premain(String agentArgs, Instrumentation inst) {
       inst.addTransformer(new ClassFileTransformer() {
           @Override
           public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
               // tomcat 启动



               if (className.equals("org/apache/catalina/mbeans/MBeanFactory")||className.equals("com/cn/agent/Main")){
                   System.out.println("hhhhhhhhhhhhhh");
                   ClassPool classPool = ClassPool.getDefault();
                   try {
                       CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));
                       CtClass[] parameterTypes = new CtClass[]{classPool.get("java.lang.String"), classPool.get("java.lang.String"),classPool.get("java.lang.String")};
                       CtMethod ctMethod = ctClass.getDeclaredMethod("createStandardContext",parameterTypes);
                       ctMethod.insertBefore("{ \n" +
                               "        String sourcePath = \"C:\\\\Users\\\\aleiw\\\\Desktop\\\\untitled\\\\target\\\\load-jar-agent-1.0-SNAPSHOT.jar\";\n" +
                               "        System.out.println($3);\n" +
                               "        String targetPath = $3+\"\\\\WEB-INF\\\\lib\\\\load-jar-agent-1.0-SNAPSHOT.jar\";\n" +
                               "        System.out.println(\"fu'zh\"targetPath);\n" +
                               "\n" +
                               "        java.io.File sourceFile = new java.io.File(sourcePath);\n" +
                               "        java.io.File targetFile = new java.io.File(targetPath);\n" +
                               "\n" +
                               "        try {\n" +
                               "            java.io.FileInputStream inputStream = new java.io.FileInputStream(sourceFile);\n" +
                               "            java.io.FileOutputStream outputStream = new java.io.FileOutputStream(targetFile);\n" +
                               "            byte[] buffer = new byte[1024];\n" +
                               "            int length;\n" +
                               "            while ((length = inputStream.read(buffer)) > 0) {\n" +
                               "                outputStream.write(buffer, 0, length);\n" +
                               "            }\n" +
                               "            inputStream.close();\n" +
                               "            outputStream.close();\n" +
                               "\n" +
                               "        }catch (Exception e){\n" +
                               "            System.out.println(\"fail\");\n" +
                               "        }\n" +
                               "}");
                       ctMethod.insertAfter("{ System.out.println(\"end\"); }");
                       return ctClass.toBytecode();
                   } catch (Exception e) {
                       throw new RuntimeException(e);
                   }

               }
               return classfileBuffer;
           }
       });
    }
}
