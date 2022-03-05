import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `maven-publish`
    signing

    kotlin("jvm") version "1.6.10"
}

object Meta {
    const val ARTIFACT_ID = "semver"
    const val GROUP_ID = "com.oliverspryn.library"
    const val JVM_VERSION = "1.8"
    const val VERSION = "1.0.0"

    const val DESCRIPTION =
        "A Kotlin library for parsing and comparing version numbers which adhere to the Semantic Versioning 2.0.0 standard."
    const val PROJECT_NAME = "Semver"
    const val PROJECT_URL = "https://oliverspryn.com/portfolio/semver"

    object License {
        const val NAME = "MIT License"
        const val URL = "https://mit-license.org/"
    }

    object Developer {
        const val ID = "oliverspryn"
        const val NAME = "Oliver Spryn"
        const val URL = "https://oliverspryn.com/"
    }
}

group = Meta.GROUP_ID
version = Meta.VERSION

repositories {
    mavenCentral()
}

val compileKotlin: KotlinCompile by tasks

compileKotlin.kotlinOptions {
    jvmTarget = Meta.JVM_VERSION
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

java {
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = Meta.GROUP_ID
            artifactId = Meta.ARTIFACT_ID
            version = Meta.VERSION
            from(components["kotlin"])

            pom {
                name.set(Meta.PROJECT_NAME)
                description.set(Meta.DESCRIPTION)
                url.set(Meta.PROJECT_URL)

                developers {
                    developer {
                        id.set(Meta.Developer.ID)
                        name.set(Meta.Developer.NAME)
                        url.set(Meta.Developer.URL)
                    }
                }

                licenses {
                    license {
                        name.set(Meta.License.NAME)
                        name.set(Meta.License.URL)
                    }
                }

                scm {
                    connection.set("scm:git:git://example.com/my-library.git")
                    developerConnection.set("scm:git:ssh://example.com/my-library.git")
                    url.set("http://example.com/my-library/")
                }
            }
        }
    }

    repositories {
        maven {
            val releaseUri = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            val snapshotUri = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            url = if (Meta.VERSION.endsWith("SNAPSHOT")) snapshotUri else releaseUri
        }
    }
}

signing {
    sign(publishing.publications["maven"])
}
