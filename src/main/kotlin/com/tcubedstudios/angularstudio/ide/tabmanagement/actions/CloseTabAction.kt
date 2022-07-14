package com.tcubedstudios.angularstudio.ide.tabmanagement.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import com.tcubedstudios.angularstudio.shared.simpletons.Direction

abstract class CloseTabAction (
    private val direction: Direction
): DumbAwareAction() {
    override fun actionPerformed(event: AnActionEvent) {
        CloseTabLeftRightAction(event, direction).closeFiles()
    }

    override fun update(event: AnActionEvent) {
        super.update(event)
        CloseTabLeftRightAction(event, direction).updatePresentationEnabled()
    }
}