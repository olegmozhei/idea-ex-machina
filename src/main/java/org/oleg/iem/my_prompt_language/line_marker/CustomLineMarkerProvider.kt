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
import org.oleg.iem.my_prompt_language.gen.psi.MyPromptContextChunks
import org.oleg.iem.my_prompt_language.gen.psi.MyPromptDetails
import org.oleg.iem.my_prompt_language.gen.psi.impl.MyPromptContextChunksImpl
import org.oleg.iem.my_prompt_language.gen.psi.impl.MyPromptDetailsImpl
import org.oleg.iem.my_prompt_language.gen.psi.impl.MyPromptPromptImpl
import org.oleg.iem.my_prompt_language.gen.psi.impl.MyPromptQueryImpl
import org.oleg.iem.my_prompt_language.gen.psi.impl.MyPromptTableHeaderImpl
import org.oleg.iem.my_prompt_language.gen.psi.impl.MyPromptTableRowImpl
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

    private fun processContextChunks(contextChunks: MyPromptContextChunks, requestBuilder: AskLLMRequest.Builder){
        val chunksNumber = contextChunks.text.replace("=", ":")
            .split(":")[1]
            .replace(" ", "")
            .replace("\n", "")
            .toInt()
        if (chunksNumber != 0){
            requestBuilder.contextChunksNumber = chunksNumber
            requestBuilder.useRAG = true
        }
    }

    private fun processQueryElement(queryElement: MyPromptQueryImpl, requestBuilder: AskLLMRequest.Builder){
        val query = queryElement.text.replace("Query:", "")
            .replace("^\n+", "")
            .replace("\n+$", "")
        println("Found query element $query")
        requestBuilder.query(query)
    }

    private fun processDetailsElement(details: MyPromptDetails, requestBuilder: AskLLMRequest.Builder){
        val valueToAdd = details.text.replace("Details:", "")
            .replace("^\n+", "")
        requestBuilder.contextData("details", valueToAdd)
    }

    private fun processPromptElement(promptElement: MyPromptPromptImpl, requestBuilder: AskLLMRequest.Builder){
        var template: String = promptElement.text.replace("Prompt:", "")
            .replace("^\n+", "")
            .replace("\n+$", "")
        if (template.startsWith("\"\"\"") && template.endsWith("\"\"\"")){
            template = template.substring(3, template.length - 3)
        }
        requestBuilder.promptTemplate(template)
        println("Added template: $template")
    }

    private fun processVariablesElement(variablesElement: MyPromptVariablesImpl, requestBuilder: AskLLMRequest.Builder){
        var headers = ""
        var variables = ""
        var context = HashMap<String, String>()
        variablesElement.accept(object : PsiRecursiveElementVisitor() {
            override fun visitElement(@NotNull element: PsiElement) {
                super.visitElement(element)
                if (element is MyPromptTableHeaderImpl){
                    headers = element.text
                } else if (element is MyPromptTableRowImpl){
                    variables = element.text
                }
            }
        })

        val h = headers.split("|")
        val v = variables.split("|")
        for (i in h.indices){
            if (h[i] == "") continue
            context[h[i].trim()] = v[i].trim()
        }
        requestBuilder.contextData(context)
    }
}