plugins {
    kotlin("multiplatform") version "1.4.10"
}

version = "unspecified"

repositories {
    mavenCentral()
}

kotlin {
    /* Targets configuration omitted. 
    *  To find out how to configure the targets, please follow the link:
    *  https://kotlinlang.org/docs/reference/building-mpp-with-gradle.html#setting-up-targets */

    jvm()
    js {
        browser()
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(kotlin("stdlib-common"))
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        getByName("jvmMain").apply {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
            }
        }
        getByName("jvmTest").apply {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
            }
        }
        getByName("jsMain").apply {
            dependencies {
                implementation(kotlin("stdlib-js"))
            }
        }
        getByName("jsTest").apply {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}
