plugins {
    alias(libs.plugins.crbt.android.feature)
    alias(libs.plugins.crbt.android.library.jacoco)
    alias(libs.plugins.crbt.android.library.compose)
}

android {
    namespace = "com.itengs.crbt.feature.subscription"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.ui)
    implementation(libs.androidx.graphics.shapes)
    implementation(projects.core.domain)
    implementation(libs.androidx.constraintlayout.compose)
}