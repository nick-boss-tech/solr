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
 */package org.apache.solr.ui.components.files.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.isSuccess
import kotlinx.serialization.Serializable
import org.apache.solr.ui.domain.FileTreeFile
import org.apache.solr.ui.domain.FileTreeFolder
import org.apache.solr.ui.domain.FileTreeNode

class HttpConfigsetFilesRepository(private val httpClient: HttpClient) : ConfigsetFilesRepository {
    override suspend fun findCollection(configset: String): Result<String?> {
        val response = httpClient.get("solr/admin/collections") {
            parameter("action", "CLUSTERSTATUS")
            parameter("wt", "json")
        }
        return when {
            response.status.isSuccess() -> {
                val collections = response.body<ClusterStatusResponse>().cluster.collections
                Result.success(collections.filterValues { it.configName == configset }.keys.minOrNull())
            }
            else -> Result.failure(Exception("Unknown Error"))
        }
    }

    override suspend fun listFiles(collection: String): Result<List<FileTreeNode>> = runCatching {
        listDirectory(collection, directory = null)
    }

    override suspend fun readFile(collection: String, path: String): Result<String> {
        val response = httpClient.get("solr/$collection/admin/file") {
            parameter("file", path)
        }
        return when {
            response.status.isSuccess() -> Result.success(response.body<String>())
            else -> Result.failure(Exception("Unknown Error"))
        }
    }

    private suspend fun listDirectory(collection: String, directory: String?): List<FileTreeNode> {
        val response = httpClient.get("solr/$collection/admin/file") {
            parameter("wt", "json")
            directory?.let { parameter("file", it) }
        }
        if (!response.status.isSuccess()) throw Exception("Unknown Error")
        val entries = response.body<AdminFileResponse>().files
        return entries.map { (name, entry) ->
            val path = if (directory == null) name else "$directory/$name"
            if (entry.directory) {
                FileTreeFolder(name = name, path = path, children = listDirectory(collection, path))
            } else {
                FileTreeFile(name = name, path = path)
            }
        }.sortedWith(compareBy<FileTreeNode>({ it !is FileTreeFolder }, { it.name }))
    }
}

@Serializable
private class ClusterStatusResponse(val cluster: ClusterStatus)

@Serializable
private class ClusterStatus(val collections: Map<String, CollectionStatus> = emptyMap())

@Serializable
private class CollectionStatus(val configName: String? = null)

@Serializable
private class AdminFileResponse(val files: Map<String, AdminFileEntry> = emptyMap())

@Serializable
private class AdminFileEntry(val directory: Boolean = false)
