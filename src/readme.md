在src目录下面有一个processor-1.0-SNAPSHOT.jar包，只有将他添加springboot项目的依赖里面将会输出一段文字。
对于web项目同样也可以这样做，但更为简便方式是直接将这个包复制到META-INF/lib目录下面。以上是我们在日常开发
过程中的常规操作。在开发过程中我们会遇到如下常见，对于一个包，我们只在自己的机器上面开发会使用到，
但我们部署到测试环境可能就不需要这个包。有没有什么办法能做到这件事情呢？假设我们的项目是使用maven进行包管理，我们能想到
将这个包加入到maven的依赖中，这样确实可以实现我们的需求，但是这样需要修改pom.xml，即使你可以将这个文件
添加到skipworktree中去，不让git追踪这个文件，但是也显得比较麻烦。第二就是，我们可以直接在idea工具里面增加一个依赖，但是你的项目
目前是由maven管理依赖，也就是说当你下次更新依赖可能就会把当前的这个依赖给丢弃掉。
在添加依赖的过程中，idea也会提示 moudle 'xxx' is imported from Maven. Any changes made in
its configuration might be lost after reimporting.第三种，就是在idea启动项目时候手动指定
classpath，但是这种指定会覆盖idea原由的classpath导致应用无法运行（可能有更好的方法直接追加一个classpath，如果有可以issue讨论）。
第四种，曲线救国的方法是，agent也会进入classpath，把你的包假装成为一个agent，添加到jvm参数中去。

该项目就是为解决这问题而来的：

对于springboot项目，我们在启动的过程中，只需替换当前线程classloader即可

配置jvm参数

```shell
-javaagent:C:\Users\aleiw\Desktop\untitled\target\load-jar-agent-1.0-SNAPSHOT.jar=filePath=C:\Users\aleiw\Desktop\untitled\src\processor-1.0-SNAPSHOT.jar;replaceClassLoader=true
```
replaceClassLoader  表示需要替换当启动线程的classloader

filePath 你需要加载到应用的jar是什么


tomcat & jetty项目

找到当前启动的应用程序的webapp目录，将当前jar复制到WEB-INF/lib目录中去。因为tomcat和jetty在启动应用的时候
和隔离class类，采取了自定义classloader，直接替换启动线程的classloader或者是启动应用线程的classloader
都不好使。当前采取复制这种简便的做法。这个jvm参数的配置只要配置filePath参数即可，replaceClassLoader可不配置

```shell
-javaagent:C:\Users\aleiw\Desktop\untitled\target\load-jar-agent-1.0-SNAPSHOT.jar=filePath=C:\Users\aleiw\Desktop\untitled\src\processor-1.0-SNAPSHOT.jar
```