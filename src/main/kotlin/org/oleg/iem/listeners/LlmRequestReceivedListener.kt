package org.oleg.iem.listeners

import com.intellij.util.messages.Topic
import org.oleg.iem.services.lmm.AskLLMRequest

interface LlmRequestReceivedListener {
    fun requestReceived(request: AskLLMRequest)

    companion object{
        @Topic.ProjectLevel
        val LLM_REQUEST_RECEIVED_TOPIC = Topic.create("LLM Request Received",
            LlmRequestReceivedListener::class.java)
    }
}