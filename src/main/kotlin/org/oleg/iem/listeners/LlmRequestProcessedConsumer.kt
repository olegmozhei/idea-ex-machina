package org.oleg.iem.listeners

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import org.oleg.iem.MyToolWindow
import org.oleg.iem.services.lmm.AskLLMRequest

class LlmRequestProcessedConsumer(private val project: Project) : LlmRequestProcessedListener {

    override fun requestProcessed(request: AskLLMRequest) {
        val myToolWindow = project.service<MyToolWindow>()
        myToolWindow.addMessageToChat("You: ${request.prompt}")
    }
}