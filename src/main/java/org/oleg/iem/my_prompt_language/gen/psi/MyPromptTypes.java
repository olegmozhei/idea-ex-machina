// This is a generated file. Not intended for manual editing.
package org.oleg.iem.my_prompt_language.gen.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import org.oleg.iem.my_prompt_language.MyPromptElementType;
import org.oleg.iem.my_prompt_language.gen.psi.impl.*;

public interface MyPromptTypes {

  IElementType CELL_VALUE = new MyPromptElementType("CELL_VALUE");
  IElementType CONTEXT_CHUNKS = new MyPromptElementType("CONTEXT_CHUNKS");
  IElementType DETAILS = new MyPromptElementType("DETAILS");
  IElementType PROMPT = new MyPromptElementType("PROMPT");
  IElementType QUERY = new MyPromptElementType("QUERY");
  IElementType TABLE_HEADER = new MyPromptElementType("TABLE_HEADER");
  IElementType TABLE_ROW = new MyPromptElementType("TABLE_ROW");
  IElementType VARIABLES = new MyPromptElementType("VARIABLES");
  IElementType VARIABLES_TABLE = new MyPromptElementType("VARIABLES_TABLE");

  IElementType COMMENT = new MyPromptTokenType("comment");
  IElementType CONTEXT_CHUNKS_KEYWORD = new MyPromptTokenType("CONTEXT_CHUNKS_KEYWORD");
  IElementType DETAILS_KEYWORD = new MyPromptTokenType("DETAILS_KEYWORD");
  IElementType PIPE = new MyPromptTokenType("PIPE");
  IElementType PROMPT_KEYWORD = new MyPromptTokenType("PROMPT_KEYWORD");
  IElementType QUERY_KEYWORD = new MyPromptTokenType("QUERY_KEYWORD");
  IElementType SEPARATOR = new MyPromptTokenType("SEPARATOR");
  IElementType VALUE = new MyPromptTokenType("VALUE");
  IElementType VALUE_LINE = new MyPromptTokenType("VALUE_LINE");
  IElementType VALUE_MULTILINE = new MyPromptTokenType("VALUE_MULTILINE");
  IElementType VARIABLES_KEYWORD = new MyPromptTokenType("VARIABLES_KEYWORD");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == CELL_VALUE) {
        return new MyPromptCellValueImpl(node);
      }
      else if (type == CONTEXT_CHUNKS) {
        return new MyPromptContextChunksImpl(node);
      }
      else if (type == DETAILS) {
        return new MyPromptDetailsImpl(node);
      }
      else if (type == PROMPT) {
        return new MyPromptPromptImpl(node);
      }
      else if (type == QUERY) {
        return new MyPromptQueryImpl(node);
      }
      else if (type == TABLE_HEADER) {
        return new MyPromptTableHeaderImpl(node);
      }
      else if (type == TABLE_ROW) {
        return new MyPromptTableRowImpl(node);
      }
      else if (type == VARIABLES) {
        return new MyPromptVariablesImpl(node);
      }
      else if (type == VARIABLES_TABLE) {
        return new MyPromptVariablesTableImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
