plugins {
    alias(libs.plugins.crbt.android.library)
    alias(libs.plugins.crbt.android.library.compose)
    alias(libs.plugins.crbt.android.library.jacoco)
    alias(libs.plugins.roborazzi)
}

android {
    namespace = "com.example.crbtjetcompose.core.designsystem"
}

dependencies {

    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.foundation.layout)
    api(libs.androidx.compose.material.iconsExtended)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.ui.util)

    implementation(libs.coil.kt.compose)
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.androidx.graphics.shapes)
}