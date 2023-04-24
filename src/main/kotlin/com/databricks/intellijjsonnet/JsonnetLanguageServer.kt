package com.databricks.intellijjsonnet

import com.databricks.intellijjsonnet.settings.JLSSettingsStateComponent
import com.intellij.openapi.diagnostic.Logger
import org.wso2.lsp4intellij.client.connection.ProcessStreamConnectionProvider
import org.wso2.lsp4intellij.client.connection.StreamConnectionProvider
import org.wso2.lsp4intellij.client.languageserver.serverdefinition.RawCommandServerDefinition
import java.io.File
import java.io.FileOutputStream

class JsonnetLanguageServer(ext: String, command: Array<String>) : RawCommandServerDefinition(ext, command) {

    private val logger = Logger.getInstance(JsonnetLanguageServer::class.java)

    override fun createConnectionProvider(workingDir: String?): StreamConnectionProvider {
        val realStreamConnectionProvider = ProcessStreamConnectionProvider(listOf(*command), workingDir)
        val debugRpcCalls = JLSSettingsStateComponent.instance.state.debugRpcCalls
        if (debugRpcCalls.isNotEmpty()) {
            File(debugRpcCalls).mkdirs()
            val debugStdinFileName = "./jsonnet-lsp.client.log"
            val debugStdinFile = File(debugRpcCalls, debugStdinFileName)
            logger.info("Writing CLIENT (intellij) logs to " + debugStdinFile.canonicalPath)

            val debugStdoutFileName = "./jsonnet-lsp.server.log"
            val debugStdoutFile = File(debugRpcCalls, debugStdoutFileName)
            logger.info("Writing SERVER (jsonnet-lsp) logs to " + debugStdoutFile.canonicalPath)
            return TeeStreamConnectionProvider(realStreamConnectionProvider, FileOutputStream(debugStdinFile, true), FileOutputStream(debugStdoutFile, true))
        }
        return realStreamConnectionProvider
    }
}