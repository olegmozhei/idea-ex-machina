package org.oleg.iem

import com.intellij.openapi.actionSystem.DataKey

val MY_DATA_KEY: DataKey<String> = DataKey.create("MY_DATA_KEY")

const val PATH_TO_PROJECT_CONTEXT = "PLEASE ADD SOMETHING"

// Should be equal to values in plugin.xml
const val AI_ASSISTANT_CHAT_ID = "MyCustomAiAssistantChatId"

const val OLLAMA_LLM_MODEL_API_URL: String = "http://127.0.0.1:11434/api/generate"
const val OLLAMA_DEFAULT_LLM_MODEL: String = "mistral-nemo"
const val OLLAMA_LLM_MODEL_TOKENS: Int = 128000

const val OLLAMA_EMBEDDING_MODEL_API_URL: String = "http://localhost:11434/api/embeddings"
const val OLLAMA_DEFAULT_EMBEDDING_MODEL: String = "nomic-embed-text"
const val OLLAMA_EMBEDDING_MODEL_TOKENS: Int = 8192

// 4 characters = 1 token