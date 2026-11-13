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

# bats file_tags=nightly

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
}

@test "secure example starts two HTTPS nodes with authentication and ZooKeeper ACLs" {
  mkdir -p "$SOLR_HOME/node1"
  printf 'left over\n' > "$SOLR_HOME/node1/incomplete-file"
  run env SOLR_ULIMIT_CHECKS=true solr start -e secure --no-prompt --solr-home "$SOLR_HOME" -p "$SOLR_PORT"
  assert_success
  assert_output --partial 'Two-node secure example is ready'
  assert_output --partial "http://localhost:$SOLR_PORT/api/node/health"
  assert_output --partial "http://localhost:$SOLR2_PORT/api/node/health"
  refute_output --partial 'forcefully killing it now'
  refute_output --partial 'set SOLR_ULIMIT_CHECKS to false'
  assert_output --partial 'Check status: SOLR_INCLUDE='

  password=$(echo "$output" | grep -o 'Generated password: [A-Za-z0-9]*' | awk '{print $NF}')
  [ -n "$password" ]

  run ls -a "$SOLR_HOME"
  assert_success
  refute_output --partial '.secure-example'
  run ls -a "$SOLR_HOME/node1"
  assert_success
  refute_output --partial 'incomplete-file'

  run cat "$SOLR_INCLUDE"
  assert_output ''

  run curl --noproxy '*' --silent --show-error --fail --cacert "$SOLR_HOME/tls/cert.pem" \
    --user "admin:$password" "https://localhost:$SOLR_PORT/api/cluster/security/authentication"
  assert_success
  assert_output --partial '"admin"'
  refute_output --partial '"solr"'

  for port in "$SOLR_PORT" "$SOLR2_PORT"; do
    run curl --noproxy '*' --silent --show-error --fail --cacert "$SOLR_HOME/tls/cert.pem" \
      --user "search:$password" "https://localhost:$port/solr/techproducts/select?q=*:*&rows=0&shards.info=true"
    assert_success
    assert_output --partial '"numFound":31'
    assert_output --partial "https://127.0.0.1:$SOLR_PORT/"
    assert_output --partial "https://127.0.0.1:$SOLR2_PORT/"
    refute_output --partial 'http://'

    run curl --noproxy '*' --silent --show-error --fail --max-time 5 "https://localhost:$port/api/node/health"
    assert_failure
    assert_output --partial 'certificate'

    run curl --noproxy '*' --silent --show-error --fail --cacert "$SOLR_HOME/tls/cert.pem" \
      "https://localhost:$port/api/node/health"
    assert_failure
    assert_output --partial '401'

    run curl --noproxy '*' --silent --show-error --fail --cacert "$SOLR_HOME/tls/cert.pem" \
      --user "solr:$password" "https://localhost:$port/api/node/health"
    assert_failure
    assert_output --partial '401'

    run curl --noproxy '*' --silent --show-error --fail --cacert "$SOLR_HOME/tls/cert.pem" \
      --user "admin:$password" "https://localhost:$port/solr/techproducts/select?q=*:*"
    assert_failure
    assert_output --partial '403'

    run curl --noproxy '*' --silent --show-error --fail --cacert "$SOLR_HOME/tls/cert.pem" \
      --user "admin:$password" -H 'Content-Type: application/json' --data-binary '[{"id":"forbidden-admin"}]' \
      "https://localhost:$port/solr/techproducts/update"
    assert_failure
    assert_output --partial '403'

    run curl --noproxy '*' --silent --show-error --fail --cacert "$SOLR_HOME/tls/cert.pem" \
      --user "search:$password" -H 'Content-Type: application/json' --data-binary '[{"id":"forbidden"}]' \
      "https://localhost:$port/solr/techproducts/update"
    assert_failure
    assert_output --partial '403'

    run curl --noproxy '*' --silent --show-error --fail --max-time 5 "http://localhost:$port/api/node/health"
    assert_failure
  done

  run env SOLR_INCLUDE="$SOLR_HOME/tls/solr.in.sh" solr zk ls / --zk-host "localhost:$ZK_PORT"
  assert_success
  assert_output --partial 'collections'

  # The Admin UI's ZK status page (ZookeeperStatusHandler) talks to ZooKeeper over a raw
  # socket with four-letter-word commands, separately from the SolrZkClient used above;
  # confirm it also works against the now TLS-only embedded ZooKeeper.
  run curl --noproxy '*' --silent --show-error --fail --cacert "$SOLR_HOME/tls/cert.pem" \
    --user "admin:$password" "https://localhost:$SOLR_PORT/solr/admin/zookeeper/status?wt=json"
  assert_success
  assert_output --partial '"ok":true'
  assert_output --partial '"status":"green"'

  # bin/solr writes SOLR_ZK_TLS_OPTS into the include file automatically (because
  # SOLR_SSL_ENABLED=true and node 1 started its own embedded ZooKeeper); read it back
  # rather than hardcoding its keystore/password-file paths here.
  local tls_opts
  tls_opts=$(grep '^SOLR_ZK_TLS_OPTS=' "$SOLR_HOME/tls/solr.in.sh" | sed -E 's/^SOLR_ZK_TLS_OPTS="(.*)"$/\1/')
  [ -n "$tls_opts" ]

  # Embedded ZooKeeper's clientPort is TLS-only; a client that doesn't speak TLS at all can't
  # even reach the ACL check, so it times out instead of getting a clean rejection.
  run env SOLR_INCLUDE=/dev/null SOLR_ZK_CREDS_AND_ACLS= solr zk ls / --zk-host "localhost:$ZK_PORT"
  assert_failure
  assert_output --partial 'Timeout while waiting for Zookeeper Client to connect'

  run env SOLR_INCLUDE=/dev/null SOLR_ZK_CREDS_AND_ACLS="$tls_opts" \
    solr zk ls / --zk-host "localhost:$ZK_PORT"
  assert_failure
  assert_output --partial 'NoAuth'

  local readonly_opts="$tls_opts -DzkCredentialsInjector=org.apache.solr.common.cloud.VMParamsZkCredentialsInjector -DzkCredentialsProvider=org.apache.solr.common.cloud.DigestZkCredentialsProvider -DzkDigestUsername=zk-readonly -DzkDigestPassword=$password"
  run env SOLR_INCLUDE=/dev/null SOLR_ZK_CREDS_AND_ACLS="$readonly_opts" \
    solr zk ls /collections --zk-host "localhost:$ZK_PORT"
  assert_success
  assert_output --partial 'techproducts'
  run env SOLR_INCLUDE=/dev/null SOLR_ZK_CREDS_AND_ACLS="$readonly_opts" \
    solr zk cp zk:/security.json "$BATS_TEST_TMPDIR/security.json" --zk-host "localhost:$ZK_PORT"
  assert_failure
  assert_output --partial 'NoAuth'
  run env SOLR_INCLUDE=/dev/null SOLR_ZK_CREDS_AND_ACLS="$readonly_opts" \
    solr zk mkroot /forbidden --zk-host "localhost:$ZK_PORT"
  assert_failure
  assert_output --partial 'NoAuth'
}

