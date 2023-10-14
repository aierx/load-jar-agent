package com.cn.agent;

import java.net.URL;
import java.net.URLClassLoader;

public class Main {

    public static void main(String[] args) throws Exception{
        URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{new URL("file:/C:\\Users\\aleiw\\Desktop\\untitled\\src\\processor-1.0-SNAPSHOT.jar")});
        Thread.currentThread().setContextClassLoader(urlClassLoader);
        Thread.currentThread().getContextClassLoader().loadClass("com.cn.agent.Student");
    }

}
