package com.github.zzehring.intellijjsonnet

import org.apache.commons.io.input.TeeInputStream
import org.apache.commons.io.output.TeeOutputStream
import org.wso2.lsp4intellij.client.connection.ProcessStreamConnectionProvider
import org.wso2.lsp4intellij.client.connection.StreamConnectionProvider
import java.io.InputStream
import java.io.OutputStream

class TeeStreamConnectionProvider(
        private val realStreamConnectionProvider: ProcessStreamConnectionProvider,
        private val debugStdinStream: OutputStream,
        private val debugStdoutStream: OutputStream) : StreamConnectionProvider {

    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null

    override fun start() {
        realStreamConnectionProvider.start()
        inputStream = TeeInputStream(realStreamConnectionProvider.inputStream, debugStdoutStream)
        outputStream = TeeOutputStream(realStreamConnectionProvider.outputStream, debugStdinStream)
    }

    override fun getInputStream(): InputStream? {
        return inputStream
    }

    override fun getOutputStream(): OutputStream? {
        return outputStream
    }

    override fun stop() {
        realStreamConnectionProvider.stop()
        inputStream = null
        outputStream = null
    }

}
