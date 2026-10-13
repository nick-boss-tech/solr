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
package org.apache.solr.core;

import static org.apache.solr.SolrTestCaseJ4.configset;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.stream.Stream;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import org.apache.solr.SolrTestCase;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.request.SolrQuery;
import org.apache.solr.client.solrj.request.schema.FieldTypeDefinition;
import org.apache.solr.client.solrj.request.schema.SchemaRequest;
import org.junit.Test;

/**
 * With shareSchema enabled, a cached schema keeps the resource loader of the core that built it, so
 * other cores must still load classes through it after that core is unloaded or reloaded.
 */
public class CoreUnloadSharedSchemaTest extends SolrTestCase {

  private static final String CONFIG_SET = "S";
  private static final String FIELD_TYPE_PACKAGE = "org.apache.solr.core.sharedschema";
  private static final String FIELD_TYPE_CLASS = FIELD_TYPE_PACKAGE + ".SharedSchemaFieldType";

  @Test
  public void testUnloadOriginKeepsSharedSchemaUsable() throws Exception {
    CoreContainer cc = createContainer(true);
    try {
      SolrCore oldCore = createCore(cc, "A");
      SolrResourceLoader oldLoader = oldCore.getResourceLoader();
      SolrCore coreB = createCore(cc, "B");
      assertSame(oldCore.getLatestSchema(), coreB.getLatestSchema());
      assertSame(oldLoader, coreB.getLatestSchema().getResourceLoader());

      cc.unload("A");
      queryAndAddFieldType(cc, "B");
      assertFalse(oldLoader.isClosed());
    } finally {
      cc.shutdown();
    }
  }

  @Test
  public void testReloadOriginKeepsSharedSchemaUsable() throws Exception {
    CoreContainer cc = createContainer(true);
    try {
      SolrCore oldCore = createCore(cc, "A");
      SolrResourceLoader oldLoader = oldCore.getResourceLoader();
      SolrCore coreB = createCore(cc, "B");
      assertSame(oldCore.getLatestSchema(), coreB.getLatestSchema());

      cc.reload("A");
      queryAndAddFieldType(cc, "B");
      assertFalse(oldLoader.isClosed());
    } finally {
      cc.shutdown();
    }
  }

  @Test
  public void testShareSchemaOffClosesOriginLoader() throws Exception {
    CoreContainer cc = createContainer(false);
    try {
      SolrCore oldCore = createCore(cc, "A");
      SolrResourceLoader oldLoader = oldCore.getResourceLoader();
      SolrCore coreB = createCore(cc, "B");
      assertNotSame(oldCore.getLatestSchema(), coreB.getLatestSchema());

      cc.unload("A");
      assertTrue(oldLoader.isClosed());
      queryAndAddFieldType(cc, "B");
    } finally {
      cc.shutdown();
    }
  }

  private CoreContainer createContainer(boolean shareSchema) throws IOException {
    Path solrHome = createTempDir("solrHome");
    Path configSetBase = createTempDir("configsets");
    Path configSetDir = configSetBase.resolve(CONFIG_SET);
    copyDirectory(configset("_default"), configSetDir.resolve("conf"));
    // a configSet core's loader is rooted at the configSet directory, not at the core's instance
    // directory, so this is the lib directory every core on the configSet loads
    writeLibJar(configSetDir.resolve("lib"));

    String xml =
        "<solr><str name=\"configSetBaseDir\">"
            + configSetBase.toAbsolutePath()
            + "</str><str name=\"shareSchema\">"
            + shareSchema
            + "</str></solr>";
    CoreContainer cc = new CoreContainer(SolrXmlConfig.fromString(solrHome, xml));
    cc.load();
    return cc;
  }

  private static SolrCore createCore(CoreContainer cc, String coreName) {
    return cc.create(coreName, Map.of("configSet", CONFIG_SET));
  }

  private static void queryAndAddFieldType(CoreContainer cc, String coreName) throws Exception {
    try (EmbeddedSolrServer server = new EmbeddedSolrServer(cc, coreName)) {
      assertEquals(0, server.query(coreName, new SolrQuery("*:*")).getStatus());

      FieldTypeDefinition ftd = new FieldTypeDefinition();
      ftd.setAttributes(Map.of("name", "sharedSchemaTest", "class", FIELD_TYPE_CLASS));
      assertEquals(0, new SchemaRequest.AddFieldType(ftd).process(server, coreName).getStatus());
    }
  }

  private static void copyDirectory(Path source, Path target) throws IOException {
    List<Path> paths;
    try (Stream<Path> walk = Files.walk(source)) {
      paths = walk.toList();
    }
    for (Path path : paths) {
      Path dest = target.resolve(source.relativize(path).toString());
      if (Files.isDirectory(path)) {
        Files.createDirectories(dest);
      } else {
        Files.copy(path, dest);
      }
    }
  }

  private void writeLibJar(Path libDir) throws IOException {
    Path sourceDir = createTempDir("fieldTypeSource");
    Path classesDir = createTempDir("fieldTypeClasses");
    Path sourceFile = sourceDir.resolve("SharedSchemaFieldType.java");
    Files.writeString(
        sourceFile,
        "package "
            + FIELD_TYPE_PACKAGE
            + ";\n"
            + "public class SharedSchemaFieldType extends org.apache.solr.schema.StrField {}\n",
        StandardCharsets.UTF_8);

    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    assertNotNull("No system Java compiler available", compiler);
    int result =
        compiler.run(
            null,
            null,
            null,
            "-proc:none",
            "-classpath",
            System.getProperty("java.class.path"),
            "-d",
            classesDir.toString(),
            sourceFile.toString());
    assertEquals("javac exit code", 0, result);

    List<Path> classFiles;
    try (Stream<Path> walk = Files.walk(classesDir)) {
      classFiles = walk.filter(p -> p.toString().endsWith(".class")).toList();
    }
    assertFalse("no compiled classes", classFiles.isEmpty());

    Files.createDirectories(libDir);
    Path jarFile = libDir.resolve("shared-schema-fieldtype.jar");
    String separator = classesDir.getFileSystem().getSeparator();
    try (JarOutputStream jar = new JarOutputStream(Files.newOutputStream(jarFile))) {
      for (Path classFile : classFiles) {
        String entryName = classesDir.relativize(classFile).toString().replace(separator, "/");
        jar.putNextEntry(new JarEntry(entryName));
        Files.copy(classFile, jar);
        jar.closeEntry();
      }
    }
  }
}
