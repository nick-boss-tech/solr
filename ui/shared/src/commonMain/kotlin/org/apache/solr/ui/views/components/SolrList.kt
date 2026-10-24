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

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import org.apache.solr.ui.shared.generated.resources.Res
import org.apache.solr.ui.shared.generated.resources.cd_clear_field
import org.apache.solr.ui.shared.generated.resources.close
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * A filterable list with optional actions left and right of the filter input.
 *
 * Each item is rendered by [itemContent] and scrolls horizontally on its own when it is wider
 * than the list. The list scrolls vertically when the items exceed the available height.
 *
 * @param items The rows to display.
 * @param filter The current text of the filter input.
 * @param onFilterChange Called with the new filter text, and with an empty string when the
 * clear button is pressed.
 * @param modifier Modifier applied to the root column.
 * @param filterPlaceholder Placeholder shown in the filter input while it is empty.
 * @param leadingActions Actions displayed left of the filter input. Nothing is shown by default.
 * @param trailingActions Actions displayed right of the filter input. Nothing is shown by default.
 * @param itemContent Renders a single item.
 */
@Composable
fun <T> SolrList(
    items: List<T>,
    filter: String,
    onFilterChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    filterPlaceholder: (@Composable () -> Unit)? = null,
    leadingActions: @Composable RowScope.() -> Unit = {},
    trailingActions: @Composable RowScope.() -> Unit = {},
    itemContent: @Composable (T) -> Unit,
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            leadingActions()
            SolrOutlinedTextField(
                value = filter,
                onValueChange = onFilterChange,
                modifier = Modifier.weight(1f),
                singleLine = true,
                placeholder = filterPlaceholder,
                trailingIcon = if (filter.isEmpty()) {
                    null
                } else {
                    { ClearFilterButton(onClick = { onFilterChange("") }) }
                },
            )
            trailingActions()
        }
        LazyColumn(modifier = Modifier.weight(1f).fillMaxWidth()) {
            items(items) { item ->
                Box(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                    itemContent(item)
                }
            }
        }
    }
}

@Composable
private fun ClearFilterButton(onClick: () -> Unit) {
    IconButton(
        modifier = Modifier.testTag("solr_list_clear_button"),
        onClick = onClick,
    ) {
        Icon(
            painter = painterResource(Res.drawable.close),
            contentDescription = stringResource(Res.string.cd_clear_field),
        )
    }
}
