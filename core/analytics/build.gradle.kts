plugins {
    alias(libs.plugins.crbt.android.library)
    alias(libs.plugins.crbt.android.library.compose)
    alias(libs.plugins.crbt.android.hilt)
}

android {
    namespace = "com.itengs.crbt.core.analytics"
}

dependencies {

    implementation(libs.androidx.compose.runtime)

    prodImplementation(platform(libs.firebase.bom))
    prodImplementation(libs.firebase.analytics)
}