package com.oliverspryn.library.models

import java.lang.NumberFormatException

data class Version(
    val major: Int,
    val minor: Int,
    val patch: Int
) {

    companion object {
        fun parse(version: String): Version {
            val versionOnly = version.split("-").firstOrNull() ?: ""
            val parts = splitIntoParts(versionOnly)

            if (parts.size != 3) {
                throw IllegalArgumentException("$version does not conform to semver conventions with parts containing major.minor.patch")
            }

            return Version(
                major = parts[0],
                minor = parts[1],
                patch = parts[2]
            )
        }

        private fun splitIntoParts(versionOnly: String) = versionOnly.split(".").map {
            try {
                val number = it.toInt()

                if (number < 0) {
                    throw IllegalArgumentException("Semver does not permit negative versions")
                }

                number
            } catch (e: NumberFormatException) {
                throw IllegalArgumentException("'$versionOnly' does not conform to semver conventions, since '$it' is not a number")
            }
        }
    }

    fun isDev() = major == 0

    override fun toString(): String {
        return "$major.$minor.$patch"
    }
}