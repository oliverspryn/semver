package com.oliverspryn.library

import com.oliverspryn.library.models.PreReleaseVersion
import com.oliverspryn.library.models.Version

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
            prePreReleaseVersion = PreReleaseVersion.parse(version, parsedVersion)
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
}
