import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun properties(key: String) = project.findProperty(key).toString()

plugins {
    // Java support
    id("java")
    // Kotlin support
    id("org.jetbrains.kotlin.jvm") version "1.7.21"
    // Gradle IntelliJ Plugin
    id("org.jetbrains.intellij") version "1.11.0"
    // Gradle Changelog Plugin
    id("org.jetbrains.changelog") version "2.0.0"
    // Gradle Qodana Plugin
    id("org.jetbrains.qodana") version "0.1.13"
    // Kotlin Serializer Plugin
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.10"
    // Download plugin
    id("de.undercouch.download") version "5.4.0"
}

group = properties("pluginGroup")
version = properties("pluginVersion")

// Configure project's dependencies
repositories {
    mavenCentral()
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    implementation("io.ktor:ktor-client-core:2.2.1")
    implementation("io.ktor:ktor-client-cio:2.2.1")
    implementation("net.swiftzer.semver:semver:1.2.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.10")
    implementation("io.ktor:ktor-client-content-negotiation:2.2.1")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.2.1")
    implementation("org.jetbrains.kotlin:kotlin-native-utils:1.6.10")
    implementation("com.github.gabrielrussoc:lsp4intellij:87e88ba")
    implementation("commons-io:commons-io:2.11.0")
}

// Configure Gradle IntelliJ Plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
intellij {
    pluginName.set(properties("pluginName"))
    version.set(properties("platformVersion"))
    type.set(properties("platformType"))

    // Plugin Dependencies. Uses `platformPlugins` property from the gradle.properties file.
    plugins.set(properties("platformPlugins").split(',').map(String::trim).filter(String::isNotEmpty))
}

// Configure Gradle Changelog Plugin - read more: https://github.com/JetBrains/gradle-changelog-plugin
changelog {
    version.set(properties("pluginVersion"))
    groups.set(emptyList())
}

// Configure Gradle Qodana Plugin - read more: https://github.com/JetBrains/gradle-qodana-plugin
qodana {
    cachePath.set(projectDir.resolve(".qodana").canonicalPath)
    reportPath.set(projectDir.resolve("build/reports/inspections").canonicalPath)
    saveReport.set(true)
    showReport.set(System.getenv("QODANA_SHOW_REPORT")?.toBoolean() ?: false)
}

val jsonnetLspVersion=properties("jsonnetLspVersion")
val platforms = listOf("darwin_amd64", "darwin_arm64", "linux_amd64", "linux_arm64")
val lspUrls = platforms.map {
    "https://github.com/carlverge/jsonnet-lsp/releases/download/v$jsonnetLspVersion/jsonnet-lsp_${jsonnetLspVersion}_$it"
}
val checksumsUrl = "https://github.com/carlverge/jsonnet-lsp/releases/download/v$jsonnetLspVersion/jsonnet-lsp_${jsonnetLspVersion}_checksums.txt"
val lspOutputDir = File(buildDir, "jsonnet-lsp_$jsonnetLspVersion")

tasks {
    // Set the JVM compatibility versions
    properties("javaVersion").let {
        withType<JavaCompile> {
            sourceCompatibility = it
            targetCompatibility = it
        }
        withType<KotlinCompile> {
            kotlinOptions.jvmTarget = it
        }
    }

    wrapper {
        gradleVersion = properties("gradleVersion")
    }

    patchPluginXml {
        version.set(properties("pluginVersion"))
        sinceBuild.set(properties("pluginSinceBuild"))
        untilBuild.set(properties("pluginUntilBuild"))

        // Extract the <!-- Plugin description --> section from README.md and provide for the plugin's manifest
        pluginDescription.set(
            projectDir.resolve("README.md").readText().lines().run {
                val start = "<!-- Plugin description -->"
                val end = "<!-- Plugin description end -->"

                if (!containsAll(listOf(start, end))) {
                    throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
                }
                subList(indexOf(start) + 1, indexOf(end))
            }.joinToString("\n").run { markdownToHTML(this) }
        )

        // Get the latest available change notes from the changelog file
        changeNotes.set(provider {
            changelog.renderItem(changelog.run {
                getOrNull(properties("pluginVersion")) ?: getLatest()
            }, Changelog.OutputType.HTML)
        })
    }

    // Configure UI tests plugin
    // Read more: https://github.com/JetBrains/intellij-ui-test-robot
    runIdeForUiTests {
        systemProperty("robot-server.port", "8082")
        systemProperty("ide.mac.message.dialogs.as.sheets", "false")
        systemProperty("jb.privacy.policy.text", "<!--999.999-->")
        systemProperty("jb.consents.confirmation.enabled", "false")
    }

    runIde {
        autoReloadPlugins.set(false)
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        dependsOn("patchChangelog")
        token.set(System.getenv("PUBLISH_TOKEN"))
        // pluginVersion is based on the SemVer (https://semver.org) and supports pre-release labels, like 2.1.7-alpha.3
        // Specify pre-release label to publish the plugin in a custom Release Channel automatically. Read more:
        // https://plugins.jetbrains.com/docs/intellij/deployment.html#specifying-a-release-channel
        channels.set(listOf(properties("pluginVersion").split('-').getOrElse(1) { "default" }.split('.').first()))
    }

    register("downloadLsp") {
        doLast {
            download.run {
                src(lspUrls + checksumsUrl)
                dest(lspOutputDir)
                onlyIfNewer(true)
            }
            File(lspOutputDir, "jsonnet-lsp_${jsonnetLspVersion}_checksums.txt").forEachLine {
                val parts = it.split("  ")
                val binary = File(lspOutputDir, parts[1])
                val sha256 = parts[0]
                verifyChecksum.run {
                    src(binary)
                    checksum(sha256)
                    algorithm("SHA256")
                }
            }
        }
    }

    withType<org.jetbrains.intellij.tasks.PrepareSandboxTask> {
        dependsOn("downloadLsp")
        from(lspOutputDir) { into("${pluginName.get()}/bin") }
    }
}

sourceSets.main {
    java.srcDirs("src/main/gen")
}
