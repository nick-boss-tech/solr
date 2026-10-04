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
package org.apache.solr.handler;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import org.apache.lucene.index.IndexFileNames;
import org.apache.lucene.tests.util.LuceneTestCase;
import org.apache.lucene.tests.util.TestUtil;
import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.embedded.JettyConfig;
import org.apache.solr.embedded.JettySolrRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@SolrTestCaseJ4.SuppressSSL // Currently, unknown why SSL does not work with this test
// Backups do checksum validation against a footer value not present in 'SimpleText'
@LuceneTestCase.SuppressCodecs("SimpleText")
public class TestRestoreCore extends SolrTestCaseJ4 {

  JettySolrRunner leaderJetty;
  ReplicationTestHelper.SolrInstance leader = null;
  SolrClient leaderClient;

  private static final Path CONF_DIR = Path.of("solr", DEFAULT_TEST_CORENAME, "conf");

  private static long docsSeed; // see indexDocs()

  private static JettySolrRunner createAndStartJetty(ReplicationTestHelper.SolrInstance instance)
      throws Exception {
    JettyConfig jettyConfig = JettyConfig.builder().setPort(0).build();
    JettySolrRunner jetty =
        new JettySolrRunner(instance.getHomeDir(), new Properties(), jettyConfig);
    jetty.start();
    return jetty;
  }

  @Override
  @Before
  public void setUp() throws Exception {
    super.setUp();
    String configFile = "solrconfig-leader.xml";

    leader = new ReplicationTestHelper.SolrInstance(createTempDir("solr-instance"), "leader", null);
    leader.setUp();
    leader.copyConfigFile(CONF_DIR.resolve(configFile).toString(), "solrconfig.xml");

    leaderJetty = createAndStartJetty(leader);
    leaderClient = leaderJetty.getSolrClient();
    docsSeed = random().nextLong();
  }

  @Override
  @After
  public void tearDown() throws Exception {
    super.tearDown();
    if (null != leaderClient) {
      leaderClient.close();
      leaderClient = null;
    }
    if (null != leaderJetty) {
      leaderJetty.stop();
      leaderJetty = null;
    }
    leader = null;
  }

  @Test
  public void testSimpleRestore() throws Exception {

    int nDocs = usually() ? BackupRestoreUtils.indexDocs(leaderClient, "collection1", docsSeed) : 0;

    final BackupStatusChecker backupStatus =
        new BackupStatusChecker(leaderClient, "/" + DEFAULT_TEST_CORENAME + "/replication");
    final String oldBackupDir = backupStatus.checkBackupSuccess();
    String snapshotName = null;
    String location;
    String params = "";
    String baseUrl = leaderJetty.getBaseUrl().toString();

    // Use the default backup location or an externally provided location.
    if (random().nextBoolean()) {
      location = createTempDir().toString();
      leaderJetty
          .getCoreContainer()
          .getAllowPaths()
          .add(Path.of(location)); // Allow core to be created outside SOLR_HOME
      params += "&location=" + URLEncoder.encode(location, StandardCharsets.UTF_8);
    }

    // named snapshot vs default snapshot name
    if (random().nextBoolean()) {
      snapshotName = TestUtil.randomSimpleString(random(), 1, 5);
      params += "&name=" + snapshotName;
    }

    TestReplicationHandlerBackup.runBackupCommand(
        leaderJetty, ReplicationHandler.CMD_BACKUP, params);

    if (null == snapshotName) {
      backupStatus.waitForDifferentBackupDir(oldBackupDir, 30);
    } else {
      backupStatus.waitForBackupSuccess(snapshotName, 30);
    }

    int numRestoreTests = nDocs > 0 ? TestUtil.nextInt(random(), 1, 5) : 1;

    for (int attempts = 0; attempts < numRestoreTests; attempts++) {
      // Modify existing index before we call restore.

      if (nDocs > 0) {
        // Delete a few docs
        int numDeletes = TestUtil.nextInt(random(), 1, nDocs);
        for (int i = 0; i < numDeletes; i++) {
          leaderClient.deleteByQuery(DEFAULT_TEST_CORENAME, "id:" + i);
        }
        leaderClient.commit(DEFAULT_TEST_CORENAME);

        // Add a few more
        int moreAdds = TestUtil.nextInt(random(), 1, 100);
        for (int i = 0; i < moreAdds; i++) {
          SolrInputDocument doc = new SolrInputDocument();
          doc.addField("id", i + nDocs);
          doc.addField("name", "name = " + (i + nDocs));
          leaderClient.add(DEFAULT_TEST_CORENAME, doc);
        }
        // Purposely not calling commit once in a while. There can be some docs which are not
        // committed
        if (usually()) {
          leaderClient.commit(DEFAULT_TEST_CORENAME);
        }
      }

      TestReplicationHandlerBackup.runBackupCommand(
          leaderJetty, ReplicationHandler.CMD_RESTORE, params);

      while (!TestRestoreCoreUtil.fetchRestoreStatus(baseUrl, DEFAULT_TEST_CORENAME)) {
        Thread.sleep(1000);
      }

      // See if restore was successful by checking if all the docs are present again
      BackupRestoreUtils.verifyDocs(nDocs, leaderClient, DEFAULT_TEST_CORENAME);
    }
  }

