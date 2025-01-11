plugins {
    alias(libs.plugins.crbt.android.library)
    alias(libs.plugins.crbt.android.library.jacoco)
    alias(libs.plugins.crbt.android.hilt)
}

android {
    namespace = "com.itengs.crbt.core.domain"
}

dependencies {
    implementation(projects.core.model)
    implementation(projects.core.data)
}