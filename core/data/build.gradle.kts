plugins {
    alias(libs.plugins.crbt.android.library)
    alias(libs.plugins.crbt.android.library.jacoco)
    alias(libs.plugins.crbt.android.hilt)
    id("kotlinx-serialization")
}

android {
    namespace = "com.itengs.crbt.core.data"
}

dependencies {
    implementation(libs.play.services.auth.api.phone)
    api(projects.core.common)
    api(projects.core.network)
    api(projects.core.datastore)
    implementation(projects.core.analytics)

    implementation(libs.firebase.auth.ktx)
    implementation(libs.room.ktx)
    implementation(libs.androidx.media3.common)
    implementation(libs.exoplayer.core)
    implementation(libs.exoplayer.ui)
    implementation(libs.extension.mediasession)
    implementation(libs.media3.exoplayer)
    implementation(libs.media3.session)
    implementation(libs.play.services.location)
    implementation(libs.androidx.appcompat)

    implementation(libs.voipUssd)
}