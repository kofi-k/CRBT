plugins {
    alias(libs.plugins.crbt.android.library)
    alias(libs.plugins.crbt.android.library.jacoco)
    alias(libs.plugins.crbt.android.hilt)
    id("kotlinx-serialization")
}

android {
    namespace = "com.example.crbtjetcompose.core.data"
}

dependencies {
    implementation(libs.play.services.auth.api.phone)
    api(projects.core.common)
    api(projects.core.network)
    api(projects.core.datastore)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.room.ktx)
    implementation(libs.androidx.media3.common)
    implementation(libs.exoplayer.core)
    implementation(libs.exoplayer.ui)
    implementation(libs.extension.mediasession)
    implementation(libs.media3.exoplayer)
    implementation(libs.media3.session)
}