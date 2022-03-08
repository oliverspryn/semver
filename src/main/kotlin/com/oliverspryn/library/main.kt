package com.oliverspryn.library

/**
 * @suppress
 */
fun main() {

    if (Semver("1.0.0") == Semver("1.0.0")) {
        println("1.0.0 == 1.0.0")
    }

    if (Semver("1.0.0").equals("1.0.0")) {
        println("1.0.0 == 1.0.0")
    }

    if (!Semver("1.0.0").equals("Something else")) {
        println("1.0.0 != Something else")
    }

    if (Semver("1.0.0") != Semver("2.0.0")) {
        println("1.0.0 != 2.0.0")
    }

    if (Semver("1.0.0") < Semver("2.0.0")) {
        println("1.0.0 < 2.0.0")
    }

    if (Semver("2.0.0") <= Semver("2.0.0")) {
        println("2.0.0 <= 2.0.0")
    }

    if (Semver("2.1.0") > Semver("2.0.36")) {
        println("2.1.0 > 2.0.36")
    }

    if (Semver("2.1.0") >= Semver("2.0.36")) {
        println("2.1.0 >= 2.0.36")
    }

    if (Semver("2.1.0-beta") == Semver("2.1.0-beta.0")) {
        println("2.1.0-beta == 2.1.0-beta.0")
    }

    if (Semver("2.1.0-beta.1") != Semver("2.1.0-beta.0")) {
        println("2.1.0-beta.1 != 2.1.0-beta.0")
    }

    if (Semver("2.1.0-beta") > Semver("2.1.0-alpha.10")) {
        println("2.1.0-beta > 2.1.0-alpha.10")
    }

    if (Semver("2.1.0-beta.1") > Semver("2.1.0-beta")) {
        println("2.1.0-beta.1 > 2.1.0-beta")
    }

    if (Semver("2.1.0-rc.11") < Semver("2.1.0")) {
        println("2.1.0-rc.11 < 2.1.0")
    }

    println(Semver("2.01.000100"))
    println(Semver("1.0.0-alpha.0"))
    println(Semver("1.0.00-ALphA.1"))

    try {
        Semver("Something else")
    } catch (e: IllegalArgumentException) {
        println(e.message)
    }
}
