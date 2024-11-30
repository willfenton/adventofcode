plugins {
    kotlin("jvm") version "2.1.0"
    application
    id("org.jlleitschuh.gradle.ktlint") version "12.1.2"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(23)
}

application {
    mainClass.set("MainKt")
}

sourceSets {
    main {
        kotlin {
            exclude("**/aoc2021/**")
        }
    }
}

tasks.withType<JavaExec> {
    jvmArgs("-ea")
}

ktlint {
    version.set("1.4.1")
}
