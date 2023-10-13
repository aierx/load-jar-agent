package com.cn.agent;
public class Main {

    public static void main(String[] args) throws ClassNotFoundException {
        System.setProperty("java.class.path", System.getProperty("java.class.path") + ";" + "C:\\Users\\aleiw" +
                "\\Desktop\\untitled\\target\\load-jar-agent-1.0-SNAPSHOT.jar");
        System.out.println(System.getProperty("java.class.path"));

        Class<?> aClass = Main.class.getClassLoader().loadClass("com.aiex.lei");

    }

}
