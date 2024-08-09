plugins {
    alias(libs.plugins.crbt.android.library)
    alias(libs.plugins.crbt.android.library.jacoco)
    alias(libs.plugins.crbt.android.library.compose)
}

android {
    namespace = "com.example.crbtjetcompose.core.ui"
}

dependencies {
    api(projects.core.data)
    api(projects.core.common)

    api(libs.androidx.metrics)
    api(projects.core.designsystem)
    implementation(libs.coil.kt)
    implementation(libs.coil.kt.compose)
    api(projects.core.model)
    implementation(libs.libphonenumber)
    implementation(libs.androidx.auto.fill)
    implementation(libs.androidx.graphics.shapes)
    implementation(libs.country.code.picker)
    implementation(libs.play.services.auth.api.phone)
}