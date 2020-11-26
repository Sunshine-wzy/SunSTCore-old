import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.4.10"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("com.github.johnrengelman.shadow") version "5.2.0"
}

group = "io.github.sunshinewzy"
version = "1.0.0"

repositories {
    mavenLocal()
    mavenCentral()
    
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
    maven {
        url = uri("http://maven.imagej.net/content/groups/public")
        name = "imagej.public"
    }
    maven("http://maven.aliyun.com/nexus/content/groups/public")
    maven {
        name = "spigotmc-repo"
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
    maven {
        name = "sonatype"
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }

    jcenter()
}

dependencies {
    testImplementation(kotlin("test-junit"))

    implementation("com.google.code.gson:gson:2.8.2")
    implementation("org.scilab.forge:jlatexmath:1.0.7")
    implementation("io.netty:netty-all:4.1.48.Final")

    implementation(kotlin("stdlib-jdk8"))
    implementation("net.mamoe:mirai-core-qqandroid:1.3.0")
    compileOnly("org.spigotmc:spigot-api:1.12.2-R0.1-SNAPSHOT")

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks {
    jar {
        archiveBaseName.set("SunSTCore")
        archiveVersion.set(project.version.toString())
        destinationDirectory.set(file("F:/Kotlin/Workspace/SkyDream/cores"))
    }
    
    shadowJar {
        archiveBaseName.set("SunSTCore")
        archiveVersion.set(project.version.toString())
        destinationDirectory.set(file("F:/Kotlin/Workspace/SkyDream/cores"))
    }
}