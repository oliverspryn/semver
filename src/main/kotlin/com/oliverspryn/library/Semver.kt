package com.oliverspryn.library

class Semver(version: String) {

    data class Parts(
        val major: Int,
        val minor: Int,
        val patch: Int
    )

    private var parsed = Parts(0, 0, 0)

    init {
        val parts = version.split(".")

        if (parts.size != 3) {
            throw IllegalArgumentException("$version does not conform to semver conventions")
        }

        parsed = Parts(
            major = parts[0].toInt(),
            minor = parts[1].toInt(),
            patch = parts[2].toInt()
        )
    }

    operator fun compareTo(semver: Semver): Int {
        val theseParts = arrayOf(parsed.major, parsed.minor, parsed.patch)
        val thoseParts = arrayOf(semver.parsed.major, semver.parsed.minor, semver.parsed.patch)

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

        val theseParts = arrayOf(parsed.major, parsed.minor, parsed.patch)
        val thoseParts = arrayOf(semver.parsed.major, semver.parsed.minor, semver.parsed.patch)

        for (i in 0..2) {
            if (theseParts[i] != thoseParts[i]) {
                return false
            }
        }

        return true
    }

    override fun toString(): String {
        return "${parsed.major}.${parsed.minor}.${parsed.patch}"
    }
}
