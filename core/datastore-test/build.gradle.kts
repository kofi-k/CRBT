plugins {
    alias(libs.plugins.crbt.android.library)
    alias(libs.plugins.crbt.android.hilt)
}

android {
    namespace = "com.itengs.crbt.coredatastore.test"
}

dependencies {

    implementation(libs.hilt.android.testing)
    implementation(projects.core.common)
    implementation(projects.core.datastore)
}