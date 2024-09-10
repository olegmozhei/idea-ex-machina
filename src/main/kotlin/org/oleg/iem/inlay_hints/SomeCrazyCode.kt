package org.oleg.iem.inlay_hints

import com.intellij.codeInsight.hints.FactoryInlayHintsCollector
import com.intellij.codeInsight.hints.InlayHintsSink
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.actionSystem.Presentation
import com.intellij.openapi.actionSystem.impl.SimpleDataContext
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.startOffset
import org.oleg.iem.AI_ASSISTANT_CHAT_ID
import org.oleg.iem.MY_DATA_KEY
import org.jetbrains.plugins.cucumber.psi.GherkinFile
import org.jetbrains.plugins.cucumber.psi.GherkinRecursiveElementVisitor
import org.jetbrains.plugins.cucumber.psi.GherkinScenario


@Suppress("UnstableApiUsage")
class FunctionInfoInlayHintsCollector(private val file: PsiFile,
                                      editor: Editor) :
    FactoryInlayHintsCollector(editor) {

    override fun collect(element: PsiElement, editor: Editor, sink: InlayHintsSink): Boolean {

        println("Starting analyzing open in editor file")

        val gherkinFile: GherkinFile = file as GherkinFile
        // TODO: also implement iteration through scenario outlines
        val a = Analyzer()
        gherkinFile.accept(a)

        for (i in 0 until a.getOffsets().size) {
            val explainPresentation =
                factory.referenceOnHover(factory.smallText("Explain")) { _, _ ->
                    println("Explain scenario '" + a.getScenarios()[i] + "'")
                }
            val generatePresentation =
                factory.referenceOnHover(factory.smallText("Generate Case")) { _, _ ->
                    println("Going to generate case for test '" + a.getScenarios()[i] + "'")

                    val window = ToolWindowManager.getInstance(file.project).getToolWindow(AI_ASSISTANT_CHAT_ID)
                    if (window == null){
                        println("Can't find Tool Window!")
                        return@referenceOnHover
                    }
                    window.show()

                    triggerCustomAction("Going to generate case for test '" + a.getScenarios()[i] + "'")
                }
            val presentations =
                arrayOf(
                    factory.smallText("⛪ Assistant actions:  "),
                    explainPresentation,
                    factory.text(" | "),
                    generatePresentation)

            val seq = factory.seq(*presentations)
            sink.addBlockElement(
                a.getOffsets()[i],
                relatesToPrecedingText = false,
                showAbove = true,
                priority = 0,
                presentation = seq)
        }
        return false    // returning false prevents iteration from whole PSI tree
    }

    private fun triggerCustomAction(message: String) {
        println("Triggered custom action programmatically")
        val actionManager = ActionManager.getInstance()

        // Find the action by its ID (should be equal to plugin.xml):
        val action = actionManager.getAction("MyCustomActionId")

        if (action != null) {
            val dataContext: DataContext = SimpleDataContext.builder()
                .add(MY_DATA_KEY, message)
                .build()

            val presentation = Presentation()
            val actionEvent = AnActionEvent(
                null, dataContext, "", presentation, actionManager, 0
            )

            action.actionPerformed(actionEvent)
        } else {
            println("Can't trigger action")
        }
    }

    internal class Analyzer : GherkinRecursiveElementVisitor() {

        private val scenarios: MutableList<String> = ArrayList()
        private val offsets: MutableList<Int> = ArrayList()

        override fun visitScenario(scenario: GherkinScenario?) {
            super.visitScenario(scenario)
            scenarios.add(scenario!!.scenarioName)
            offsets.add(scenario.startOffset)
        }

        fun getScenarios():List<String> {
            return scenarios
        }

        fun getOffsets():List<Int> {
            return offsets
        }
    }
}
