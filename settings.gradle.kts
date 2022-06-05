pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()

        maven { url = uri("https://jitpack.io") }
    }
}

dependencyResolutionManagement {

    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        maven("show-now")
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "Messenger"
include(":app")
enableFeaturePreview("VERSION_CATALOGS")