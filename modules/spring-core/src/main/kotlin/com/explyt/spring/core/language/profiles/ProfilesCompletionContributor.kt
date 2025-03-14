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

package com.explyt.spring.core.language.profiles

import com.explyt.spring.core.profile.SpringProfilesService
import com.explyt.spring.core.statistic.StatisticActionId.COMPLETION_PROFILES
import com.explyt.spring.core.statistic.StatisticInsertHandler
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.project.modules
import org.apache.commons.lang3.StringUtils

class ProfilesCompletionContributor : CompletionContributor() {

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        val position = parameters.position
        val profilesService = SpringProfilesService.getInstance(position.project)

        position.project.modules.asSequence()
            .flatMap { profilesService.loadExistingProfiles(it).asSequence() }
            .map { StringUtils.trim(it) }
            .filter { it.isNotBlank() }
            .distinct()
            .sorted()
            .map {
                LookupElementBuilder
                    .create(it)
                    .withInsertHandler(StatisticInsertHandler(COMPLETION_PROFILES))
            }
            .forEach { result.addElement(it) }
    }

}