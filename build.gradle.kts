import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.0"
    id("com.glovoapp.semantic-versioning") version "1.1.8"
    `java-library`
    `maven-publish`
}

group = "com.semanticmart.alphaid"

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation("org.apache.commons:commons-math3:3.6.1")
    testImplementation(kotlin("test"))
    testImplementation("org.assertj:assertj-core:3.23.1")
}

java {
    withJavadocJar()
    withSourcesJar()
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.register("printSemanticVersion") {
    doLast {
        println("The project current version is ${project.semanticVersion.version}")
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/aleris/alphaid")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
            }
        }
    }

    publications {
        register<MavenPublication>("gpr") {
            from(components["java"])
        }
    }
}
