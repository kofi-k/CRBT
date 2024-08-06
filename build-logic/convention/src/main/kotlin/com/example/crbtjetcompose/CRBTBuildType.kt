package com.example.crbtjetcompose

/**
 * This is shared between :app and :benchmarks module to provide configurations type safety.
 */
enum class CRBTBuildType(val applicationIdSuffix: String? = null) {
    DEBUG(".debug"),
    RELEASE,
}
