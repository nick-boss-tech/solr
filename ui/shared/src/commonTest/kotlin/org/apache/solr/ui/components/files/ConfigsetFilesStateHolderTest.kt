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
 */package org.apache.solr.ui.components.files

import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.apache.solr.ui.components.files.data.ConfigsetFilesRepository
import org.apache.solr.ui.components.files.viewmodel.ConfigsetFilesStateHolder
import org.apache.solr.ui.domain.FileTreeFile
import org.apache.solr.ui.domain.FileTreeNode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

private class FakeConfigsetFilesRepository(
    var collection: Result<String?> = Result.success(null),
    var tree: Result<List<FileTreeNode>> = Result.success(emptyList()),
    var content: Result<String> = Result.success(""),
) : ConfigsetFilesRepository {
    override suspend fun findCollection(configset: String): Result<String?> = collection

    override suspend fun listFiles(collection: String): Result<List<FileTreeNode>> = tree

    override suspend fun readFile(collection: String, path: String): Result<String> = content
}

class ConfigsetFilesStateHolderTest {

    @Test
    fun testSelectingConfigsetLoadsItsFileTree() = runTest {
        val file = FileTreeFile(name = "solrconfig.xml", path = "solrconfig.xml")
        val repository = FakeConfigsetFilesRepository(
            collection = Result.success("films"),
            tree = Result.success(listOf(file)),
        )
        val holder = ConfigsetFilesStateHolder(repository, this)

        holder.selectConfigset("films_conf")
        advanceUntilIdle()

        assertEquals(listOf<FileTreeNode>(file), holder.uiState.value.tree)
        assertFalse(holder.uiState.value.noCollection)
        assertFalse(holder.uiState.value.loading)
    }

    @Test
    fun testConfigsetWithoutCollectionIsReported() = runTest {
        val holder = ConfigsetFilesStateHolder(FakeConfigsetFilesRepository(collection = Result.success(null)), this)

        holder.selectConfigset("orphan_conf")
        advanceUntilIdle()

        assertTrue(holder.uiState.value.noCollection)
        assertTrue(holder.uiState.value.tree.isEmpty())
    }

    @Test
    fun testSelectingFileLoadsItsContent() = runTest {
        val file = FileTreeFile(name = "solrconfig.xml", path = "solrconfig.xml")
        val repository = FakeConfigsetFilesRepository(
            collection = Result.success("films"),
            tree = Result.success(listOf(file)),
            content = Result.success("<config/>"),
        )
        val holder = ConfigsetFilesStateHolder(repository, this)
        holder.selectConfigset("films_conf")
        advanceUntilIdle()

        holder.selectFile("solrconfig.xml")
        advanceUntilIdle()

        assertEquals("solrconfig.xml", holder.uiState.value.selectedPath)
        assertEquals("<config/>", holder.uiState.value.content)
        assertNull(holder.uiState.value.collection?.takeIf { it.isEmpty() })
    }

    @Test
    fun testFailedTreeLoadSetsError() = runTest {
        val repository = FakeConfigsetFilesRepository(
            collection = Result.success("films"),
            tree = Result.failure(Exception("down")),
        )
        val holder = ConfigsetFilesStateHolder(repository, this)

        holder.selectConfigset("films_conf")
        advanceUntilIdle()

        assertTrue(holder.uiState.value.error)
    }
}
