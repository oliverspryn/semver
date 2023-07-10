object CentralRepository {
    object Artifact {
        const val GROUP_ID = "com.oliverspryn.library"
        const val ID = "semver"
        const val VERSION = "1.1.0"
    }

    object Developer {
        const val ID = "oliverspryn"
        const val NAME = "Oliver Spryn"
        const val URL = "https://oliverspryn.com/"
    }

    object Project {
        const val DESCRIPTION =
            "A Kotlin library for parsing and comparing version numbers which adhere to the Semantic Versioning 2.0.0 standard."
        const val NAME = "Semver"
        const val URL = "https://oliverspryn.com/portfolio/semver"
    }

    object License {
        const val NAME = "MIT License"
        const val URL = "https://mit-license.org/"
    }

    object SCM {
        const val URL = "github.com/oliverspryn/semver" // Omit the protocol and trailing slash
    }
}
