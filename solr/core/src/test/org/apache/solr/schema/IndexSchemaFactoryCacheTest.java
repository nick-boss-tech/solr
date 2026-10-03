/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.solr.schema;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.solr.SolrTestCase;
import org.apache.solr.cloud.ZkSolrResourceLoader;
import org.apache.solr.common.util.ObjectCache;
import org.apache.solr.common.util.Pair;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

public class IndexSchemaFactoryCacheTest extends SolrTestCase {

  private static Stat stat(int version, long mzxid) {
    Stat stat = new Stat();
    stat.setVersion(version);
    stat.setMzxid(mzxid);
    return stat;
  }

  @Test
  public void testCachedConfigIsReloadedWhenZnodeIsRecreated() {
    String path = "/configs/conf/schema.xml";
    AtomicReference<Stat> znode = new AtomicReference<>(stat(0, 10));
    ZkSolrResourceLoader loader = mock(ZkSolrResourceLoader.class);
    when(loader.getZkResourceInfo("schema.xml"))
        .thenAnswer(invocation -> new Pair<>(path, znode.get()));
    ObjectCache objectCache = new ObjectCache();
    AtomicInteger loads = new AtomicInteger();

    Runnable fetch =
        () ->
            IndexSchemaFactory.getFromCache(
                "schema.xml",
                loader,
                () -> objectCache,
                () -> {
                  loads.incrementAndGet();
                  Stat current = znode.get();
                  return new IndexSchemaFactory.VersionedConfig(
                      current.getVersion(), current.getMzxid(), null);
                });

    fetch.run();
    assertEquals(1, loads.get());

    fetch.run();
    assertEquals("unchanged znode is served from the cache", 1, loads.get());

    znode.set(stat(0, 20));
    fetch.run();
    assertEquals("deleted and created again with the same data version", 2, loads.get());

    znode.set(stat(1, 25));
    fetch.run();
    assertEquals("data changed in place", 3, loads.get());
  }
}
