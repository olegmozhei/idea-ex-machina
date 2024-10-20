package org.oleg.iem.services.lmm

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.oleg.iem.listeners.LlmRequestProcessedListener
import org.oleg.iem.listeners.LlmRequestReceivedListener
import org.oleg.iem.listeners.LlmResponseReadyListener
import org.oleg.iem.utils.AskLLMResponse

@Service(Service.Level.PROJECT)
// TODO: Rewrite code according to the community recommendations
class LlmService (
    private val project: Project,
    private val cs: CoroutineScope) : LlmRequestReceivedListener {

    override fun requestReceived(request: AskLLMRequest) {
        cs.launch {
            println("Running coroutine to get LLM response")
            LlmUtils().prepareRequest(request)

            val requestPublisher: LlmRequestProcessedListener = project.messageBus
                .syncPublisher(LlmRequestProcessedListener.LLM_REQUEST_PROCESSED_TOPIC)
            requestPublisher.requestProcessed(request)

            val client = LlmClient()
            val stringResponse = client.queryLLM(request.model, request.prompt!!)
            val response = AskLLMResponse(request.prompt, request.promptTemplate, request.query, stringResponse)

            val responsePublisher: LlmResponseReadyListener = project.messageBus
                .syncPublisher(LlmResponseReadyListener.LLM_RESPONSE_READY_TOPIC)
            responsePublisher.responseReceived(response)
        }
    }

}