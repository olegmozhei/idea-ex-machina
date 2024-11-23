package org.oleg.iem.my_prompt_language;

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.util.NlsSafe;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class MyPromptFileType extends LanguageFileType {

    public static final MyPromptFileType INSTANCE = new MyPromptFileType();
    private MyPromptFileType(){
        super(MyPromptLanguage.INSTANCE);
    }

    @Override
    public @NonNls @NotNull String getName() {
        return "MyPrompt File";
    }

    @Override
    public @NlsContexts.Label @NotNull String getDescription() {
        return "MyPrompt language file";
    }

    @Override
    public @NlsSafe @NotNull String getDefaultExtension() {
        return "my_prompt";
    }

    @Override
    public Icon getIcon() {
        return MyPromptIcons.FILE;
    }
}
