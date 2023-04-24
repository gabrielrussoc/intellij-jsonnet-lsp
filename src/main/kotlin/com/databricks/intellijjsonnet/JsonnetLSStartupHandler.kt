package com.databricks.intellijjsonnet

import com.databricks.intellijjsonnet.settings.JLSSettingsConfigurable
import com.databricks.intellijjsonnet.settings.JLSSettingsStateComponent
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.extensions.PluginId
import com.intellij.util.system.CpuArch
import org.wso2.lsp4intellij.IntellijLanguageClient
import org.wso2.lsp4intellij.listeners.LSPProjectManagerListener
import java.io.File
import java.nio.file.Files
import java.nio.file.attribute.PosixFileAttributeView
import java.nio.file.attribute.PosixFilePermissions
import kotlin.io.path.Path
import kotlin.io.path.setPosixFilePermissions

const val EXTENSIONS = "jsonnet,libsonnet,jsonnet.TEMPLATE"

class JsonnetLSStartupHandler {

    private val pluginId = "com.databricks.intellijjsonnet"

    private val log = Logger.getInstance(
        LSPProjectManagerListener::class.java
    )

    fun start() {
        val platform = getPlatform()
        val arch = getArch()

        log.info("Running on -> platform: $platform ; arch: $arch")

        val customLspBinary = JLSSettingsStateComponent.instance.state.customLspBinary
        val binFile = if (customLspBinary.isNotEmpty()) {
            log.info("Using custom LSP binary at $customLspBinary")
            customLspBinary
        } else {
            val pluginName = PluginManagerCore.getPlugin(PluginId.getId(pluginId))?.name
            val binDir = File(PathManager.getPluginsPath().plus("/$pluginName/bin/"))
            val binFile  = getLspBinaryFor(binDir, platform, arch)
            setExecutablePerms(binFile)
            binFile
        }

        // Configure language server
        IntellijLanguageClient.addServerDefinition(
            JsonnetLanguageServer(
                EXTENSIONS,
                arrayOf(binFile.toString(), "lsp")
            )
        )
    }

    private fun getLspBinaryFor(binDir: File, platform: String, arch: String): File {
        val listFiles = binDir.listFiles()
                ?: throw IllegalArgumentException("Couldn't list files inside ${binDir.canonicalPath}")
        for (file in listFiles) {
            val path = file.path
            if (platform in path && arch in path) {
                return file
            }
        }
        throw IllegalArgumentException("Couldn't find LSP binary for $platform $arch")
    }

    private fun getPlatform(): String {
        val os = System.getProperty("os.name").lowercase()
        return when {
            os.contains("win") -> {
                "windows"
            }

            os.contains("nix") || os.contains("nux") || os.contains("aix") -> {
                "linux"
            }

            os.contains("mac") -> {
                "darwin"
            }

            else -> "linux"
        }
    }

    private fun getArch(): String {
        val arch = CpuArch.CURRENT
        return when {
            arch.equals(CpuArch.ARM64) -> {
                "arm64"
            }

            arch.equals(CpuArch.X86_64) -> {
                "amd64"
            }

            else -> "amd64"
        }
    }

    private fun setExecutablePerms(file: File) {
        if (Files.getFileAttributeView(file.toPath(), PosixFileAttributeView::class.java) != null) {
            val executablePerms = PosixFilePermissions.fromString("rwxr--r--")
            Path(file.toString()).setPosixFilePermissions(executablePerms)
        } else {
            file.setReadable(true)
            file.setWritable(true, true)
            file.setExecutable(true, true)
        }
    }

}
