plugins {
   alias(libs.plugins.crbt.android.feature)
    alias(libs.plugins.crbt.android.library.jacoco)
    alias(libs.plugins.crbt.android.library.compose)
}

android {
    namespace = "com.example.crbtjetcompose.feature.subscription"
}

dependencies {
     implementation(projects.core.data)
    implementation(projects.core.ui)
}