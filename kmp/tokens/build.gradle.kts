plugins {
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    id("lemonade")
    id("lemonade-lint")
}

lemonadePublishing {
    artifactId = "lemonade-tokens"
}

android {
    namespace = "com.teya.lemonade.tokens"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.ui)
            api(projects.core)
        }
    }
}
