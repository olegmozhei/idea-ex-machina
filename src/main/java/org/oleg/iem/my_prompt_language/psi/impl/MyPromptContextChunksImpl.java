// This is a generated file. Not intended for manual editing.
package org.oleg.iem.my_prompt_language.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static org.oleg.iem.my_prompt_language.gen.psi.MyPromptTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import org.oleg.iem.my_prompt_language.psi.*;

public class MyPromptContextChunksImpl extends ASTWrapperPsiElement implements MyPromptContextChunks {

  public MyPromptContextChunksImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull MyPromptVisitor visitor) {
    visitor.visitContextChunks(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof MyPromptVisitor) accept((MyPromptVisitor)visitor);
    else super.accept(visitor);
  }

}
