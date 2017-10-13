import org.gradle.jvm.tasks.Jar

val junitVersion = "5.0.0"
val assertJVersion = "3.8.0"
val commonsIOVersion = "2.5"

group = "com.github.jntakpe"
version = "0.3.0"

buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:")
        classpath("org.junit.platform:junit-platform-gradle-plugin:1.0.0")
    }
}

plugins {
    kotlin("jvm") version "1.1.51"
}

apply {
    plugin("org.junit.platform.gradle.plugin")
}

dependencies {
    compile(kotlin("stdlib", "1.1.51"))
    testCompile("commons-io:commons-io:$commonsIOVersion")
    testCompile("org.assertj:assertj-core:$assertJVersion")
    testCompile("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntime("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    jcenter()
}

val jar: Jar by tasks

jar.apply {
    manifest.attributes.apply {
        put("Main-Class", "com.github.jntakpe.proxyswitcher.ProxySwitcherKt")
    }
    from(configurations.compile.map { if (it.isDirectory) it else zipTree(it) })
}