@test "secure example preserves existing home when interactive input ends" {
  printf 'preserve me\n' > "$SOLR_HOME/existing-file"
  run bash -c 'exec solr start -e secure --solr-home "$SOLR_HOME" -p "$SOLR_PORT" </dev/null'
  assert_failure
  assert_output --partial 'Press Enter to delete and recreate'
  [ "$(cat "$SOLR_HOME/existing-file")" = 'preserve me' ]
}

@test "secure example preserves existing home when reset is declined" {
  printf 'preserve me\n' > "$SOLR_HOME/existing-file"
  for response in 'no' '  ' '""'; do
    run bash -c 'printf "%s\n" "$1" | solr start -e secure --solr-home "$SOLR_HOME" -p "$SOLR_PORT"' _ "$response"
    assert_success
    assert_output --partial 'Secure example reset cancelled.'
    [ "$(cat "$SOLR_HOME/existing-file")" = 'preserve me' ]
  done
}

@test "secure example resets existing home when Enter confirms" {
  printf 'remove me\n' > "$SOLR_HOME/existing-file"
  run bash -c 'printf "\n" | solr start -e secure --solr-home "$SOLR_HOME" -p "$SOLR_PORT"'
  assert_success
  assert_output --partial 'Two-node secure example is ready'
  [ ! -e "$SOLR_HOME/existing-file" ]
}

@test "secure example rejects unsupported options" {
  run solr start -e secure --no-prompt --solr-home "$SOLR_HOME" --host other-host
  assert_failure
  assert_output --partial 'host'
  run solr start -e secure --no-prompt --solr-home "$SOLR_HOME" --user-managed
  assert_failure
  assert_output --partial 'requires its own embedded ZooKeeper'
}

