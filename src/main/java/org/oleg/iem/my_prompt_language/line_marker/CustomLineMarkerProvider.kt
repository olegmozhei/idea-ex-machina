package org.oleg.iem.my_prompt_language.line_marker

import com.intellij.codeInsight.daemon.GutterIconNavigationHandler
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement
import java.awt.event.MouseEvent

//internal class CustomLineMarkerProvider : LineMarkerProvider {
//    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
//        return if (element.text == "Query"){
//            println("Trying to return line marker")
//            LineMarkerInfo(
//                element,
//                element.textRange,
//                MyPromptIcons.FILE,
//                null,
//                Temp(),
//                GutterIconRenderer.Alignment.CENTER
//            )
//        } else {
//            null
//        }
//    }
//
//}
//
//class Temp : GutterIconNavigationHandler<PsiElement?> {
//
//    override fun navigate(p0: MouseEvent?, p1: PsiElement?){
//        val parent = p1!!.parent
//        if (parent !is MyPromptQueryImpl) return
//    }
//}