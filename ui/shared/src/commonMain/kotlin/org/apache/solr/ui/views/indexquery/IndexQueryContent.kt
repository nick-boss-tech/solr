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

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import org.apache.solr.ui.components.indexquery.viewmodel.IndexQueryViewModel
import org.apache.solr.ui.shared.generated.resources.Res
import org.apache.solr.ui.shared.generated.resources.index_query_error
import org.apache.solr.ui.views.searchhandler.SearchHandlerForm
import org.apache.solr.ui.views.searchhandler.SearchRequestForm
import org.jetbrains.compose.resources.stringResource

/**
 * The Index & Query screen: the collection and handler header, the request form, and the response.
 *
 * @param viewModel Holds the collections, handlers, and response.
 * @param modifier Modifier to apply to the root column.
 */
@Composable
fun IndexQueryContent(
    viewModel: IndexQueryViewModel,
    modifier: Modifier = Modifier,
) {
    val stateHolder = viewModel.stateHolder
    val state by stateHolder.uiState.collectAsState()
    var form by remember { mutableStateOf(SearchRequestForm()) }

    Column(modifier = modifier.fillMaxWidth()) {
        IndexQueryHeader(
            collections = state.collections,
            collectionsLoading = state.collectionsLoading,
            selectedCollection = state.selectedCollection,
            onSelectCollection = stateHolder::selectCollection,
            handlers = state.handlers,
            handlersLoading = state.handlersLoading,
            selectedHandler = state.selectedHandler,
            onSelectHandler = stateHolder::selectHandler,
        )
        if (state.selectedHandler != null) {
            SearchHandlerForm(
                form = form,
                onFormChange = { form = it },
                onExecute = { current, debug -> stateHolder.execute(current.toParameters(debug)) },
                modifier = Modifier.padding(top = 16.dp),
            )
        }
        if (state.error) {
            Text(
                text = stringResource(Res.string.index_query_error),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 16.dp),
            )
        }
        state.response?.let { response ->
            SelectionContainer(modifier = Modifier.padding(top = 16.dp).horizontalScroll(rememberScrollState())) {
                Text(
                    text = response,
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

/**
 * Turns the form into request parameters. Blank values are left out, and the debug flag adds debugQuery.
 *
 * @param debug Whether the request runs in debug mode.
 */
fun SearchRequestForm.toParameters(debug: Boolean): List<Pair<String, String>> = buildList {
    query.filter { it.isNotBlank() }.forEach { add("q" to it) }
    filterQueries.filter { it.isNotBlank() }.forEach { add("fq" to it) }
    fields.filter { it.isNotBlank() }.forEach { add("fl" to it) }
    if (sort.isNotBlank()) add("sort" to sort)
    add("rows" to rows)
    add("start" to start)
    add("wt" to "json")
    if (debug) add("debugQuery" to "true")
}
