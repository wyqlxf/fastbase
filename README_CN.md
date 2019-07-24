# fastbase

[![Travis](https://img.shields.io/badge/miniSdk-14%2B-blue.svg)]()　[![Travis](https://img.shields.io/badge/author-wangyongqi-orange.svg)]()　[![Travis](https://img.shields.io/github/license/wyqlxf/fastbase.svg)](https://github.com/wyqlxf/fastbase/blob/master/LICENSE)

fastbase是Android应用程序的一个强大基础库<br>
![image](https://github.com/wyqlxf/fastbase/blob/master/blob/master/image/fastbase_logo.png)
<br>

fastbase负责基础库的实现，它合理的封装了安卓开发中常用的一些方法，并解决开发中一些实际问题，避免在开发过程中重复造轮子，可以大大提高开发效率，您只需要简单的在代码中调用相对于的方法即可。<br>


## 添加依赖
Gradle:

```groovy
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}

dependencies {
    implementation 'com.github.wyqlxf:fastbase:1.0.0'
}
```

您可以从GitHub发布页面[下载jar](https://github.com/wyqlxf/fastbase/releases).<br>

## 如何使用fastbase?
```java
FastApp.init(this); // 初始化SDK
FastApp.setLogEnabled(true); // 开启日志
```
有关详细信息，请参阅[演示文件](https://github.com/wyqlxf/fastbase/blob/master/app/src/main/java/com/wyq/fast/demo/MainActivity.java)　点击下载 [FastDemo.apk](https://raw.githubusercontent.com/wyqlxf/fastbase/master/app/release/FastDemo.apk)<br>
<br>
![image](https://github.com/wyqlxf/fastbase/blob/master/blob/master/image/demo_shot.png)

## 执照
MIT License, See the [LICENSE](https://github.com/wyqlxf/fastbase/blob/master/LICENSE) file for details.<br>
