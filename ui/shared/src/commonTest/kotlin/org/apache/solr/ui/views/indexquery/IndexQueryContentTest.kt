/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */package org.apache.solr.ui.views.indexquery

import org.apache.solr.ui.views.searchhandler.SearchRequestForm
import kotlin.test.Test
import kotlin.test.assertEquals

class IndexQueryContentTest {

    @Test
    fun testBlankValuesAreLeftOut() {
        val form = SearchRequestForm(
            query = listOf("*:*", " "),
            filterQueries = listOf(""),
            fields = listOf("id", ""),
            sort = "",
        )

        assertEquals(
            listOf("q" to "*:*", "fl" to "id", "rows" to "10", "start" to "0", "wt" to "json"),
            form.toParameters(debug = false),
        )
    }

    @Test
    fun testDebugModeAddsDebugQuery() {
        val form = SearchRequestForm(query = listOf("title:solr"), filterQueries = listOf("a:1", "b:2"))

        assertEquals(
            listOf(
                "q" to "title:solr",
                "fq" to "a:1",
                "fq" to "b:2",
                "rows" to "10",
                "start" to "0",
                "wt" to "json",
                "debugQuery" to "true",
            ),
            form.toParameters(debug = true),
        )
    }
}
