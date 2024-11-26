package org.oleg.iem.my_prompt_language;

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
<YYINITIAL> {COMMENT}-{CRLF}+    { yybegin(YYINITIAL); return MyPromptTypes.COMMENT_VALUE; }