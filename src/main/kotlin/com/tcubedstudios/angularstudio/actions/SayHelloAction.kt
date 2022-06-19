package com.tcubedstudios.angularstudio.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.tcubedstudios.angularstudio.util.NotificationTool
import com.intellij.notification.NotificationType

class SayHelloAction: AnAction() {

    companion object {
        val GROUP_ID = "SayHelloAction"
    }

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        NotificationTool.notify(project, GROUP_ID, "Hello", NotificationType.INFORMATION)

    }

    override fun update(event: AnActionEvent) {
        event.presentation.isVisible = true
    }
}