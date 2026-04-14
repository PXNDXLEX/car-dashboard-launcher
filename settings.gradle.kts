pluginManagement {
    repositories {
        google()       // <--- Vital para los plugins de Android
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()       // <--- Vital para las librerías de Android (AppCompat, etc.)
        mavenCentral()
    }
}

rootProject.name = "car-dashboard-launcher"
include(":app")