package org.oleg.iem.my_prompt_language;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

public class MyPromptFile extends PsiFileBase {

    public MyPromptFile(@NotNull FileViewProvider fileViewProvider) {
        super(fileViewProvider, MyPromptLanguage.INSTANCE);
    }

    @Override
    public @NotNull FileType getFileType() {
        return MyPromptFileType.INSTANCE;
    }

    @Override
    public String toString(){
        return "My Prompt File";
    }
}
