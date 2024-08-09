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
include(":core:designsystem")
include(":core:ui")
include(":core:common")
include(":core:data")
include(":core:model")
include(":feature:onboarding")
include(":feature:home")
include(":feature:subscription")
include(":feature:payment")
include(":feature:profile")
include(":core:domain")
include(":feature:services")
include(":core:network")
