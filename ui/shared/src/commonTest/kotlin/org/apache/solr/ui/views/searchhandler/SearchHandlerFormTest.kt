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
 */package org.apache.solr.ui.views.searchhandler

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.v2.runComposeUiTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SearchHandlerFormTest {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testShowsAbbreviationsWithWrittenOutForm() = runComposeUiTest {
        setContent {
            SearchHandlerForm(SearchRequestForm(), onFormChange = {}, onExecute = { _, _ -> })
        }

        onNodeWithText("q (Query)").assertExists()
        onNodeWithText("fq (Filter Query)").assertExists()
        onNodeWithText("sort (Sort)").assertExists()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testArrayFieldStartsWithOneInputAndAddsMore() = runComposeUiTest {
        setContent {
            var form by remember { mutableStateOf(SearchRequestForm()) }
            SearchHandlerForm(form, onFormChange = { form = it }, onExecute = { _, _ -> })
        }

        onNodeWithTag("q_input_0").assertExists()
        onNodeWithTag("q_input_1").assertDoesNotExist()

        onNodeWithTag("q_add").performClick()
        onNodeWithTag("q_input_1").assertExists()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testOnlyAdditionalInputsCanBeRemoved() = runComposeUiTest {
        setContent {
            var form by remember { mutableStateOf(SearchRequestForm()) }
            SearchHandlerForm(form, onFormChange = { form = it }, onExecute = { _, _ -> })
        }

        onNodeWithTag("q_remove_0").assertDoesNotExist()

        onNodeWithTag("q_add").performClick()
        onNodeWithTag("q_remove_1").performClick()
        onNodeWithTag("q_input_1").assertDoesNotExist()
        onNodeWithTag("q_input_0").assertExists()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testExecuteNeedsANonBlankQuery() = runComposeUiTest {
        setContent {
            var form by remember { mutableStateOf(SearchRequestForm()) }
            SearchHandlerForm(form, onFormChange = { form = it }, onExecute = { _, _ -> })
        }

        onNodeWithTag("execute_normal").assertIsNotEnabled()

        onNodeWithTag("q_input_0").performTextReplacement("*:*")
        onNodeWithTag("execute_normal").assertIsEnabled()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testExecuteReportsDebugMode() = runComposeUiTest {
        val executed = mutableListOf<Pair<String, Boolean>>()
        setContent {
            var form by remember { mutableStateOf(SearchRequestForm()) }
            SearchHandlerForm(
                form,
                onFormChange = { form = it },
                onExecute = { f, debug -> executed += f.query.first() to debug },
            )
        }

        onNodeWithTag("q_input_0").performTextReplacement("*:*")
        onNodeWithTag("execute_normal").performClick()
        onNodeWithTag("execute_debug").performClick()
        assertEquals(listOf("*:*" to false, "*:*" to true), executed)
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testClearAllResetsToDefaults() = runComposeUiTest {
        var resulting: SearchRequestForm? = null
        setContent {
            var form by remember { mutableStateOf(SearchRequestForm()) }
            resulting = form
            SearchHandlerForm(form, onFormChange = { form = it }, onExecute = { _, _ -> })
        }

        onNodeWithTag("q_input_0").performTextReplacement("*:*")
        onNodeWithTag("sort_input").performTextReplacement("score desc")
        onNodeWithTag("clear_all").performScrollTo().performClick()
        waitForIdle()
        assertEquals(SearchRequestForm(), resulting)
    }
}
