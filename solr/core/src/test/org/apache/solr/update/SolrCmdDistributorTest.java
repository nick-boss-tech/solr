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
package org.apache.solr.update;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.solr.BaseDistributedSearchTestCase;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.LukeRequest;
import org.apache.solr.client.solrj.request.SolrQuery;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.cloud.ZkCoreNodeProps;
import org.apache.solr.common.cloud.ZkNodeProps;
import org.apache.solr.common.cloud.ZkStateReader;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.UpdateParams;
import org.apache.solr.common.util.JavaBinCodec;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.core.SolrCore;
import org.apache.solr.core.SolrEventListener;
import org.apache.solr.embedded.JettySolrRunner;
import org.apache.solr.index.LogDocMergePolicyFactory;
import org.apache.solr.search.SolrIndexSearcher;
import org.apache.solr.update.MockStreamingSolrClients.Exp;
import org.apache.solr.update.SolrCmdDistributor.ForwardNode;
import org.apache.solr.update.SolrCmdDistributor.Node;
import org.apache.solr.update.SolrCmdDistributor.SolrError;
import org.apache.solr.update.SolrCmdDistributor.StdNode;
import org.apache.solr.update.processor.DistributedUpdateProcessor;
import org.apache.solr.update.processor.DistributedUpdateProcessor.LeaderRequestReplicationTracker;
import org.apache.solr.update.processor.DistributedUpdateProcessor.RollupRequestReplicationTracker;
import org.apache.solr.util.ErrorLogMuter;
import org.apache.solr.util.TestInjection;
import org.junit.BeforeClass;
import org.junit.Test;

// See: https://issues.apache.org/jira/browse/SOLR-12028 Tests cannot remove files on Windows
// machines occasionally
public class SolrCmdDistributorTest extends BaseDistributedSearchTestCase {

  private static enum NodeType {
    FORWARD,
    STANDARD
  };

  private final AtomicInteger uniqueId = new AtomicInteger();

  @BeforeClass
  public static void beforeClass() {
    // we can't use the Randomized merge policy because the test depends on
    // being able to call optimize to have all deletes expunged.
    systemSetPropertySolrTestsMergePolicyFactory(LogDocMergePolicyFactory.class.getName());
    System.setProperty("solr.cloud.client.pollQueueTime", "2000");
  }

  private UpdateShardHandler updateShardHandler;

  public SolrCmdDistributorTest() {
    updateShardHandler = new UpdateShardHandler(UpdateShardHandlerConfig.DEFAULT);

    stress = 0;
  }

  public static String getSchemaFile() {
    return "schema.xml";
  }

  public static String getSolrConfigFile() {
    // use this because it has /update and is minimal
    return "solrconfig-tlog.xml";
  }

  // TODO: for now we redefine this method so that it pulls from the above
  // we don't get helpful override behavior due to the method being static
  @Override
  protected void createServers(int numShards) throws Exception {

    System.setProperty("configSetBaseDir", TEST_HOME().toString());

    Path controlHome = testDir.resolve("control");

    seedSolrHome(controlHome);
    writeCoreProperties(
        controlHome.resolve("cores").resolve(DEFAULT_TEST_CORENAME), DEFAULT_TEST_CORENAME);
    controlJetty =
        createJetty(
            controlHome, testDir + "/control/data", null, getSolrConfigFile(), getSchemaFile());
    controlJetty.start();
    controlClient = createNewSolrClient(controlJetty.getLocalPort());

    shardsArr = new String[numShards];
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < numShards; i++) {
      if (sb.length() > 0) sb.append(',');
      String shardname = "shard" + i;
      Path shardHome = testDir.resolve(shardname);
      seedSolrHome(shardHome);
      Path coresPath = shardHome.resolve("cores");
      writeCoreProperties(coresPath.resolve(DEFAULT_TEST_CORENAME), DEFAULT_TEST_CORENAME);
      JettySolrRunner j =
          createJetty(
              shardHome,
              testDir + "/shard" + i + "/data",
              null,
              getSolrConfigFile(),
              getSchemaFile());
      j.start();
      jettys.add(j);
      clients.add(createNewSolrClient(j.getLocalPort()));
      String shardStr = buildUrl(j.getLocalPort());
      shardsArr[i] = shardStr;
      sb.append(shardStr);
    }

