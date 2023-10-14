package com.cn.agent;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException, MalformedURLException {
        URL url = new URL("file:/C:\\Users\\aleiw\\Desktop\\untitled\\src\\processor-1.0-SNAPSHOT.jar");
        URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{url});

        System.out.println("aaaaaaaaaaaaa");
    }

}
