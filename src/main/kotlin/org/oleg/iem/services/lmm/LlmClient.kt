package org.oleg.iem.services.lmm

import com.fasterxml.jackson.databind.ObjectMapper
import dev.langchain4j.data.embedding.Embedding
import org.json.JSONObject
import org.oleg.iem.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

class LlmClient {

    fun queryLLM(model: String, query: String): String {
        println("sending request to llm")
        val tokensUsed: Double = query.length / 4.0
        println("Tokens used " + tokensUsed + " (" + (tokensUsed / OLLAMA_LLM_MODEL_TOKENS * 100) + "% of allowed)")

        // Create JSON request body with model and prompt
        val payload = JSONObject()
        payload.put("model", model)
        payload.put("prompt", query)
        println(query)

        val response = getApiRequestFromLLM(OLLAMA_LLM_MODEL_API_URL, payload, true)
        return response
    }

    fun getVectorData(query: String): Embedding {
        val tokensUsed: Double = query.length / 4.0
        println("Tokens used " + tokensUsed + " (" + (tokensUsed / OLLAMA_EMBEDDING_MODEL_TOKENS * 100) + "% of allowed)")

        val embeddingRequest = EmbeddingRequest(query, OLLAMA_DEFAULT_EMBEDDING_MODEL)
        val mapper = ObjectMapper()

        val payload = mapper.writeValueAsString(embeddingRequest)
        val response = getApiRequestFromLLM(OLLAMA_EMBEDDING_MODEL_API_URL, payload, false)
        val embeddingResponse = mapper.readValue(response, EmbeddingResponse::class.java)
        return Embedding(embeddingResponse.embeddings)
    }

    private fun getApiRequestFromLLM(url: String, payload: String, printChunks: Boolean): String {
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
                    val responseChunk = result.getString("response")
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

    private fun getApiRequestFromLLM(url: String, payload: JSONObject, printChunks: Boolean): String {
        val payloadString = payload.toString()
        return getApiRequestFromLLM(url, payloadString, printChunks)
    }

    internal class EmbeddingRequest(var query: String, var model: String)

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