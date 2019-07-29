package com.oliverspryn.library

import com.oliverspryn.library.models.PreReleaseVersion
import com.oliverspryn.library.models.Version
import java.lang.NumberFormatException

class Semver(version: String) {

    private data class Parts(
        val version: Version,
        val prePreReleaseVersion: PreReleaseVersion
    )

    private val parsed: Parts

    init {
        val parsedVersion = Version.parse(version)

        parsed = Parts(
            version = parsedVersion,
            prePreReleaseVersion = parsePreReleaseVersion(version, parsedVersion)
        )
    }

    // region Overloaded Operators

    operator fun compareTo(semver: Semver): Int {
        val theseParts = arrayOf(parsed.version.major, parsed.version.minor, parsed.version.patch)
        val thoseParts = arrayOf(semver.parsed.version.major, semver.parsed.version.minor, semver.parsed.version.patch)

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
        val semver: Semver = when (other) {
            is String -> try {
                Semver(other)
            } catch (e: IllegalArgumentException) {
                return false
            }
            is Semver -> other
            else -> return false
        }

        val theseParts = arrayOf(parsed.version.major, parsed.version.minor, parsed.version.patch)
        val thoseParts = arrayOf(semver.parsed.version.major, semver.parsed.version.minor, semver.parsed.version.patch)

        for (i in 0..2) {
            if (theseParts[i] != thoseParts[i]) {
                return false
            }
        }

        return true
    }

    // endregion

    // region Required Additional Overrides

    override fun hashCode() = toString().hashCode()

    override fun toString(): String {
        val builder = StringBuilder()
        builder.append(parsed.version)

        if (!parsed.prePreReleaseVersion.toString().isBlank()) builder.append("-${parsed.prePreReleaseVersion}")

        return builder.toString()
    }

    // endregion

    private fun parsePreReleaseVersion(version: String, parsedVersion: Version): PreReleaseVersion {

        // Example: https://regex101.com/r/bmu8hK/1

        val pattern = "(?<=-)[a-zA-Z0-9\\-.]*".toRegex()
        val preReleaseVersionString =
            pattern.find(version)?.value?.toLowerCase()?.trim() // e.g. Given: 1.0.0-rc.1+sha.5114f85, extract: rc.1

        // Given: 0.0.1 or 1.0.0, or something else without a pre-release version

        if (preReleaseVersionString.isNullOrEmpty()) {
            return if (parsedVersion.isDev()) { // e.g. 0.0.1
                PreReleaseVersion.Development
            } else { // e.g. 1.0.0
                PreReleaseVersion.Release
            }
        }

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

        return when {
            parsedVersion.isDev() -> PreReleaseVersion.Development
            name == PreReleaseVersion.Alpha.name -> PreReleaseVersion.Alpha(number)
            name == PreReleaseVersion.Beta.name -> PreReleaseVersion.Beta(number)
            name == PreReleaseVersion.ReleaseCandidate.name -> PreReleaseVersion.ReleaseCandidate(number)
            else -> PreReleaseVersion.Release
        }
    }
}
