import com.example.crbtjetcompose.CRBTBuildType

plugins {
    alias(libs.plugins.crbt.android.application)
    alias(libs.plugins.crbt.android.application.compose)
    alias(libs.plugins.crbt.android.application.flavors)
    alias(libs.plugins.crbt.android.application.jacoco)
    alias(libs.plugins.crbt.android.hilt)
    alias(libs.plugins.roborazzi)
}

android {
    namespace = "com.example.crbtjetcompose"

    defaultConfig {
        applicationId = "com.example.crbtjetcompose"
        versionCode = 1
        versionName = "1.0.0" // X.Y.Z; X = Major, Y = minor, Z = Patch level

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = CRBTBuildType.DEBUG.applicationIdSuffix
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
            excludes.add("META-INF/gradle/incremental.annotation.processors")
        }
    }
}

dependencies {

    implementation(projects.feature.payment)
    implementation(projects.feature.subscription)
    implementation(projects.feature.home)
    implementation(projects.feature.onboarding)
    implementation(projects.feature.services)
    implementation(projects.feature.profile)

    implementation(projects.core.ui)
    implementation(projects.core.common)
    implementation(projects.core.data)
    implementation(projects.core.model)
    implementation(projects.core.designsystem)


    implementation(libs.firebase.auth.ktx)
    implementation(libs.androidx.media3.common)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3.adaptive)
    implementation(libs.androidx.compose.material3.adaptive.layout)
    implementation(libs.androidx.compose.material3.adaptive.navigation)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.compose.runtime.tracing)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.profileinstaller)
    implementation(libs.androidx.tracing.ktx)
    implementation(libs.androidx.window.core)
    implementation(libs.kotlinx.coroutines.guava)
    implementation(libs.coil.kt)

    ksp(libs.hilt.compiler)

    // Tooling
    debugImplementation(libs.androidx.compose.ui.tooling)

    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.navigation.testing)
    androidTestImplementation(libs.androidx.compose.ui.test)
    androidTestImplementation(libs.hilt.android.testing)


}

dependencyGuard {
    configuration("prodReleaseRuntimeClasspath")
}