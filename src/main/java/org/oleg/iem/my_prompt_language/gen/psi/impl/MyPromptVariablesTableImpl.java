// This is a generated file. Not intended for manual editing.
package org.oleg.iem.my_prompt_language.gen.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import org.oleg.iem.my_prompt_language.gen.psi.MyPromptTableHeader;
import org.oleg.iem.my_prompt_language.gen.psi.MyPromptTableRow;
import org.oleg.iem.my_prompt_language.gen.psi.MyPromptVariablesTable;
import org.oleg.iem.my_prompt_language.gen.psi.MyPromptVisitor;
import org.oleg.iem.my_prompt_language.psi.*;

public class MyPromptVariablesTableImpl extends ASTWrapperPsiElement implements MyPromptVariablesTable {

  public MyPromptVariablesTableImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull MyPromptVisitor visitor) {
    visitor.visitVariablesTable(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof MyPromptVisitor) accept((MyPromptVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public MyPromptTableHeader getTableHeader() {
    return findNotNullChildByClass(MyPromptTableHeader.class);
  }

  @Override
  @NotNull
  public List<MyPromptTableRow> getTableRowList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, MyPromptTableRow.class);
  }

}
