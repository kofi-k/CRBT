plugins {
    alias(libs.plugins.crbt.android.library)
    alias(libs.plugins.crbt.android.library.jacoco)
    alias(libs.plugins.crbt.android.hilt)
}

android {
    namespace = "com.example.crbtjetcompose.core.domain"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:data"))
}