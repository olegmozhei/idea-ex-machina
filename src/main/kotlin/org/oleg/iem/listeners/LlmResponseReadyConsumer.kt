package org.oleg.iem.listeners

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import org.oleg.iem.MyToolWindow
import org.oleg.iem.utils.AskLLMResponse

class LlmResponseReadyConsumer(private val project: Project) : LlmResponseReadyListener {
    override fun responseReceived(response: AskLLMResponse) {
        val myToolWindow = project.service<MyToolWindow>()
        println("Got 'Response Received' message from message bus")
        println("Showing the response to user")
        myToolWindow.addMessageToChat("LLM: ${response.llmResponse}")
    }
}