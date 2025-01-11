plugins {
    alias(libs.plugins.crbt.android.library)
    alias(libs.plugins.crbt.android.library.jacoco)
    alias(libs.plugins.crbt.android.library.compose)
    alias(libs.plugins.crbt.android.hilt)
}

android {
    namespace = "com.itengs.crbt.core.ui"
}

dependencies {
    api(projects.core.data)
    api(projects.core.common)
    implementation(projects.core.analytics)
    implementation(projects.core.domain)

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
    implementation(libs.androidx.graphics.shapes)
    implementation(libs.androidx.media3.common)
    implementation(libs.exoplayer.core)
    implementation(libs.exoplayer.ui)
    implementation(libs.extension.mediasession)
    implementation(libs.media3.exoplayer)
    implementation(libs.media3.session)
    implementation(libs.androidx.browser)
    implementation(libs.androidx.activity.compose)
    implementation(libs.palette.ktx)
    implementation(libs.voipUssd)
    implementation(libs.lottie.compose)

    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.material)

}