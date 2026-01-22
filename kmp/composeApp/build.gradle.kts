import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    jvmToolchain(17)
    explicitApi()
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    jvm()

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.navigation3.runtime)
            implementation(libs.androidx.navigation3.ui)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.kotlinx.serializer)
            implementation(projects.ui)
            implementation(kotlin("script-runtime"))
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
        }

        val desktopMain by creating {
            dependsOn(commonMain.get())
        }
        val mobileMain by creating {
            dependsOn(commonMain.get())
        }

        // Mobile format targets
        androidMain.get().dependsOn(mobileMain)
        iosMain.get().dependsOn(mobileMain)
        iosX64Main.get().dependsOn(mobileMain)
        iosArm64Main.get().dependsOn(mobileMain)
        iosSimulatorArm64Main.get().dependsOn(mobileMain)

        // Desktop format targets
        jvmMain.get().dependsOn(desktopMain)
        linuxMain.get().dependsOn(desktopMain)
        jsMain.get().dependsOn(desktopMain)
        macosMain.get().dependsOn(desktopMain)
        macosX64Main.get().dependsOn(desktopMain)
        macosArm64Main.get().dependsOn(desktopMain)
    }

    android {
        namespace = "com.teya.lemonade"
        compileSdk = libs.versions.android.compileSdk.get().toInt()

        defaultConfig {
            applicationId = "com.teya.lemonade"
            minSdk = libs.versions.android.minSdk.get().toInt()
            targetSdk = libs.versions.android.targetSdk.get().toInt()
            versionCode = 1
            versionName = "1.0"
        }
        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }
        buildTypes {
            getByName("release") {
                isMinifyEnabled = false
            }
        }
    }
}
dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.teya.lemonade.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.teya.lemonade"
            packageVersion = "1.0.0"
        }
    }
}
