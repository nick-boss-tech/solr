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

import java.util.Arrays;
import java.util.List;
import org.apache.lucene.util.Version;
import org.apache.solr.SolrTestCase;
import org.junit.Test;

/** Unit tests for {@link BackupCmd}. */
public class BackupCmdTest extends SolrTestCase {

  @Test
  public void testMinIndexVersionPicksOldestAcrossShards() throws Exception {
    Version expected = Version.parse("8.10.1");
    assertEquals(expected, BackupCmd.minIndexVersion(List.of("8.11.0", "8.10.1", "9.0.0")));
    // The result must not depend on the order the shard responses arrived in.
    assertEquals(expected, BackupCmd.minIndexVersion(List.of("9.0.0", "8.10.1", "8.11.0")));
    assertEquals(expected, BackupCmd.minIndexVersion(List.of("8.10.1", "8.10.1")));
  }

  @Test
  public void testMinIndexVersionIgnoresUnparseableValues() throws Exception {
    assertEquals(
        Version.parse("9.1.0"), BackupCmd.minIndexVersion(List.of("not-a-version", "9.1.0")));
    // Nothing parseable means no shard version to record; the caller keeps its default.
    assertNull(BackupCmd.minIndexVersion(List.of("not-a-version")));
  }

  @Test
  public void testMinIndexVersionIgnoresAbsentValues() throws Exception {
    assertEquals(
        Version.parse("10.0.0"), BackupCmd.minIndexVersion(Arrays.asList(null, "10.0.0", null)));
    assertNull(BackupCmd.minIndexVersion(Arrays.asList((String) null)));
    assertNull(BackupCmd.minIndexVersion(List.of()));
  }

  @Test
  public void testMinIndexVersionSingleShard() throws Exception {
    assertEquals(Version.parse("10.4.0"), BackupCmd.minIndexVersion(List.of("10.4.0")));
  }
}
