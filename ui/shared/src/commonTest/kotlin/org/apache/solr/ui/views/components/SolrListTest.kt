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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals

class SolrListTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testItemsAreDisplayed() = runComposeUiTest {
        setContent {
            SolrList(
                items = listOf("first", "second"),
                filter = "",
                onFilterChange = {},
                itemContent = { Text(it, modifier = Modifier.testTag("item_$it")) },
            )
        }

        onNodeWithTag("item_first").assertExists()
        onNodeWithTag("item_second").assertExists()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testClearButtonHiddenWhenFilterIsEmpty() = runComposeUiTest {
        setContent {
            SolrList(
                items = emptyList<String>(),
                filter = "",
                onFilterChange = {},
                itemContent = { Text(it) },
            )
        }

        onNodeWithTag("solr_list_clear_button").assertDoesNotExist()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testClearButtonEmptiesFilter() = runComposeUiTest {
        var filter by mutableStateOf("logs")
        setContent {
            SolrList(
                items = emptyList<String>(),
                filter = filter,
                onFilterChange = { filter = it },
                itemContent = { Text(it) },
            )
        }

        onNodeWithTag("solr_list_clear_button").assertExists().performClick()
        waitForIdle()

        assertEquals("", filter)
        onNodeWithTag("solr_list_clear_button").assertDoesNotExist()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testLeadingAndTrailingActionsAreDisplayed() = runComposeUiTest {
        setContent {
            SolrList(
                items = emptyList<String>(),
                filter = "",
                onFilterChange = {},
                leadingActions = { Text("left", modifier = Modifier.testTag("leading")) },
                trailingActions = { Text("right", modifier = Modifier.testTag("trailing")) },
                itemContent = { Text(it) },
            )
        }

        onNodeWithTag("leading").assertIsDisplayed()
        onNodeWithTag("trailing").assertIsDisplayed()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testWideItemScrollsHorizontally() = runComposeUiTest {
        setContent {
            SolrList(
                items = listOf("wide"),
                filter = "",
                onFilterChange = {},
                itemContent = {
                    Row {
                        Box(modifier = Modifier.width(2000.dp))
                        Box(modifier = Modifier.size(10.dp).testTag("wide_end"))
                    }
                },
            )
        }

        onNodeWithTag("wide_end").assertIsNotDisplayed()
        onNodeWithTag("wide_end").performScrollTo().assertIsDisplayed()
    }
}
