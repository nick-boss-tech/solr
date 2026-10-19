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
 */package org.apache.solr.ui.components.files.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.apache.solr.ui.components.files.data.ConfigsetFilesRepository
import org.apache.solr.ui.domain.FileTreeNode

data class ConfigsetFilesUiState(
    val configset: String? = null,
    val collection: String? = null,
    val loading: Boolean = false,
    val noCollection: Boolean = false,
    val tree: List<FileTreeNode> = emptyList(),
    val selectedPath: String? = null,
    val content: String? = null,
    val error: Boolean = false,
)

class ConfigsetFilesStateHolder(
    private val repository: ConfigsetFilesRepository,
    private val scope: CoroutineScope,
) {
    val uiState: StateFlow<ConfigsetFilesUiState>
        field = MutableStateFlow(ConfigsetFilesUiState())

    fun selectConfigset(configset: String) {
        uiState.update { ConfigsetFilesUiState(configset = configset, loading = true) }
        scope.launch {
            val collection = repository.findCollection(configset).getOrElse {
                failLoad(configset)
                return@launch
            }
            if (uiState.value.configset != configset) return@launch
            if (collection == null) {
                uiState.update { it.copy(loading = false, noCollection = true) }
                return@launch
            }
            repository.listFiles(collection)
                .onSuccess { tree ->
                    uiState.update { state ->
                        if (state.configset == configset) state.copy(collection = collection, loading = false, tree = tree) else state
                    }
                }
                .onFailure { failLoad(configset) }
        }
    }

    fun selectFile(path: String) {
        val collection = uiState.value.collection ?: return
        uiState.update { it.copy(selectedPath = path, content = null, error = false) }
        scope.launch {
            repository.readFile(collection, path)
                .onSuccess { body ->
                    uiState.update { state -> if (state.selectedPath == path) state.copy(content = body) else state }
                }
                .onFailure {
                    uiState.update { state -> if (state.selectedPath == path) state.copy(error = true) else state }
                }
        }
    }

    private fun failLoad(configset: String) {
        uiState.update { state ->
            if (state.configset == configset) state.copy(loading = false, error = true) else state
        }
    }
}
