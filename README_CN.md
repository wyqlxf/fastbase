# fastbase

[![Travis](https://img.shields.io/badge/miniSdk-14%2B-blue.svg)]()　[![Travis](https://img.shields.io/badge/author-wangyongqi-orange.svg)]()　[![Travis](https://img.shields.io/github/license/wyqlxf/fastbase.svg)](https://github.com/wyqlxf/fastbase/blob/master/LICENSE)

fastbase是Android应用程序的一个强大基础库<br>
![image](https://github.com/wyqlxf/fastbase/blob/master/blob/master/image/fastbase_logo.png)
<br>

fastbase负责基础库的实现，并解决项目开发中一些实际问题，避免在过程开发中重复造轮子，您只需在代码中简单的调用它即可<br>


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

You can download a jar from GitHub's [releases page](https://github.com/wyqlxf/fastbase/releases).<br>

## 如何使用fastbase?
    ``` java
    FastApp.init(this); // 初始化SDK
    FastApp.setLogEnabled(true); // 开启日志
    ```
See the [Demo](https://github.com/wyqlxf/fastbase/blob/master/app/src/main/java/com/wyq/fast/demo/MainActivity.java) file for details.<br>

## 执照
MIT License, See the [LICENSE](https://github.com/wyqlxf/fastbase/blob/master/LICENSE) file for details.<br>
