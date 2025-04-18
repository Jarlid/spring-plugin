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

package com.explyt.spring.web.builder.openapi.json

import com.explyt.spring.core.inspections.utils.ExplytJsonUtil.iterateWithComma
import com.explyt.spring.web.builder.openapi.OpenApiPathsBuilder
import com.explyt.spring.web.inspections.quickfix.AddEndpointToOpenApiIntention

class OpenApiJsonPathsBuilder(indent: String = "", builder: StringBuilder = StringBuilder()) :
    OpenApiPathsBuilder(indent, builder), JsonValueGenerator {

    override fun addEndpoint(endpoint: AddEndpointToOpenApiIntention.EndpointInfo): OpenApiJsonPathsBuilder {
        val pathBuilder = pathBuilderByPath.computeIfAbsent(endpoint.path) {
            OpenApiJsonPathBuilder(endpoint.path, "$indent  ", builder)
        }

        pathBuilder.addEndpoint(endpoint)
        return this
    }

    override fun build() {
        builder.appendLine(",")
        builder.append("""$indent"paths": {""")

        builder.iterateWithComma(pathBuilderByPath.values.toList()) { pathBuilder ->
            pathBuilder.build()
        }

        builder.append("\n$indent}")
    }

}
