plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    id("lemonade")
    id("lemonade-lint")
}

lemonadePublishing {
    artifactId = "lemonade-core"
}

kotlin {
    jvmToolchain(17)
    explicitApi()

    iosArm64()
    iosSimulatorArm64()

    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serializer)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
