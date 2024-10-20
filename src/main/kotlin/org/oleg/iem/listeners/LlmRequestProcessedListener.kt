package org.oleg.iem.listeners

import com.intellij.util.messages.Topic
import org.oleg.iem.services.lmm.AskLLMRequest

interface LlmRequestProcessedListener {

    fun requestProcessed(request: AskLLMRequest)

    companion object {
        @Topic.ProjectLevel
        val LLM_REQUEST_PROCESSED_TOPIC = Topic.create("LLM Request Processed",
            LlmRequestProcessedListener::class.java)
    }
}