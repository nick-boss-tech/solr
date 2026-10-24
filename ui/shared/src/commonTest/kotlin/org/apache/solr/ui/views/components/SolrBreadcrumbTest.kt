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
 */
package org.apache.solr.ui.views.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals

class SolrBreadcrumbTest {

    private val entries = listOf("Home", "Collections", "techproducts")

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testShowsEveryEntry() = runComposeUiTest {
        setContent { SolrBreadcrumb(entries = entries, onEntryClick = {}) }

        entries.forEach { onNodeWithText(it).assertExists() }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testClickingAnEarlierEntryReportsItsIndex() = runComposeUiTest {
        val clicked = mutableListOf<Int>()
        setContent { SolrBreadcrumb(entries = entries, onEntryClick = { clicked.add(it) }) }

        onNodeWithText("Collections").performClick()

        assertEquals(listOf(1), clicked)
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testClickingTheLastEntryReportsNothing() = runComposeUiTest {
        val clicked = mutableListOf<Int>()
        setContent { SolrBreadcrumb(entries = entries, onEntryClick = { clicked.add(it) }) }

        onNodeWithText("techproducts").performClick()

        assertEquals(emptyList(), clicked)
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testPinWrappedEntriesStayDisplayed() = runComposeUiTest {
        setContent {
            Box(modifier = Modifier.width(120.dp)) {
                SolrBreadcrumb(entries = entries, onEntryClick = {})
            }
        }

        entries.forEach { onNodeWithText(it).assertIsDisplayed() }
    }
}
