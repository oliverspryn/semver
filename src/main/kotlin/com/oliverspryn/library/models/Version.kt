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

    // region Overloaded Operators

    operator fun compareTo(version: Version): Int {
        val theseParts = arrayOf(major, minor, patch)
        val thoseParts = arrayOf(version.major, version.minor, version.patch)

        for (i in 0..2) {
            if (theseParts[i] > thoseParts[i]) {
                return 1
            } else if (theseParts[i] < thoseParts[i]) {
                return -1
            }
        }

        return 0
    }

    override fun equals(other: Any?): Boolean {
        val version = other as? Version ?: return false
        return major == version.major && minor == version.minor && patch == version.patch
    }

    // endregion

    // region Required Additional Overrides

    override fun hashCode() = toString().hashCode()

    override fun toString(): String {
        return "$major.$minor.$patch"
    }

    // endregion
}