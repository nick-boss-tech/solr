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
package org.apache.solr.client.solrj.request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.solr.SolrTestCase;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.SolrRequest.SolrRequestType;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.junit.Test;

/** SOLR-18341: each SolrRequest answers whether a failed attempt may be replayed. */
public class SolrRequestRetriableTest extends SolrTestCase {

  @Test
  public void queryIsRetriable() {
    assertTrue(new QueryRequest(new ModifiableSolrParams()).isRetriable());
  }

  @Test
  public void nonQueryTypesAreNotRetriable() {
    assertFalse(CollectionAdminRequest.createCollection("c", "conf", 1, 1).isRetriable());
    assertFalse(new GenericSolrRequest(METHOD.GET, "/foo").isRetriable());
    assertFalse(
        new GenericSolrRequest(METHOD.GET, "/admin/info/system", SolrRequestType.ADMIN)
            .isRetriable());
    assertFalse(
        new GenericSolrRequest(METHOD.GET, "/security", SolrRequestType.SECURITY).isRetriable());
    assertFalse(
        new GenericSolrRequest(METHOD.GET, "/stream", SolrRequestType.STREAMING).isRetriable());
  }

  @Test
  public void opaqueUpdateStreamsAreNotRetriable() {
    ContentWriterUpdateRequest contentWriterRequest = new ContentWriterUpdateRequest("/update");
    contentWriterRequest.addContentWithType("{}", "application/json");
    assertFalse(contentWriterRequest.isRetriable());
    assertFalse(new StreamingUpdateRequest("/update", "{}", "application/json").isRetriable());
  }

  @Test
  public void fullAddDeleteAndCommitAreRetriable() {
    UpdateRequest add = new UpdateRequest();
    add.add(new SolrInputDocument("id", "1", "title", "x"));
    assertTrue(add.isRetriable());

    UpdateRequest deleteId = new UpdateRequest();
    deleteId.deleteById("1");
    assertTrue(deleteId.isRetriable());

    UpdateRequest deleteQuery = new UpdateRequest();
    deleteQuery.deleteByQuery("id:1");
    assertTrue(deleteQuery.isRetriable());

    UpdateRequest commit = new UpdateRequest();
    commit.setAction(AbstractUpdateRequest.ACTION.COMMIT, false, false);
    assertTrue(commit.isRetriable());
  }

  @Test
  public void atomicSetRemoveAndAddDistinctAreRetriable() {
    assertTrue(atomic("set", "v").isRetriable());
    assertTrue(atomic("set", null).isRetriable());
    assertTrue(atomic("remove", "v").isRetriable());
    assertTrue(atomic("removeregex", "v.*").isRetriable());
    assertTrue(atomic("add-distinct", "v").isRetriable());
  }

  @Test
  public void atomicIncAndScalarAddAreNotRetriable() {
    assertFalse(atomic("inc", 1).isRetriable());
    assertFalse(atomic("add", "v").isRetriable());
    assertFalse(atomic("add", List.of("a", "b")).isRetriable());
  }

  @Test
  public void atomicAddOfChildDocumentIsRetriable() {
    SolrInputDocument child = new SolrInputDocument("id", "child-1");
    SolrInputDocument parent = new SolrInputDocument("id", "p1");
    parent.setField("children", Map.of("add", child));
    assertTrue(new UpdateRequest().add(parent).isRetriable());
  }

  @Test
  public void incBuriedInChildAddIsNotRetriable() {
    SolrInputDocument child = new SolrInputDocument("id", "child-1");
    child.setField("count", Map.of("inc", 1));
    SolrInputDocument parent = new SolrInputDocument("id", "p1");
    parent.setField("children", Map.of("add", child));
    assertFalse(new UpdateRequest().add(parent).isRetriable());
  }

  @Test
  public void incInAnonymousChildListIsNotRetriable() {
    SolrInputDocument child = new SolrInputDocument("id", "child-1");
    child.setField("count", Map.of("inc", 1));
    SolrInputDocument parent = new SolrInputDocument("id", "p1");
    parent.addChildDocument(child);
    assertFalse(new UpdateRequest().add(parent).isRetriable());
  }

  @Test
  public void mixedIncAndFullAddIsNotRetriable() {
    UpdateRequest req = new UpdateRequest();
    req.add(new SolrInputDocument("id", "1"));
    req.add(atomicDoc("inc", 1));
    assertFalse(req.isRetriable());
  }

  @Test
  public void docIteratorIsNotRetriable() {
    UpdateRequest req = new UpdateRequest();
    req.setDocIterator(List.of(new SolrInputDocument("id", "1")).iterator());
    assertFalse(req.isRetriable());
  }

  @Test
  public void optimisticVersionMakesIncRetriable() {
    SolrInputDocument doc = atomicDoc("inc", 1);
    doc.setField(CommonParams.VERSION_FIELD, 7L);
    assertTrue(new UpdateRequest().add(doc).isRetriable());

    SolrInputDocument mustNotExist = atomicDoc("inc", 1);
    mustNotExist.setField(CommonParams.VERSION_FIELD, -1L);
    assertFalse(new UpdateRequest().add(mustNotExist).isRetriable());
  }

  @Test
  public void genericQueryTypeIsRetriable() {
    SolrRequest<?> req = new GenericSolrRequest(METHOD.GET, "/select", SolrRequestType.QUERY);
    assertTrue(req.isRetriable());
  }

  @Test
  public void overriddenRequestTypeControlsRetriability() {
    SolrRequest<?> req =
        new GenericSolrRequest(METHOD.GET, "/select") {
          @Override
          public SolrRequestType getRequestType() {
            return SolrRequestType.QUERY;
          }
        };
    assertTrue(req.isRetriable());
  }

  private static UpdateRequest atomic(String op, Object value) {
    return new UpdateRequest().add(atomicDoc(op, value));
  }

  private static SolrInputDocument atomicDoc(String op, Object value) {
    SolrInputDocument doc = new SolrInputDocument("id", "1");
    Map<String, Object> cmd = new HashMap<>();
    cmd.put(op, value);
    doc.setField("f", cmd);
    return doc;
  }
}
