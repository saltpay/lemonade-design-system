plugins {
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    id("lemonade")
    id("lemonade-lint")
}

lemonadePublishing {
    artifactId = "lemonade-expressive"
}

android {
    namespace = "com.teya.lemonade.expressive"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.components.resources)
            implementation(libs.compose.ui.tooling.preview)
            api(projects.ui)
            implementation(projects.tokens)
            implementation(libs.compose.material3)
        }

        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
        }
    }
}

dependencies {
    debugImplementation(libs.compose.ui.tooling)
}
