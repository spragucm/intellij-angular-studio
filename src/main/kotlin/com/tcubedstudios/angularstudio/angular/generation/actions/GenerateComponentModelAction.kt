package com.tcubedstudios.angularstudio.angular.generation.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE
import com.intellij.openapi.actionSystem.PlatformCoreDataKeys.FILE_EDITOR
import com.intellij.openapi.project.DumbAwareAction
import org.angular2.cli.AngularCliUtil

class GenerateComponentModelAction: DumbAwareAction() {
    override fun actionPerformed(event: AnActionEvent) {
    }

    override fun update(event: AnActionEvent) {
        super.update(event)
    }
}