@test "automatic embedded ZooKeeper TLS disables existing plaintext ports and port unification" {
  local store="$BATS_TEST_TMPDIR/zk-test.p12"
  run keytool -genkeypair -alias solr-ssl -keyalg RSA -keysize 2048 \
    -storepass secret -keypass secret -validity 30 -keystore "$store" -storetype PKCS12 \
    -ext SAN=DNS:localhost,IP:127.0.0.1 -dname 'CN=localhost'
  assert_success

  export SOLR_SSL_ENABLED=true SOLR_SSL_KEY_STORE="$store" SOLR_SSL_TRUST_STORE="$store"
  export SOLR_SSL_KEY_STORE_PASSWORD=secret SOLR_SSL_TRUST_STORE_PASSWORD=secret
  export SOLR_SSL_KEY_STORE_TYPE=PKCS12 SOLR_SSL_TRUST_STORE_TYPE=PKCS12
  export SOLR_HOST_ADVERTISE=localhost
  cp "$SOLR_TIP/server/solr/solr.xml" "$SOLR_HOME/solr.xml"
  cp "$SOLR_TIP/server/solr/zoo.cfg" "$SOLR_HOME/zoo.cfg"
  cat >> "$SOLR_HOME/zoo.cfg" <<EOF
clientPort=$((ZK_PORT + 1))
secureClientPort=$ZK_PORT
serverCnxnFactory=org.apache.zookeeper.server.NettyServerCnxnFactory
ssl.keyStore.location=$store
ssl.keyStore.type=PKCS12
ssl.keyStore.passwordPath=$SOLR_HOME/.zk-tls-keystore-password
ssl.trustStore.location=$store
ssl.trustStore.type=PKCS12
ssl.trustStore.passwordPath=$SOLR_HOME/.zk-tls-truststore-password
ssl.clientAuth=none
client.portUnification=true
EOF

  # An unusable password path must abort before launching Solr.
  mkdir "$SOLR_HOME/.zk-tls-keystore-password"
  run solr start -c -p "$SOLR_PORT" --solr-home "$SOLR_HOME"
  assert_failure
  assert_output --partial 'Unable to write embedded ZooKeeper TLS password files'
  rmdir "$SOLR_HOME/.zk-tls-keystore-password"
  rm -f "$SOLR_HOME/.zk-tls-truststore-password"
  [ ! -e "$SOLR_HOME/.zk-tls-keystore-password" ]
  [ ! -e "$SOLR_HOME/.zk-tls-truststore-password" ]

  # Inspect the file immediately after creation, before the script can chmod or write it.
  local intercept_dir="$BATS_TEST_TMPDIR/touch-intercept"
  mkdir "$intercept_dir"
  cat > "$intercept_dir/touch" <<'EOF'
#!/usr/bin/env bash
command -p touch "$@" || exit $?
if [[ "$1" == */.zk-tls-keystore-password ]]; then
  [[ $(ls -ld "$1") == "-rw------- "* ]] || exit 1
  [[ $(ls -ld "$2") == "-rw------- "* ]] || exit 1
  printf 'private at creation\n' > "$ZK_TOUCH_PROOF"
fi
EOF
  chmod +x "$intercept_dir/touch"
  run env PATH="$intercept_dir:$PATH" ZK_TOUCH_PROOF="$BATS_TEST_TMPDIR/touch-proof" \
    bash -c 'umask 022; exec solr start -c -p "$SOLR_PORT" --solr-home "$SOLR_HOME"'
  assert_success
  [ -f "$BATS_TEST_TMPDIR/touch-proof" ]
  for password_file in "$SOLR_HOME/.zk-tls-keystore-password" "$SOLR_HOME/.zk-tls-truststore-password"; do
    run ls -ld "$password_file"
    assert_success
    assert_output --regexp '^-rw------- '
  done

  run solr zk ls / --zk-host "localhost:$ZK_PORT"
  assert_success
  run env SOLR_INCLUDE=/dev/null SOLR_ZK_CREDS_AND_ACLS= SOLR_ZK_TLS_OPTS= \
    solr zk ls / --zk-host "localhost:$ZK_PORT"
  assert_failure
  assert_output --partial 'Timeout while waiting for Zookeeper Client to connect'
  run bash -c 'exec 3<>"/dev/tcp/127.0.0.1/$((ZK_PORT + 1))"'
  assert_failure
}
