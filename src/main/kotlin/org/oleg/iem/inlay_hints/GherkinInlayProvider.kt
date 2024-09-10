package org.oleg.iem.inlay_hints

import com.intellij.codeInsight.hints.*
import com.intellij.lang.Language
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.currentOrDefaultProject
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import javax.swing.JPanel

@Suppress("UnstableApiUsage")
class GherkinInlayProvider : InlayHintsProvider<NoSettings> {
    override fun getCollectorFor(
        psiFileArgument: PsiFile,
        editor: Editor,
        settings: NoSettings,
        sink: InlayHintsSink
    ): InlayHintsCollector? {
        println("Trying to add inlay hints to Gherkin File")
        val file = PsiDocumentManager
            .getInstance(currentOrDefaultProject(editor.project))
            .getPsiFile(editor.document)
        if (file == null) return null
        return FunctionInfoInlayHintsCollector(file, editor)
    }

    override val key = SettingsKey<NoSettings>(GherkinInlayProvider::class.qualifiedName!!)
    override val name = "My inlay hints"
    override val previewText = "Hints to simplify work"
    override fun createSettings() = NoSettings()

    override val isVisibleInSettings = false

    override fun isLanguageSupported(language: Language): Boolean {
        return true
    }

    override fun createConfigurable(settings: NoSettings): ImmediateConfigurable {
        return object : ImmediateConfigurable {
            override fun createComponent(listener: ChangeListener) = JPanel()

            override val mainCheckboxText: String
                get() = "Show Inlay Hints"
        }
    }
}