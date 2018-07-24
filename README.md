# Greendao3GradlePlugin
Greendao 3生成代码插件，非增量编译模式下，在macOS上运行，由于文件系统的差异，可能出现
生成的DaoSession和DaoMaster内容乱序，但是在Windows系统上运行，内容是按字母排序。同
时，greendao生成plugin支持增量编译，但是该特性生成的代码也会与全量生成的代码不一致。所
以为了解决双平台生成代码不一致，git不便管理的问题，为此基于greendao的plugin，创建了本
plugin。

该插件基于greendao gradle插件3.2.2版本（org.greenrobot:greendao-gradle-plugin:3.2.2）进行开发。

[![Download](https://api.bintray.com/packages/wrs/maven/greendao-gradle-plugin/images/download.svg) ](https://bintray.com/wrs/maven/greendao-gradle-plugin/_latestVersion)

# Add plugin to your project
插件已上传到jcenter。

```groovy
// In your root build.gradle file:
buildscript {

    repositories {
        // plugin already published to jcenter
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.1.3'
        // greendao
//        classpath 'org.greenrobot:greendao-gradle-plugin:3.2.2'
        // 使用基于greendao拓展的插件
        classpath 'com.lancewu:greendao-gradle-plugin:1.0.0'
    }
}

// In your app projects build.gradle file:
//apply plugin: 'org.greenrobot.greendao' // comment the greendao plugin
// use my plugin
apply plugin: 'com.lancewu.greendao'

dependencies {
    implementation 'org.greenrobot:greendao:3.2.2' // add library
}
```

**注意：代码生成插件要使用该插件，注释掉greendao的插件引入；但是依赖库用的还是greendao的插件。**

