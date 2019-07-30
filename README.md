# Semver

[![Build Status](https://dev.azure.com/oliverspryn/semver/_apis/build/status/oliverspryn.semver?branchName=develop)](https://dev.azure.com/oliverspryn/semver/_build/latest?definitionId=1&branchName=develop) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/939149ebbddd487b9beae339f0f2d836)](https://www.codacy.com/app/oliverspryn/semver?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=oliverspryn/semver&amp;utm_campaign=Badge_Grade) [![Maintainability](https://api.codeclimate.com/v1/badges/053a1e8e2bb58c5b2426/maintainability)](https://codeclimate.com/github/oliverspryn/semver/maintainability) [![Known Vulnerabilities](https://snyk.io//test/github/oliverspryn/semver/badge.svg?targetFile=build.gradle)](https://snyk.io//test/github/oliverspryn/semver?targetFile=build.gradle)

A Kotlin library for parsing and comparing version numbers which adhere to the [Semantic Versioning 2.0.0 standard](https://semver.org). This library handles the most common semver version and pre-release version conventions. Here are a few examples of versions the library can understand:

- `0.0.1`
- `1.0.0`
- `2.1.136`
- `2.0.0-alpha`
- `2.0.0-alpha.1`
- `2.0.0-beta.12`
- `2.0.0-rc.3`

Here are a few less common applications of the standard which are not supported by the library:

- `1.0.0-0.3.7`
- `1.0.0-x.7.z.92`
- `1.0.0-alpha.beta`

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

### Formatting

This library is also capable of performing some light formatting on the given input to conform it to the semver convention.

```kotlin
println(Semver("2.01.000100")) // 2.1.100
println(Semver("1.0.0-alpha.0")) // 1.0.0-alpha
println(Semver("1.0.00-ALphA.1")) // 1.0.0-alpha.1
```
