import com.vanniktech.maven.publish.MavenPublishBaseExtension
import com.vanniktech.maven.publish.MavenPublishBasePlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.configure

class LemonadePublishPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        pluginManager.apply(MavenPublishBasePlugin::class.java)

        val extension = extensions
            .create("lemonadePublishing", LemonadePublishingPluginExtension::class.java)

        afterEvaluate {
            val artifactId = extension.artifactId.orNull ?: run {
                error("You must set the 'artifactId' property in the 'lemonadePublishing' extension.")
            }

            configurePublication(artifactId)
        }
    }

    private fun Project.configurePublication(artifactId: String) {
        extensions.configure<MavenPublishBaseExtension> {
            val version: String = findEnvironmentVariable("PUBLICATION_VERSION")
            coordinates(
                groupId = "com.teya.foundation",
                artifactId = artifactId,
                version = version
            )

            publishToMavenCentral(true)
            signAllPublications()

            pom()
        }
    }

    private fun MavenPublishBaseExtension.pom() {
        pom {
            name.set("Teya Lemonade Design System")
            description.set("Compose Multiplatform implementation of Teya's lemonade design system")
            inceptionYear.set("2026")
            url.set("https://teya.com")
            licenses {
                license {
                    name.set("The Apache License, Version 2.0")
                    url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }
            developers {
                developer {
                    id.set("teya")
                    name.set("Teya")
                    url.set("https://teya.com")
                    email.set("terminal-team@teya.com")
                }
            }
            scm {
                url.set("https://github.com/saltpay/lemonade-design-system/")
                connection.set("scm:git:git://github.com/saltpay/lemonade-design-system.git")
                developerConnection.set("scm:git:ssh://git@github.com/saltpay/lemonade-design-system.git")
            }
        }
    }
}

private inline fun <reified T> Project.findEnvironmentVariable(variable: String): T {
    val envValue = System.getenv(variable)
    if (envValue != null) {
        return envValue as T
    }

    val gradleProperty = findProperty(variable)
    if (gradleProperty != null) {
        return gradleProperty as T
    }

    error("$variable not found in environment variables or Gradle properties")
}

abstract class LemonadePublishingPluginExtension {
    abstract val artifactId: Property<String>
}