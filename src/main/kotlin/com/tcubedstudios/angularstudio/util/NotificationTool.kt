package com.tcubedstudios.angularstudio.util

import com.tcubedstudios.angularstudio.actions.SayHelloAction
import com.intellij.notification.*
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project

class NotificationTool {

    companion object {
        fun notify(project: Project, title: String, message: String, notificationType: NotificationType) {
            ApplicationManager.getApplication().runWriteAction {
                NotificationsConfiguration.getNotificationsConfiguration().register(SayHelloAction.GROUP_ID, NotificationDisplayType.BALLOON, false)
                val notification = Notification(SayHelloAction.GROUP_ID, title, message, notificationType, null)
                Notifications.Bus.notify(notification, project)
            }
        }
    }

}