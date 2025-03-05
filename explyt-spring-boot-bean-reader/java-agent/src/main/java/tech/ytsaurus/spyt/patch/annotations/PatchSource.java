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
 * Original source code: https://github.com/ytsaurus/ytsaurus-spyt/blob/feature/spark-3.5.x_support/spark-patch/src/main/java/tech/ytsaurus/spyt/patch/annotations/PatchSource.java
 */

package tech.ytsaurus.spyt.patch.annotations;

public @interface PatchSource {
    /**
     * The patch class that should be used instead of this class. It is helpful when origin class was renamed,
     * but method bodies wasn't changed, for example all methods of CastBase class for versions prior to 3.4.0
     * were moved to Cast class since 3.4.0 version.
     */
    String value();
}
