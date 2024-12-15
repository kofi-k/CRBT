pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "CRBTJetCompose"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")
include(":benchmark")
include(":feature:onboarding")
include(":feature:home")
include(":feature:subscription")
include(":feature:payment")
include(":feature:profile")
include(":feature:services")
include(":core:designsystem")
include(":core:ui")
include(":core:common")
include(":core:data")
include(":core:model")
include(":core:domain")
include(":core:network")
include(":core:testing")
include(":core:datastore")
include(":core:datastore-proto")
include(":core:datastore-test")
include(":core:analytics")
