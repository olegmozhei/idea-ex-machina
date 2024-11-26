package org.oleg.iem.my_prompt_language;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

import org.oleg.iem.my_prompt_language.gen.parser.myPromptParser;
import org.oleg.iem.my_prompt_language.gen.psi.MyPromptTypes;

final class MyPromptParserDefinition implements ParserDefinition {

    public static final IFileElementType FILE = new IFileElementType(MyPromptLanguage.INSTANCE);

    @Override
    public @NotNull Lexer createLexer(Project project) {
        return new MyPromptLexerAdapter();
    }

    @Override
    public @NotNull TokenSet getCommentTokens() {
        return MyPromptTokenSets.COMMENTS;
    }

    @Override
    public @NotNull TokenSet getStringLiteralElements() {
        return TokenSet.EMPTY;
    }

    @Override
    public @NotNull PsiParser createParser(Project project) {
        return new myPromptParser();
    }

    @Override
    public @NotNull IFileElementType getFileNodeType() {
        return FILE;
    }

    @Override
    public @NotNull PsiFile createFile(@NotNull FileViewProvider fileViewProvider) {
        return new MyPromptFile(fileViewProvider);
    }

    @Override
    public @NotNull PsiElement createElement(ASTNode astNode) {
        return MyPromptTypes.Factory.createElement(astNode);
    }

}
