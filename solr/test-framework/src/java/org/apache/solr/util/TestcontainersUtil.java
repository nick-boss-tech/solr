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

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.PullImageResultCallback;
import com.github.dockerjava.api.exception.DockerClientException;
import com.github.dockerjava.api.exception.NotFoundException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.junit.Assume;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.ImageNameSubstitutor;

/** Utility methods for Testcontainers-based tests. */
public final class TestcontainersUtil {

  private static final String S390X = "s390x";
  private static final Duration S390X_PULL_TIMEOUT = Duration.ofMinutes(5);

  private TestcontainersUtil() {}

  /**
   * Assumes Docker/Testcontainers is available and, on s390x, that the specified image supports
   * linux/s390x. Unavailable Docker or a missing platform skips the calling test via {@link Assume};
   * other pull failures propagate.
   *
   * @param dockerImage the Docker image to check
   */
  public static void assumeContainerAvailable(String dockerImage) {
    Assume.assumeTrue(
        "Docker/Testcontainers not available; skipping test",
        DockerClientFactory.instance().isDockerAvailable());
    if (S390X.equalsIgnoreCase(System.getProperty("os.arch"))) {
      Assume.assumeTrue(
          dockerImage + " has no s390x variant; skipping test", hasS390xVariant(dockerImage));
    }
  }

  /**
   * Whether the image can be pulled for s390x. Only a missing platform manifest returns false;
   * unrelated pull failures, timeouts and interruptions fail the test.
   *
   * @param dockerImage the Docker image name
   * @return true if the pull succeeds, false if its manifest has no s390x variant
   */
  public static boolean hasS390xVariant(String dockerImage) {
    return hasS390xVariant(
        dockerImage,
        DockerClientFactory.instance().client(),
        ImageNameSubstitutor.instance(),
        S390X_PULL_TIMEOUT);
  }

  static boolean hasS390xVariant(
      String dockerImage,
      DockerClient client,
      ImageNameSubstitutor imageNameSubstitutor,
      Duration timeout) {
    DockerImageName imageName = imageNameSubstitutor.apply(DockerImageName.parse(dockerImage));
    try {
      boolean completed =
          client
              .pullImageCmd(imageName.getUnversionedPart())
              .withTag(imageName.getVersionPart())
              .withPlatform("linux/" + S390X)
              .exec(new PullImageResultCallback())
              .awaitCompletion(timeout.toMillis(), TimeUnit.MILLISECONDS);
      if (!completed) {
        throw new IllegalStateException("Timed out pulling " + dockerImage + " for linux/" + S390X);
      }
      return true;
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new IllegalStateException("Interrupted pulling " + dockerImage + " for linux/" + S390X, e);
    } catch (DockerClientException | NotFoundException e) {
      // Docker reports platform absence in the pull error, rather than a structured platform field.
      if (e.getMessage() != null
          && e.getMessage()
              .contains("no matching manifest for linux/" + S390X + " in the manifest list entries")) {
        return false;
      }
      throw e;
    }
  }
}
