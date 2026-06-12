import org.jetbrains.compose.resources.ResourcesExtension

plugins {
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    id("lemonade")
    id("lemonade-lint")
}

lemonadePublishing {
    artifactId = "lemonade-ui"
}

android {
    namespace = "com.teya.lemonade.ui"
}

kotlin {
    sourceSets.all {
        languageSettings.optIn("com.teya.lemonade.InternalLemonadeApi")
    }
    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            api(projects.tokens)
            api(projects.core)
        }

        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
        }
        val desktopMain by getting
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

dependencies {
    debugImplementation(libs.compose.ui.tooling)
}

compose.resources {
    generateResClass = ResourcesExtension.ResourceClassGeneration.Always
    // Keep the generated LemonadeRes accessors internal: consumers use the typed
    // LemonadeIcons / LemonadeCountryFlags / LemonadeBrandLogos APIs, never the raw
    // gen_* drawables. Marking the class internal removes the auto-partitioned
    // Drawable*_commonMainKt facades from the public ABI, so adding icons no longer
    // reshuffles public symbols and trips the API Stability gate.
    publicResClass = false
    nameOfResClass = "LemonadeRes"
    packageOfResClass = "com.teya.lemonade"
}
