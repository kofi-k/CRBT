plugins {
    alias(libs.plugins.crbt.android.library)
    alias(libs.plugins.crbt.android.library.jacoco)
    alias(libs.plugins.crbt.android.hilt)
    id("kotlinx-serialization")
}

android {
    namespace = "com.example.crbtjetcompose.core.data"
}

dependencies {
    implementation(libs.play.services.auth.api.phone)
    api(projects.core.common)
    api(projects.core.network)
}