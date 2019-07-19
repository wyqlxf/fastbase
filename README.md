# fast

## 使用步骤
### 1. 在project的build.gradle添加如下代码

	allprojects {
	    repositories {
	        ...
	        maven { url "https://jitpack.io" }
	    }
	}
  
### 2. 在Module的build.gradle添加依赖

    implementation 'com.github.wyqlxf:fastbase:1.0.0'
