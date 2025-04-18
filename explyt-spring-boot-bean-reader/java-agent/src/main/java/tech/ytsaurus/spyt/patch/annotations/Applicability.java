/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 * Original source code: https://github.com/ytsaurus/ytsaurus-spyt/blob/feature/spark-3.5.x_support/spark-patch/src/main/java/tech/ytsaurus/spyt/patch/annotations/Applicability.java
 */

package tech.ytsaurus.spyt.patch.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Applicability {
    /**
     * Spark version from which this patch is applicable, inclusive.
     */
    String from() default "3.2.2";

    /**
     * Spark version to which this patch is applicable, inclusive. If empty string, then the upper version is unbounded.
     */
    String to() default "";
}
