/*
 * Copyright © 2024 Explyt Ltd
 *
 * All rights reserved.
 *
 * This code and software are the property of Explyt Ltd and are protected by copyright and other intellectual property laws.
 *
 * You may use this code under the terms of the Explyt Source License Version 1.0 ("License"), if you accept its terms and conditions.
 *
 * By installing, downloading, accessing, using, or distributing this code, you agree to the terms and conditions of the License.
 * If you do not agree to such terms and conditions, you must cease using this code and immediately delete all copies of it.
 *
 * You may obtain a copy of the License at: https://github.com/explyt/spring-plugin/blob/main/EXPLYT-SOURCE-LICENSE.md
 *
 * Unauthorized use of this code constitutes a violation of intellectual property rights and may result in legal action.
 */

package com.explyt.spring.core.inspections.quickfix

import com.explyt.spring.core.SpringCoreBundle
import com.explyt.spring.core.util.RenameUtil
import com.intellij.codeInspection.LocalQuickFixAndIntentionActionOnPsiElement
import com.intellij.lang.properties.psi.impl.PropertyImpl
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorModificationUtil
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementFactory
import com.intellij.psi.PsiFile
import com.intellij.psi.search.searches.ReferencesSearch
import org.jetbrains.kotlin.idea.KotlinLanguage
import org.jetbrains.kotlin.psi.KtPsiFactory

class ReplacementKeyQuickFix(val key: String, element: PsiElement) :
    LocalQuickFixAndIntentionActionOnPsiElement(element) {
    override fun getFamilyName(): String =
        SpringCoreBundle.message("explyt.spring.inspection.properties.quick.fix.replacement", key)

    override fun getText(): String = familyName

    override fun invoke(
        project: Project,
        file: PsiFile,
        editor: Editor?,
        startElement: PsiElement,
        endElement: PsiElement
    ) {
        if (!ApplicationManager.getApplication().isWriteAccessAllowed) return

        if (startElement !is PropertyImpl) return
        val containingFile = startElement.context?.containingFile

        WriteCommandAction.runWriteCommandAction(project, "Replace key", null, {
            if (editor != null) {
                val startOffset = startElement.textRange.startOffset
                val oldKey = startElement.text.substringBefore("=")
                val end = startOffset + oldKey.length
                editor.caretModel.moveToOffset(startOffset)
                editor.selectionModel.setSelection(startOffset, end)
                EditorModificationUtil.deleteSelectedText(editor)
                EditorModificationUtil.insertStringAtCaret(editor, key)

                renameUsages(project, startElement, key)
                RenameUtil.renameSameProperty(project, startElement, oldKey, key)
            }
        }, containingFile)
    }

    private fun renameUsages(project: Project, elementToRename: PsiElement, newKey: String) {
        val usages = ReferencesSearch.search(elementToRename).findAll().toList()
        if (usages.isEmpty()) return

        for (usage in usages) {
            val usageElement = usage.element
            val oldText = usageElement.text.substringAfter("{").substringBefore("}").substringBefore(":")
            val newText = usageElement.text.replace(oldText, newKey)
            val newElement = if (usageElement.language == KotlinLanguage.INSTANCE) {
                val factory = KtPsiFactory(usageElement.project)
                factory.createExpression(newText)
            } else {
                PsiElementFactory.getInstance(usageElement.project)
                    .createExpressionFromText(newText, usageElement.context)
            }
            WriteCommandAction.runWriteCommandAction(project) {
                usageElement.replace(newElement)
            }
        }
    }
}
