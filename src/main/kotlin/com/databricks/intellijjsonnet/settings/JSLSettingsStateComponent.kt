package com.databricks.intellijjsonnet.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import org.jetbrains.annotations.Nullable

@State(
    name = "com.databricks.intellijjsonnet.JLSSettingsState",
    storages = [Storage("SdkSettingsPlugin.xml")]
)
open class JLSSettingsStateComponent : PersistentStateComponent<JLSSettingsStateComponent.SettingsState> {

    companion object {
        val instance: JLSSettingsStateComponent
            get() = ApplicationManager.getApplication().getService(JLSSettingsStateComponent::class.java)
    }

    var settingsState = SettingsState()

    @Nullable
    override fun getState(): SettingsState {
        return settingsState
    }

    override fun loadState(state: SettingsState) {
        settingsState = state
    }

    class SettingsState {
        var customLspBinary = ""
        var debugRpcCalls = ""
    }
}