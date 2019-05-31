package com.oliverspryn.library

import java.lang.NumberFormatException

class Semver(version: String) {

    private data class Parts(
        val version: Version,
        val preReleaseVersion: PreReleaseVersion
    )

    private object Regex {
        val preReleaseVersion = "(?<=-)[a-zA-Z0-9\\-.]*".toRegex()
    }

    private data class Version(
        val major: Int,
        val minor: Int,
        val patch: Int
    ) {
        override fun toString(): String {
            return "$major.$minor.$patch"
        }
    }

    private data class PreReleaseVersion(
        val parts: List<String>? = null,
        val isDev: Boolean = false,
        val isAlpha: Boolean = false,
        val isBeta: Boolean = false,
        val isReleaseCandidate: Boolean = false,
        val isRelease: Boolean = false
    ) {
        override fun toString(): String {
            if (parts.isNullOrEmpty()) return ""
            val builder = StringBuilder()

            for (part in parts) {
                try {
                    builder.append("${part.toInt()}.")
                } catch (e: NumberFormatException) {
                    builder.append("$part.")
                }
            }

            return builder.toString().trim('.')
        }
    }

    private val parsed: Parts

    init {
        val parsedVersion = parseVersion(version)

        parsed = Parts(
            version = parsedVersion,
            preReleaseVersion = parsePreReleaseType(version, parsedVersion)
        )
    }

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

    override fun hashCode() = toString().hashCode()

    override fun toString(): String {
        val builder = StringBuilder()
        builder.append(parsed.version)

        if (!parsed.preReleaseVersion.toString().isBlank()) builder.append("-${parsed.preReleaseVersion}")

        return builder.toString()
    }

    private fun parseVersion(version: String): Version {
        val versionOnly = version.split("-").first()

        val parts = versionOnly.split(".").map {
            try {
                it.toInt()
            } catch (e: NumberFormatException) {
                throw IllegalArgumentException("'$version' does not conform to semver conventions, since '$it' is not a number")
            }
        }

        if (parts.size != 3) {
            throw IllegalArgumentException("$version does not conform to semver conventions with parts containing major.minor.patch")
        }

        for (i in 0..2) {
            if (parts[i] < 0) {
                throw IllegalArgumentException("Semver does not permit negative versions")
            }
        }

        return Version(
            major = parts[0],
            minor = parts[1],
            patch = parts[2]
        )
    }

    private fun parsePreReleaseType(version: String, parsedVersion: Version): PreReleaseVersion {

        val pattern = Regex.preReleaseVersion
        val preReleaseVersion = pattern.find(version)?.value?.toLowerCase()?.trim()

        val preReleaseVersionParts = pattern.find(version)?.value?.split(".")?.map {
            try {
                it.toInt().toString() // Removes leading zeros
            } catch (e: NumberFormatException) {
                it
            }
        }

        // 2.0.0-alpha.beta is a valid pre-release type, so check for multiple pre-release identifiers

        return PreReleaseVersion(
            parts = preReleaseVersionParts,
            isDev = parsedVersion.major == 0,
            isAlpha = preReleaseVersion?.contains(ReleaseType.Alpha.name) == true,
            isBeta = preReleaseVersion?.contains(ReleaseType.Beta.name) == true,
            isReleaseCandidate = preReleaseVersion?.contains(ReleaseType.ReleaseCandidate.name) == true,
            isRelease = preReleaseVersion.isNullOrEmpty()
        )
    }
}
