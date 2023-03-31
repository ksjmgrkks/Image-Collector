buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Build.androidBuildTools)
        classpath(Build.hiltAndroidGradlePlugin)
        classpath(Build.kotlinGradlePlugin)
    }
}

plugins {
    id("org.jetbrains.kotlin.android") version Kotlin.version apply false
    id("com.google.dagger.hilt.android") version DaggerHilt.version apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}