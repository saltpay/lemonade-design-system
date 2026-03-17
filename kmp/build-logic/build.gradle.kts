plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.gradle.agp)
    implementation(libs.gradle.vanniktech.publish)
    implementation(libs.gradle.kmp)
    implementation(libs.detekt.gradlePlugin)
    implementation(libs.ktlint.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("LemonadePlugin") {
            id = "lemonade"
            implementationClass = "LemonadePlugin"
        }
        register("LemonadeLintPlugin") {
            id = "lemonade-lint"
            implementationClass = "LemonadeLintPlugin"
        }
    }
}