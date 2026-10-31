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

package org.apache.solr.util;

import static org.apache.solr.SolrTestCaseJ4.assumeWorkingMockito;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.PullImageCmd;
import com.github.dockerjava.api.command.PullImageResultCallback;
import com.github.dockerjava.api.exception.DockerClientException;
import com.github.dockerjava.api.exception.NotFoundException;
import java.io.Closeable;
import java.time.Duration;
import java.util.List;
import org.apache.solr.SolrTestCase;
import org.junit.Before;
import org.junit.BeforeClass;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.ImageNameSubstitutor;

/** Exercises the architecture probe without a Docker daemon or registry. */
public class TestcontainersUtilTest extends SolrTestCase {

  private static final String IMAGE = "apache/kafka:4.3.1";
  private static final String MISSING_PLATFORM =
      "no matching manifest for linux/s390x in the manifest list entries";
  private static final Duration COMPLETED_PULL_TIMEOUT = Duration.ofSeconds(1);

  private DockerClient client;
  private PullImageCmd pull;
  private ImageNameSubstitutor substitutor;

  @BeforeClass
  public static void ensureWorkingMockito() {
    assumeWorkingMockito();
  }

  @Before
  public void preparePull() {
    client = mock(DockerClient.class);
    pull = mock(PullImageCmd.class);
    substitutor = mock(ImageNameSubstitutor.class);
    when(substitutor.apply(any(DockerImageName.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));
    when(client.pullImageCmd("apache/kafka")).thenReturn(pull);
    when(pull.withTag("4.3.1")).thenReturn(pull);
    when(pull.withPlatform("linux/s390x")).thenReturn(pull);
  }

  public void testSuccessfulPull() {
    when(pull.exec(any(PullImageResultCallback.class)))
        .thenAnswer(
            invocation -> {
              PullImageResultCallback callback = invocation.getArgument(0);
              callback.onComplete();
              return callback;
            });

    assertTrue(probe(COMPLETED_PULL_TIMEOUT));
    verify(pull).withPlatform("linux/s390x");
  }

  public void testMissingPlatform() {
    for (RuntimeException error :
        List.of(
            new DockerClientException("Could not pull image: " + MISSING_PLATFORM),
            new NotFoundException(MISSING_PLATFORM))) {
      doThrow(error).when(pull).exec(any(PullImageResultCallback.class));
      assertFalse(probe(COMPLETED_PULL_TIMEOUT));
    }
  }

  public void testRegistryFailuresPropagate() {
    for (RuntimeException error :
        List.of(
            new DockerClientException("unauthorized: authentication required"),
            new DockerClientException("toomanyrequests: pull rate limit exceeded"),
            new DockerClientException("connection reset"),
            new NotFoundException("manifest unknown"))) {
      doThrow(error).when(pull).exec(any(PullImageResultCallback.class));
      assertSame(error, expectThrows(RuntimeException.class, () -> probe(COMPLETED_PULL_TIMEOUT)));
    }
  }

  public void testOtherPlatformMismatchPropagates() {
    var error =
        new NotFoundException(
            "no matching manifest for linux/amd64 in the manifest list entries");
    when(pull.exec(any(PullImageResultCallback.class))).thenThrow(error);
    assertSame(error, expectThrows(NotFoundException.class, () -> probe(COMPLETED_PULL_TIMEOUT)));
  }

  public void testCallbackFailurePropagates() {
    var error = new DockerClientException("registry connection reset");
    when(pull.exec(any(PullImageResultCallback.class)))
        .thenAnswer(
            invocation -> {
              PullImageResultCallback callback = invocation.getArgument(0);
              callback.onError(error);
              return callback;
            });

    assertSame(
        error, expectThrows(DockerClientException.class, () -> probe(COMPLETED_PULL_TIMEOUT)));
  }

  public void testUnexpectedFailurePropagates() {
    var error = new IllegalArgumentException("invalid pull configuration");
    when(pull.exec(any(PullImageResultCallback.class))).thenThrow(error);
    assertSame(
        error, expectThrows(IllegalArgumentException.class, () -> probe(COMPLETED_PULL_TIMEOUT)));
  }

  public void testTimeoutFailsAndClosesPull() throws Exception {
    Closeable stream = mock(Closeable.class);
    startUnfinishedPull(stream);

    var error = expectThrows(IllegalStateException.class, () -> probe(Duration.ZERO));
    assertTrue(error.getMessage().contains("Timed out pulling " + IMAGE));
    verify(stream).close();
  }

  public void testInterruptedPullFailsAndPreservesInterrupt() throws Exception {
    Closeable stream = mock(Closeable.class);
    startUnfinishedPull(stream);

    Thread.currentThread().interrupt();
    try {
      var error = expectThrows(IllegalStateException.class, () -> probe(COMPLETED_PULL_TIMEOUT));
      assertTrue(error.getCause() instanceof InterruptedException);
      assertTrue(Thread.currentThread().isInterrupted());
    } finally {
      Thread.interrupted();
    }
    verify(stream).close();
  }

  public void testImageSubstitution() {
    DockerImageName mirror = DockerImageName.parse("registry.example/mirror/apache/kafka:4.3.1");
    when(substitutor.apply(any(DockerImageName.class))).thenReturn(mirror);
    when(client.pullImageCmd("registry.example/mirror/apache/kafka")).thenReturn(pull);
    when(pull.exec(any(PullImageResultCallback.class)))
        .thenAnswer(
            invocation -> {
              PullImageResultCallback callback = invocation.getArgument(0);
              callback.onComplete();
              return callback;
            });

    assertTrue(probe(COMPLETED_PULL_TIMEOUT));
    verify(client).pullImageCmd("registry.example/mirror/apache/kafka");
    verify(pull).withTag("4.3.1");
  }

  private void startUnfinishedPull(Closeable stream) {
    when(pull.exec(any(PullImageResultCallback.class)))
        .thenAnswer(
            invocation -> {
              PullImageResultCallback callback = invocation.getArgument(0);
              callback.onStart(stream);
              return callback;
            });
  }

  private boolean probe(Duration timeout) {
    return TestcontainersUtil.hasS390xVariant(IMAGE, client, substitutor, timeout);
  }
}
