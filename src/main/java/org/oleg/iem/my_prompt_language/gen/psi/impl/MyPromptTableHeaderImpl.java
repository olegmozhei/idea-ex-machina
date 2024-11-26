// This is a generated file. Not intended for manual editing.
package org.oleg.iem.my_prompt_language.gen.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import org.oleg.iem.my_prompt_language.gen.psi.MyPromptCellValue;
import org.oleg.iem.my_prompt_language.gen.psi.MyPromptTableHeader;
import org.oleg.iem.my_prompt_language.gen.psi.MyPromptVisitor;
import org.oleg.iem.my_prompt_language.psi.*;

public class MyPromptTableHeaderImpl extends ASTWrapperPsiElement implements MyPromptTableHeader {

  public MyPromptTableHeaderImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull MyPromptVisitor visitor) {
    visitor.visitTableHeader(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof MyPromptVisitor) accept((MyPromptVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<MyPromptCellValue> getCellValueList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, MyPromptCellValue.class);
  }

}
