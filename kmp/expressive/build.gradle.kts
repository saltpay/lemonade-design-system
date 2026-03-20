@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kotlinSerialization)
    id("lemonade")
    id("lemonade-lint")
}

lemonadePublishing {
    artifactId = "lemonade-expressive"
}

kotlin {
    jvmToolchain(17)
    explicitApi()
    androidTarget {
        publishLibraryVariants("release")
    }

    iosArm64()
    iosSimulatorArm64()

    jvm("desktop")

    applyDefaultHierarchyTemplate {
        common {
            group("mobile") {
                withAndroidTarget()
                withIosArm64()
                withIosSimulatorArm64()
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.components.resources)
            implementation(libs.compose.ui.tooling.preview)
            api(projects.ui)
            implementation(libs.compose.material3)
        }

        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
        }
    }
}

android {
    namespace = "com.teya.lemonade.expressive"
    compileSdk = libs.versions.android.compileSdk
        .get()
        .toInt()

    defaultConfig {
        testOptions.targetSdk = libs.versions.android.targetSdk
            .get()
            .toInt()
        minSdk = libs.versions.android.minLibSdk
            .get()
            .toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    debugImplementation(libs.compose.ui.tooling)
}
