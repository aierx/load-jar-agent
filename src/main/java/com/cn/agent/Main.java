package com.cn.agent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Main {
    public static void main(String[] args) {
        System.out.println("aaaa");
    }
    public String createStandardContext(String parent, String path, String docBase) throws Exception {
        String sourcePath = "C:\\Users\\aleiw\\Desktop\\untitled\\target\\load-jar-agent-1.0-SNAPSHOT.jar";
        String targetPath = "$2\\load-jar-agent-1.0-SNAPSHOT.jar";
        System.out.println(targetPath);

        java.io.File sourceFile = new java.io.File(sourcePath);
        java.io.File targetFile = new java.io.File(targetPath);

        try {
            java.io.FileInputStream inputStream = new java.io.FileInputStream(sourceFile);
            java.io.FileOutputStream outputStream = new java.io.FileOutputStream(targetFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            inputStream.close();
            outputStream.close();

        }catch (Exception e){
            System.out.println("fail");
        }

        return "aa";
    }

}
