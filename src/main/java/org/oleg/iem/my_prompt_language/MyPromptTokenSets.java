package org.oleg.iem.my_prompt_language;

import com.intellij.psi.tree.TokenSet;
import org.oleg.iem.my_prompt_language.gen.psi.MyPromptTypes;

public interface MyPromptTokenSets {
    TokenSet COMMENTS = TokenSet.create(MyPromptTypes.COMMENT);
}
