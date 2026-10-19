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

import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.HttpResponseData
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import org.apache.solr.ui.components.files.data.HttpConfigsetFilesRepository
import org.apache.solr.ui.createMockEngine
import org.apache.solr.ui.domain.FileTreeFile
import org.apache.solr.ui.domain.FileTreeFolder
import org.apache.solr.ui.domain.FileTreeNode
import org.apache.solr.ui.testHttpClient
import kotlin.test.Test
import kotlin.test.assertEquals

private const val CLUSTER_STATUS = """{"cluster":{"collections":{"films":{"configName":"films_conf"},"shop":{"configName":"shop_conf"}}}}"""
private const val ROOT = """{"files":{"README.md":{"size":3},"conf":{"directory":true}}}"""
private const val CONF = """{"files":{"solrconfig.xml":{"size":9},"lang":{"directory":true}}}"""
private const val LANG = """{"files":{"en.txt":{"size":2}}}"""

private fun MockRequestHandleScope.json(body: String): HttpResponseData =
    respond(body, HttpStatusCode.OK, headersOf(HttpHeaders.ContentType, "application/json"))

private fun MockRequestHandleScope.text(body: String): HttpResponseData =
    respond(body, HttpStatusCode.OK, headersOf(HttpHeaders.ContentType, "text/plain"))

class HttpConfigsetFilesRepositoryTest {

    @Test
    fun testFindsTheCollectionUsingTheConfigset() = runTest {
        val engine = createMockEngine({ _ -> json(CLUSTER_STATUS) })
        val repository = HttpConfigsetFilesRepository(testHttpClient(engine))

        assertEquals("films", repository.findCollection("films_conf").getOrNull())
        assertEquals(null, repository.findCollection("unused_conf").getOrNull())
    }

    @Test
    fun testBuildsTheFileTreeFoldersFirst() = runTest {
        val engine = createMockEngine({ request ->
            when (request.url.parameters["file"]) {
                null -> json(ROOT)
                "conf" -> json(CONF)
                "conf/lang" -> json(LANG)
                else -> respond("", HttpStatusCode.NotFound)
            }
        })
        val repository = HttpConfigsetFilesRepository(testHttpClient(engine))

        val expected: List<FileTreeNode> = listOf(
            FileTreeFolder(
                name = "conf",
                path = "conf",
                children = listOf(
                    FileTreeFolder(
                        name = "lang",
                        path = "conf/lang",
                        children = listOf(FileTreeFile(name = "en.txt", path = "conf/lang/en.txt")),
                    ),
                    FileTreeFile(name = "solrconfig.xml", path = "conf/solrconfig.xml"),
                ),
            ),
            FileTreeFile(name = "README.md", path = "README.md"),
        )
        assertEquals(expected, repository.listFiles("films").getOrNull())
    }

    @Test
    fun testReadsTheContentOfAFile() = runTest {
        val engine = createMockEngine({ request ->
            if (request.url.parameters["file"] == "conf/solrconfig.xml") text("<config/>") else respond("", HttpStatusCode.NotFound)
        })
        val repository = HttpConfigsetFilesRepository(testHttpClient(engine))

        assertEquals("<config/>", repository.readFile("films", "conf/solrconfig.xml").getOrNull())
    }
}
