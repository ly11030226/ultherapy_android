pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        id("com.google.devtools.ksp") version "1.9.24-1.0.20" apply false
        id("org.jetbrains.kotlin.android") version "1.9.0"
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

rootProject.name = "Ultherapy_Android"
include(":app")
include(":MiscModule")
include(":LaserSerialModule")
include(":SerialAsciiCRLFImpl")
include(":SerialModule")
include(":ASentinel")
