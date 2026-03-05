import com.android.build.api.dsl.ApplicationExtension
import com.goflow.app.getPluginId
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import java.io.File

private const val APPLICATION_ID = "com.goflow.app"

/**
 * A convention plugin that applies common configurations to Android application modules.
 * This centralizes configuration, preventing duplication across multiple modules.
 *
 * This plugin applies several Gradle plugins that are commonly used in all application modules,
 * including the [AndroidCommonConventionPlugin].
 *
 * After applying this plugin, the configured values can be overridden if necessary. However,
 * if extensive overrides are required, it may indicate that the configuration should be moved
 * out of this convention plugin.
 *
 * The application's `versionCode` can be set via the `VERSION_CODE` environment variable.
 *
 * A `release` signing configuration is automatically created. The keystore information can be
 * provided through the following environment variables:
 * - `KEYSTORE_PATH`: The path to the keystore file.
 * - `KEYSTORE_PASSWORD`: The password for the keystore.
 * - `KEYSTORE_ALIAS`: The alias for the key within the keystore.
 * - `KEYSTORE_ALIAS_PASSWORD`: The password for the key alias.
 */
class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = libs.plugins.android.application.getPluginId())
            apply(plugin = libs.plugins.kotlin.android.getPluginId())
            apply(plugin = libs.plugins.ksp.getPluginId())
            apply(plugin = libs.plugins.hilt.getPluginId())
            AndroidCommonConventionPlugin().apply(target)
            AndroidComposeConventionPlugin().apply(target)

            extensions.configure<ApplicationExtension> {
                namespace = APPLICATION_ID

                defaultConfig {
                    applicationId = APPLICATION_ID
                    targetSdk = libs.versions.androidSdk.target.get().toInt()

                    versionName = "2.0.2"
                    versionCode = System.getenv("VERSION_CODE")?.toIntOrNull() ?: 30

                    val noStrictMode = project.findProperty("noStrictMode")?.toString()?.ifEmpty { "true" }
                        ?.toBoolean() ?: false
                    buildConfigField("Boolean", "NO_STRICT_MODE", noStrictMode.toString())
                }

                buildFeatures {
                    viewBinding = true
                }

                signingConfigs {
                    val releaseKeystore: File = file(System.getenv("KEYSTORE_PATH") ?: "release_keystore.keystore")
                    create("release") {
                        // Default to a local keystore; fall back to env overrides when provided.
                        storeFile = releaseKeystore
                        storePassword = System.getenv("KEYSTORE_PASSWORD") ?: "android"
                        keyAlias = System.getenv("KEYSTORE_ALIAS") ?: "release"
                        keyPassword = System.getenv("KEYSTORE_ALIAS_PASSWORD") ?: "android"
                        enableV1Signing = true
                        enableV2Signing = true
                    }
                }

                buildTypes {
                    named("debug").configure {
                        applicationIdSuffix = ".debug"
                    }
                    named("release").configure {
                        isDebuggable = false
                        isJniDebuggable = false
                        // If the release keystore is missing locally, use the debug signing config to keep builds unblocked.
                        signingConfig = if (signingConfigs.getByName("release").storeFile?.exists() == true) {
                            signingConfigs.getByName("release")
                        } else {
                            signingConfigs.getByName("debug")
                        }
                    }
                }
            }
        }
    }
}
