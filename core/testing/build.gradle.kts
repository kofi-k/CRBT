plugins {
    alias(libs.plugins.crbt.android.library)
    alias(libs.plugins.crbt.android.library.compose)
    alias(libs.plugins.crbt.android.hilt)
}

android {
    namespace = "com.example.crbtjetcompose.core.testing"
}

dependencies {
    api(projects.core.data)
    api(projects.core.model)

    debugApi(libs.androidx.compose.ui.testManifest)
    implementation(libs.androidx.test.rules)
    implementation(libs.hilt.android.testing)
    implementation(libs.kotlinx.coroutines.test)
    implementation(libs.kotlinx.datetime)
    implementation(projects.core.common)
    implementation(projects.core.designsystem)
}