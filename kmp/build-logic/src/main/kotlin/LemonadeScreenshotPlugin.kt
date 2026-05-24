import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

/**
 * Wires Roborazzi Compose `@Preview` screenshot testing onto a KMP `:ui`-style
 * module. Apply *after* the `lemonade` plugin so the `androidTarget` /
 * `androidUnitTest` source set already exists.
 *
 * Screenshot test dependencies are added here (not in the module's
 * build.gradle.kts) so the module's `dependencies.allowlist` stays limited to
 * shipped dependencies.
 */
class LemonadeScreenshotPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("io.github.takahirom.roborazzi")

            dependencies {
                add("androidUnitTestImplementation", lib("roborazzi"))
                add("androidUnitTestImplementation", lib("roborazzi-compose"))
                add(
                    "androidUnitTestImplementation",
                    lib("roborazzi-compose-preview-scanner-support"),
                )
                add("androidUnitTestImplementation", lib("composable-preview-scanner"))
                add("androidUnitTestImplementation", lib("robolectric"))
                add("androidUnitTestImplementation", lib("junit"))
            }

            extensions.configure<LibraryExtension> {
                testOptions {
                    unitTests.isIncludeAndroidResources = true
                    unitTests.isReturnDefaultValues = true
                }
            }
        }
    }
}

private fun Project.lib(alias: String): Provider<MinimalExternalModuleDependency> {
    val catalog = extensions
        .getByType(VersionCatalogsExtension::class.java)
        .named("libs")
    return catalog.findLibrary(alias).get()
}
