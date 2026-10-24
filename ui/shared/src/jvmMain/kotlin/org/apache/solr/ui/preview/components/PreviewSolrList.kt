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

package org.apache.solr.ui.preview.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.apache.solr.ui.preview.PreviewContainer
import org.apache.solr.ui.views.components.SolrList

@Preview
@Composable
private fun PreviewSolrList() = PreviewContainer {
    var filter by remember { mutableStateOf("") }
    val rows = listOf(
        "2026-10-24 04:20:01 INFO  (main) [   ] o.a.s.core.SolrCore Opened new searcher",
        "2026-10-24 04:20:02 WARN  (qtp1) [c:logs] o.a.s.handler.RequestHandlerBase slow query",
        "2026-10-24 04:20:03 ERROR (qtp2) [c:logs] o.a.s.servlet.HttpSolrCall failed request",
    )
    SolrList(
        items = rows.filter { it.contains(filter, ignoreCase = true) },
        filter = filter,
        onFilterChange = { filter = it },
        modifier = Modifier.fillMaxSize(),
        filterPlaceholder = { Text("Filter logs") },
        itemContent = { Text(it) },
    )
}
