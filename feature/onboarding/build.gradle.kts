plugins {
    alias(libs.plugins.crbt.android.feature)
    alias(libs.plugins.crbt.android.library.jacoco)
    alias(libs.plugins.crbt.android.library.compose)
}

android {
    namespace = "com.example.crbtjetcompose.feature.onboarding"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.ui)
    implementation(projects.core.domain)
    implementation(libs.country.code.picker)
    implementation(libs.androidx.graphics.shapes)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.mockito.core)
    implementation(libs.androidx.appcompat)
}