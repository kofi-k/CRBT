plugins {
    alias(libs.plugins.crbt.android.library)
    alias(libs.plugins.crbt.android.library.jacoco)
    alias(libs.plugins.crbt.android.hilt)
}

android {
    namespace = "com.itengs.crbt.core.datastore"
    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {

    api(libs.androidx.dataStore.core)
    api(projects.core.datastoreProto)
    api(projects.core.model)
    implementation(projects.core.common)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(projects.core.datastoreTest)
}