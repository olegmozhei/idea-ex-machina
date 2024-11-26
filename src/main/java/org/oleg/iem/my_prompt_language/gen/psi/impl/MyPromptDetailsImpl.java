// This is a generated file. Not intended for manual editing.
package org.oleg.iem.my_prompt_language.gen.psi.impl;

import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import org.oleg.iem.my_prompt_language.gen.psi.MyPromptDetails;
import org.oleg.iem.my_prompt_language.gen.psi.MyPromptVisitor;
import org.oleg.iem.my_prompt_language.psi.*;

public class MyPromptDetailsImpl extends ASTWrapperPsiElement implements MyPromptDetails {

  public MyPromptDetailsImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull MyPromptVisitor visitor) {
    visitor.visitDetails(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof MyPromptVisitor) accept((MyPromptVisitor)visitor);
    else super.accept(visitor);
  }

}
