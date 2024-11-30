package org.oleg.iem.services.lmm

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.ObjectMapper
import com.intellij.platform.ml.embeddings.utils.generateEmbeddingBlocking
import dev.langchain4j.data.embedding.Embedding
import dev.langchain4j.data.segment.TextSegment
import org.json.JSONObject
import org.oleg.iem.*
import org.oleg.iem.llm_models.LlmModel
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

class LlmClient(
    private val apiEndpoint: String,
    private val queryEndpoint: String,
    private val embeddingEndpoint: String,
    private val taskLLM: LlmModel,
    private val embeddingLLM: LlmModel
) {

    fun queryLLM(query: String): String {
        println("sending request to llm")
        val tokensUsed = LlmUtils.tokensUsed(query.length)
        println("Tokens used " + tokensUsed + " (" + (tokensUsed / taskLLM.tokensNumber * 100) + "% of allowed)")

        // Create JSON request body with model and prompt
        val payload = JSONObject()
        payload.put("model", taskLLM.modelName)
        payload.put("prompt", query)
        println(query)

        val response = sendApiRequestToLlm(apiEndpoint + queryEndpoint, payload.toString(), true)
        return response
    }

    fun getVectorData(query: String): Embedding {
        val tokensUsed = LlmUtils.tokensUsed(query.length)
        println("Tokens used " + tokensUsed + " (" + (tokensUsed / embeddingLLM.tokensNumber * 100) + "% of allowed)")

        val embeddingRequest = EmbeddingRequest(query, embeddingLLM.modelName)
        val mapper = ObjectMapper()

        val payload = mapper.writeValueAsString(embeddingRequest)
        val response = sendApiRequestToLlm(apiEndpoint + embeddingEndpoint, payload, false)
        val embeddingResponse = try {
            mapper.readValue(response, EmbeddingResponse::class.java)
        } catch (e: JsonParseException) {
            println("Can't get response from embedding model")
            println("Response: $response")
            println("URL: $apiEndpoint${embeddingEndpoint}")
            println("Payload: $payload")
            throw RuntimeException(e)
        }
        val data = Embedding(embeddingResponse.embeddings)
        if (data.vector().isEmpty()) {
            throw RuntimeException("Something is going wrong. No vector data. URL:$apiEndpoint$embeddingEndpoint. Payload: $payload")
        }
        return data
    }

    fun getVectorData(segments: List<TextSegment>): List<Embedding> {
        return try {
            val result: MutableList<Embedding> = ArrayList()
            for (segment in segments){
                val data = getVectorData(segment.text())
                result.add(data)
            }
            result
        } catch (e: IOException){
            throw RuntimeException(e)
        }
    }

    private fun sendApiRequestToLlm(url: String, payload: String, printChunks: Boolean): String {
        try {
            val urlEntity = URL(url)

            val connection = urlEntity.openConnection() as HttpURLConnection

            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.doOutput = true


            connection.outputStream.use { os ->
                val input = payload.toByteArray(StandardCharsets.UTF_8)
                os.write(input, 0, input.size)
            }
            BufferedReader(InputStreamReader(connection.inputStream, StandardCharsets.UTF_8)).use { br ->
                var chunk: String?
                val responseBuilder = StringBuilder()

                // Read the stream in chunks
                while (br.readLine().also { chunk = it } != null) {
                    val result = JSONObject(chunk)
                    val responseChunk = if (result.has("response")) result.getString("response") else result.toString()
                    responseBuilder.append(responseChunk)
                    if (printChunks) print(responseChunk)
                }

                // Full response after the streaming is done
                return responseBuilder.toString()
            }
        } catch (e: Exception) {
            println("Error: " + e.message)
            return "Error: " + e.message
        }
    }

    internal class EmbeddingRequest(var prompt: String, var model: String)

    internal class EmbeddingResponse {
        var embedding: List<Float>? = null
        val embeddings: FloatArray
            get(){
                val floatArray = FloatArray(embedding!!.size)

                for (i in embedding!!.indices) {
                    floatArray[i] = embedding!![i]
                }
                return floatArray
            }
    }

}