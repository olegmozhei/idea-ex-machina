package org.oleg.iem.general.notifications

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project


object MyNotifier {
    fun notifyError(project: Project, content: String) {
        NotificationGroupManager.getInstance()
            .getNotificationGroup("MyPromptNotifications")
            .createNotification(content, NotificationType.ERROR)
            .notify(project)
    }
}