package com.databricks.intellijjsonnet.settings

import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import org.jetbrains.annotations.NotNull
import javax.swing.JComponent
import javax.swing.JPanel

/**
 * Supports creating and managing a {@link JPanel} for the Settings Dialog
 */
class JLSSettingsComponent {
    var myMainPanel: JPanel
    private val customLspBinary = JBTextField()
    private val debugRpcCalls = JBTextField()

    init {
        this.myMainPanel = FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel("Absolute path to custom Jsonnet LSP binary (requires restart): "), customLspBinary, 1, true)
            .addLabeledComponent(JBLabel("Absolute path to directory to log LSP json RPC calls (requires restart): "), debugRpcCalls, 1, true)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }

    fun getPreferredFocusedComponent(): JComponent {
        return customLspBinary
    }

    @NotNull
    fun getCustomLspBinary(): String {
        return customLspBinary.text
    }

    fun setCustomLspBinary(newPath: String) {
        customLspBinary.text = newPath
    }

    fun getDebugRpcCalls(): String {
        return debugRpcCalls.text
    }

    fun setDebugRpcCalls(newDir: String) {
        debugRpcCalls.text = newDir
    }

}