plugins {
    alias(libs.plugins.crbt.android.library)
    alias(libs.plugins.crbt.android.hilt)
}

android {
    namespace = "com.example.crbtjetcompose.coredatastore.test"
}

dependencies {

    implementation(libs.hilt.android.testing)
    implementation(projects.core.common)
    implementation(projects.core.datastore)
}