plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.crbtjetcompose.core.ui"
}

dependencies {
    api(libs.androidx.metrics)
    api(projects.core.designsystem)
    implementation(libs.coil.kt)
    implementation(libs.coil.kt.compose)
    api(projects.core.model)
    implementation(libs.libphonenumber)
    implementation(libs.androidx.auto.fill)
    implementation(libs.country.code.picker)
    implementation(libs.play.services.auth.api.phone)
}