package org.oleg.iem.inlay_hints

import com.intellij.codeInsight.hints.*
import com.intellij.lang.Language

@Suppress("UnstableApiUsage")
class FunctionInfoInlayProviderFactory : InlayHintsProviderFactory {

    override fun getProvidersInfoForLanguage(language: Language): List<InlayHintsProvider<out Any>> {
        println("Trying to get inlay hints provider for language " + language.id)
        if (language.id == "TEXT"){
            println("Unknown language. Not supported. Please write PST file or install plugin")
            return emptyList()
        } else if (language.id == "Gherkin"){
            println("Good! Trying to add inlay hints to Gherkin language file")
            return listOf(GherkinInlayProvider())
        }
        return emptyList()
    }
}