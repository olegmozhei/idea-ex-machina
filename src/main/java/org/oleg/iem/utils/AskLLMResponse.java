package org.oleg.iem.utils;

public class AskLLMResponse {
    public final String prompt;
    public final String promptTemplate;
    public final String query;
    public final String llmResponse;

    public AskLLMResponse(String prompt, String promptTemplate, String query, String llmResponse) {
        this.prompt = prompt;
        this.promptTemplate = promptTemplate;
        this.query = query;
        this.llmResponse = llmResponse;
    }
}
