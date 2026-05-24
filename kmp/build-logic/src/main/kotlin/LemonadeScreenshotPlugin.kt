import org.gradle.api.Plugin
import org.gradle.api.Project

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
            // Configuration added in later tasks.
        }
    }
}
