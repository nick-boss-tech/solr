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
 */package org.apache.solr.ui.views.files

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp

@Composable
fun FileContentViewer(
    content: String,
    modifier: Modifier = Modifier,
) {
    val lines = remember(content) { content.lines() }
    val numberWidth = lines.size.toString().length
    val textStyle = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace)
    val verticalScroll = rememberScrollState()
    val horizontalScroll = rememberScrollState()
    Box(
        modifier = modifier
            .fillMaxSize()
            .horizontalScroll(horizontalScroll),
    ) {
        Row(modifier = Modifier.verticalScroll(verticalScroll)) {
            DisableSelection {
                Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                    lines.forEachIndexed { index, _ ->
                        Text(
                            text = (index + 1).toString().padStart(numberWidth),
                            style = textStyle,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            softWrap = false,
                        )
                    }
                }
            }
            SelectionContainer {
                Column(modifier = Modifier.padding(end = 8.dp)) {
                    lines.forEach { line ->
                        Text(text = line, style = textStyle, softWrap = false)
                    }
                }
            }
        }
    }
}
