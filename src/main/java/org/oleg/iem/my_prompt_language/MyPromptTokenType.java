package org.oleg.iem.my_prompt_language;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class MyPromptTokenType extends IElementType {

    public MyPromptTokenType(@NotNull @NonNls String name){
        super(name, MyPromptLanguage.INSTANCE);
    }

    @Override
    public String toString(){
        return "MyPromptTokenType." + super.toString();
    }
}
