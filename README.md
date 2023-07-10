# Semver

[![Maven Central](https://img.shields.io/maven-central/v/com.oliverspryn.library/semver?label=Maven%20Central)](https://mvnrepository.com/artifact/com.oliverspryn.library/semver)
[![Build Project](https://github.com/oliverspryn/semver/actions/workflows/build.yml/badge.svg)](https://github.com/oliverspryn/semver/actions/workflows/build.yml)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/939149ebbddd487b9beae339f0f2d836)](https://app.codacy.com/gh/oliverspryn/semver/)
[![Maintainability](https://api.codeclimate.com/v1/badges/053a1e8e2bb58c5b2426/maintainability)](https://codeclimate.com/github/oliverspryn/semver/maintainability)
![Current Release](https://img.shields.io/github/v/release/oliverspryn/semver?label=Latest%20Release&sort=semver)

A Kotlin library for parsing and comparing version numbers which adhere to the [Semantic Versioning 2.0.0 standard](https://semver.org). This library handles the most common semver version and pre-release version conventions. Here are a few examples of versions the library can understand:

-   `0.0.1`
-   `1.0.0`
-   `2.1.136`
-   `2.0.0-alpha`
-   `2.0.0-alpha.1`
-   `2.0.0-beta.12`
-   `2.0.0-rc.3`

Here are a few less common applications of the standard which are not supported by the library:

-   `1.0.0-0.3.7`
-   `1.0.0-x.7.z.92`
-   `1.0.0-alpha.beta`

## Getting Started

To import this library into your app using Gradle, follow these steps.

For reference, the current version of this library is: [![Maven Central](https://img.shields.io/maven-central/v/com.oliverspryn.library/semver?label=Maven%20Central)](https://mvnrepository.com/artifact/com.oliverspryn.library/semver)

**build.gradle.kts**

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("com.oliverspryn.library:semver:<current version tag without the v>")
}
```

**build.gradle**

```groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation "com.oliverspryn.library:semver:<current version tag without the v>"
}
```

## Applications of this Library

There are a variety of applications for which this library may be used. This section shows some common use cases. All of these examples can be tried out in the [main.kt file](https://github.com/oliverspryn/semver/blob/develop/src/main/kotlin/com/oliverspryn/library/main.kt) of the code.

### Comparison

Perhaps the most powerful aspect of this library is the ability to use comparison operators to perform version checks:

```kotlin
Semver("1.0.0") == Semver("1.0.0") // true
Semver("1.0.0").equals("1.0.0") // true
Semver("1.0.0").equals("Something else") // false
Semver("1.0.0") != Semver("2.0.0") // true

Semver("1.0.0") < Semver("2.0.0") // true
Semver("2.0.0") <= Semver("2.0.0") // true
Semver("2.1.0") > Semver("2.0.36") // true
Semver("2.1.0") >= Semver("2.0.36") // true

Semver("2.1.0-beta") == Semver("2.1.0-beta.0") // true
Semver("2.1.0-beta.1") == Semver("2.1.0-beta.0") // false
Semver("2.1.0-beta") > Semver("2.1.0-alpha.10") // true
Semver("2.1.0-beta.1") > Semver("2.1.0-beta") // true
Semver("2.1.0-rc.11") < Semver("2.1.0") // true
```

### Components

You can also create `Semver` objects by passing in individual components of the version number:

```kotlin
val semver = Semver(1, 5, 12)
println(semver) // 1.5.12

println(semver.major) // 1
println(semver.minor) // 5
println(semver.patch) // 12

val semverPrerelease = Semver(1, 5, 13, "beta.1")
println(semverPrerelease) // 1.5.13-beta.1
println(semverPrerelease.preReleaseVersion) // beta.1

val semverString = Semver("1.5.14-alpha.10")
println(semverString) // 1.5.14-alpha.10

println(semverString.major) // 1
println(semverString.minor) // 5
println(semverString.patch) // 14
println(semverString.preReleaseVersion) // alpha.10
```

### Formatting

This library is also capable of performing some light formatting on the given input to conform it to the semver convention.

```kotlin
println(Semver("2.01.000100")) // 2.1.100
println(Semver("1.0.0-alpha.0")) // 1.0.0-alpha
println(Semver("1.0.00-ALphA.1")) // 1.0.0-alpha.1
```
