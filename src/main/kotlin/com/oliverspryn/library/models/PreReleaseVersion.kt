package com.oliverspryn.library.models

interface PreReleaseVersion {

    object Development : PreReleaseVersion {
        const val weight = 0
    }

    data class Alpha(
        val number: Int
    ) : PreReleaseVersion {
        companion object {
            const val name = "alpha"
            const val weight = 1
        }
    }

    data class Beta(
        val number: Int
    ) : PreReleaseVersion {
        companion object {
            const val name = "beta"
            const val weight = 2
        }
    }

    data class ReleaseCandidate(
        val number: Int
    ) : PreReleaseVersion {
        companion object {
            const val name = "rc"
            const val weight = 4
        }
    }

    object Release : PreReleaseVersion {
        const val weight = 8
    }
}
