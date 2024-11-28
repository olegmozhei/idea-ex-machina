package org.oleg.iem

import com.intellij.openapi.actionSystem.DataKey

// TODO: Keep Application specific variables here, move project specific variables to MySettings Class
val MY_DATA_KEY: DataKey<String> = DataKey.create("MY_DATA_KEY")

// Should be equal to values in plugin.xml
const val AI_ASSISTANT_CHAT_ID = "MyCustomAiAssistantChatId"


var OLLAMA_LLM_MODEL_API_ENDPOINT: String = "/generate"
const val OLLAMA_DEFAULT_LLM_MODEL: String = "mistral-nemo"
const val OLLAMA_LLM_MODEL_TOKENS: Int = 128000

var OLLAMA_EMBEDDING_MODEL_API_ENDPOINT: String = "/embeddings"
const val OLLAMA_DEFAULT_EMBEDDING_MODEL: String = "nomic-embed-text"
const val OLLAMA_EMBEDDING_MODEL_TOKENS: Int = 8192

// 4 characters = 1 token
var EMBEDDING_MAX_RESULTS: String = "5"
var EMBEDDING_MIN_SCORE: String = "0.7"

var EMBEDDING_DIMENSIONALITY = 768