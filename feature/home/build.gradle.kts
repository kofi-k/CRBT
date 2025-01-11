plugins {
    alias(libs.plugins.crbt.android.feature)
    alias(libs.plugins.crbt.android.library.jacoco)
    alias(libs.plugins.crbt.android.library.compose)
}

android {
    namespace = "com.itengs.crbt.feature.home"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(libs.androidx.graphics.shapes)
    implementation(libs.palette.ktx)
    implementation(libs.voipUssd)
}