  /**
   * A standalone restore replaces the whole index, so the update log must be left empty: a doc added
   * after the backup (and only present in the tlog / realtime view) must not be visible to
   * realtime-get after the restore, and a restart must not replay it.
   */
  @Test
  public void testRestoreClearsUpdateLog() throws Exception {
    // this test needs an update log (which in turn needs a schema with _version_) and RTG
    leaderClient.close();
    leaderJetty.stop();
    leader.copyConfigFile(
        CONF_DIR.resolve("solrconfig-leader-ulog.xml").toString(), "solrconfig.xml");
    leader.copyConfigFile(CONF_DIR.resolve("schema.xml").toString(), "schema.xml");
    leaderJetty = createAndStartJetty(leader);
    leaderClient = leaderJetty.getSolrClient();

    final String snapshotName = "ulogsnap";
    final String params = "&name=" + snapshotName;
    final String baseUrl = leaderJetty.getBaseUrl().toString();

    final SolrInputDocument before = new SolrInputDocument();
    before.addField("id", "before-backup");
    leaderClient.add(DEFAULT_TEST_CORENAME, before);
    leaderClient.commit(DEFAULT_TEST_CORENAME);

    final BackupStatusChecker backupStatus =
        new BackupStatusChecker(leaderClient, "/" + DEFAULT_TEST_CORENAME + "/replication");
    TestReplicationHandlerBackup.runBackupCommand(
        leaderJetty, ReplicationHandler.CMD_BACKUP, params);
    backupStatus.waitForBackupSuccess(snapshotName, 30);

    // added after the backup and deliberately not committed: it lives only in the update log
    final SolrInputDocument after = new SolrInputDocument();
    after.addField("id", "after-backup");
    leaderClient.add(DEFAULT_TEST_CORENAME, after);
    assertNotNull(
        "doc should be visible to realtime-get before the restore",
        leaderClient.getById(DEFAULT_TEST_CORENAME, "after-backup"));

    TestReplicationHandlerBackup.runBackupCommand(
        leaderJetty, ReplicationHandler.CMD_RESTORE, params);
    while (!TestRestoreCoreUtil.fetchRestoreStatus(baseUrl, DEFAULT_TEST_CORENAME)) {
      Thread.sleep(1000);
    }

    assertNotNull(
        "doc from the backup should still be there",
        leaderClient.getById(DEFAULT_TEST_CORENAME, "before-backup"));
    assertNull(
        "update log should have been cleared by the restore",
        leaderClient.getById(DEFAULT_TEST_CORENAME, "after-backup"));
  }

  public void testBackupFailsMissingAllowPaths() {
    final String params =
        "&location=" + URLEncoder.encode(createTempDir().toString(), StandardCharsets.UTF_8);
    Throwable t =
        expectThrows(
            IOException.class,
            () -> {
              TestReplicationHandlerBackup.runBackupCommand(
                  leaderJetty, ReplicationHandler.CMD_BACKUP, params);
            });
    // The backup command will fail since the tmp dir is outside allowPaths
    assertTrue(t.getMessage().contains("Server returned HTTP response code: 400"));
  }

  @Test
  public void testFailedRestore() throws Exception {
    int nDocs = BackupRestoreUtils.indexDocs(leaderClient, "collection1", docsSeed);

    String location = createTempDir().toString();
    leaderJetty.getCoreContainer().getAllowPaths().add(Path.of(location));
    String snapshotName = TestUtil.randomSimpleString(random(), 1, 5);
    String params =
        "&name="
            + snapshotName
            + "&location="
            + URLEncoder.encode(location, StandardCharsets.UTF_8);
    String baseUrl = leaderJetty.getBaseUrl().toString();

    TestReplicationHandlerBackup.runBackupCommand(
        leaderJetty, ReplicationHandler.CMD_BACKUP, params);

    final BackupStatusChecker backupStatus =
        new BackupStatusChecker(leaderClient, "/" + DEFAULT_TEST_CORENAME + "/replication");
    final String backupDirName = backupStatus.waitForBackupSuccess(snapshotName, 30);

    // Remove the segments_n file so that the backup index is corrupted.
    // Restore should fail, and it should automatically roll back to the original index.
    final Path restoreIndexPath = Path.of(location, backupDirName);
    assertTrue("Does not exist: " + restoreIndexPath, Files.exists(restoreIndexPath));
    try (DirectoryStream<Path> stream =
        Files.newDirectoryStream(restoreIndexPath, IndexFileNames.SEGMENTS + "*")) {
      Path segmentFileName = stream.iterator().next();
      Files.delete(segmentFileName);
    }

    TestReplicationHandlerBackup.runBackupCommand(
        leaderJetty, ReplicationHandler.CMD_RESTORE, params);

    expectThrows(
        AssertionError.class,
        () -> {
          for (int i = 0; i < 10; i++) {
            // this will throw an assertion once we get what we expect
            TestRestoreCoreUtil.fetchRestoreStatus(baseUrl, DEFAULT_TEST_CORENAME);
            Thread.sleep(50);
          }
          // if we never got an assertion let expectThrows complain
        });

    BackupRestoreUtils.verifyDocs(nDocs, leaderClient, DEFAULT_TEST_CORENAME);

    // make sure we can write to the index again
    nDocs = BackupRestoreUtils.indexDocs(leaderClient, "collection1", docsSeed);
    BackupRestoreUtils.verifyDocs(nDocs, leaderClient, DEFAULT_TEST_CORENAME);
  }
}
