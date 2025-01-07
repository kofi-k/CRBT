plugins {
    alias(libs.plugins.crbt.android.feature)
    alias(libs.plugins.crbt.android.library.jacoco)
    alias(libs.plugins.crbt.android.library.compose)
}

android {
    namespace = "com.example.crbtjetcompose.feature.profile"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.core.ui)
    implementation(libs.androidx.graphics.shapes)
    implementation(libs.coil.kt.compose)
    implementation(libs.androidx.appcompat)
}