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

import java.util.concurrent.TimeUnit;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.common.util.TimeSource;
import org.apache.solr.util.TimeOut;
import org.junit.BeforeClass;
import org.junit.Test;

public class OverseerProcessorExitTest extends SolrCloudTestCase {

  @BeforeClass
  public static void setupCluster() throws Exception {
    configureCluster(2).addConfig("conf", configset("cloud-minimal")).configure();
  }

  @Test
  public void testOverseerRejoinsElectionWhenCollectionProcessorExits() throws Exception {
    Overseer overseer = cluster.getOpenOverseer();
    Overseer.OverseerThread oldProcessor = overseer.getCollectionProcessorThread();
    assertNotNull(oldProcessor);

    // makes the processor's run loop end by itself, as it does when it hits an expired ZK session
    oldProcessor.getThread().close();

    TimeOut timeout = new TimeOut(60, TimeUnit.SECONDS, TimeSource.NANO_TIME);
    Overseer.OverseerThread newProcessor = null;
    while (!timeout.hasTimedOut()) {
      Overseer current = cluster.getOpenOverseer();
      Overseer.OverseerThread candidate = current.getCollectionProcessorThread();
      if (candidate != null && !candidate.equals(oldProcessor) && candidate.isAlive()) {
        newProcessor = candidate;
        break;
      }
      Thread.sleep(250);
    }
    assertNotNull("no new collection processor after the old one exited", newProcessor);

    // the cluster must still process Collection API commands
    CollectionAdminRequest.createCollection("processorExit", "conf", 1, 1)
        .process(cluster.getSolrClient());
    cluster.waitForActiveCollection("processorExit", 1, 1);
  }
}
