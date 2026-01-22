import com.vanniktech.maven.publish.MavenPublishBaseExtension
import com.vanniktech.maven.publish.MavenPublishBasePlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.publish.PublishingExtension
import org.gradle.kotlin.dsl.configure

class LemonadePublishPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        pluginManager.apply(MavenPublishBasePlugin::class.java)

        val extension = extensions
            .create("lemonadePublishing", LemonadePublishingPluginExtension::class.java)

        configureJfrogRepository()

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
                groupId = "com.teya.teya-lemonade-ds",
                artifactId = artifactId,
                version = version
            )
        }
    }

    private fun Project.configureJfrogRepository() {
        extensions.configure<PublishingExtension> {
            repositories {
                maven {
                    name = "JFrog"
                    url = uri("https://saltpay.jfrog.io/artifactory/main-maven-virtual/")

                    credentials {
                        username = System.getenv("JFROG_USER")
                        password = System.getenv("JFROG_PASSWORD")
                    }
                }
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