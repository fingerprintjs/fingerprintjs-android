buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.4.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Constants.kotlinVersion}")
        classpath("com.github.dcendents:android-maven-gradle-plugin:2.1")
    }
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}
