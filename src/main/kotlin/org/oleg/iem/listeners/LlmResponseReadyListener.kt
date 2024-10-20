package org.oleg.iem.listeners

import com.intellij.util.messages.Topic
import com.intellij.util.messages.Topic.ProjectLevel
import org.oleg.iem.utils.AskLLMResponse

interface LlmResponseReadyListener {
    fun responseReceived(response: AskLLMResponse)

    companion object {
        @ProjectLevel
        val LLM_RESPONSE_READY_TOPIC = Topic.create("LLM Response Ready",
            LlmResponseReadyListener::class.java)
    }
}