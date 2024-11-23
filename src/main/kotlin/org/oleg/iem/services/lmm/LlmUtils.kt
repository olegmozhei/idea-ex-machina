package org.oleg.iem.services.lmm

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import dev.langchain4j.data.document.Document
import dev.langchain4j.data.document.splitter.DocumentSplitters
import dev.langchain4j.data.embedding.Embedding
import dev.langchain4j.data.segment.TextSegment
import io.qdrant.client.PointIdFactory.id
import io.qdrant.client.QdrantClient
import io.qdrant.client.QdrantGrpcClient
import io.qdrant.client.QueryFactory.nearest
import io.qdrant.client.ValueFactory.value
import io.qdrant.client.VectorsFactory.vectors
import io.qdrant.client.WithPayloadSelectorFactory.enable
import io.qdrant.client.grpc.Collections
import io.qdrant.client.grpc.JsonWithInt
import io.qdrant.client.grpc.Points
import io.qdrant.client.grpc.Points.PointStruct
import org.oleg.iem.*
import org.oleg.iem.utils.TransformText
import java.io.IOException
import java.nio.charset.MalformedInputException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors
import kotlin.io.path.absolutePathString

class LlmUtils(private val project: Project) {

    fun prepareRequest(request: AskLLMRequest) {
        request.prompt = if (request.contextData != null && request.promptTemplate != null) {
            if (!request.contextData.containsKey("query")) {
                request.contextData["query"] = request.query!!
            }
            TransformText.transformString(request.promptTemplate, request.contextData)
        } else {
            request.query!!
        }
        if (request.useRAG) {
            val context = prepareRagContext(request.query!!)
            request.prompt = addProjectContextToPrompt(context, request.prompt!!)
        }
    }

    private fun getAllFolders(dir: Path): List<Path> {
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

    private fun prepareRagContext(query: String): List<JsonWithInt.Value> {
        val projectService = project.service<MySettings>()

        val directoryPath = projectService.state.PATH_TO_PROJECT_CONTEXT
        val path = Paths.get(directoryPath)

        if (Files.exists(path) && Files.isDirectory(path)) {
            println("Directory exists: " + path.toAbsolutePath())
        } else {
            println("Directory does not exist or is not a valid directory: $directoryPath")
        }


        val client = QdrantClient(
            QdrantGrpcClient.newBuilder(projectService.state.QDRANT_HOST,
            projectService.state.QDRANT_PORT,
            false).build()
        )

        if (!client.collectionExistsAsync("test_collection").get()){
            client.createCollectionAsync("test_collection",
                Collections.VectorParams.newBuilder().setDistance(Collections.Distance.Dot).setSize(EMBEDDING_DIMENSIONALITY + 0L).build()).get()
        }

        var pointNumber = 1

        val dirs = getAllFolders(path)

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

            var splitter = DocumentSplitters.recursive(3000, 100)

            for (file in files){
                val segments = splitter.split(Document.document(file))

                // TODO: Use tokenizer to count tokens and provide some feedback to user
                val embeddings = getOllamaEmbeddings(segments)
                println("Generated ${embeddings.size} embeddings")
                if (embeddings.isEmpty()) continue
                val vectorData = ArrayList<PointStruct>()

                for (i in embeddings.indices){
                    val vector = embeddings[i].vectorAsList()
                    vectorData.add(PointStruct.newBuilder()
                        .setId(id(pointNumber + 0L))
                        .setVectors(vectors(vector))
                        .putPayload("data", value(segments[i].text()))
                        .build())
                    pointNumber++
                }
                val operationInfo: Points.UpdateResult = client.upsertAsync(
                    "test_collection", vectorData).get()

                //println(operationInfo)

                println("Added embeddings into vector database. Total number is ${pointNumber - 1}")
            }
        }



        val queryAsVectorData = LlmClient().getVectorData(query)
        val maxResults = EMBEDDING_MAX_RESULTS.toInt()
        val minScore = EMBEDDING_MIN_SCORE.toDouble()

        println("Started Search:")
        val searchResult =
            client.queryAsync(
                Points.QueryPoints.newBuilder()
                    .setCollectionName("test_collection")
                    .setLimit(maxResults + 0L)
                    .setScoreThreshold(minScore.toFloat())
                    .setQuery(nearest(queryAsVectorData.vectorAsList()))
                    .setWithPayload(enable(true))
                    .build()
            ).get()

        println(searchResult)

        client.close()

        val content = searchResult.map { e-> e.getPayloadOrThrow("data") }
        println("Found ${content.size} chunks suitable for query context. Max Results: ${maxResults}, min score: $minScore")
        return content
    }

    fun getOllamaEmbeddings(segments: List<TextSegment>): List<Embedding> {
        return try {
            val result: MutableList<Embedding> = ArrayList()
            for (segment in segments){
                val data = getVectorData(segment)
                result.add(data)
            }
            result
        } catch (e: IOException){
            throw RuntimeException(e)
        }
    }

    private fun getVectorData(segment: TextSegment): Embedding {
        return LlmClient().getVectorData(segment.text())
    }

    private fun addProjectContextToPrompt(context: List<JsonWithInt.Value>, prompt: String): String {

        val result = StringBuilder("Here are the context that you can use to fulfil the task:\n")
        for (chunk in context){
            result.append(chunk.stringValue).append("\n================\n")
        }
        result.append(prompt)
        return result.toString()
    }
}