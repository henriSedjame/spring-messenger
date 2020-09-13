rootProject.name = "spring-messenger"

pluginManagement {
    val springBootVersion = "2.4.0-M2"

    repositories {
        maven { url = uri("https://repo.spring.io/milestone") }
        maven { url = uri("https://repo.spring.io/snapshot") }
        gradlePluginPortal()
    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "org.springframework.boot") {
                useModule("org.springframework.boot:spring-boot-gradle-plugin:$springBootVersion")
            }
        }
    }

    plugins {
        val kotlinVersion = "1.4.10"
        id("org.jetbrains.kotlin.jvm") version kotlinVersion apply false
        id("org.jetbrains.kotlin.plugin.spring") version kotlinVersion apply false

        id("org.springframework.boot") version springBootVersion apply false
        id("io.spring.dependency-management") version "1.0.10.RELEASE" apply false
    }
}
include("shared", "backend", "bot", "frontend")
