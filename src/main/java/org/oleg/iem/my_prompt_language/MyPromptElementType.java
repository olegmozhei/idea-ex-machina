package org.oleg.iem.my_prompt_language;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class MyPromptElementType extends IElementType {

    public MyPromptElementType(@NotNull @NonNls String name){
        super(name, MyPromptLanguage.INSTANCE);
    }
}
