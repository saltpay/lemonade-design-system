import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinSerialization)
    id("lemonade")
}

lemonadePublishing {
    artifactId = "lemonade-core"
}

kotlin {
    jvmToolchain(17)
    explicitApi()
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    iosArm64()
    iosSimulatorArm64()

    macosArm64()
    macosX64()

    js()
    jvm()

    sourceSets {
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

        commonMain.dependencies {
            implementation(libs.kotlinx.serializer)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }

    android {
        namespace = "com.teya.lemonade.core"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        defaultConfig {
            testOptions.targetSdk = libs.versions.android.targetSdk.get().toInt()
            minSdk = libs.versions.android.minLibSdk.get().toInt()
        }
    }
}