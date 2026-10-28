#!/usr/bin/env bats

# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

load bats_helper

# Opt in with locally built SOLR_RELEASE_IMAGE and SOLR_SNAPSHOT_IMAGE, and set
# SOLR_RELEASE_VERSION to their numeric release (e.g. 11.0.0). The snapshot image
# must advertise that version with -SNAPSHOT in SolrVersion.LATEST, not just its tag.
setup() {
  ARTIFACT_DIR=''
  common_setup
  if [[ -z "${SOLR_RELEASE_IMAGE:-}" && -z "${SOLR_SNAPSHOT_IMAGE:-}" ]]; then
    skip "Set SOLR_RELEASE_IMAGE, SOLR_SNAPSHOT_IMAGE and SOLR_RELEASE_VERSION"
  fi

  run test -n "${SOLR_RELEASE_IMAGE:-}"
  assert_success
  run test -n "${SOLR_SNAPSHOT_IMAGE:-}"
  assert_success
  run test -n "${SOLR_RELEASE_VERSION:-}"
  assert_success
  run docker info --format '{{.ServerVersion}}'
  assert_success
  run command -v jq
  assert_success

  local resource_prefix="solr-version-$(date +%s%N)-$$"
  RELEASE_NODE="${resource_prefix}-release"
  SNAPSHOT_NODE="${resource_prefix}-snapshot"
  TEST_NETWORK="${resource_prefix}-network"
  ARTIFACT_DIR="${TEST_OUTPUT_DIR}/snapshot-cluster-version/${BATS_TEST_NUMBER}"
  mkdir -p "$ARTIFACT_DIR"

  run docker image inspect --format '{{.Id}}' "$SOLR_RELEASE_IMAGE"
  assert_success
  printf '%s\n' "$output" >"$ARTIFACT_DIR/release-image-id.txt"
  run docker image inspect --format '{{.Id}}' "$SOLR_SNAPSHOT_IMAGE"
  assert_success
  printf '%s\n' "$output" >"$ARTIFACT_DIR/snapshot-image-id.txt"
}

teardown() {
  if [[ -n "${ARTIFACT_DIR:-}" ]]; then
    for node in "$RELEASE_NODE" "$SNAPSHOT_NODE"; do
      docker logs "$node" >"$ARTIFACT_DIR/${node}.console.log" 2>&1 || true
      docker inspect "$node" >"$ARTIFACT_DIR/${node}.inspect.json" 2>&1 || true
      mkdir -p "$ARTIFACT_DIR/${node}-logs"
      docker cp "$node:/var/solr/logs/." "$ARTIFACT_DIR/${node}-logs/" 2>/dev/null || true
      docker rm --force --volumes "$node" >/dev/null 2>&1 || true
    done
    docker network rm "$TEST_NETWORK" >/dev/null 2>&1 || true
  fi
}

assert_node_version() {
  local node="$1" expected="$2"
  run docker exec "$RELEASE_NODE" solr zk cp \
    "zk:/live_nodes/${node}:8983_solr" "/tmp/${node}.json" -z "$RELEASE_NODE:9983"
  assert_success
  run docker cp "$RELEASE_NODE:/tmp/${node}.json" "$ARTIFACT_DIR/${node}.json"
  assert_success
  run jq -r '.solrVersion' "$ARTIFACT_DIR/${node}.json"
  assert_success
  assert_output "$expected"
}

replicas_are_active() {
  docker exec "$RELEASE_NODE" curl --fail --silent --show-error \
    "http://$RELEASE_NODE:8983/solr/admin/collections?action=CLUSTERSTATUS&collection=snapshot_join" \
    >"$ARTIFACT_DIR/cluster-status.json" || return
  jq -er --arg release "${RELEASE_NODE}:8983_solr" --arg snapshot "${SNAPSHOT_NODE}:8983_solr" \
    '([.cluster.collections.snapshot_join.shards.shard1.replicas[] |
       select(.state == "active") | .node_name] | sort) ==
     ([$release, $snapshot] | sort) | select(.)' "$ARTIFACT_DIR/cluster-status.json"
}

@test "A snapshot node joins its release cluster without an older-version warning" {
  run docker network create "$TEST_NETWORK"
  assert_success
  run docker run --name "$RELEASE_NODE" --network "$TEST_NETWORK" --memory=1g \
    -e SOLR_MODE=solrcloud -d "$SOLR_RELEASE_IMAGE" \
    solr start -f -m 512m --host "$RELEASE_NODE" -p 8983
  assert_success
  run docker exec "$RELEASE_NODE" solr assert \
    --started "http://$RELEASE_NODE:8983" --timeout 60000
  assert_success
  assert_node_version "$RELEASE_NODE" "$SOLR_RELEASE_VERSION"

  run docker exec "$RELEASE_NODE" solr create -c snapshot_join --shards 1 --replication-factor 1
  assert_success
  run docker exec "$RELEASE_NODE" curl --fail --silent --show-error \
    -H 'Content-Type: application/json' --data-binary '[{"id":"snapshot-join"}]' \
    "http://$RELEASE_NODE:8983/solr/snapshot_join/update?commit=true"
  assert_success

  run docker run --name "$SNAPSHOT_NODE" --network "$TEST_NETWORK" --memory=1g \
    -e SOLR_MODE=solrcloud -d "$SOLR_SNAPSHOT_IMAGE" \
    solr start -f -m 512m --host "$SNAPSHOT_NODE" -p 8983 -z "$RELEASE_NODE:9983"
  assert_success
  run docker exec "$SNAPSHOT_NODE" solr assert \
    --started "http://$SNAPSHOT_NODE:8983" --timeout 60000
  assert_success
  assert_node_version "$SNAPSHOT_NODE" "$SOLR_RELEASE_VERSION-SNAPSHOT"

  run docker exec "$RELEASE_NODE" curl --fail --silent --show-error --get \
    --data-urlencode 'action=ADDREPLICA' --data-urlencode 'collection=snapshot_join' \
    --data-urlencode 'shard=shard1' --data-urlencode "node=${SNAPSHOT_NODE}:8983_solr" \
    "http://$RELEASE_NODE:8983/solr/admin/collections"
  assert_success
  run wait_for 60 1 replicas_are_active
  assert_success
  assert_output 'true'

  run docker exec "$SNAPSHOT_NODE" curl --fail --silent --show-error \
    "http://$SNAPSHOT_NODE:8983/solr/snapshot_join/select?q=*:*&distrib=false&wt=json"
  assert_success
  printf '%s\n' "$output" >"$ARTIFACT_DIR/snapshot-query.json"
  run jq -r '.response.numFound' "$ARTIFACT_DIR/snapshot-query.json"
  assert_success
  assert_output '1'

  # Flush logging before checking absence; joining alone also succeeds with the old warning.
  run docker stop --time 60 "$SNAPSHOT_NODE"
  assert_success
  run docker inspect --format '{{.State.ExitCode}}' "$SNAPSHOT_NODE"
  assert_success
  refute_output '137'
  run docker cp "$SNAPSHOT_NODE:/var/solr/logs/solr.log" "$ARTIFACT_DIR/snapshot-solr.log"
  assert_success
  run cat "$ARTIFACT_DIR/snapshot-solr.log"
  assert_success
  refute_output --partial \
    "Our Solr version $SOLR_RELEASE_VERSION-SNAPSHOT is older than cluster version $SOLR_RELEASE_VERSION"
}
