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
package org.apache.solr.cloud.api.collections;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.solr.client.solrj.response.RequestStatusState;
import org.apache.solr.cloud.DistributedMultiLock;
import org.apache.solr.cloud.SolrCloudTestCase;
import org.apache.solr.cloud.ZkDistributedCollectionLockFactory;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.cloud.ZkNodeProps;
import org.apache.solr.common.params.CollectionParams.CollectionAction;
import org.junit.BeforeClass;
import org.junit.Test;

/** Exercises bounded command admission while real distributed locks keep the workers occupied. */
public class DistributedCommandAdmissionTest extends SolrCloudTestCase {
  private static final long COMMAND_TIMEOUT_MS = TimeUnit.SECONDS.toMillis(30);

  @BeforeClass
  public static void setupCluster() throws Exception {
    configureCluster(1).withOverseer(false).configure();
  }

  @Test
  public void testRejectedAsyncIdCanBeRetried() throws Exception {
    DistributedCollectionConfigSetCommandRunner runner =
        cluster
            .getJettySolrRunner(0)
            .getCoreContainer()
            .getZkController()
            .getDistributedCommandRunner()
            .orElseThrow();
    DistributedMultiLock blocker =
        new CollectionApiLockFactory(
                new ZkDistributedCollectionLockFactory(
                    cluster.getZkClient(),
                    DistributedCollectionConfigSetCommandRunner.ZK_COLLECTION_LOCKS))
            .createCollectionApiLock(
                new AdminCmdContext(CollectionAction.CREATE), "busy", null, null);
    List<String> acceptedIds = new ArrayList<>();
    String rejectedId = null;
    try {
      assertTrue("External collection write lock must be held", blocker.isAcquired());
      // The current executor admits at most 20 commands. Use a bounded probe so the test
      // checks rejection without assuming that exact capacity or relying on task durations.
      for (int i = 0; i < 64; i++) {
        String asyncId = "admission-" + i;
        try {
          runner.runCollectionCommand(
              new AdminCmdContext(CollectionAction.MOCK_COLL_TASK, asyncId),
              new ZkNodeProps("collection", "busy"),
              COMMAND_TIMEOUT_MS);
          acceptedIds.add(asyncId);
        } catch (SolrException e) {
          assertEquals(SolrException.ErrorCode.SERVICE_UNAVAILABLE.code, e.code());
          rejectedId = asyncId;
          break;
        }
      }
      assertFalse("At least one command should be accepted", acceptedIds.isEmpty());
      assertNotNull("Executor must reject excess work while all workers wait on locks", rejectedId);
      assertFalse(runner.getTrackedAsyncIds().contains(rejectedId));
      assertEquals(
          RequestStatusState.NOT_FOUND, runner.getAsyncTaskRequestStatus(rejectedId).first());

      String acceptedId = acceptedIds.getFirst();
      SolrException duplicate =
          expectThrows(
              SolrException.class,
              () ->
                  runner.runCollectionCommand(
                      new AdminCmdContext(CollectionAction.MOCK_COLL_TASK, acceptedId),
                      new ZkNodeProps("collection", "busy"),
                      COMMAND_TIMEOUT_MS));
      assertEquals(SolrException.ErrorCode.BAD_REQUEST.code, duplicate.code());
    } finally {
      blocker.release();
    }

    for (String asyncId : acceptedIds) {
      assertCompleted(runner, asyncId);
    }
    runner.runCollectionCommand(
        new AdminCmdContext(CollectionAction.MOCK_COLL_TASK, rejectedId),
        new ZkNodeProps("collection", "busy"),
        COMMAND_TIMEOUT_MS);
    assertCompleted(runner, rejectedId);
  }

  private void assertCompleted(DistributedCollectionConfigSetCommandRunner runner, String asyncId)
      throws Exception {
    assertBusy(
        () -> {
          var status = runner.getAsyncTaskRequestStatus(asyncId);
          assertEquals(RequestStatusState.COMPLETED, status.first());
          assertNotNull(status.second().getResponse().get("MOCK_FINISHED"));
        },
        COMMAND_TIMEOUT_MS,
        TimeUnit.MILLISECONDS);
  }
}
