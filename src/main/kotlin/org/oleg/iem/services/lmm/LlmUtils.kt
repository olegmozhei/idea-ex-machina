package org.oleg.iem.services.lmm

import io.qdrant.client.grpc.JsonWithInt
import org.oleg.iem.utils.TransformText
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.absolutePathString

object LlmUtils {

    // 4 characters = 1 token
    fun tokensUsed(queryLength: Int): Double {
        return queryLength / 4.0
    }

    fun getAllFolders(dir: Path): List<Path> {
        val result = ArrayList<Path>()
        Files.walk(dir).use { stream ->
            stream.filter(Files::isDirectory)
                .filter { directory ->
                    !directory.absolutePathString().contains("/venv/")
                }
                .filter { directory ->
                    !directory.absolutePathString().contains("/.")
                }
                .forEach { e ->
                    run {
                        println("Found directory " + e.absolutePathString())
                        result.add(e)
                    }
                }
        }
        return result
    }

    private fun addRAGContextToPrompt(context: List<JsonWithInt.Value>, prompt: String): String {

        val result = StringBuilder("Here are the context that you can use to fulfil the task:\n")
        for (chunk in context){
            result.append(chunk.stringValue).append("\n================\n")
        }
        result.append(prompt)
        return result.toString()
    }

    fun prepareRequest(request: AskLLMRequest, context: List<JsonWithInt.Value>?) {
        if (!request.contextData.containsKey("query")) {
            request.contextData["query"] = request.query!!
        }
        println("Preparing request...")
        request.prompt = if (request.promptTemplate != null) {
            println("Processing prompt...")
            TransformText.transformString(request.promptTemplate, request.contextData)
        } else {
            println("Not enough data to process prompt")
            request.query!!
        }
        if (context != null) {
            request.prompt = addRAGContextToPrompt(context, request.prompt!!)
        }
    }
}