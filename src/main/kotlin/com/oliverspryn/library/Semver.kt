package com.oliverspryn.library

import com.oliverspryn.library.models.PreReleaseVersion
import com.oliverspryn.library.models.Version

/**
 * A general-purpose processor for comparing version numbers and
 * pre-release versions that adhere to the
 * [Semantic Versioning 2.0.0 standard](https://semver.org).
 *
 * Here are a few examples of versions the library can understand:
 *
 *   - 0.0.1
 *   - 1.0.0
 *   - 2.1.136
 *   - 2.0.0-alpha
 *   - 2.0.0-alpha.1
 *   - 2.0.0-beta.12
 *   - 2.0.0-rc.3
 *
 * Here are a few less common applications of the standard which are not supported by the library:
 *
 *   - 1.0.0-0.3.7
 *   - 1.0.0-x.7.z.92
 *   - 1.0.0-alpha.beta
 */
class Semver {

    private val parsedVersion: Version
    private val parsedPreReleaseVersion: PreReleaseVersion

    /**
     * Constructs a [Semver] object from a version string.
     *
     * @param versionString The version string.
     * @constructor Create a fully-processed and understood representation of Semver.
     * @throws IllegalArgumentException Whenever the version string does not adhere to Semver standards.
     */
    constructor(versionString: String) {
        parsedVersion = Version.parse(versionString)
        parsedPreReleaseVersion = PreReleaseVersion.parse(versionString, parsedVersion)
    }

    /**
     * Constructs a [Semver] object from the [major], [minor], and
     * [patch] parts of a version.
     *
     * @param major The major version number.
     * @param minor The minor version number.
     * @param patch The patch version number.
     * @constructor Create a fully-processed and understood representation of Semver.
     */
    constructor(major: Int = 0, minor: Int = 0, patch: Int = 0) {
        parsedVersion = Version(major, minor, patch)
        parsedPreReleaseVersion = PreReleaseVersion.parse(parsedVersion.toString(), parsedVersion)
    }

    /**
     * Constructs a [Semver] object from the [major], [minor], [patch],
     * [preReleaseVersion] string parts of a version.
     *
     * @param major The major version number.
     * @param minor The minor version number.
     * @param patch The patch version number.
     * @param preReleaseVersion The pre-release version string, such as `alpha`, `beta`, `rc`, or `release`.
     * @constructor Create a fully-processed and understood representation of [Semver].
     */
    constructor(major: Int = 0, minor: Int = 0, patch: Int = 0, preReleaseVersion: String = "release") {
        parsedVersion = Version(major, minor, patch)
        val combinedVersion = "$parsedVersion-$preReleaseVersion"
        parsedPreReleaseVersion = PreReleaseVersion.parse(combinedVersion, parsedVersion)
    }

    /**
     * The major version number.
     */
    val major: Int
        get() = parsedVersion.major

    /**
     * The minor version number.
     */
    val minor: Int
        get() = parsedVersion.minor

    /**
     * The patch version number.
     */
    val patch: Int
        get() = parsedVersion.patch

    /**
     * The pre-release version string, such as `alpha`, `beta`, `rc`,
     * or `release`.
     */
    val preReleaseVersion: String
        get() = parsedPreReleaseVersion.toString()

    // region Overloaded Operators

    /**
     * Overloaded comparison operator to determine if two version strings
     * are equal or if one is greater than another.
     *
     * @param semver Another [Semver] object to do a comparison against
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
     * Accepts comparison against both [Semver] object and strings. This
     * class will attempt to cast [String] objects to [Semver] objects and
     * do comparison from there.
     *
     * @param other A [String] or [Semver] object to compare against
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
