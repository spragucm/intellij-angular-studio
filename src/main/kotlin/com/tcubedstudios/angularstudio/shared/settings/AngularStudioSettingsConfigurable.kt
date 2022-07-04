package com.tcubedstudios.angularstudio.shared.settings

import com.intellij.openapi.options.Configurable
import javax.swing.JComponent
import com.intellij.ui.dsl.builder.panel
class AngularStudioSettingsConfigurable: Configurable {

    override fun getDisplayName(): String  = "Angular Studio"

    override fun getHelpTopic(): String = "Configure Angular Studio"

    override fun createComponent(): JComponent? {
        return panel {
        }
    }

    override fun isModified(): Boolean= false

    override fun apply() {}
}