plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.gradle.vanniktech.publish)
    implementation(libs.gradle.kmp)
    implementation(libs.detekt.gradlePlugin)
    implementation(libs.ktlint.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("LemonadePublishPlugin") {
            id = "lemonade"
            implementationClass = "LemonadePublishPlugin"
        }
        register("LemonadeLintPlugin") {
            id = "lemonade-lint"
            implementationClass = "LemonadeLintPlugin"
        }
    }
}