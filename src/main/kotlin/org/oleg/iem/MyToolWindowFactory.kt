package org.oleg.iem

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory

class MyToolWindowFactory : ToolWindowFactory, AnAction("Send System Message") {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        println("Creating tool window for project ${project.name} from MyToolWindowFactory")

        val myToolWindow = project.service<MyToolWindow>()

        val contentFactory = ContentFactory.getInstance()
        val content = contentFactory.createContent(myToolWindow.getContent(), "", false)
        toolWindow.contentManager.addContent(content)
    }

    override fun actionPerformed(e: AnActionEvent) {

        // TODO: Implement custom actions here
    }
}
