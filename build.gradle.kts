import org.gradle.api.tasks.testing.logging.TestExceptionFormat

buildscript {

    repositories {
        google()
        jcenter()

        maven(url = "https://maven.fabric.io/public")
    }

    dependencies {
        classpath(Libs.com_android_tools_build_gradle)
        classpath(Libs.kotlin_gradle_plugin)
        classpath(Libs.io_fabric_tools_gradle)
        classpath(Libs.google_services)
        classpath(Libs.navigation_safe_args_gradle_plugin)
        classpath(Libs.frodo_plugin)
        classpath(Libs.onesignal_gradle_plugin)
    }
}

plugins {
    // id("de.fayard.buildSrcVersions") version "0.3.2"
    id("com.diffplug.gradle.spotless") version "3.21.1"
    id("com.github.ben-manes.versions") version "0.21.0"
}

allprojects {
    repositories {
        google()
        jcenter()

        maven("https://jitpack.io")
        maven("https://maven.fabric.io/public")
        maven("https://dl.bintray.com/mockito/maven/")
    }

    tasks.withType(Javadoc::class).all {
        enabled = false
    }
}

tasks.register("cleanDir", Delete::class) {
    delete(rootProject.buildDir)
}

tasks.withType(Test::class) {
    testLogging {
        showExceptions = true
        showStandardStreams = true
        exceptionFormat = TestExceptionFormat.FULL
        events("started", "skipped", "passed", "failed")
    }
}