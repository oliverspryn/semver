package com.oliverspryn.library.models

import java.lang.NumberFormatException

abstract class PreReleaseVersion(
    val name: String? = null,
    val number: Int = 0,
    private val weight: Int
) {

    private object Names {
        const val alpha = "alpha"
        const val beta = "beta"
        const val rc = "rc"
    }

    // region Overloaded Operators

    override fun equals(other: Any?): Boolean {
        val version = other as? PreReleaseVersion ?: return false

        val namesMatch = version.name?.toLowerCase()?.trim() == name?.toLowerCase()?.trim()
        val numbersMatch = version.number == number

        return namesMatch && numbersMatch
    }

    // endregion

    // region Required Additional Overrides

    override fun hashCode() = toString().hashCode()

    override fun toString(): String {
        if (name.isNullOrBlank()) {
            return "" // For "dev" and "release", since "-dev" or "-release" are not valid semver pre-release versions, the convention is just blank
        }

        return if (number == 0) {
            name // e.g. alpha
        } else {
            "$name.$number" // e.g. alpha.1
        }
    }

    // endregion

    object Development : PreReleaseVersion(
        weight = 0
    )

    class Alpha(
        number: Int
    ) : PreReleaseVersion(
        name = Names.alpha,
        number = number,
        weight = 1
    )

    class Beta(
        number: Int
    ) : PreReleaseVersion(
        name = Names.beta,
        number = number,
        weight = 2
    )

    class ReleaseCandidate(
        number: Int
    ) : PreReleaseVersion(
        name = Names.rc,
        number = number,
        weight = 4
    )

    object Release : PreReleaseVersion(
        weight = 8
    )

    companion object {

        private data class Parts(
            val name: String,
            val number: Int
        )

        fun parse(version: String, parsedVersion: Version): PreReleaseVersion {

            // Example: https://regex101.com/r/bmu8hK/1

            val pattern = "(?<=-)[a-zA-Z0-9\\-.]*".toRegex()
            val preReleaseVersionString =
                pattern.find(version)?.value?.toLowerCase()?.trim() // e.g. Given: 1.0.0-rc.1+sha.5114f85, extract: rc.1

            // Given: 0.0.1 or 1.0.0, or something else without a pre-release version

            if (preReleaseVersionString.isNullOrEmpty()) {
                return if (parsedVersion.isDev()) { // e.g. 0.0.1
                    Development
                } else { // e.g. 1.0.0
                    Release
                }
            }

            val parts = splitIntoParts(preReleaseVersionString)

            return when {
                parsedVersion.isDev() -> Development
                parts.name == Names.alpha -> Alpha(parts.number)
                parts.name == Names.beta -> Beta(parts.number)
                parts.name == Names.rc -> ReleaseCandidate(parts.number)
                else -> Release
            }
        }

        private fun splitIntoParts(preReleaseVersionString: String): Parts {

            val preReleaseVersionParts = preReleaseVersionString.split(".").map {
                try {
                    it.toInt().toString() // Removes leading zeros from numbers
                } catch (e: NumberFormatException) {
                    it // Cannot perform .toInt(), must be a string like "alpha" or "beta"
                }
            }

            val name = preReleaseVersionParts.firstOrNull()
                ?: throw IllegalArgumentException("The pre-release version is missing a name, e.g.: alpha, beta, or rc")

            val number = try {
                if (preReleaseVersionParts.count() == 1) { // e.g. alpha
                    0
                } else {
                    preReleaseVersionParts[1].toInt() // e.g. alpha.1
                }
            } catch (e: NumberFormatException) {
                throw IllegalArgumentException("The pre-release version number cannot be read")
            }

            return Parts(
                name = name,
                number = number
            )
        }
    }
}
