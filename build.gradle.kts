import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `maven-publish`
    signing

    id("org.jetbrains.dokka") version Versions.KOTLIN
    kotlin("jvm") version Versions.KOTLIN
}

group = CentralRepository.Artifact.GROUP_ID
version = CentralRepository.Artifact.VERSION

repositories {
    mavenCentral()
}

val compileKotlin: KotlinCompile by tasks

compileKotlin.kotlinOptions {
    jvmTarget = Versions.JVM
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = CentralRepository.Artifact.GROUP_ID
            artifactId = CentralRepository.Artifact.ID
            version = CentralRepository.Artifact.VERSION
            from(components["kotlin"])

            pom {
                name.set(CentralRepository.Project.NAME)
                description.set(CentralRepository.Project.DESCRIPTION)
                url.set(CentralRepository.Project.URL)

                developers {
                    developer {
                        id.set(CentralRepository.Developer.ID)
                        name.set(CentralRepository.Developer.NAME)
                        url.set(CentralRepository.Developer.URL)
                    }
                }

                issueManagement {
                    url.set("https://${CentralRepository.SCM.URL}/issues")
                }

                licenses {
                    license {
                        name.set(CentralRepository.License.NAME)
                        name.set(CentralRepository.License.URL)
                    }
                }

                scm {
                    connection.set("scm:git:git://${CentralRepository.SCM.URL}.git")
                    developerConnection.set("scm:git:ssh://${CentralRepository.SCM.URL}.git")
                    url.set("https://${CentralRepository.SCM.URL}")
                }
            }
        }
    }

    repositories {
        maven {
            val releaseUri = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            val snapshotUri = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            url = if (CentralRepository.Artifact.VERSION.endsWith("SNAPSHOT")) snapshotUri else releaseUri
        }
    }
}

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications["maven"])
}

tasks.dokkaHtml.configure {
    outputDirectory.set(buildDir.resolve("dokka"))
}
