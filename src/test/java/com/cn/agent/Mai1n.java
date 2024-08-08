package com.cn.agent;

/**
 * todo leiwenyong
 *
 * @Author leiwenyong
 * @Date 2024/8/8 16:09
 */
public class Mai1n {
    public static void main(String[] args) {
        String str = "$_";
        System.out.println("leiwenyong111");
        java.util.regex.Matcher m1 = java.util.regex.Pattern.compile("package +(.*);").matcher(str);
        java.util.regex.Matcher m2 = java.util.regex.Pattern.compile("class +(.*?) ").matcher(str);
        if (m1.find() && m2.find()) {
            String fileDir = System.getProperty("user.dir") + "/src/main/java/"+m1.group(1).replaceAll("\\.", "/")+"/"+ m2.group(1)+".java";
            java.io.File file = new java.io.File(fileDir);
            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                    //创建上级目录
                    file.getParentFile().mkdirs();
                }
                try {
                    //在上级目录里创建文件
                    file.createNewFile();
                    java.io.FileOutputStream fos = new java.io.FileOutputStream(file);
                    fos.write(str.getBytes(java.nio.charset.StandardCharsets.UTF_8));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("not match");
        }
    }
}
