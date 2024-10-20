package org.oleg.iem.services.lmm

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.oleg.iem.listeners.LlmRequestReceivedListener

@Service(Service.Level.PROJECT)
// TODO: Rewrite code according to the community recommendations
class LlmService (
    private val project: Project,
    private val cs: CoroutineScope) : LlmRequestReceivedListener {

    override fun requestReceived(request: AskLLMRequest) {
        cs.launch {
            println("Running coroutine to get LLM response")
            LlmUtils().prepareRequest(request)
        }
    }

}