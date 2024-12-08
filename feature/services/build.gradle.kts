plugins {
    alias(libs.plugins.crbt.android.feature)
    alias(libs.plugins.crbt.android.library.jacoco)
    alias(libs.plugins.crbt.android.library.compose)
}

android {
    namespace = "com.example.crbtjetcompose.feature.services"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.ui)
    implementation(libs.palette.ktx)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.mlkitTextRecognition)
    implementation(libs.cameraxCore)
    implementation(libs.cameraxCamera2)
    implementation(libs.cameraxLifecycle)
    implementation(libs.cameraxView)
    implementation(libs.cameraxExtensions)
    implementation(libs.cameraxVideo)
    implementation(libs.country.code.picker)
    implementation(projects.core.domain)
}