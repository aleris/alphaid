import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.0"
    `java-library`
    `maven-publish`
    signing
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
}

group = "com.semanticmart.alphaid"
version = "0.0.1"

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

tasks {
    "sourcesJar"(Jar::class) {
        classifier = "sources"
        from(java.sourceSets["main"].allSource)
        dependsOn("classes")
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            pom {
                name.set("alphaid")
                description.set("Random pseudo-unique string id generator with a default web friendly alphabet.")
                url.set("https://github.com/aleris/alphaid")
                licenses {
                    license {
                        name.set("MIT")
                        url.set("https://github.com/aleris/alphaid/blob/main/LICENSE.md")
                    }
                }
                developers {
                    developer {
                        id.set("aleris")
                    }
                }
                scm {
                    connection.set("https://github.com/aleris/alphaid.git")
                    url.set("https://github.com/aleris/alphaid")
                }
            }
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications["mavenJava"])
}

nexusPublishing {
    repositories {
        sonatype {
            stagingProfileId.set("9bec3460265011")
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
        }
    }
}
