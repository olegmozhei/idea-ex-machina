package org.oleg.iem.services.lmm

import dev.langchain4j.data.document.DocumentParser
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader
import dev.langchain4j.data.document.parser.TextDocumentParser
import dev.langchain4j.data.document.splitter.DocumentSplitters
import dev.langchain4j.data.embedding.Embedding
import dev.langchain4j.data.segment.TextSegment
import dev.langchain4j.rag.content.Content
import dev.langchain4j.store.embedding.EmbeddingMatch
import dev.langchain4j.store.embedding.EmbeddingSearchRequest
import dev.langchain4j.store.embedding.EmbeddingStore
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore
import org.oleg.iem.PATH_TO_PROJECT_CONTEXT
import org.oleg.iem.utils.TransformText
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors

class LlmUtils {

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

    private fun prepareRagContext(query: String): List<Content> {
        val documentParser: DocumentParser = TextDocumentParser()
        val directoryPath = PATH_TO_PROJECT_CONTEXT
        val path = Paths.get(directoryPath)

        if (Files.exists(path) && Files.isDirectory(path)) {
            println("Directory exists: " + path.toAbsolutePath())
        } else {
            println("Directory does not exist or is not a valid directory: $directoryPath")
        }
        val documents = FileSystemDocumentLoader.loadDocumentsRecursively(path, documentParser)
            .filter { e -> !e.metadata().getString("file_name").equals(".DS_Store") }

        val splitter = DocumentSplitters.recursive(3000, 100)
        val segments = splitter.splitAll(documents)

        // TODO: Use tokenizer to count tokens and provide some feedback to user

        val embeddings = getOllamaEmbeddings(segments)
        println("Generated ${embeddings.size} embeddings")

        val embeddingStore: EmbeddingStore<TextSegment> = InMemoryEmbeddingStore()
        embeddingStore.addAll(embeddings, segments)
        println("Added embeddings into memory database")

        val queryAsVectorData = LlmClient().getVectorData(query)
        val result = embeddingStore.search(
            EmbeddingSearchRequest.builder()
                .maxResults(5)
                .minScore(0.5)
                .queryEmbedding(queryAsVectorData)
                .build())
        val content = result.matches().stream()
            .map { e: EmbeddingMatch<TextSegment> -> Content.from(e.embedded())}
            .collect(Collectors.toList())
        println("Found ${content.size} chunks suitable for query context")
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

    private fun addProjectContextToPrompt(context: List<Content>, prompt: String): String {
        val result = StringBuilder("Here are the context that you can use to fulfil the task:\n")
        for (chunk in context){
            result.append(chunk.textSegment().text()).append("\n================\n")
        }
        result.append(prompt)
        return result.toString()
    }
}