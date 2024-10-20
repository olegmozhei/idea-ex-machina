package org.oleg.iem

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory

class MyToolWindowFactory : ToolWindowFactory, AnAction("Send System Message") {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val contentFactory = ContentFactory.getInstance()
        val content = contentFactory.createContent(MyToolWindow.getContent(), "", false)
        toolWindow.contentManager.addContent(content)
        MyToolWindow.project = project
    }

    override fun actionPerformed(e: AnActionEvent) {

        // TODO: Implement custom actions here
    }
}
