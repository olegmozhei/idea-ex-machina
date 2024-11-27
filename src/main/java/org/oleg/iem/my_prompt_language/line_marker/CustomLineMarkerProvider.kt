package org.oleg.iem.my_prompt_language.line_marker

import com.intellij.codeInsight.daemon.GutterIconNavigationHandler
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiRecursiveElementVisitor
import org.jetbrains.annotations.NotNull
import org.oleg.iem.MyToolWindow
import org.oleg.iem.listeners.LlmRequestReceivedListener
import org.oleg.iem.my_prompt_language.MyPromptFile
import org.oleg.iem.my_prompt_language.MyPromptIcons
import org.oleg.iem.my_prompt_language.gen.psi.impl.MyPromptContextChunksImpl
import org.oleg.iem.my_prompt_language.gen.psi.impl.MyPromptDetailsImpl
import org.oleg.iem.my_prompt_language.gen.psi.impl.MyPromptPromptImpl
import org.oleg.iem.my_prompt_language.gen.psi.impl.MyPromptQueryImpl
import org.oleg.iem.my_prompt_language.gen.psi.impl.MyPromptVariablesImpl
import org.oleg.iem.services.lmm.AskLLMRequest
import java.awt.event.MouseEvent

internal class CustomLineMarkerProvider : LineMarkerProvider {
    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
        return if (element.text == "Query") {
            println("Trying to return line marker")
            LineMarkerInfo(
                element,
                element.textRange,
                MyPromptIcons.FILE,
                null,
                Temp(),
                GutterIconRenderer.Alignment.CENTER
            )
        } else {
            null
        }
    }
}

class Temp : GutterIconNavigationHandler<PsiElement?> {
    override fun navigate(p0: MouseEvent?, p1: PsiElement?) {
        val parent = p1!!.parent
        if (parent !is MyPromptQueryImpl) return

        val grandParent = parent.parent
        if (grandParent !is MyPromptFile) return

        val requestBuilder = AskLLMRequest.newBuilder()

        grandParent.accept(object : PsiRecursiveElementVisitor() {
            override fun visitElement(@NotNull element: PsiElement) {
                super.visitElement(element)
                if (element is MyPromptContextChunksImpl){
                    processContextChunks(element, requestBuilder)
                } else if (element is MyPromptQueryImpl){
                    processQueryElement(element, requestBuilder)
                } else if (element is MyPromptDetailsImpl){
                    processDetailsElement(element, requestBuilder)
                } else if (element is MyPromptPromptImpl){
                    processPromptElement(element, requestBuilder)
                } else if (element is MyPromptVariablesImpl){
                    processVariablesElement(element, requestBuilder)
                }
            }
        })

        val request = requestBuilder.build()

        val requestPublisher: LlmRequestReceivedListener = MyToolWindow.project!!.messageBus
            .syncPublisher(LlmRequestReceivedListener.LLM_REQUEST_RECEIVED_TOPIC)
        requestPublisher.requestReceived(request)
    }

    private fun processContextChunks()

    private fun processQueryElement(queryElement: MyPromptQueryImpl, requestBuilder: AskLLMRequest.Builder){

    }
}