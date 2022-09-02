@file:Suppress("UnstableApiUsage")

pluginManagement {
    // 配置插件仓库
    repositories {
        maven { setUrl("https://s01.oss.sonatype.org/content/repositories/releases/") }
        maven { setUrl("https://maven.aliyun.com/repository/gradle-plugin/") }
        maven { setUrl("https://maven.aliyun.com/repository/public/") }
        maven { setUrl("https://maven.aliyun.com/repository/google/") }
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    // 配置只能在当前文件配置三方依赖仓库，否则编译异常退出
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    // 配置三方依赖仓库
    repositories {
        maven { setUrl("https://s01.oss.sonatype.org/content/repositories/releases/") }
        maven { setUrl("https://maven.aliyun.com/repository/public/") }
        maven { setUrl("https://maven.aliyun.com/repository/google/") }
        maven { setUrl("https://jitpack.io") }
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            from("io.github.wangjie0822:catalog:gradle.7.4.2-1.2.5")
        }
    }
}

// 项目包含的 Module
include(":convention")