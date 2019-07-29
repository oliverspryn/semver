package com.oliverspryn.library

import com.oliverspryn.library.models.PreReleaseVersion
import com.oliverspryn.library.models.Version

class Semver(version: String) {

    private val parsedVersion = Version.parse(version)
    private val parsedPreReleaseVersion: PreReleaseVersion

    init {
        parsedPreReleaseVersion = PreReleaseVersion.parse(version, parsedVersion)
    }

    // region Overloaded Operators

    operator fun compareTo(semver: Semver): Int {
        val versionCompare = parsedVersion.compareTo(semver.parsedVersion)
        if (versionCompare != 0) return versionCompare

        val preReleaseVersionCompare = parsedPreReleaseVersion.compareTo(semver.parsedPreReleaseVersion)
        if (preReleaseVersionCompare != 0) return preReleaseVersionCompare

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

        return parsedVersion == semver.parsedVersion && parsedPreReleaseVersion == semver.parsedPreReleaseVersion
    }

    // endregion

    // region Required Additional Overrides

    override fun hashCode() = toString().hashCode()

    override fun toString(): String {
        val builder = StringBuilder()
        builder.append(parsedVersion.toString())

        if (!parsedPreReleaseVersion.toString().isBlank()) builder.append("-$parsedPreReleaseVersion")

        return builder.toString()
    }

    // endregion
}
