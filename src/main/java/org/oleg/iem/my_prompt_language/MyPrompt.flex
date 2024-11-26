package org.oleg.iem.my_prompt_language;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import org.oleg.iem.my_prompt_language.gen.psi.MyPromptTypes;
import com.intellij.psi.TokenType;

%%

%class GeneratedMyPromptLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{ return;
%eof}

COMMENT=("// ".*)|[\n]
CRLF=\R
WHITE_SPACE=[\ \n\t\f]
FIRST_VALUE_CHARACTER=[^ \n\f\\\|] | "\\"{CRLF} | "\\".
VALUE_CHARACTER=[^\f\\\|] | "\\"{CRLF} | "\\".
UNTIL_KEYWORDS=.*?\n("?=Query|Details")
SEPARATOR=[:=]
VARIABLES_SEPARATOR=[:=]
CONTEXT_CHUNKS_KEYWORD="Context Chunks"
QUERY_KEYWORD="Query"
VALUE=[\n]*[d]*?(=Details)
VALUE_LINE=[^\"\n][^\n]*
VALUE_MULTILINE=([^\n\"][^\n\"][^\n\"]+[\n])+

DETAILS_KEYWORD=Details
PROMPT_KEYWORD=Prompt
VARIABLES_KEYWORD=Variables
PIPE=[]*\|[ ]*

// Custom states:
%state WAITING_VALUE WAITING_CELL WAITING_SEPARATOR WAITING_CONTEXT_VALUE WAITING_CONTEXT_SEPARATOR

%%
// Rules (Token Matching Rules) part:
// This rule applies only when the lexer is in the YYINITIAL state.
<YYINITIAL> {COMMENT}-{CRLF}+                                          { yybegin(YYINITIAL); return MyPromptTypes.COMMENT_VALUE; }

<YYINITIAL> {CONTEXT_CHUNKS_KEYWORD}+                                  { yybegin(WAITING_SEPARATOR); return MyPromptTypes.CONTEXT_CHUNKS_KEYWORD; }

<WAITING_SEPARATOR> {SEPARATOR}                                        { yybegin(WAITING_VALUE); return MyPromptTypes.SEPARATOR; }

<WAITING_VALUE> {VALUE_LINE}{CRLF}+                                    { yybegin(YYINITIAL); return MyPromptTypes.VALUE_LINE; }

<WAITING_VALUE> \"\"\"{CRLF}{VALUE_MULTILINE}\"\"\"{CRLF}+             { yybegin(YYINITIAL); return MyPromptTypes.VALUE_MULTILINE; }

<YYINITIAL> {QUERY_KEYWORD}                                            { yybegin(WAITING_SEPARATOR); return MyPromptTypes.QUERY_KEYWORD; }

<YYINITIAL> {DETAILS_KEYWORD}                                          { yybegin(WAITING_SEPARATOR); return MyPromptTypes.DETAILS_KEYWORD; }

<YYINITIAL> {PROMPT_KEYWORD}                                           { yybegin(WAITING_SEPARATOR); return MyPromptTypes.PROMPT_KEYWORD; }

<YYINITIAL> {VARIABLES_KEYWORD}                                        { yybegin(WAITING_SEPARATOR); return MyPromptTypes.VARIABLES_KEYWORD; }

<WAITING_SEPARATOR> {SEPARATOR}                                        { yybegin(WAITING_VALUE); return MyPromptTypes.SEPARATOR; }

<WAITING_SEPARATOR> {VARIABLES_SEPARATOR}                              { yybegin(WAITING_CELL); return MyPromptTypes.VARIABLES_SEPARATOR; }

{PIPE}                                                                 { yybegin(WAITING_CELL); return MyPromptTypes.PIPE; }

<WAITING_CELL> {UNTIL_KEYWORDS}                                        { yybegin(WAITING_CELL); return MyPromptTypes.VALUE; }

<WAITING_VALUE, WAITING_CELL> {CRLF}({CRLF}|{WHITE_SPACE})+            { yybegin(YYINITIAL); return TokenType.WHITE_SPACE; }

<WAITING_VALUE> {WHITE_SPACE}+                                         { yybegin(WAITING_VALUE); return TokenType.WHITE_SPACE; }

({CRLF}|{WHITE_SPACE})+                                                { yybegin(YYINITIAL); return TokenType.WHITE_SPACE; }

[^]                                                                    { return TokenType.BAD_CHARACTER; }
