package com.oliverspryn.library

import com.oliverspryn.library.models.PreReleaseVersion
import com.oliverspryn.library.models.Version

/**
 * A general-purpose processor for comparing version numbers that
 * adhere to the <a href="https://semver.org">Semantic Versioning 2.0.0 standard</a>.
 *
 * @param version The version string to compare
 * @constructor Create a fully-processed and understood representation of Semver to compare
 * @throws IllegalArgumentException Whenever the version string does not adhere to Semver standards
 */
class Semver(version: String) {

    private val parsedVersion = Version.parse(version)
    private val parsedPreReleaseVersion: PreReleaseVersion

    init {
        parsedPreReleaseVersion = PreReleaseVersion.parse(version, parsedVersion)
    }

    // region Overloaded Operators

    /**
     * Overloaded comparison operator to determine if two version strings
     * are equal or if one is greater than another.
     *
     * @param semver Another Semver object to do a comparison against
     * @return 0 when equal, 1 when this object is greater, or -1 when the other object is greater
     */
    operator fun compareTo(semver: Semver): Int {
        val versionCompare = parsedVersion.compareTo(semver.parsedVersion)
        if (versionCompare != 0) return versionCompare

        val preReleaseVersionCompare = parsedPreReleaseVersion.compareTo(semver.parsedPreReleaseVersion)
        if (preReleaseVersionCompare != 0) return preReleaseVersionCompare

        return 0
    }

    /**
     * Accepts comparison against both Semver object and strings. This
     * class will attempt to cast String objects to Semver objects and
     * do comparison from there.
     *
     * @param other A String or Semver object to compare against
     * @return Whether the two version stamps are the same
     */
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

    /**
     * @suppress
     */
    override fun hashCode() = toString().hashCode()

    /**
     * Returns a nicely formatted, standards-compliant representation of
     * the version string provided to the constructor.
     *
     * @return A formatted version string
     */
    override fun toString(): String {
        val builder = StringBuilder()
        builder.append(parsedVersion.toString())

        if (parsedPreReleaseVersion.toString().isNotBlank()) builder.append("-$parsedPreReleaseVersion")

        return builder.toString()
    }

    // endregion
}
