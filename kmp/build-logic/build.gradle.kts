plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.gradle.vanniktech.publish)
    implementation(libs.gradle.kmp)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")
}

gradlePlugin {
    plugins {
        register("LemonadePublishPlugin") {
            id = "lemonade"
            implementationClass = "LemonadePublishPlugin"
        }
    }
}