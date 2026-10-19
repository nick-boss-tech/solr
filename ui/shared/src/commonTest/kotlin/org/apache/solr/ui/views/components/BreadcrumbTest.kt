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
 */package org.apache.solr.ui.views.components

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import kotlin.test.Test
import kotlin.test.assertEquals

class BreadcrumbTest {

    private val items = listOf("Collections", "films", "shard1")

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testShowsAllLabelsWithSeparators() = runComposeUiTest {
        setContent { Breadcrumb(items, onItemClick = {}) }

        onNodeWithText("Collections").assertExists()
        onNodeWithText("films").assertExists()
        onNodeWithText("shard1").assertExists()
        onAllNodesWithText(">").assertCountEquals(2)
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testClickOnEarlierItemReportsItsIndex() = runComposeUiTest {
        val clicked = mutableListOf<Int>()
        setContent { Breadcrumb(items, onItemClick = { clicked += it }) }

        onNodeWithTag("crumb_0").performClick()
        onNodeWithTag("crumb_1").performClick()
        assertEquals(listOf(0, 1), clicked)
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testCurrentItemIsNotClickable() = runComposeUiTest {
        val clicked = mutableListOf<Int>()
        setContent { Breadcrumb(items, onItemClick = { clicked += it }) }

        onNodeWithTag("crumb_current").performClick()
        assertEquals(emptyList<Int>(), clicked)
    }
}
