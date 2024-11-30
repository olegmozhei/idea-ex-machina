package org.oleg.iem.services.lmm

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import dev.langchain4j.data.document.Document
import dev.langchain4j.data.document.splitter.DocumentSplitters
import io.qdrant.client.PointIdFactory.id
import io.qdrant.client.QueryFactory.nearest
import io.qdrant.client.ValueFactory.value
import io.qdrant.client.VectorsFactory.vectors
import io.qdrant.client.WithPayloadSelectorFactory.enable
import io.qdrant.client.grpc.JsonWithInt
import io.qdrant.client.grpc.Points
import io.qdrant.client.grpc.Points.PointStruct
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.oleg.iem.*
import org.oleg.iem.listeners.LlmRequestProcessedListener
import org.oleg.iem.listeners.LlmRequestReceivedListener
import org.oleg.iem.listeners.LlmResponseReadyListener
import org.oleg.iem.llm_models.MistralNemo
import org.oleg.iem.llm_models.NomicEmbedText
import org.oleg.iem.utils.AskLLMResponse
import java.nio.charset.MalformedInputException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors
import kotlin.io.path.absolutePathString

@Service(Service.Level.PROJECT)
// TODO: Rewrite code according to the community recommendations
class LlmService (
    private val project: Project,
    private val cs: CoroutineScope) : LlmRequestReceivedListener {

    private val llmClient: LlmClient
    private val vectorDatabaseClient: VectorDatabaseClient

    init {
        val projectService = project.service<MySettings>()
        llmClient = LlmClient(projectService.state.API_ENDPOINT!!,
            OLLAMA_LLM_MODEL_API_ENDPOINT,
            OLLAMA_EMBEDDING_MODEL_API_ENDPOINT,
            MistralNemo(),
            NomicEmbedText()
        )
        vectorDatabaseClient = VectorDatabaseClient(
            projectService.state.QDRANT_HOST!!,
            projectService.state.QDRANT_PORT,
            EMBEDDING_DIMENSIONALITY
        )
    }

    override fun requestReceived(request: AskLLMRequest) {
        cs.launch {
            println("Running coroutine to get LLM response")
            var context: List<JsonWithInt.Value>? = null
            if (request.useRAG){
                context = prepareRagContext(request.query!!, request.contextChunksNumber)
            }
            LlmUtils.prepareRequest(request, context)
            println("Request preparation is finished. Sending message to message bus...")

            val requestPublisher: LlmRequestProcessedListener = project.messageBus
                .syncPublisher(LlmRequestProcessedListener.LLM_REQUEST_PROCESSED_TOPIC)
            requestPublisher.requestProcessed(request)

            val stringResponse = llmClient.queryLLM(request.prompt!!)
            val response = AskLLMResponse(request.prompt, request.promptTemplate, request.query, stringResponse)

            val responsePublisher: LlmResponseReadyListener = project.messageBus
                .syncPublisher(LlmResponseReadyListener.LLM_RESPONSE_READY_TOPIC)
            responsePublisher.responseReceived(response)
        }
    }

    private fun prepareRagContext(query: String, contextChunksNumber: Int): List<JsonWithInt.Value> {
        val projectService = project.service<MySettings>()

        val directoryPath = projectService.state.PATH_TO_PROJECT_CONTEXT
        val path = Paths.get(directoryPath)

        if (Files.exists(path) && Files.isDirectory(path)) {
            println("Directory exists: " + path.toAbsolutePath())
        } else {
            println("Directory does not exist or is not a valid directory: $directoryPath")
        }

        vectorDatabaseClient.createCollectionIfNotExist("test_collection")

        var pointNumber = 1

        val dirs = LlmUtils.getAllFolders(path)

        for (dir in dirs){
            val files = Files.list(dir).use { stream ->
                stream.filter { file -> !Files.isDirectory(file)}
                    .filter { file -> file.fileName.toString() != ".DS_Store" }
                    .map { file ->
                        var content = ""
                        try {
                            Files.newBufferedReader(file, StandardCharsets.UTF_8).use { reader ->
                                while (true) {
                                    val line = reader.readLine() ?: break
                                    content += line + "\n"
                                }
                            }
                        } catch (e: MalformedInputException) {
                            println("Error reading file ${file.fileName}: ${e.message}")
                            content = ""
                        }
                        content.replace("  ", " ")
                    }
                    .filter { content -> !content.equals("")}
                    .collect(Collectors.toSet())
            }

            println("Found " + files.size + " files in " + dir.absolutePathString() + " directory")

            if (files.size == 0) continue

            val splitter = DocumentSplitters.recursive(3000, 100)

            for (file in files){
                val segments = splitter.split(Document.document(file))

                // TODO: Use tokenizer to count tokens and provide some feedback to user
                val embeddings = llmClient.getVectorData(segments)
                println("Generated ${embeddings.size} embeddings")
                if (embeddings.isEmpty()) continue
                val vectorData = ArrayList<PointStruct>()

                for (i in embeddings.indices){
                    val vector = embeddings[i].vectorAsList()
                    vectorData.add(
                        PointStruct.newBuilder()
                        .setId(id(pointNumber + 0L))
                        .setVectors(vectors(vector))
                        .putPayload("data", value(segments[i].text()))
                        .build())
                    pointNumber++
                }

                vectorDatabaseClient.upsertVectorDatabase(vectorData, "test_collection")

                println("Added embeddings into vector database. Total number is ${pointNumber - 1}")
            }
        }



        val queryAsVectorData = llmClient.getVectorData(query)
        val maxResults = EMBEDDING_MAX_RESULTS.toInt()
        val minScore = EMBEDDING_MIN_SCORE.toDouble()

        println("Started Search:")
        val request = Points.QueryPoints.newBuilder()
            .setCollectionName("test_collection")
            .setScoreThreshold(minScore.toFloat())
            .setQuery(nearest(queryAsVectorData.vectorAsList()))
            .setWithPayload(enable(true))

        if (contextChunksNumber != 0){
            request.setLimit(contextChunksNumber + 0L)
        } else {
            request.setLimit(maxResults + 0L)
        }

        val searchResult = vectorDatabaseClient.queryVectorDatabase(request.build())

        println(searchResult)

        val content = searchResult.map { e-> e.getPayloadOrThrow("data") }
        println("Found ${content.size} chunks suitable for query context. Max Results: ${maxResults}, min score: $minScore")
        return content
    }

}