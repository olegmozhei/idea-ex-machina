// This is a generated file. Not intended for manual editing.
package org.oleg.iem.my_prompt_language.gen.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static org.oleg.iem.my_prompt_language.gen.psi.MyPromptTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class myPromptParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, null);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return myPromptFile(b, l + 1);
  }

  /* ********************************************************** */
  // VALUE
  public static boolean cell_value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "cell_value")) return false;
    if (!nextTokenIs(b, VALUE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, VALUE);
    exit_section_(b, m, CELL_VALUE, r);
    return r;
  }

  /* ********************************************************** */
  // CONTEXT_CHUNKS_KEYWORD SEPARATOR VALUE_LINE
  public static boolean context_chunks(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "context_chunks")) return false;
    if (!nextTokenIs(b, CONTEXT_CHUNKS_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, CONTEXT_CHUNKS_KEYWORD, SEPARATOR, VALUE_LINE);
    exit_section_(b, m, CONTEXT_CHUNKS, r);
    return r;
  }

  /* ********************************************************** */
  // DETAILS_KEYWORD SEPARATOR (VALUE_LINE|VALUE_MULTILINE)
  public static boolean details(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "details")) return false;
    if (!nextTokenIs(b, DETAILS_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, DETAILS_KEYWORD, SEPARATOR);
    r = r && details_2(b, l + 1);
    exit_section_(b, m, DETAILS, r);
    return r;
  }

  // VALUE_LINE|VALUE_MULTILINE
  private static boolean details_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "details_2")) return false;
    boolean r;
    r = consumeToken(b, VALUE_LINE);
    if (!r) r = consumeToken(b, VALUE_MULTILINE);
    return r;
  }

  /* ********************************************************** */
  // comment* context_chunks comment* query comment* details comment* prompt comment* variables* comment* variables_table* comment*
  static boolean myPromptFile(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "myPromptFile")) return false;
    if (!nextTokenIs(b, "", COMMENT, CONTEXT_CHUNKS_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = myPromptFile_0(b, l + 1);
    r = r && context_chunks(b, l + 1);
    r = r && myPromptFile_2(b, l + 1);
    r = r && query(b, l + 1);
    r = r && myPromptFile_4(b, l + 1);
    r = r && details(b, l + 1);
    r = r && myPromptFile_6(b, l + 1);
    r = r && prompt(b, l + 1);
    r = r && myPromptFile_8(b, l + 1);
    r = r && myPromptFile_9(b, l + 1);
    r = r && myPromptFile_10(b, l + 1);
    r = r && myPromptFile_11(b, l + 1);
    r = r && myPromptFile_12(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // comment*
  private static boolean myPromptFile_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "myPromptFile_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, COMMENT)) break;
      if (!empty_element_parsed_guard_(b, "myPromptFile_0", c)) break;
    }
    return true;
  }

  // comment*
  private static boolean myPromptFile_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "myPromptFile_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, COMMENT)) break;
      if (!empty_element_parsed_guard_(b, "myPromptFile_2", c)) break;
    }
    return true;
  }

  // comment*
  private static boolean myPromptFile_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "myPromptFile_4")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, COMMENT)) break;
      if (!empty_element_parsed_guard_(b, "myPromptFile_4", c)) break;
    }
    return true;
  }

  // comment*
  private static boolean myPromptFile_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "myPromptFile_6")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, COMMENT)) break;
      if (!empty_element_parsed_guard_(b, "myPromptFile_6", c)) break;
    }
    return true;
  }

  // comment*
  private static boolean myPromptFile_8(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "myPromptFile_8")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, COMMENT)) break;
      if (!empty_element_parsed_guard_(b, "myPromptFile_8", c)) break;
    }
    return true;
  }

  // variables*
  private static boolean myPromptFile_9(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "myPromptFile_9")) return false;
    while (true) {
      int c = current_position_(b);
      if (!variables(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "myPromptFile_9", c)) break;
    }
    return true;
  }

  // comment*
  private static boolean myPromptFile_10(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "myPromptFile_10")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, COMMENT)) break;
      if (!empty_element_parsed_guard_(b, "myPromptFile_10", c)) break;
    }
    return true;
  }

  // variables_table*
  private static boolean myPromptFile_11(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "myPromptFile_11")) return false;
    while (true) {
      int c = current_position_(b);
      if (!variables_table(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "myPromptFile_11", c)) break;
    }
    return true;
  }

  // comment*
  private static boolean myPromptFile_12(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "myPromptFile_12")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, COMMENT)) break;
      if (!empty_element_parsed_guard_(b, "myPromptFile_12", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // PROMPT_KEYWORD SEPARATOR (VALUE_LINE|VALUE_MULTILINE)
  public static boolean prompt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "prompt")) return false;
    if (!nextTokenIs(b, PROMPT_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, PROMPT_KEYWORD, SEPARATOR);
    r = r && prompt_2(b, l + 1);
    exit_section_(b, m, PROMPT, r);
    return r;
  }

  // VALUE_LINE|VALUE_MULTILINE
  private static boolean prompt_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "prompt_2")) return false;
    boolean r;
    r = consumeToken(b, VALUE_LINE);
    if (!r) r = consumeToken(b, VALUE_MULTILINE);
    return r;
  }

  /* ********************************************************** */
  // QUERY_KEYWORD SEPARATOR (VALUE_LINE|VALUE_MULTILINE)
  public static boolean query(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "query")) return false;
    if (!nextTokenIs(b, QUERY_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, QUERY_KEYWORD, SEPARATOR);
    r = r && query_2(b, l + 1);
    exit_section_(b, m, QUERY, r);
    return r;
  }

  // VALUE_LINE|VALUE_MULTILINE
  private static boolean query_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "query_2")) return false;
    boolean r;
    r = consumeToken(b, VALUE_LINE);
    if (!r) r = consumeToken(b, VALUE_MULTILINE);
    return r;
  }

  /* ********************************************************** */
  // PIPE cell_value (PIPE cell_value)* PIPE
  public static boolean table_header(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_header")) return false;
    if (!nextTokenIs(b, PIPE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PIPE);
    r = r && cell_value(b, l + 1);
    r = r && table_header_2(b, l + 1);
    r = r && consumeToken(b, PIPE);
    exit_section_(b, m, TABLE_HEADER, r);
    return r;
  }

  // (PIPE cell_value)*
  private static boolean table_header_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_header_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!table_header_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "table_header_2", c)) break;
    }
    return true;
  }

  // PIPE cell_value
  private static boolean table_header_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_header_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PIPE);
    r = r && cell_value(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // PIPE cell_value (PIPE cell_value)* PIPE
  public static boolean table_row(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_row")) return false;
    if (!nextTokenIs(b, PIPE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PIPE);
    r = r && cell_value(b, l + 1);
    r = r && table_row_2(b, l + 1);
    r = r && consumeToken(b, PIPE);
    exit_section_(b, m, TABLE_ROW, r);
    return r;
  }

  // (PIPE cell_value)*
  private static boolean table_row_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_row_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!table_row_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "table_row_2", c)) break;
    }
    return true;
  }

  // PIPE cell_value
  private static boolean table_row_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "table_row_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PIPE);
    r = r && cell_value(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // VARIABLES_KEYWORD SEPARATOR
  public static boolean variables(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variables")) return false;
    if (!nextTokenIs(b, VARIABLES_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, VARIABLES_KEYWORD, SEPARATOR);
    exit_section_(b, m, VARIABLES, r);
    return r;
  }

  /* ********************************************************** */
  // table_header table_row*
  public static boolean variables_table(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variables_table")) return false;
    if (!nextTokenIs(b, PIPE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = table_header(b, l + 1);
    r = r && variables_table_1(b, l + 1);
    exit_section_(b, m, VARIABLES_TABLE, r);
    return r;
  }

  // table_row*
  private static boolean variables_table_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variables_table_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!table_row(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "variables_table_1", c)) break;
    }
    return true;
  }

}
