plugins {
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    id("lemonade")
    id("lemonade-lint")
}

lemonadePublishing {
    artifactId = "lemonade-calendar"
}

android {
    namespace = "com.teya.lemonade.extensions.calendar"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.ui)
            api(projects.ui)
            implementation(projects.tokens)
            api(libs.kotlinx.datetime)
        }
    }
}
