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

package com.explyt.spring.core.inspections

import com.explyt.inspection.SpringBaseUastLocalInspectionTool
import com.explyt.spring.core.inspections.utils.ResourceFileInspectionUtil
import com.intellij.codeInsight.AnnotationUtil
import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.ide.highlighter.XmlFileType
import org.jetbrains.uast.UClass

class SpringContextConfigurationClasspathInspection : SpringBaseUastLocalInspectionTool() {

    override fun checkClass(
        aClass: UClass,
        manager: InspectionManager,
        isOnTheFly: Boolean,
    ): Array<out ProblemDescriptor> {
        val problems = mutableListOf<ProblemDescriptor>()
        val propertySourceAnnotation = ResourceFileInspectionUtil
            .psiAnnotationContextConfigurationMembers(aClass.javaPsi)
        for (member in propertySourceAnnotation) {
            val pathValue = AnnotationUtil.getStringAttributeValue(member) ?: continue
            problems += ResourceFileInspectionUtil.getPathProblemsClasspath(
                XmlFileType.INSTANCE,
                pathValue,
                member,
                manager,
                isOnTheFly
            )
        }
        return problems.toTypedArray()
    }
}