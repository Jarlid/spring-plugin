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

package com.explyt.spring.core.externalsystem.action

import com.explyt.spring.core.SpringCoreBundle.message
import com.explyt.spring.core.SpringCoreClasses
import com.explyt.spring.core.SpringIcons.SpringExplorer
import com.explyt.spring.core.externalsystem.process.SpringBootOpenProjectProvider
import com.explyt.spring.core.externalsystem.utils.Constants.SYSTEM_ID
import com.explyt.spring.core.externalsystem.utils.NativeBootUtils
import com.explyt.spring.core.runconfiguration.SpringBootRunConfiguration
import com.intellij.codeInsight.daemon.GutterIconNavigationHandler
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProviderDescriptor
import com.intellij.execution.RunManager
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.openapi.externalSystem.importing.ImportSpecBuilder
import com.intellij.openapi.externalSystem.service.project.manage.ExternalProjectsManagerImpl
import com.intellij.openapi.externalSystem.util.ExternalSystemApiUtil
import com.intellij.openapi.externalSystem.util.ExternalSystemUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.psi.PsiElement
import org.jetbrains.uast.UAnnotation
import org.jetbrains.uast.UClass
import org.jetbrains.uast.getParentOfType
import org.jetbrains.uast.toUElement
import java.awt.event.MouseEvent

class AttachSpringBootProjectLineMarkerContributor : LineMarkerProviderDescriptor() {

    override fun getName(): String? = null

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
        val uAnnotation = element.toUElement() as? UAnnotation ?: return null
        if (uAnnotation.qualifiedName != SpringCoreClasses.SPRING_BOOT_APPLICATION) return null
        val uClass = element.toUElement()?.getParentOfType<UClass>() ?: return null
        val sourcePsi = uAnnotation.sourcePsi ?: return null
        val canonicalPath = uClass.javaPsi.containingFile.virtualFile.canonicalPath ?: return null
        return LineMarkerInfo(
            sourcePsi,
            sourcePsi.textRange,
            SpringExplorer,
            { getTooltipText(element.project, canonicalPath) },
            AttachProjectIconGutterHandler(canonicalPath),
            GutterIconRenderer.Alignment.RIGHT,
            { getTooltipText(element.project, canonicalPath) },
        )
    }

    companion object {
        fun isExist(project: Project, canonicalPath: String) =
            ExternalSystemApiUtil.getSettings(project, SYSTEM_ID).getLinkedProjectSettings(canonicalPath) != null

        private fun getTooltipText(project: Project, canonicalPath: String): String {
            return if (isExist(project, canonicalPath)
            ) message("explyt.external.project.link.line.marker.refresh.text") else
                message("explyt.external.project.link.line.marker.text")
        }
    }
}

class AttachProjectIconGutterHandler(private val canonicalPath: String) : GutterIconNavigationHandler<PsiElement> {

    override fun navigate(e: MouseEvent?, elt: PsiElement?) {
        val project = elt?.project ?: return
        val exist = AttachSpringBootProjectLineMarkerContributor.isExist(project, canonicalPath)
        if (exist) {
            ExternalProjectsManagerImpl.getInstance(project).runWhenInitialized {
                ExternalSystemUtil.refreshProject(
                    canonicalPath, ImportSpecBuilder(project, SYSTEM_ID)
                )
            }
        } else {
            val runConfiguration = getRunConfiguration(project, canonicalPath)
            val virtualFile = LocalFileSystem.getInstance().findFileByPath(canonicalPath) ?: return
            SpringBootOpenProjectProvider().linkToExistingProject(virtualFile, runConfiguration, project)
        }
    }


    private fun getRunConfiguration(project: Project, canonicalPath: String): RunConfiguration? {
        val currentRunConfiguration = RunManager.getInstance(project).selectedConfiguration?.configuration
        if (currentRunConfiguration != null) {
            if (checkRunConfigurationForRun(currentRunConfiguration, canonicalPath)) return currentRunConfiguration
        }
        for (runConfiguration in RunManager.getInstance(project).allConfigurationsList) {
            if (runConfiguration !is SpringBootRunConfiguration) continue
            if (checkRunConfigurationForRun(runConfiguration, canonicalPath)) return currentRunConfiguration
        }
        return null
    }

    private fun checkRunConfigurationForRun(
        runConfiguration: RunConfiguration, canonicalPath: String
    ): Boolean {
        val mainClass = NativeBootUtils.getMainClass(runConfiguration)
        if (mainClass?.containingFile?.virtualFile?.canonicalPath == canonicalPath) {
            return true
        }
        return false
    }
}