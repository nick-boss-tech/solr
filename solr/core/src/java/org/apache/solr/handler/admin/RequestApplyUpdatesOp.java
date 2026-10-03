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

package org.apache.solr.handler.admin;

import java.util.concurrent.Future;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.cloud.Replica;
import org.apache.solr.common.params.CoreAdminParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.core.SolrCore;
import org.apache.solr.update.UpdateLog;

class RequestApplyUpdatesOp implements CoreAdminHandler.CoreAdminOp {
  @Override
  public void execute(CoreAdminHandler.CallInfo it) throws Exception {
    SolrParams params = it.req.getParams();
    String cname = params.required().get(CoreAdminParams.NAME);
    CoreAdminOperation.log().info("Applying buffered updates on core: " + cname);
    CoreContainer coreContainer = it.handler.coreContainer;
    try (SolrCore core = coreContainer.getCore(cname)) {
      if (core == null)
        throw new SolrException(
            SolrException.ErrorCode.BAD_REQUEST, "Core [" + cname + "] not found");
      UpdateLog updateLog = core.getUpdateHandler().getUpdateLog();
      if (updateLog.getState() != UpdateLog.State.BUFFERING) {
        throw new SolrException(
            SolrException.ErrorCode.SERVER_ERROR, "Core " + cname + " not in buffering state");
      }
      String status = applyBufferedUpdates(coreContainer, core, updateLog);
      it.rsp.add("core", cname);
      it.rsp.add("status", status);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      CoreAdminOperation.log().warn("Recovery was interrupted", e);
    } catch (Exception e) {
      if (e instanceof SolrException) throw e;
      else
        throw new SolrException(
            SolrException.ErrorCode.SERVER_ERROR, "Could not apply buffered updates", e);
    } finally {
      if (it.req != null) it.req.close();
    }
  }

  /**
   * Applies the buffered updates of a buffering core and publishes the replica ACTIVE once its
   * update log is ACTIVE.
   *
   * @return the response status, {@code BUFFER_APPLIED} or {@code EMPTY_BUFFER}
   */
  static String applyBufferedUpdates(
      CoreContainer coreContainer, SolrCore core, UpdateLog updateLog) throws Exception {
    Future<UpdateLog.RecoveryInfo> future = updateLog.applyBufferedUpdates();
    if (future == null) {
      CoreAdminOperation.log().info("No buffered updates available. core=" + core.getName());
      // A null future also means the log was no longer buffering, e.g. it is being recovered or
      // already applying a buffer, in which case the replica must not be published ACTIVE here.
      if (updateLog.getState() == UpdateLog.State.ACTIVE) {
        publishActive(coreContainer, core);
      }
      return "EMPTY_BUFFER";
    }
    UpdateLog.RecoveryInfo report = future.get();
    if (report.failed) {
      CoreAdminOperation.log().error("Replay failed");
      throw new SolrException(SolrException.ErrorCode.SERVER_ERROR, "Replay failed");
    }
    publishActive(coreContainer, core);
    return "BUFFER_APPLIED";
  }

  private static void publishActive(CoreContainer coreContainer, SolrCore core) throws Exception {
    if (coreContainer.isZooKeeperAware()) {
      coreContainer.getZkController().publish(core.getCoreDescriptor(), Replica.State.ACTIVE);
    }
  }
}
