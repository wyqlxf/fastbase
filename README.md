# fastbase

[![Travis](https://img.shields.io/badge/miniSdk-14%2B-blue.svg)]()　[![Travis](https://img.shields.io/badge/author-wangyongqi-orange.svg)]()　[![Travis](https://img.shields.io/github/license/wyqlxf/fastbase.svg)](https://github.com/wyqlxf/fastbase/blob/master/LICENSE)　|　[简体中文文档](https://github.com/wyqlxf/fastbase/blob/master/README_CN.md)

fastbase is a powerful base library for Android apps.<br>
![image](https://github.com/wyqlxf/fastbase/blob/master/blob/master/image/fastbase_logo.png)
<br>

fastbase is responsible for the implementation of the base library. It properly encapsulates some of the commonly used methods in Android development, and solves some practical problems in development, avoiding repetitive wheel making during the development process, which can greatly improve development efficiency. You only need to simply use the code. Call the relative method.<br>

## Download
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

## How do I use fastbase?
```java
// Initialize the SDK
FastApp.init(this);
// Open log
FastApp.setLogEnabled(true);
```
See the [Demo](https://github.com/wyqlxf/fastbase/blob/master/app/src/main/java/com/wyq/fast/demo/MainActivity.java) file for details.　click to download[FastDemo.apk](https://raw.githubusercontent.com/wyqlxf/fastbase/master/app/release/FastDemo.apk)<br>
<br>
![image](https://github.com/wyqlxf/fastbase/blob/master/blob/master/image/demo_shot.png)

## License
MIT License, See the [LICENSE](https://github.com/wyqlxf/fastbase/blob/master/LICENSE) file for details.<br>
