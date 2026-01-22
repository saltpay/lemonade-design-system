import org.jetbrains.compose.resources.ResourcesExtension

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kotlinSerialization)
    id("lemonade")
}

lemonadePublishing {
    artifactId = "lemonade-ui"
}

kotlin {
    jvmToolchain(17)
    explicitApi()
    androidTarget {
        publishLibraryVariants("release")
    }

    iosArm64()
    iosSimulatorArm64()

    macosArm64()
    macosX64()

    js()
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
                implementation(libs.androidx.lifecycle.viewmodelCompose)
                implementation(libs.androidx.lifecycle.runtimeCompose)
                implementation(libs.kotlinx.serializer)
                api(projects.core)
            }
        }
        val desktopMain by creating {
            dependsOn(commonMain)
        }
        val mobileMain by creating {
            dependsOn(commonMain)
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

        androidMain.dependencies {
            implementation(compose.preview)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.teya.lemonade.ui"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        testOptions.targetSdk = libs.versions.android.targetSdk.get().toInt()
        minSdk = libs.versions.android.minLibSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.resources {
    generateResClass = ResourcesExtension.ResourceClassGeneration.Always
    publicResClass = true
    nameOfResClass = "LemonadeRes"
    packageOfResClass = "com.teya.lemonade"
}