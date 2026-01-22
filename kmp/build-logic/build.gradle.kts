plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.gradle.vanniktech.publish)
    implementation(libs.gradle.kmp)
}

gradlePlugin {
    plugins {
        register("LemonadePublishPlugin") {
            id = "lemonade"
            implementationClass = "LemonadePublishPlugin"
        }
    }
}