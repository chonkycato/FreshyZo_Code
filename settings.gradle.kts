pluginManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri ("https://jitpack.io") }
        maven { url = uri("https://artifactory.paytm.in/artifactory/libs-release-local/") }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri ("https://jitpack.io") }
        maven { url = uri("https://artifactory.paytm.in/artifactory/libs-release-local/") }
    }
}

rootProject.name = "FreshyZo"
include(":app")
