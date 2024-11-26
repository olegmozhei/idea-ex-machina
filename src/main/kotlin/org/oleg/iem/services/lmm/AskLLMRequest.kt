package org.oleg.iem.services.lmm

import org.oleg.iem.OLLAMA_DEFAULT_LLM_MODEL

class AskLLMRequest private constructor(builder: Builder) {
    val query: String?
    val useRAG: Boolean
    val contextChunksNumber: Int
    val model: String
    val promptTemplate: String?
    var prompt: String? = null
    val contextData: HashMap<String, String> = HashMap()

    init {
        query = builder.query
        useRAG = builder.useRAG
        model = builder.model
        promptTemplate = builder.promptTemplate
        contextData.putAll(builder.contextData)
        contextChunksNumber = builder.contextChunksNumber
    }

    class Builder {
        var query: String? = null
        var useRAG = true   // default value is to use RAG
        var model = OLLAMA_DEFAULT_LLM_MODEL   // default value
        var promptTemplate: String? = null
        val contextData: HashMap<String, String> = HashMap()
        var contextChunksNumber: Int = 0

        fun promptTemplate(promptTemplate: String?): Builder {
            this.promptTemplate = promptTemplate
            return this
        }

        fun contextChunksNumber(contextChunksNumber: Int): Builder {
            this.contextChunksNumber = contextChunksNumber
            return this
        }

        fun model(model: String): Builder {
            this.model = model
            return this
        }

        fun query(query: String): Builder {
            this.query = query
            return this
        }

        fun useRAG(useRAG: Boolean): Builder {
            this.useRAG = useRAG
            return this
        }

        fun contextData(contextData: HashMap<String, String>): Builder {
            this.contextData.putAll(contextData)
            return this
        }

        fun contextData(key: String, value: String): Builder {
            this.contextData[key] = value
            return this
        }

        fun build(): AskLLMRequest {
            return AskLLMRequest(this)
        }
    }

    companion object {
        fun newBuilder(): Builder {
            return Builder()
        }
    }
}