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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import org.apache.solr.ui.shared.generated.resources.Res
import org.apache.solr.ui.shared.generated.resources.search_form_add_input
import org.apache.solr.ui.shared.generated.resources.search_form_clear_all
import org.apache.solr.ui.shared.generated.resources.search_form_execute
import org.apache.solr.ui.shared.generated.resources.search_form_execute_debug
import org.apache.solr.ui.shared.generated.resources.search_form_filter_query
import org.apache.solr.ui.shared.generated.resources.search_form_fields
import org.apache.solr.ui.shared.generated.resources.search_form_query
import org.apache.solr.ui.shared.generated.resources.search_form_remove_input
import org.apache.solr.ui.shared.generated.resources.search_form_rows
import org.apache.solr.ui.shared.generated.resources.search_form_sort
import org.apache.solr.ui.shared.generated.resources.search_form_start
import org.jetbrains.compose.resources.stringResource

/**
 * The input of a request to solr.SearchHandler. Array parameters always hold at least one entry.
 *
 * @property query The q parameter values. Required; at least one value must be non-blank to execute.
 * @property filterQueries The fq parameter values.
 * @property fields The fl parameter values.
 * @property sort The sort parameter.
 * @property rows The rows parameter.
 * @property start The start parameter.
 */
data class SearchRequestForm(
    val query: List<String> = listOf(""),
    val filterQueries: List<String> = listOf(""),
    val fields: List<String> = listOf(""),
    val sort: String = "",
    val rows: String = "10",
    val start: String = "0",
)

/**
 * A form for a request to solr.SearchHandler. The form only holds input; the caller decides what
 * executing a request does.
 *
 * @param form The current input.
 * @param onFormChange Called with the new input after every edit, including Clear all.
 * @param onExecute Called with the input and whether the request runs in debug mode.
 * @param modifier Modifier to apply to the root column.
 */
@Composable
fun SearchHandlerForm(
    form: SearchRequestForm,
    onFormChange: (SearchRequestForm) -> Unit,
    onExecute: (SearchRequestForm, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val canExecute = form.query.any { it.isNotBlank() }
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        ArrayInputs(
            label = stringResource(Res.string.search_form_query),
            tag = "q",
            values = form.query,
            onValuesChange = { onFormChange(form.copy(query = it)) },
        )
        ArrayInputs(
            label = stringResource(Res.string.search_form_filter_query),
            tag = "fq",
            values = form.filterQueries,
            onValuesChange = { onFormChange(form.copy(filterQueries = it)) },
        )
        ArrayInputs(
            label = stringResource(Res.string.search_form_fields),
            tag = "fl",
            values = form.fields,
            onValuesChange = { onFormChange(form.copy(fields = it)) },
        )
        SingleInput(
            label = stringResource(Res.string.search_form_sort),
            tag = "sort",
            value = form.sort,
            onValueChange = { onFormChange(form.copy(sort = it)) },
        )
        SingleInput(
            label = stringResource(Res.string.search_form_rows),
            tag = "rows",
            value = form.rows,
            onValueChange = { onFormChange(form.copy(rows = it)) },
        )
        SingleInput(
            label = stringResource(Res.string.search_form_start),
            tag = "start",
            value = form.start,
            onValueChange = { onFormChange(form.copy(start = it)) },
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Button(
                onClick = { onExecute(form, false) },
                enabled = canExecute,
                modifier = Modifier.testTag("execute_normal"),
            ) {
                Text(stringResource(Res.string.search_form_execute))
            }
            OutlinedButton(
                onClick = { onExecute(form, true) },
                enabled = canExecute,
                modifier = Modifier.testTag("execute_debug"),
            ) {
                Text(stringResource(Res.string.search_form_execute_debug))
            }
            TextButton(
                onClick = { onFormChange(SearchRequestForm()) },
                modifier = Modifier.testTag("clear_all"),
            ) {
                Text(stringResource(Res.string.search_form_clear_all))
            }
        }
    }
}

@Composable
private fun ArrayInputs(
    label: String,
    tag: String,
    values: List<String>,
    onValuesChange: (List<String>) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        values.forEachIndexed { index, value ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = value,
                    onValueChange = { changed ->
                        onValuesChange(values.toMutableList().also { it[index] = changed })
                    },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("${tag}_input_$index"),
                )
                if (values.size > 1) {
                    TextButton(
                        onClick = { onValuesChange(values.toMutableList().also { it.removeAt(index) }) },
                        modifier = Modifier.testTag("${tag}_remove_$index"),
                    ) {
                        Text(stringResource(Res.string.search_form_remove_input))
                    }
                }
            }
        }
        TextButton(
            onClick = { onValuesChange(values + "") },
            modifier = Modifier.testTag("${tag}_add"),
        ) {
            Text(stringResource(Res.string.search_form_add_input))
        }
    }
}

@Composable
private fun SingleInput(
    label: String,
    tag: String,
    value: String,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .testTag("${tag}_input"),
    )
}
