package org.oleg.iem.my_prompt_language;

import com.intellij.lang.Language;

public class MyPromptLanguage extends Language {
    public static final MyPromptLanguage INSTANCE = new MyPromptLanguage();
    private MyPromptLanguage() {
        super("MyPrompt");
    }
}
