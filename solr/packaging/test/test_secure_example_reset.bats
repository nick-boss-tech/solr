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

setup() {
  common_clean_setup
  command -v openssl >/dev/null 2>&1 || skip "OpenSSL is not available"
  export SOLR_INCLUDE="$BATS_TEST_TMPDIR/global-solr.in.sh"
  export SOLR_STOP_WAIT=15
  touch "$SOLR_INCLUDE"
}

teardown() {
  save_home_on_failure
  solr stop --all >/dev/null 2>&1 || true
  for home in "$BATS_TEST_TMPDIR/unowned" "$BATS_TEST_TMPDIR/fake-install"; do
    if [ -d "$home" ]; then
      env SOLR_PID_DIR="$home" SOLR_INCLUDE=/dev/null solr stop --all >/dev/null 2>&1 || true
    fi
  done
}

@test "secure example refuses an unowned home" {
  local home="$BATS_TEST_TMPDIR/unowned"
  mkdir -p "$home"
  printf 'keep this data\n' > "$home/sentinel"

  run solr start -e secure --no-prompt --solr-home "$home" -p "$SOLR_PORT"
  assert_failure
  assert_output --partial 'must be empty or created by the secure example'
  run cat "$home/sentinel"
  assert_success
  assert_output 'keep this data'
}

@test "secure example refuses a symlink home" {
  local home="$BATS_TEST_TMPDIR/unowned"
  mkdir -p "$home"
  printf 'keep this data\n' > "$home/sentinel"
  ln -s "$home" "$BATS_TEST_TMPDIR/example-link"

  run solr start -e secure --no-prompt --solr-home "$BATS_TEST_TMPDIR/example-link" -p "$SOLR_PORT"
  assert_failure
  assert_output --partial 'must be a dedicated directory'
  run cat "$home/sentinel"
  assert_success
  assert_output 'keep this data'
}

@test "secure example refuses a symlink ownership marker" {
  local home="$BATS_TEST_TMPDIR/unowned"
  mkdir -p "$home"
  printf 'Solr secure example\n' > "$BATS_TEST_TMPDIR/marker"
  ln -s "$BATS_TEST_TMPDIR/marker" "$home/.solr-secure-example"
  printf 'keep this data\n' > "$home/sentinel"

  run solr start -e secure --no-prompt --solr-home "$home" -p "$SOLR_PORT"
  assert_failure
  assert_output --partial 'must be empty or created by the secure example'
  run cat "$home/sentinel"
  assert_success
  assert_output 'keep this data'
}

@test "secure example refuses its installation directory" {
  local install="$BATS_TEST_TMPDIR/fake-install"
  mkdir -p "$install/bin" "$install/server"
  ln -s "$SOLR_TIP/bin/solr" "$install/bin/solr"
  printf 'Solr secure example\n' > "$install/.solr-secure-example"
  printf 'keep this data\n' > "$install/sentinel"

  run solr start -e secure --no-prompt --solr-home "$install" \
    --server-dir "$install/server" --example-dir "$SOLR_TIP/example" -p "$SOLR_PORT"
  assert_failure
  assert_output --partial 'must be a dedicated directory'
  run cat "$install/sentinel"
  assert_success
  assert_output 'keep this data'
}

