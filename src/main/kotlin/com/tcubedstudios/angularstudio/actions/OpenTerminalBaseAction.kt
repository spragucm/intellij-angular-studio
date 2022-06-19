package com.tcubedstudios.angularstudio.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.diagnostic.Logger

abstract class OpenTerminalBaseAction: DumbAwareAction() {
    companion object {
        val loggger = Logger.getInstance(OpenTerminalBaseAction::class.java)
    }

    override fun actionPerformed(action: AnActionEvent) {

    }
}