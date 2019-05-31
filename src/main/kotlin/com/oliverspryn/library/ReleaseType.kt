package com.oliverspryn.library

sealed class ReleaseType {
    object Development : ReleaseType() {
        const val weight = 0
    }

    object Alpha : ReleaseType() {
        const val name = "alpha"
        const val weight = 1
    }

    object Beta : ReleaseType() {
        const val name = "beta"
        const val weight = 2
    }

    object ReleaseCandidate : ReleaseType() {
        const val name = "rc"
        const val weight = 4
    }

    object Release : ReleaseType() {
        const val weight = 8
    }

    object Unknown : ReleaseType() {
        const val name = "unknown"
        const val weight = 0
    }
}
