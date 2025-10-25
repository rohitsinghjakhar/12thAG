pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS) // This line enforces the rule
    repositories {
        google()
        mavenCentral()
        // If you have other repositories like JitPack, add them here.
        // maven { url = uri("https://jitpack.io") }
    }
}
rootProject.name = "Class 12th AG JET Notes"
include(":app")

