import com.android.build.gradle.LibraryExtension
import com.itengs.crbt.configureGradleManagedDevices
import com.itengs.crbt.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("crbt.android.library")
                apply("crbt.android.hilt")
            }
            extensions.configure<LibraryExtension> {
                defaultConfig {
                    testInstrumentationRunner =
                        "com.example.crbt.core.testing.HiltTestRunner"
                }
                testOptions.animationsDisabled = true
                configureGradleManagedDevices(this)
            }

            dependencies {
                add("implementation", project(":core:ui"))
                add("implementation", project(":core:designsystem"))

                add("implementation", libs.findLibrary("androidx.hilt.navigation.compose").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.runtimeCompose").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.viewModelCompose").get())
                add("implementation", libs.findLibrary("androidx.tracing.ktx").get())

                add(
                    "androidTestImplementation",
                    libs.findLibrary("androidx.lifecycle.runtimeTesting").get()
                )
            }
        }
    }
}
