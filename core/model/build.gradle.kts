plugins {
    alias(libs.plugins.crbt.jvm.library)
}

dependencies {
    api(libs.kotlinx.datetime)
    implementation(libs.androidx.lifecycle.runtimeCompose)
}
