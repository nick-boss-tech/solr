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
package org.apache.solr.cloud;

import static org.junit.Assume.assumeTrue;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import org.apache.solr.cloud.api.collections.AbstractConfigSetUploadFromRepositoryTest;
import org.apache.solr.cloud.api.collections.AbstractInstallShardTest;
import org.eclipse.jetty.client.ContentResponse;
import org.eclipse.jetty.client.StringRequestContent;
import org.eclipse.jetty.http.HttpMethod;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests uploading configsets from a backup repository with {@link
 * org.apache.solr.core.backup.repository.LocalFileSystemRepository}.
 */
public class LocalFSConfigSetUploadFromRepositoryTest
    extends AbstractConfigSetUploadFromRepositoryTest {

  private static final String REPOSITORY_NAME = "local";

  private static final String SOLR_XML =
      AbstractInstallShardTest.defaultSolrXmlTextWithBackupRepository(
          "<str name=\"allowPaths\">${solr.security.allow.paths:}</str>\n"
              + "<backup>\n"
              + "  <repository name=\""
              + REPOSITORY_NAME
              + "\" class=\"org.apache.solr.core.backup.repository.LocalFileSystemRepository\"/>\n"
              + "</backup>\n");

  private static Path repositoryDir;

  @BeforeClass
  public static void setupClass() throws Exception {
    System.setProperty("solr.security.allow.paths", "*");
    repositoryDir = createTempDir("repository");
    configureCluster(1).withSolrXml(SOLR_XML).configure();
  }

  @Override
  protected String repositoryName() {
    return REPOSITORY_NAME;
  }

  @Override
  protected String baseLocation() {
    return repositoryDir.toString();
  }

  @Override
  protected String locationParam(URI uri) {
    return Path.of(uri).toString();
  }

  @Test
  public void testRawBodiesOfAnyContentTypeAreStillStored() throws Exception {
    // A JSON config file is stored as-is
    final var jsonFile = put("rawjson/files/params.json", "application/json", "{\"a\":1}");
    assertEquals(jsonFile.getContentAsString(), 200, jsonFile.getStatus());
    assertTrue(cluster.getZkClient().exists("/configs/rawjson/params.json"));

    // Clients such as curl --data-binary send this content type by default
    final var form =
        put("rawform/files/solrconfig.xml", "application/x-www-form-urlencoded", "<config/>");
    assertEquals(form.getContentAsString(), 200, form.getStatus());
    assertTrue(cluster.getZkClient().exists("/configs/rawform/solrconfig.xml"));
  }

  @Test
  public void testV1RejectsBodyWhenRepositoryIsProvided() throws Exception {
    final URI dir = newTestDir();
    stageFile(dir, "solrconfig.xml", "<config/>");
    final String name = "v1both-" + UUID.randomUUID();
    final var jetty = cluster.getJettySolrRunners().getFirst();
    final String url =
        jetty.getBaseUrl()
            + "/admin/configs?action=UPLOAD&name="
            + name
            + "&repository="
            + REPOSITORY_NAME
            + "&location="
            + URLEncoder.encode(locationParam(dir), StandardCharsets.UTF_8);

    final var response =
        jetty
            .getSolrClient()
            .getHttpClient()
            .newRequest(url)
            .method(HttpMethod.POST)
            .body(new StringRequestContent("application/json", "{\"a\":1}"))
            .send();

    assertEquals(response.getContentAsString(), 400, response.getStatus());
    assertFalse(cluster.getZkClient().exists("/configs/" + name));
  }

  @Test
  public void testUploadFromRepositoryRejectsSymbolicLinkDirectories() throws Exception {
    assumeTrue(
        "Symbolic links require filesystem support",
        Files.getFileStore(repositoryDir).supportsFileAttributeView("posix"));
    final URI dir = newTestDir();
    stageFile(dir, "solrconfig.xml", "<config/>");
    final Path outsideDir = createTempDir("outside-repository");
    Files.writeString(outsideDir.resolve("secret.txt"), "secret");
    // The link is created through the default filesystem (Path.of), so the
    // target has to be a default-filesystem path for the same directory,
    // not the test-framework temp path.
    Files.createSymbolicLink(Path.of(dir).resolve("linked"), Path.of(outsideDir.toUri()));
    final String name = "symlink-" + UUID.randomUUID();
    final String encodedLocation = URLEncoder.encode(locationParam(dir), StandardCharsets.UTF_8);

    final var response =
        put(
            name + "?repository=" + REPOSITORY_NAME + "&location=" + encodedLocation,
            "application/octet-stream",
            "");

    assertEquals(response.getContentAsString(), 400, response.getStatus());
    assertFalse(cluster.getZkClient().exists("/configs/" + name));
  }

  private ContentResponse put(String path, String contentType, String body) throws Exception {
    final var jetty = cluster.getJettySolrRunners().getFirst();
    return jetty
        .getSolrClient()
        .getHttpClient()
        .newRequest(
            jetty.getBaseUrl().toString().replaceAll("/solr$", "") + "/api/configsets/" + path)
        .method(HttpMethod.PUT)
        .body(new StringRequestContent(contentType, body))
        .send();
  }
}
