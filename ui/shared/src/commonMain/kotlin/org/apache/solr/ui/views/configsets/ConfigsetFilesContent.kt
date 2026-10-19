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

package org.apache.solr.ui.views.configsets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.apache.solr.ui.components.configsets.viewmodel.ConfigsetsViewModel
import org.apache.solr.ui.components.files.viewmodel.ConfigsetFilesViewModel
import org.apache.solr.ui.domain.FileTreeFile
import org.apache.solr.ui.shared.generated.resources.Res
import org.apache.solr.ui.shared.generated.resources.configset_files_error
import org.apache.solr.ui.shared.generated.resources.configset_files_no_collection
import org.apache.solr.ui.shared.generated.resources.configset_files_select_configset
import org.apache.solr.ui.shared.generated.resources.configset_files_select_file
import org.apache.solr.ui.views.files.FileContentViewer
import org.apache.solr.ui.views.files.FileTree
import org.jetbrains.compose.resources.stringResource

/**
 * The Configsets Files tab: the files of the selected configset as a tree, and the content of the selected file.
 *
 * @param viewModel Holds the file tree and the selected file's content.
 * @param configsetsViewModel The shared configsets state. Its selected configset drives the tree.
 * @param modifier Modifier to apply to the root column.
 */
@Composable
fun ConfigsetFilesContent(
    viewModel: ConfigsetFilesViewModel,
    configsetsViewModel: ConfigsetsViewModel,
    modifier: Modifier = Modifier,
) {
    val stateHolder = viewModel.stateHolder
    val state by stateHolder.uiState.collectAsState()
    val configsetsState by configsetsViewModel.uiState.collectAsState()
    val selected = configsetsState.selectedConfigset

    LaunchedEffect(selected) {
        if (selected != null) stateHolder.selectConfigset(selected)
    }

    Column(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        ConfigsetsDropdown(viewModel = configsetsViewModel)
        when {
            selected == null -> Text(
                text = stringResource(Res.string.configset_files_select_configset),
                style = MaterialTheme.typography.bodyMedium,
            )
            state.noCollection -> Text(
                text = stringResource(Res.string.configset_files_no_collection),
                style = MaterialTheme.typography.bodyMedium,
            )
            state.error -> Text(
                text = stringResource(Res.string.configset_files_error),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
            )
            else -> Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                FileTree(
                    nodes = state.tree,
                    selectedPath = state.selectedPath,
                    onItemClick = { node -> if (node is FileTreeFile) stateHolder.selectFile(node.path) },
                    onItemDoubleClick = {},
                    modifier = Modifier.weight(0.4f),
                )
                Spacer(Modifier.height(0.dp))
                Box(modifier = Modifier.weight(0.6f).fillMaxSize()) {
                    val content = state.content
                    if (content != null) {
                        FileContentViewer(content = content)
                    } else {
                        Text(
                            text = stringResource(Res.string.configset_files_select_file),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
        }
    }
}
