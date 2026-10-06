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

import static org.apache.solr.common.params.CommonParams.ID;

import java.lang.invoke.MethodHandles;
import java.util.Map;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrException.ErrorCode;
import org.apache.solr.common.cloud.SolrZkClient;
import org.apache.solr.common.cloud.ZkMaintenanceUtils;
import org.apache.solr.common.cloud.ZkNodeProps;
import org.apache.solr.common.util.Utils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class OverseerElectionContext extends ElectionContext {
  private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private final SolrZkClient zkClient;
  private final Overseer overseer;
  private volatile boolean isClosed = false;

  public OverseerElectionContext(
      SolrZkClient zkClient, Overseer overseer, final String zkNodeName) {
    super(zkNodeName, Overseer.OVERSEER_ELECT, Overseer.OVERSEER_ELECT + "/leader", null, zkClient);
    this.overseer = overseer;
    this.zkClient = zkClient;
    try {
      ZkMaintenanceUtils.ensureExists(Overseer.OVERSEER_ELECT, zkClient);
    } catch (KeeperException e) {
      throw new SolrException(ErrorCode.SERVER_ERROR, e);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new SolrException(ErrorCode.SERVER_ERROR, e);
    }
  }

  @Override
  void runLeaderProcess(boolean weAreReplacement) throws KeeperException, InterruptedException {
    if (isClosed) {
      return;
    }
    log.info("I am going to be the leader {}", id);
    final String id = leaderSeqPath.substring(leaderSeqPath.lastIndexOf('/') + 1);
    ZkNodeProps myProps = new ZkNodeProps(ID, id);

    zkClient.makePath(leaderPath, Utils.toJSON(myProps), CreateMode.EPHEMERAL);

    synchronized (this) {
      if (!this.isClosed && !overseer.getZkController().getCoreContainer().isShutDown()) {
        overseer.start(id);
        return;
      }
    }
    removeLeaderRegistration(id);
  }

  /** Removes the leader node just created, if still ours, when no Overseer was started for it. */
  private void removeLeaderRegistration(String id) throws KeeperException, InterruptedException {
    try {
      Stat stat = new Stat();
      byte[] data = zkClient.getData(leaderPath, null, stat);
      Map<?, ?> registered = (Map<?, ?>) Utils.fromJSON(data);
      if (id.equals(registered.get(ID))) {
        log.info("Not starting the Overseer, removing leader registration of {}", id);
        zkClient.delete(leaderPath, stat.getVersion());
      }
    } catch (KeeperException.NoNodeException
        | KeeperException.BadVersionException
        | IllegalStateException e) {
      // gone already, taken over by another Overseer, or the client is closed (in which case the
      // ephemeral registration dies with the session anyway)
    }
  }

  @Override
  public void cancelElection() throws InterruptedException, KeeperException {
    super.cancelElection();
    overseer.close();
  }

  @Override
  public synchronized void close() {
    this.isClosed = true;
    overseer.close();
  }

  @Override
  public ElectionContext copy() {
    return new OverseerElectionContext(zkClient, overseer, id);
  }

  @Override
  public void joinedElectionFired() {
    overseer.close();
  }

  @Override
  public void checkIfIamLeaderFired() {
    // leader changed - close the overseer
    overseer.close();
  }
}
