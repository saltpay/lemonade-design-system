plugins {
    id("lemonade")
    id("lemonade-lint")
}

lemonadePublishing {
    artifactId = "lemonade-core"
}

android {
    namespace = "com.teya.lemonade.core"
}
