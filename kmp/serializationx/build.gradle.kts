plugins {
    alias(libs.plugins.kotlinSerialization)
    id("lemonade")
    id("lemonade-lint")
}

lemonadePublishing {
    artifactId = "lemonade-serializationx"
}

android {
    namespace = "com.teya.lemonade.extensions.serializationx"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core)
            api(libs.kotlinx.serializer)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
