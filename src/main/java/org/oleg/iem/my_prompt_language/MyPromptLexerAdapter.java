package org.oleg.iem.my_prompt_language;

import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.FlexLexer;
import org.jetbrains.annotations.NotNull;

public class MyPromptLexerAdapter extends FlexAdapter {

    public MyPromptLexerAdapter(){
        super(new GeneratedMyPromptLexer(null));
    }

    public MyPromptLexerAdapter(@NotNull FlexLexer flex) {
        super(flex);
    }
}
