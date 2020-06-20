pluginManagement {
    val korgePluginVersion: String by settings

    repositories {
        maven("https://dl.bintray.com/korlibs/korlibs")
        maven("https://plugins.gradle.org/m2/")
    }

    plugins {
        id("com.soywiz.korge") version korgePluginVersion
    }
}

enableFeaturePreview("GRADLE_METADATA")