    shards = sb.toString();
  }

  @SuppressWarnings("unchecked")
  @Test
  @ShardsFixed(num = 4)
  public void test() throws Exception {
    del("*:*");

    ModifiableSolrParams params = new ModifiableSolrParams();
    List<Node> nodes = new ArrayList<>();
    AddUpdateCommand cmd = new AddUpdateCommand(null);
    List<SolrError> errors;
    CommitUpdateCommand ccmd = new CommitUpdateCommand(null, false);
    long numFound;
    HttpSolrClient client;
    ZkNodeProps nodeProps;

    try (SolrCmdDistributor cmdDistrib = new SolrCmdDistributor(updateShardHandler)) {

      nodeProps =
          new ZkNodeProps(
              ZkStateReader.BASE_URL_PROP,
              controlClient.getBaseURL(),
              ZkStateReader.CORE_NAME_PROP,
              controlClient.getDefaultCollection());
      nodes.add(new StdNode(new ZkCoreNodeProps(nodeProps)));

      // add one doc to controlClient
      cmd.solrDoc = sdoc("id", uniqueId.incrementAndGet());
      params = new ModifiableSolrParams();

      cmdDistrib.distribAdd(cmd, nodes, params);

      params = new ModifiableSolrParams();
      params.set(DistributedUpdateProcessor.COMMIT_END_POINT, true);
      cmdDistrib.distribCommit(ccmd, nodes, params);
      cmdDistrib.finish();

      errors = cmdDistrib.getErrors();

      assertEquals(errors.toString(), 0, errors.size());

      numFound = controlClient.query(new SolrQuery("*:*")).getResults().getNumFound();
      assertEquals(1, numFound);

      client = clients.getFirst();
      nodeProps =
          new ZkNodeProps(
              ZkStateReader.BASE_URL_PROP,
              client.getBaseURL(),
              ZkStateReader.CORE_NAME_PROP,
              client.getDefaultCollection());
      nodes.add(new StdNode(new ZkCoreNodeProps(nodeProps)));
    }
    int id2;
    // add another 2 docs to control and 3 to client
    try (SolrCmdDistributor cmdDistrib = new SolrCmdDistributor(updateShardHandler)) {
      cmd.solrDoc = sdoc("id", uniqueId.incrementAndGet());
      params = new ModifiableSolrParams();
      params.set(DistributedUpdateProcessor.COMMIT_END_POINT, true);
      cmdDistrib.distribAdd(cmd, nodes, params);

      id2 = uniqueId.incrementAndGet();
      AddUpdateCommand cmd2 = new AddUpdateCommand(null);
      cmd2.solrDoc = sdoc("id", id2);

      params = new ModifiableSolrParams();
      params.set(DistributedUpdateProcessor.COMMIT_END_POINT, true);
      cmdDistrib.distribAdd(cmd2, nodes, params);

      AddUpdateCommand cmd3 = new AddUpdateCommand(null);
      cmd3.solrDoc = sdoc("id", uniqueId.incrementAndGet());

      params = new ModifiableSolrParams();
      params.set(DistributedUpdateProcessor.COMMIT_END_POINT, true);
      cmdDistrib.distribAdd(cmd3, List.of(nodes.get(1)), params);

      params = new ModifiableSolrParams();
      params.set(DistributedUpdateProcessor.COMMIT_END_POINT, true);
      cmdDistrib.distribCommit(ccmd, nodes, params);
      cmdDistrib.finish();
      errors = cmdDistrib.getErrors();
    }
    assertEquals(errors.toString(), 0, errors.size());

    SolrDocumentList results = controlClient.query(new SolrQuery("*:*")).getResults();
    numFound = results.getNumFound();
    assertEquals(results.toString(), 3, numFound);

    numFound = client.query(new SolrQuery("*:*")).getResults().getNumFound();
    assertEquals(3, numFound);

    // now delete doc 2 which is on both control and client1

    DeleteUpdateCommand dcmd = new DeleteUpdateCommand(null);
    dcmd.id = Integer.toString(id2);

    try (SolrCmdDistributor cmdDistrib = new SolrCmdDistributor(updateShardHandler)) {

      params = new ModifiableSolrParams();
      params.set(DistributedUpdateProcessor.COMMIT_END_POINT, true);

      cmdDistrib.distribDelete(dcmd, nodes, params);

      params = new ModifiableSolrParams();
      params.set(DistributedUpdateProcessor.COMMIT_END_POINT, true);

      cmdDistrib.distribCommit(ccmd, nodes, params);
      cmdDistrib.finish();

      errors = cmdDistrib.getErrors();
    }

    assertEquals(errors.toString(), 0, errors.size());

    results = controlClient.query(new SolrQuery("*:*")).getResults();
    numFound = results.getNumFound();
    assertEquals(results.toString(), 2, numFound);

    numFound = client.query(new SolrQuery("*:*")).getResults().getNumFound();
    assertEquals(results.toString(), 2, numFound);

    for (SolrClient c : clients) {
      c.optimize();
      // System.out.println(clients.get(0).request(new LukeRequest()));
    }

    try (SolrCmdDistributor cmdDistrib = new SolrCmdDistributor(updateShardHandler)) {

      int cnt = atLeast(303);
      for (int i = 0; i < cnt; i++) {
        nodes.clear();
        for (HttpSolrClient c : clients) {
          if (random().nextBoolean()) {
            continue;
          }
          nodeProps =
              new ZkNodeProps(
                  ZkStateReader.BASE_URL_PROP,
                  c.getBaseURL(),
                  ZkStateReader.CORE_NAME_PROP,
                  c.getDefaultCollection());
          nodes.add(new StdNode(new ZkCoreNodeProps(nodeProps)));
        }
        AddUpdateCommand c = new AddUpdateCommand(null);
        c.solrDoc = sdoc("id", uniqueId.incrementAndGet());
        if (nodes.size() > 0) {
          params = new ModifiableSolrParams();
          cmdDistrib.distribAdd(c, nodes, params);
        }
      }

      nodes.clear();

      for (HttpSolrClient c : clients) {
        nodeProps =
            new ZkNodeProps(
                ZkStateReader.BASE_URL_PROP,
                c.getBaseURL(),
                ZkStateReader.CORE_NAME_PROP,
                c.getDefaultCollection());

        nodes.add(new StdNode(new ZkCoreNodeProps(nodeProps)));
      }

      final AtomicInteger commits = new AtomicInteger();
      for (JettySolrRunner jetty : jettys) {
        CoreContainer cores = jetty.getCoreContainer();
        try (SolrCore core = cores.getCore("collection1")) {
          core.getUpdateHandler()
              .registerCommitCallback(
                  new SolrEventListener() {
                    @Override
                    public void postSoftCommit() {}

                    @Override
                    public void postCommit() {
                      commits.incrementAndGet();
                    }

                    @Override
                    public void newSearcher(
                        SolrIndexSearcher newSearcher, SolrIndexSearcher currentSearcher) {}
                  });
        }
      }
      params = new ModifiableSolrParams();
      params.set(DistributedUpdateProcessor.COMMIT_END_POINT, true);

      cmdDistrib.distribCommit(ccmd, nodes, params);

      cmdDistrib.finish();

      assertEquals(getShardCount(), commits.get());

      for (SolrClient c : clients) {
        NamedList<Object> resp = c.request(new LukeRequest());
        assertEquals(
            "SOLR-3428: We only did adds - there should be no deletes",
            ((NamedList<Object>) resp.get("index")).get("numDocs"),
            ((NamedList<Object>) resp.get("index")).get("maxDoc"));
      }
    }

    testMaxRetries(NodeType.FORWARD);
    testMaxRetries(NodeType.STANDARD);
    testOneRetry(NodeType.FORWARD);
    testOneRetry(NodeType.STANDARD);
    testRetryNodeAgainstBadAddress();
    testStdNodeRetriesSocketError();
    testForwardNodeWontRetrySocketError();
    testNodeWontRetryBadRequest(NodeType.FORWARD);
    testNodeWontRetryBadRequest(NodeType.STANDARD);
    testMinRfOnRetries(NodeType.FORWARD);
    testMinRfOnRetries(NodeType.STANDARD);
    testDistribOpenSearcher();
    testReqShouldRetryNoRetries();
    testReqShouldRetryMaxRetries();
    testReqShouldRetryBadRequest();
    testReqShouldRetryNotFound();
    testReqShouldRetryDBQ();
    testDeletes(false, true);
    testDeletes(false, false);
    testDeletes(true, true);
    testDeletes(true, false);
    getRfFromResponseShouldNotCloseTheInputStream();
    testStuckUpdates();
  }

  private void testDeletes(boolean dbq, boolean withFailures) throws Exception {
    final var solrclient = clients.getFirst();
    solrclient.commit(true, true);
    long numFoundBefore = solrclient.query(new SolrQuery("*:*")).getResults().getNumFound();
    final MockStreamingSolrClients streamingClients =
        new MockStreamingSolrClients(updateShardHandler);
    try (SolrCmdDistributor cmdDistrib = new SolrCmdDistributor(streamingClients, 0)) {
      if (withFailures) {
        streamingClients.setExp(Exp.CONNECT_EXCEPTION);
      }
      ArrayList<Node> nodes = new ArrayList<>();

      final AtomicInteger retries = new AtomicInteger();
      ZkNodeProps nodeProps =
          new ZkNodeProps(
              ZkStateReader.BASE_URL_PROP,
              solrclient.getBaseURL(),
              ZkStateReader.CORE_NAME_PROP,
              solrclient.getDefaultCollection());
      Node retryNode =
          new StdNode(new ZkCoreNodeProps(nodeProps), "collection1", "shard1", 5) {
            @Override
            public boolean checkRetry(SolrError err) {
              streamingClients.setExp(null);
              retries.incrementAndGet();
              return super.checkRetry(err);
            }
          };

      nodes.add(retryNode);

      for (int i = 0; i < 5; i++) {
        AddUpdateCommand cmd = new AddUpdateCommand(null);
        int currentId = uniqueId.incrementAndGet();
        cmd.solrDoc = sdoc("id", currentId);
        ModifiableSolrParams params = new ModifiableSolrParams();
        cmdDistrib.distribAdd(cmd, nodes, params);
        DeleteUpdateCommand dcmd = new DeleteUpdateCommand(null);
        if (dbq) {
          dcmd.setQuery("id:" + currentId);
        } else {
          dcmd.setId(String.valueOf(currentId));
        }
        cmdDistrib.distribDelete(dcmd, nodes, params, false, null, null);
      }

      CommitUpdateCommand ccmd = new CommitUpdateCommand(null, false);
      cmdDistrib.distribCommit(ccmd, nodes, new ModifiableSolrParams());
      cmdDistrib.finish();

      int expectedRetryCount = 0;
      if (withFailures) {
        if (dbq) {
          expectedRetryCount = 1; // just the first cmd would be retried
        } else {
          expectedRetryCount = 10;
        }
      }
      assertEquals(expectedRetryCount, retries.get());

      long numFoundAfter = solrclient.query(new SolrQuery("*:*")).getResults().getNumFound();

      // we will get java.net.ConnectException which we retry on
      assertEquals(numFoundBefore, numFoundAfter);
      assertEquals(0, cmdDistrib.getErrors().size());
    }
  }

  private void testMinRfOnRetries(NodeType nodeType) throws Exception {
    final var solrclient = clients.getFirst();
    final MockStreamingSolrClients streamingClients =
        new MockStreamingSolrClients(updateShardHandler);
    try (SolrCmdDistributor cmdDistrib = new SolrCmdDistributor(streamingClients, 0)) {
      streamingClients.setExp(Exp.CONNECT_EXCEPTION);
      ArrayList<Node> nodes = new ArrayList<>();

      final AtomicInteger retries = new AtomicInteger();
      ZkNodeProps nodeProps =
          new ZkNodeProps(
              ZkStateReader.BASE_URL_PROP,
              solrclient.getBaseURL(),
              ZkStateReader.CORE_NAME_PROP,
              solrclient.getDefaultCollection());
      if (nodeType == NodeType.FORWARD) {
        nodes.add(
            new ForwardNode(new ZkCoreNodeProps(nodeProps), null, "collection1", "shard1", 5) {
              @Override
              public boolean checkRetry(SolrError err) {
                if (retries.incrementAndGet() >= 3) {
                  streamingClients.setExp(null);
                }
                return super.checkRetry(err);
              }
            });
      } else {
        nodes.add(
            new StdNode(new ZkCoreNodeProps(nodeProps), "collection1", "shard1", 5) {
              @Override
              public boolean checkRetry(SolrError err) {
                if (retries.incrementAndGet() >= 3) {
                  streamingClients.setExp(null);
                }
                return super.checkRetry(err);
              }
            });
      }

      AddUpdateCommand cmd = new AddUpdateCommand(null);
      cmd.solrDoc = sdoc("id", uniqueId.incrementAndGet());
      ModifiableSolrParams params = new ModifiableSolrParams();
      RollupRequestReplicationTracker rollupReqTracker = new RollupRequestReplicationTracker();
      LeaderRequestReplicationTracker leaderReqTracker =
          new LeaderRequestReplicationTracker("shard1");

      cmdDistrib.distribAdd(cmd, nodes, params, false, rollupReqTracker, leaderReqTracker);
      cmdDistrib.finish();
      assertEquals(3, retries.get());
      // "2" here is because one would be the leader, that creates the instance of
      // LeaderRequestReplicationTracker, the second one is the node
      assertEquals(2, leaderReqTracker.getAchievedRf());

      assertEquals(0, cmdDistrib.getErrors().size());
    }
  }

  private void testMaxRetries(NodeType nodeType) throws IOException {
    final MockStreamingSolrClients streamingClients =
        new MockStreamingSolrClients(updateShardHandler);
    try (SolrCmdDistributor cmdDistrib = new SolrCmdDistributor(streamingClients, 0)) {
      streamingClients.setExp(Exp.CONNECT_EXCEPTION);
      ArrayList<Node> nodes = new ArrayList<>();
      final var solrClient1 = clients.getFirst();

      final AtomicInteger retries = new AtomicInteger();
      ZkNodeProps nodeProps =
          new ZkNodeProps(
              ZkStateReader.BASE_URL_PROP,
              solrClient1.getBaseURL(),
              ZkStateReader.CORE_NAME_PROP,
              solrClient1.getDefaultCollection());
      Node retryNode;
      if (nodeType == NodeType.FORWARD) {
        retryNode =
            new ForwardNode(new ZkCoreNodeProps(nodeProps), null, "collection1", "shard1", 6) {
              @Override
              public boolean checkRetry(SolrError err) {
                retries.incrementAndGet();
                return super.checkRetry(err);
              }
            };
      } else {
        retryNode =
            new StdNode(new ZkCoreNodeProps(nodeProps), "collection1", "shard1", 6) {
              @Override
              public boolean checkRetry(SolrError err) {
                retries.incrementAndGet();
                return super.checkRetry(err);
              }
            };
      }

      nodes.add(retryNode);

      AddUpdateCommand cmd = new AddUpdateCommand(null);
      cmd.solrDoc = sdoc("id", uniqueId.incrementAndGet());
      ModifiableSolrParams params = new ModifiableSolrParams();

      cmdDistrib.distribAdd(cmd, nodes, params);
      cmdDistrib.finish();

      assertEquals(7, retries.get());

      assertEquals(1, cmdDistrib.getErrors().size());
    }
  }

  private void testReqShouldRetryNoRetries() {
    SolrError err = getError(new SocketException());
    SolrCmdDistributor.Req req =
        new SolrCmdDistributor.Req(
            null, new StdNode(null, "collection1", "shard1", 0), new UpdateRequest(), true);
    assertFalse(req.shouldRetry(err));
  }

  private void testReqShouldRetryDBQ() {
    SolrError err = getError(new SocketException());
    UpdateRequest dbqReq = new UpdateRequest();
    dbqReq.deleteByQuery("*:*");
    SolrCmdDistributor.Req req =
        new SolrCmdDistributor.Req(
            null, new StdNode(null, "collection1", "shard1", 1), dbqReq, true);
    assertFalse(req.shouldRetry(err));
  }

  public void getRfFromResponseShouldNotCloseTheInputStream() {
    UpdateRequest dbqReq = new UpdateRequest();
    dbqReq.deleteByQuery("*:*");
    SolrCmdDistributor.Req req =
        new SolrCmdDistributor.Req(
            null, new StdNode(null, "collection1", "shard1", 1), dbqReq, true);
    AtomicBoolean isClosed = new AtomicBoolean(false);
    ByteArrayInputStream is =
        new ByteArrayInputStream(new byte[100]) {
          @Override
          public void close() throws IOException {
            isClosed.set(true);
            super.close();
          }
        };
    req.trackRequestResult(null, is, true);
    assertFalse("Underlying stream should not be closed!", isClosed.get());
  }

  private void testReqShouldRetryMaxRetries() {
    SolrError err = getError(new SocketException());
    SolrCmdDistributor.Req req =
        new SolrCmdDistributor.Req(
            null, new StdNode(null, "collection1", "shard1", 1), new UpdateRequest(), true);
    assertTrue(req.shouldRetry(err));
    req.retries++;
    assertFalse(req.shouldRetry(err));
  }

  private void testReqShouldRetryBadRequest() {
    SolrError err = getError(new SolrException(SolrException.ErrorCode.BAD_REQUEST, "bad request"));
    SolrCmdDistributor.Req req =
        new SolrCmdDistributor.Req(
            null, new StdNode(null, "collection1", "shard1", 1), new UpdateRequest(), true);
    assertFalse(req.shouldRetry(err));
  }

  private void testReqShouldRetryNotFound() {
    SolrError err = getError(new SolrException(SolrException.ErrorCode.NOT_FOUND, "not found"));
    SolrCmdDistributor.Req req =
        new SolrCmdDistributor.Req(
            null, new StdNode(null, "collection1", "shard1", 1), new UpdateRequest(), true);
    assertTrue(req.shouldRetry(err));
  }

  private SolrError getError(Exception e) {
    SolrError err = new SolrError();
    err.e = e;
    if (e instanceof SolrException) {
      err.statusCode = ((SolrException) e).code();
    }
    return err;
  }

  @Test
  @ShardsFixed(num = 1)
  public void testStallKeepsErrorsFromOtherNodes() throws Exception {
    assertStallKeepsErrorsFromOtherNodes(true);
  }

  @Test
  @ShardsFixed(num = 1)
  public void testFinishKeepsErrorsAfterStall() throws Exception {
    assertStallKeepsErrorsFromOtherNodes(false);
  }

  @Test
  @ShardsFixed(num = 1)
  @SuppressWarnings("try")
  public void testRetryReachesServerWhileAnotherClientStalls() throws Exception {
    final var solrclient = clients.getFirst();
    String previousPollQueueTime = System.getProperty("solr.cloud.client.pollQueueTime");
    String previousStallTime = System.getProperty("solr.cloud.client.stallTime");
    System.setProperty("solr.cloud.client.pollQueueTime", "100");
    System.setProperty("solr.cloud.client.stallTime", "1000");
    try (ServerSocket silent = new ServerSocket(0)) {
      silent.setSoTimeout(10000);
      Node retryNode =
          new StdNode(
              coreNodeProps(solrclient.getBaseURL(), solrclient), "collection1", "shard1", 1);
      Node stalledNode =
          new StdNode(
              coreNodeProps("http://127.0.0.1:" + silent.getLocalPort() + "/solr", solrclient),
              "collection1",
              "shard1",
              0);
      MockStreamingSolrClients streamingClients = new MockStreamingSolrClients(updateShardHandler);
      try (SolrCmdDistributor cmdDistrib = new SolrCmdDistributor(streamingClients, 0)) {
        String id = String.valueOf(uniqueId.incrementAndGet());
        AddUpdateCommand retry = new AddUpdateCommand(null);
        retry.solrDoc = sdoc("id", id);
        streamingClients.setExp(Exp.SOCKET_EXCEPTION);
        cmdDistrib.distribAdd(retry, List.of(retryNode), new ModifiableSolrParams());
        streamingClients.setExp(null);

        AddUpdateCommand first = new AddUpdateCommand(null);
        first.solrDoc = sdoc("id", uniqueId.incrementAndGet());
        ModifiableSolrParams firstParams = new ModifiableSolrParams();
        firstParams.set("stream", 0);
        cmdDistrib.distribAdd(first, List.of(stalledNode), firstParams);
        try (Socket accepted = silent.accept()) {
          for (int i = 1; i < 3; i++) {
            AddUpdateCommand queued = new AddUpdateCommand(null);
            queued.solrDoc = sdoc("id", uniqueId.incrementAndGet());
            ModifiableSolrParams params = new ModifiableSolrParams();
            params.set("stream", i);
            cmdDistrib.distribAdd(queued, List.of(stalledNode), params);
          }
          cmdDistrib.finish();
          assertTrue(
              cmdDistrib.getErrors().toString(),
              cmdDistrib.getErrors().stream()
                  .anyMatch(
                      e ->
                          stalledNode.equals(e.req.node)
                              && e.e instanceof IOException
                              && e.e.getMessage().contains("Request processing has stalled")));
        }
        solrclient.commit();
        assertEquals(1, solrclient.query(new SolrQuery("id:" + id)).getResults().getNumFound());
      }
    } finally {
      restoreProperty("solr.cloud.client.pollQueueTime", previousPollQueueTime);
      restoreProperty("solr.cloud.client.stallTime", previousStallTime);
    }
  }

  @Test
  @ShardsFixed(num = 1)
  public void testStalledClientIgnoresLateCallbacks() throws Exception {
    var solrclient = clients.getFirst();
    Node node =
        new StdNode(
            coreNodeProps("http://127.0.0.1:4/solr", solrclient), "collection1", "shard1", 0);
    LeaderRequestReplicationTracker tracker = new LeaderRequestReplicationTracker("shard1");
    AddUpdateCommand command = new AddUpdateCommand(null);
    command.solrDoc = sdoc("id", uniqueId.incrementAndGet());
    SolrCmdDistributor.Req req =
        new SolrCmdDistributor.Req(command, node, new UpdateRequest(), false, null, tracker);
    StreamingSolrClients streamingClients = new StreamingSolrClients(updateShardHandler);
    try {
      ErrorReportingConcurrentUpdateSolrClient client =
          (ErrorReportingConcurrentUpdateSolrClient) streamingClients.getSolrClient(req);
      ExecutorService callbackExecutor = Executors.newSingleThreadExecutor();
      try {
        // blockUntilFinished holds this monitor while waiting for runner callbacks.
        synchronized (client) {
          callbackExecutor.submit(() -> client.onSuccess(null, null)).get(5, TimeUnit.SECONDS);
          assertEquals(2, tracker.getAchievedRf());
          client.reportStall(new IOException("stalled"));
          assertEquals(1, streamingClients.getErrors().size());
          callbackExecutor.submit(() -> client.onSuccess(null, null)).get(5, TimeUnit.SECONDS);
          callbackExecutor
              .submit(() -> client.handleError(new IOException("late error")))
              .get(5, TimeUnit.SECONDS);
        }
      } finally {
        callbackExecutor.shutdownNow();
      }
      assertEquals(2, tracker.getAchievedRf());
      assertEquals(1, streamingClients.getErrors().size());
      assertEquals(1, streamingClients.drainErrors().size());
      assertTrue(streamingClients.drainErrors().isEmpty());
    } finally {
      streamingClients.shutdown();
    }
  }

  @Test
  @ShardsFixed(num = 1)
  public void testSuccessfulResponseKeepsReplicationFactorAccounting() throws Exception {
    var solrclient = clients.getFirst();
    Node node =
        new StdNode(
            coreNodeProps("http://127.0.0.1:4/solr", solrclient), "collection1", "shard1", 0);
    LeaderRequestReplicationTracker leader = new LeaderRequestReplicationTracker("shard1");
    RollupRequestReplicationTracker rollup = new RollupRequestReplicationTracker();
    AddUpdateCommand command = new AddUpdateCommand(null);
    command.solrDoc = sdoc("id", uniqueId.incrementAndGet());
    SolrCmdDistributor.Req req =
        new SolrCmdDistributor.Req(command, node, new UpdateRequest(), false, rollup, leader);
    NamedList<Object> header = new NamedList<>();
    header.add(UpdateRequest.REPFACT, 2);
    NamedList<Object> response = new NamedList<>();
    response.add("responseHeader", header);
    ByteArrayOutputStream body = new ByteArrayOutputStream();
    try (JavaBinCodec codec = new JavaBinCodec()) {
      codec.marshal(response, body);
    }
    StreamingSolrClients streamingClients = new StreamingSolrClients(updateShardHandler);
    try {
      ErrorReportingConcurrentUpdateSolrClient client =
          (ErrorReportingConcurrentUpdateSolrClient) streamingClients.getSolrClient(req);
      client.onSuccess(null, new ByteArrayInputStream(body.toByteArray()));
      assertEquals(2, rollup.getAchievedRf());
      assertEquals(1, leader.getAchievedRf());
    } finally {
      streamingClients.shutdown();
    }
  }

  @Test
  @ShardsFixed(num = 1)
  public void testRealRunnerCallbacksDoNotNeedClientMonitor() throws Exception {
    String previousPollQueueTime = System.getProperty("solr.cloud.client.pollQueueTime");
    System.setProperty("solr.cloud.client.pollQueueTime", "100");
    try (UpdateShardHandler http1Handler = newHttp1UpdateShardHandler()) {
      for (boolean success : List.of(true, false)) {
        try (ServerSocket server = new ServerSocket(0)) {
          server.setSoTimeout(10000);
          Node node =
              new StdNode(
                  coreNodeProps(
                      "http://127.0.0.1:" + server.getLocalPort() + "/solr", clients.getFirst()),
                  "collection1",
                  "shard1",
                  0);
          CountDownLatch successReported = new CountDownLatch(1);
          LeaderRequestReplicationTracker tracker =
              new LeaderRequestReplicationTracker("shard1") {
                @Override
                public void trackRequestResult(Node resultNode, boolean result) {
                  super.trackRequestResult(resultNode, result);
                  if (result) successReported.countDown();
                }
              };
          AddUpdateCommand command = new AddUpdateCommand(null);
          command.solrDoc = sdoc("id", uniqueId.incrementAndGet());
          UpdateRequest update = new UpdateRequest();
          update.setParams(new ModifiableSolrParams());
          update.add(command.solrDoc);
          SolrCmdDistributor.Req req =
              new SolrCmdDistributor.Req(command, node, update, false, null, tracker);
          StreamingSolrClients streamingClients = new StreamingSolrClients(http1Handler);
          try {
            ErrorReportingConcurrentUpdateSolrClient client =
                (ErrorReportingConcurrentUpdateSolrClient) streamingClients.getSolrClient(req);
            client.request(update);
            try (Socket accepted = server.accept()) {
              accepted.setSoTimeout(10000);
              consumeHttpRequest(accepted.getInputStream());
              byte[] body = new byte[0];
              if (success) {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                try (JavaBinCodec codec = new JavaBinCodec()) {
                  codec.marshal(new NamedList<>(), bytes);
                }
                body = bytes.toByteArray();
              }
              synchronized (client) {
                String status = success ? "200 OK" : "503 Service Unavailable";
                String headers =
                    "HTTP/1.1 "
                        + status
                        + "\r\nContent-Type: application/octet-stream\r\nContent-Length: "
                        + body.length
                        + "\r\nConnection: close\r\n\r\n";
                accepted.getOutputStream().write(headers.getBytes(StandardCharsets.US_ASCII));
                accepted.getOutputStream().write(body);
                accepted.getOutputStream().flush();
                if (success) {
                  assertTrue(
                      "Success callback was blocked", successReported.await(5, TimeUnit.SECONDS));
                } else {
                  long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);
                  boolean expectedError = false;
                  while (!expectedError && System.nanoTime() < deadline) {
                    synchronized (streamingClients.getErrors()) {
                      expectedError =
                          streamingClients.getErrors().stream()
                              .anyMatch(e -> node.equals(e.req.node) && e.statusCode == 503);
                    }
                    if (!expectedError) Thread.sleep(10);
                  }
                  assertTrue("Expected HTTP 503 callback was not recorded", expectedError);
                }
              }
            }
            client.blockUntilFinished();
          } finally {
            streamingClients.shutdown();
          }
        }
      }
    } finally {
      restoreProperty("solr.cloud.client.pollQueueTime", previousPollQueueTime);
    }
  }

  @Test
  @ShardsFixed(num = 1)
  public void testErrorCallbacksDoNotClassifyRetries() throws Exception {
    var solrclient = clients.getFirst();
    AtomicInteger retryChecks = new AtomicInteger();
    Node node =
        new StdNode(
            coreNodeProps("http://127.0.0.1:4/solr", solrclient), "collection1", "shard1", 1) {
          @Override
          public boolean checkRetry(SolrError error) {
            retryChecks.incrementAndGet();
            return super.checkRetry(error);
          }
        };
    AddUpdateCommand command = new AddUpdateCommand(null);
    command.solrDoc = sdoc("id", uniqueId.incrementAndGet());
    SolrCmdDistributor.Req req =
        new SolrCmdDistributor.Req(command, node, new UpdateRequest(), false);
    StreamingSolrClients streamingClients = new StreamingSolrClients(updateShardHandler);
    try {
      ErrorReportingConcurrentUpdateSolrClient client =
          (ErrorReportingConcurrentUpdateSolrClient) streamingClients.getSolrClient(req);
      client.handleError(new SocketException("connection failed"));
      client.reportStall(new IOException("stalled"));
      assertEquals("Callbacks must leave retry decisions to the distributor", 0, retryChecks.get());
      assertEquals(2, streamingClients.drainErrors().size());
    } finally {
      streamingClients.shutdown();
    }
  }

  @Test
  @ShardsFixed(num = 1)
  public void testConcurrentErrorDrainRetainsEveryError() throws Exception {
    CountDownLatch clearReached = new CountDownLatch(1);
    CountDownLatch allowClear = new CountDownLatch(1);
    AtomicBoolean pauseClear = new AtomicBoolean(true);
    List<SolrError> errors =
        new AbstractList<>() {
          private final List<SolrError> delegate = new ArrayList<>();

          @Override
          public synchronized SolrError get(int index) {
            return delegate.get(index);
          }

          @Override
          public synchronized int size() {
            return delegate.size();
          }

          @Override
          public synchronized boolean add(SolrError error) {
            return delegate.add(error);
          }

          @Override
          public synchronized Object[] toArray() {
            return delegate.toArray();
          }

          @Override
          public void clear() {
            if (pauseClear.compareAndSet(true, false)) {
              clearReached.countDown();
              try {
                if (!allowClear.await(5, TimeUnit.SECONDS)) {
                  throw new AssertionError("Timed out waiting to clear errors");
                }
              } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new AssertionError(e);
              }
            }
            synchronized (this) {
              delegate.clear();
            }
          }
        };
    StreamingSolrClients streamingClients = new StreamingSolrClients(updateShardHandler, errors);
    ExecutorService executor = Executors.newSingleThreadExecutor();
    SolrError before = new SolrError();
    SolrError concurrent = new SolrError();
    CountDownLatch writerStarted = new CountDownLatch(1);
    CountDownLatch writerDone = new CountDownLatch(1);
    Thread writer =
        new Thread(
            () -> {
              writerStarted.countDown();
              errors.add(concurrent);
              writerDone.countDown();
            });
    writer.setDaemon(true);
    try {
      errors.add(before);
      var drained = executor.submit(streamingClients::drainErrors);
      assertTrue("Drain did not reach clear", clearReached.await(5, TimeUnit.SECONDS));
      writer.start();
      assertTrue("Writer did not start", writerStarted.await(5, TimeUnit.SECONDS));
      long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);
      while (writerDone.getCount() != 0
          && writer.getState() != Thread.State.BLOCKED
          && System.nanoTime() < deadline) {
        Thread.yield();
      }
      assertTrue(
          "Writer neither completed nor blocked on the error list",
          writerDone.getCount() == 0 || writer.getState() == Thread.State.BLOCKED);
      allowClear.countDown();
      List<SolrError> collected = drained.get(5, TimeUnit.SECONDS);
      assertTrue("Writer did not finish", writerDone.await(5, TimeUnit.SECONDS));
      collected.addAll(streamingClients.drainErrors());
      assertEquals(2, collected.size());
      assertTrue(collected.contains(before));
      assertTrue(collected.contains(concurrent));
    } finally {
      allowClear.countDown();
      writer.interrupt();
      executor.shutdownNow();
      streamingClients.shutdown();
    }
  }

  @Test
  @ShardsFixed(num = 1)
  public void testStallReportedWhileResponseBodyIsBlocked() throws Exception {
    var solrclient = clients.getFirst();
    Node node =
        new StdNode(
            coreNodeProps("http://127.0.0.1:4/solr", solrclient), "collection1", "shard1", 0);
    LeaderRequestReplicationTracker tracker = new LeaderRequestReplicationTracker("shard1");
    AddUpdateCommand command = new AddUpdateCommand(null);
    command.solrDoc = sdoc("id", uniqueId.incrementAndGet());
    SolrCmdDistributor.Req req =
        new SolrCmdDistributor.Req(command, node, new UpdateRequest(), false, null, tracker);
    StreamingSolrClients streamingClients = new StreamingSolrClients(updateShardHandler);
    CountDownLatch reading = new CountDownLatch(1);
    CountDownLatch release = new CountDownLatch(1);
    ExecutorService executor = Executors.newFixedThreadPool(2);
    try {
      ErrorReportingConcurrentUpdateSolrClient client =
          (ErrorReportingConcurrentUpdateSolrClient) streamingClients.getSolrClient(req);
      InputStream body =
          new InputStream() {
            @Override
            public int read() throws IOException {
              reading.countDown();
              try {
                if (!release.await(10, TimeUnit.SECONDS)) {
                  throw new IOException("Timed out waiting to release response body");
                }
              } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IOException(e);
              }
              return -1;
            }
          };
      var success = executor.submit(() -> client.onSuccess(null, body));
      assertTrue("Response body was not read", reading.await(5, TimeUnit.SECONDS));
      executor
          .submit(() -> client.reportStall(new IOException("stalled")))
          .get(5, TimeUnit.SECONDS);
      assertEquals(1, streamingClients.getErrors().size());
      release.countDown();
      success.get(5, TimeUnit.SECONDS);
      assertEquals(1, tracker.getAchievedRf());
    } finally {
      release.countDown();
      executor.shutdownNow();
      streamingClients.shutdown();
    }
  }

  @SuppressWarnings("try")
  private void assertStallKeepsErrorsFromOtherNodes(boolean testOrderedOperations)
      throws Exception {
    final var solrclient = clients.getFirst();
    String previousPollQueueTime = System.getProperty("solr.cloud.client.pollQueueTime");
    String previousStallTime = System.getProperty("solr.cloud.client.stallTime");
    System.setProperty("solr.cloud.client.pollQueueTime", "100");
    System.setProperty("solr.cloud.client.stallTime", "1000");
    try (ServerSocket silent = new ServerSocket(0)) {
      silent.setSoTimeout(10000);
      Node stalledNode =
          new StdNode(
              coreNodeProps("http://127.0.0.1:" + silent.getLocalPort() + "/solr", solrclient),
              "collection1",
              "shard1",
              0);
      AtomicInteger retryChecks = new AtomicInteger();
      Node failingNode =
          new StdNode(
              coreNodeProps("http://" + DEAD_HOST_1 + "/solr", solrclient),
              "collection1",
              "shard1",
              1) {
            @Override
            public boolean checkRetry(SolrError error) {
              retryChecks.incrementAndGet();
              return super.checkRetry(error);
            }
          };

      StreamingSolrClients streamingClients = new StreamingSolrClients(updateShardHandler);
      try (SolrCmdDistributor cmdDistrib = new SolrCmdDistributor(streamingClients, 0)) {
        AddUpdateCommand failing = new AddUpdateCommand(null);
        failing.solrDoc = sdoc("id", uniqueId.incrementAndGet());
        cmdDistrib.distribAdd(failing, List.of(failingNode), new ModifiableSolrParams());
        long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(10);
        boolean failureRecorded = false;
        while (!failureRecorded && System.nanoTime() < deadline) {
          synchronized (streamingClients.getErrors()) {
            failureRecorded =
                streamingClients.getErrors().stream().anyMatch(e -> failingNode.equals(e.req.node));
          }
          if (!failureRecorded) Thread.sleep(10);
        }
        assertTrue("Other node's error was not recorded", failureRecorded);

        AddUpdateCommand first = new AddUpdateCommand(null);
        first.solrDoc = sdoc("id", uniqueId.incrementAndGet());
        ModifiableSolrParams firstParams = new ModifiableSolrParams();
        firstParams.set("stream", 0);
        cmdDistrib.distribAdd(first, List.of(stalledNode), firstParams);
        try (Socket accepted = silent.accept()) {
          for (int i = 1; i < 3; i++) {
            AddUpdateCommand cmd = new AddUpdateCommand(null);
            cmd.solrDoc = sdoc("id", uniqueId.incrementAndGet());
            ModifiableSolrParams params = new ModifiableSolrParams();
            // Different params keep these updates out of the in-flight stream.
            params.set("stream", i);
            cmdDistrib.distribAdd(cmd, List.of(stalledNode), params);
          }
          if (testOrderedOperations) {
            IOException commitFailure =
                expectThrows(
                    IOException.class,
                    () ->
                        cmdDistrib.distribCommit(
                            new CommitUpdateCommand(null, false),
                            List.of(stalledNode),
                            new ModifiableSolrParams()));
            assertTrue(commitFailure.getMessage().contains("Request processing has stalled"));
            DeleteUpdateCommand deleteByQuery = new DeleteUpdateCommand(null);
            deleteByQuery.query = "*:*";
            expectThrows(
                IOException.class,
                () ->
                    cmdDistrib.distribDelete(
                        deleteByQuery, List.of(stalledNode), new ModifiableSolrParams()));
          }
          cmdDistrib.finish();
          List<SolrError> errors = cmdDistrib.getErrors();
          assertTrue(
              errors.toString(), errors.stream().anyMatch(e -> failingNode.equals(e.req.node)));
          assertTrue("Expected retry decisions for both attempts", retryChecks.get() >= 2);
          assertTrue(
              "Expected the failing request to have been retried",
              errors.stream().anyMatch(e -> failingNode.equals(e.req.node) && e.req.retries == 1));
          assertTrue(
              errors.toString(),
              errors.stream()
                  .anyMatch(
                      e ->
                          stalledNode.equals(e.req.node)
                              && e.e instanceof IOException
                              && e.e.getMessage().contains("Request processing has stalled")));
        }
      }
    } finally {
      restoreProperty("solr.cloud.client.pollQueueTime", previousPollQueueTime);
      restoreProperty("solr.cloud.client.stallTime", previousStallTime);
    }
  }

  private static UpdateShardHandler newHttp1UpdateShardHandler() {
    String previousHttp1 = System.getProperty("solr.http1");
    System.setProperty("solr.http1", "true");
    try {
      return new UpdateShardHandler(UpdateShardHandlerConfig.DEFAULT);
    } finally {
      restoreProperty("solr.http1", previousHttp1);
    }
  }

  private static void restoreProperty(String name, String value) {
    if (value == null) {
      System.clearProperty(name);
    } else {
      System.setProperty(name, value);
    }
  }

  private static void consumeHttpRequest(InputStream input) throws IOException {
    String requestLine = readHttpLine(input);
    if (!requestLine.startsWith("POST ")) {
      throw new IOException("Unexpected request: " + requestLine);
    }
    boolean chunked = false;
    int contentLength = -1;
    String line;
    while (!(line = readHttpLine(input)).isEmpty()) {
      if (line.regionMatches(true, 0, "Transfer-Encoding:", 0, 18)) {
        chunked = line.substring(18).trim().equalsIgnoreCase("chunked");
      } else if (line.regionMatches(true, 0, "Content-Length:", 0, 15)) {
        contentLength = Integer.parseInt(line.substring(15).trim());
      }
    }
    if (chunked) {
      while (true) {
        String chunkHeader = readHttpLine(input).split(";", 2)[0].trim();
        int length = Integer.parseInt(chunkHeader, 16);
        if (length == 0) {
          while (!readHttpLine(input).isEmpty()) {
            // Consume trailers, if any.
          }
          break;
        }
        if (input.readNBytes(length).length != length) throw new EOFException("Truncated chunk");
        if (!readHttpLine(input).isEmpty()) throw new IOException("Invalid chunk terminator");
      }
    } else if (contentLength >= 0) {
      if (input.readNBytes(contentLength).length != contentLength) {
        throw new EOFException("Truncated request body");
      }
    } else {
      throw new IOException("Request has no supported body framing");
    }
  }

  private static String readHttpLine(InputStream input) throws IOException {
    ByteArrayOutputStream line = new ByteArrayOutputStream();
    int b;
    while ((b = input.read()) != -1) {
      if (b == '\n') {
        String value = line.toString(StandardCharsets.US_ASCII);
        return value.endsWith("\r") ? value.substring(0, value.length() - 1) : value;
      }
      line.write(b);
    }
    throw new EOFException("Truncated HTTP line");
  }

  private static ZkCoreNodeProps coreNodeProps(String baseUrl, HttpSolrClient client) {
    return new ZkCoreNodeProps(
        new ZkNodeProps(
            ZkStateReader.BASE_URL_PROP,
            baseUrl,
            ZkStateReader.CORE_NAME_PROP,
            client.getDefaultCollection()));
  }

  private void testOneRetry(NodeType nodeType) throws Exception {
    final var solrclient = clients.getFirst();
    long numFoundBefore = solrclient.query(new SolrQuery("*:*")).getResults().getNumFound();
    final MockStreamingSolrClients streamingClients =
        new MockStreamingSolrClients(updateShardHandler);
    try (SolrCmdDistributor cmdDistrib = new SolrCmdDistributor(streamingClients, 0)) {
      streamingClients.setExp(Exp.CONNECT_EXCEPTION);
      ArrayList<Node> nodes = new ArrayList<>();

      final AtomicInteger retries = new AtomicInteger();
      ZkNodeProps nodeProps =
          new ZkNodeProps(
              ZkStateReader.BASE_URL_PROP,
              solrclient.getBaseURL(),
              ZkStateReader.CORE_NAME_PROP,
              solrclient.getDefaultCollection());
      Node retryNode;
      if (nodeType == NodeType.FORWARD) {
        retryNode =
            new ForwardNode(new ZkCoreNodeProps(nodeProps), null, "collection1", "shard1", 5) {
              @Override
              public boolean checkRetry(SolrError err) {
                streamingClients.setExp(null);
                retries.incrementAndGet();
                return super.checkRetry(err);
              }
            };
      } else {
        retryNode =
            new StdNode(new ZkCoreNodeProps(nodeProps), "collection1", "shard1", 5) {
              @Override
              public boolean checkRetry(SolrError err) {
                streamingClients.setExp(null);
                retries.incrementAndGet();
                return super.checkRetry(err);
              }
            };
      }

      nodes.add(retryNode);

      AddUpdateCommand cmd = new AddUpdateCommand(null);
      cmd.solrDoc = sdoc("id", uniqueId.incrementAndGet());
      ModifiableSolrParams params = new ModifiableSolrParams();

      CommitUpdateCommand ccmd = new CommitUpdateCommand(null, false);
      cmdDistrib.distribAdd(cmd, nodes, params);
      cmdDistrib.distribCommit(ccmd, nodes, params);
      cmdDistrib.finish();

      assertEquals(1, retries.get());

      long numFoundAfter = solrclient.query(new SolrQuery("*:*")).getResults().getNumFound();

      // we will get java.net.ConnectException which we retry on
      assertEquals(numFoundBefore + 1, numFoundAfter);
      assertEquals(0, cmdDistrib.getErrors().size());
    }
  }

  @SuppressWarnings("try")
  private void testNodeWontRetryBadRequest(NodeType nodeType) throws Exception {
    final var solrclient = clients.getFirst();
    long numFoundBefore = solrclient.query(new SolrQuery("*:*")).getResults().getNumFound();
    final MockStreamingSolrClients streamingClients =
        new MockStreamingSolrClients(updateShardHandler);
    try (SolrCmdDistributor cmdDistrib = new SolrCmdDistributor(streamingClients, 0);
        ErrorLogMuter ignored = ErrorLogMuter.regex("Bad Request")) {
      streamingClients.setExp(Exp.BAD_REQUEST);
      ArrayList<Node> nodes = new ArrayList<>();
      ZkNodeProps nodeProps =
          new ZkNodeProps(
              ZkStateReader.BASE_URL_PROP,
              solrclient.getBaseURL(),
              ZkStateReader.CORE_NAME_PROP,
              solrclient.getDefaultCollection());

      final AtomicInteger retries = new AtomicInteger();
      Node retryNode;
      if (nodeType == NodeType.FORWARD) {
        retryNode =
            new ForwardNode(new ZkCoreNodeProps(nodeProps), null, "collection1", "shard1", 5) {
              @Override
              public boolean checkRetry(SolrError err) {
                retries.incrementAndGet();
                return super.checkRetry(err);
              }
            };
      } else {
        retryNode =
            new StdNode(new ZkCoreNodeProps(nodeProps), "collection1", "shard1", 5) {
              @Override
              public boolean checkRetry(SolrError err) {
                retries.incrementAndGet();
                return super.checkRetry(err);
              }
            };
      }
      nodes.add(retryNode);

      AddUpdateCommand cmd = new AddUpdateCommand(null);
      cmd.solrDoc = sdoc("id", uniqueId.incrementAndGet());
      ModifiableSolrParams params = new ModifiableSolrParams();

      CommitUpdateCommand ccmd = new CommitUpdateCommand(null, false);
      cmdDistrib.distribAdd(cmd, nodes, params);

      streamingClients.setExp(null);
      cmdDistrib.distribCommit(ccmd, nodes, params);
      cmdDistrib.finish();

      // it will checkRetry, but not actually do it...
      assertEquals(1, retries.get());

      long numFoundAfter = solrclient.query(new SolrQuery("*:*")).getResults().getNumFound();

      // we will get java.net.SocketException: Network is unreachable, which we don't retry on
      assertEquals(numFoundBefore, numFoundAfter);
      assertEquals(1, cmdDistrib.getErrors().size());
    }
  }

  private void testForwardNodeWontRetrySocketError() throws Exception {
    final var solrclient = clients.getFirst();
    long numFoundBefore = solrclient.query(new SolrQuery("*:*")).getResults().getNumFound();
    final MockStreamingSolrClients streamingClients =
        new MockStreamingSolrClients(updateShardHandler);
    try (SolrCmdDistributor cmdDistrib = new SolrCmdDistributor(streamingClients, 0)) {
      streamingClients.setExp(Exp.SOCKET_EXCEPTION);
      ArrayList<Node> nodes = new ArrayList<>();

      final AtomicInteger retries = new AtomicInteger();
      ZkNodeProps nodeProps =
          new ZkNodeProps(
              ZkStateReader.BASE_URL_PROP,
              solrclient.getBaseURL(),
              ZkStateReader.CORE_NAME_PROP,
              solrclient.getDefaultCollection());
      ForwardNode retryNode =
          new ForwardNode(new ZkCoreNodeProps(nodeProps), null, "collection1", "shard1", 5) {
            @Override
            public boolean checkRetry(SolrError err) {
              retries.incrementAndGet();
              return super.checkRetry(err);
            }
          };

      nodes.add(retryNode);

      AddUpdateCommand cmd = new AddUpdateCommand(null);
      cmd.solrDoc = sdoc("id", uniqueId.incrementAndGet());
      ModifiableSolrParams params = new ModifiableSolrParams();

      CommitUpdateCommand ccmd = new CommitUpdateCommand(null, false);
      cmdDistrib.distribAdd(cmd, nodes, params);

      streamingClients.setExp(null);
      cmdDistrib.distribCommit(ccmd, nodes, params);
      cmdDistrib.finish();

      // it will checkRetry, but not actually do it...
      assertEquals(1, retries.get());

      long numFoundAfter = solrclient.query(new SolrQuery("*:*")).getResults().getNumFound();

      // we will get java.net.SocketException: Network is unreachable, which we don't retry on
      assertEquals(numFoundBefore, numFoundAfter);
      assertEquals(1, cmdDistrib.getErrors().size());
    }
  }

  private void testStdNodeRetriesSocketError() throws Exception {
    final var solrclient = clients.getFirst();
    final MockStreamingSolrClients streamingClients =
        new MockStreamingSolrClients(updateShardHandler);
    try (SolrCmdDistributor cmdDistrib = new SolrCmdDistributor(streamingClients, 0)) {
      streamingClients.setExp(Exp.SOCKET_EXCEPTION);
      ArrayList<Node> nodes = new ArrayList<>();

      final AtomicInteger retries = new AtomicInteger();
      ZkNodeProps nodeProps =
          new ZkNodeProps(
              ZkStateReader.BASE_URL_PROP,
              solrclient.getBaseURL(),
              ZkStateReader.CORE_NAME_PROP,
              solrclient.getDefaultCollection());
      Node retryNode =
          new StdNode(new ZkCoreNodeProps(nodeProps), "collection1", "shard1", 5) {
            @Override
            public boolean checkRetry(SolrError err) {
              retries.incrementAndGet();
              return super.checkRetry(err);
            }
          };

      nodes.add(retryNode);

      AddUpdateCommand cmd = new AddUpdateCommand(null);
      cmd.solrDoc = sdoc("id", uniqueId.incrementAndGet());
      ModifiableSolrParams params = new ModifiableSolrParams();

      cmdDistrib.distribAdd(cmd, nodes, params);
      cmdDistrib.finish();

      // it will checkRetry, but not actually do it...
      assertEquals(6, retries.get());
    }
  }

  private void testRetryNodeAgainstBadAddress() throws SolrServerException, IOException {
    // Test RetryNode
    try (SolrCmdDistributor cmdDistrib = new SolrCmdDistributor(updateShardHandler)) {
      final var solrclient = clients.getFirst();
      long numFoundBefore = solrclient.query(new SolrQuery("*:*")).getResults().getNumFound();

      ArrayList<Node> nodes = new ArrayList<>();

      ZkNodeProps nodeProps =
          new ZkNodeProps(
              ZkStateReader.BASE_URL_PROP, DEAD_HOST_1 + "/solr", ZkStateReader.CORE_NAME_PROP, "");
      ForwardNode retryNode =
          new ForwardNode(new ZkCoreNodeProps(nodeProps), null, "collection1", "shard1", 5) {
            @Override
            public boolean checkRetry(SolrError err) {
              ZkNodeProps leaderProps =
                  new ZkNodeProps(
                      ZkStateReader.BASE_URL_PROP,
                      solrclient.getBaseURL(),
                      ZkStateReader.CORE_NAME_PROP,
                      solrclient.getDefaultCollection());
              this.nodeProps = new ZkCoreNodeProps(leaderProps);

              return super.checkRetry(err);
            }
          };

      nodes.add(retryNode);

      AddUpdateCommand cmd = new AddUpdateCommand(null);
      cmd.solrDoc = sdoc("id", uniqueId.incrementAndGet());
      ModifiableSolrParams params = new ModifiableSolrParams();

      cmdDistrib.distribAdd(cmd, nodes, params);

      CommitUpdateCommand ccmd = new CommitUpdateCommand(null, false);
      params = new ModifiableSolrParams();
      params.set(DistributedUpdateProcessor.COMMIT_END_POINT, true);
      cmdDistrib.distribCommit(ccmd, nodes, params);
      cmdDistrib.finish();

      long numFoundAfter = solrclient.query(new SolrQuery("*:*")).getResults().getNumFound();

      // different OS's will throw different exceptions for the bad address above
      if (numFoundBefore != numFoundAfter) {
        assertEquals(0, cmdDistrib.getErrors().size());
        assertEquals(numFoundBefore + 1, numFoundAfter);
      } else {
        // we will get java.net.SocketException: Network is unreachable and not retry
        assertEquals(numFoundBefore, numFoundAfter);

        assertEquals(1, cmdDistrib.getErrors().size());
      }
    }
  }

  @Override
  public void distribTearDown() throws Exception {
    updateShardHandler.close();
    super.distribTearDown();
  }

  private void testDistribOpenSearcher() {
    try (SolrCmdDistributor cmdDistrib = new SolrCmdDistributor(updateShardHandler)) {
      UpdateRequest updateRequest = new UpdateRequest();

      CommitUpdateCommand ccmd = new CommitUpdateCommand(null, false);

      // test default value (should be true)
      cmdDistrib.addCommit(updateRequest, ccmd);
      boolean openSearcher = updateRequest.getParams().getBool(UpdateParams.OPEN_SEARCHER, false);
      assertTrue(openSearcher);

      // test openSearcher = false
      ccmd.openSearcher = false;

      cmdDistrib.addCommit(updateRequest, ccmd);
      openSearcher = updateRequest.getParams().getBool(UpdateParams.OPEN_SEARCHER, true);
      assertFalse(openSearcher);
    }
  }

  private void testStuckUpdates() {
    TestInjection.directUpdateLatch = new CountDownLatch(1);
    List<Node> nodes = new ArrayList<>();
    ModifiableSolrParams params;
    try (SolrCmdDistributor cmdDistrib = new SolrCmdDistributor(updateShardHandler)) {
      for (int i = 0; i < 3; i++) {
        nodes.clear();
        for (HttpSolrClient c : clients) {
          if (random().nextBoolean()) {
            continue;
          }
          ZkNodeProps nodeProps =
              new ZkNodeProps(
                  ZkStateReader.BASE_URL_PROP, c.getBaseURL(), ZkStateReader.CORE_NAME_PROP, "");
          StdNode node = new StdNode(new ZkCoreNodeProps(nodeProps));
          nodes.add(node);
        }
        AddUpdateCommand c = new AddUpdateCommand(null);
        c.solrDoc = sdoc("id", uniqueId.incrementAndGet());
        if (nodes.size() > 0) {
          params = new ModifiableSolrParams();
          cmdDistrib.distribAdd(c, nodes, params, false);
        }
      }
      cmdDistrib.blockAndDoRetries();
    } catch (IOException e) {
      assertTrue(e.toString(), e.toString().contains("processing has stalled"));
    } finally {
      TestInjection.directUpdateLatch.countDown();
    }
  }
}
