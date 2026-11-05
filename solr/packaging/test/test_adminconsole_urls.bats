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
}

teardown() {
  # save a snapshot of SOLR_HOME for failed tests
  save_home_on_failure

  solr stop --all >/dev/null 2>&1
}


@test "assert able to launch solr admin console" {
  run solr start

  run curl -s -o /dev/null -w "%{http_code}" http://localhost:${SOLR_PORT}/solr/
  assert_output "200"

  run curl -s -I http://localhost:${SOLR_PORT}/solr/
  assert_output --partial "text/html"

  run curl -s http://localhost:${SOLR_PORT}/solr/
  assert_output --partial "<html"

  run curl -s -o /dev/null -w "%{http_code}" http://localhost:${SOLR_PORT}/solr/css/angular/chosen.css
  assert_output "200"

  run curl -s -I http://localhost:${SOLR_PORT}/solr/css/angular/chosen.css
  assert_output --partial "text/css"

  run curl -s http://localhost:${SOLR_PORT}/solr/css/angular/chosen.css
  assert_output --partial "{"
}

@test "assert admin console static assets require revalidation" {
  run solr start
  assert_success

  for asset in libs/solr/index.js css/angular/chosen.css; do
    run curl -fsS --head "http://localhost:${SOLR_PORT}/solr/${asset}"
    assert_success
    assert_line --regexp '^HTTP/[0-9.]+ 200([[:space:]]|$)'
    assert_line --regexp '^[Cc]ache-[Cc]ontrol: no-cache[[:space:]]*$'
  done
}

@test "assert admin console static assets support conditional requests" {
  run solr start
  assert_success

  local asset_url="http://localhost:${SOLR_PORT}/solr/libs/solr/index.js"
  local headers="${BATS_TEST_TMPDIR}/asset.headers"
  local original="${BATS_TEST_TMPDIR}/original.js"
  local revalidated="${BATS_TEST_TMPDIR}/revalidated.js"
  local stale="${BATS_TEST_TMPDIR}/stale.js"

  run curl -fsS --dump-header "${headers}" --output "${original}" --write-out '%{http_code}' "${asset_url}"
  assert_success
  assert_output "200"
  assert [ -s "${original}" ]

  run cat "${headers}"
  assert_success
  assert_line --regexp '^[Cc]ache-[Cc]ontrol: no-cache[[:space:]]*$'

  run awk 'tolower($1) == "last-modified:" {sub(/^[^:]*: */, ""); sub(/\r$/, ""); print}' "${headers}"
  assert_success
  assert_output --regexp '^[A-Z][a-z]{2}, [0-9]{2} [A-Z][a-z]{2} [0-9]{4} [0-9]{2}:[0-9]{2}:[0-9]{2} GMT$'
  local last_modified="${output}"

  run curl -fsS --header "If-Modified-Since: ${last_modified}" --dump-header "${headers}" --output "${revalidated}" --write-out '%{http_code}' "${asset_url}"
  assert_success
  assert_output "304"
  assert [ ! -s "${revalidated}" ]

  run cat "${headers}"
  assert_success
  assert_line --regexp '^[Cc]ache-[Cc]ontrol: no-cache[[:space:]]*$'

  run curl -fsS --header 'If-Modified-Since: Thu, 01 Jan 1970 00:00:00 GMT' --output "${stale}" --write-out '%{http_code}' "${asset_url}"
  assert_success
  assert_output "200"

  run cmp "${original}" "${stale}"
  assert_success
}

@test "assert admin console references a versioned client asset" {
  run solr start
  assert_success

  local page="${BATS_TEST_TMPDIR}/index.html"
  run curl -fsS --output "${page}" --write-out '%{http_code}' "http://localhost:${SOLR_PORT}/solr/"
  assert_success
  assert_output "200"

  run cat "${page}"
  assert_success
  assert_output --partial "<html"
  assert_output --regexp 'src="libs/solr/index[.]js[?]_=[^"[:space:]]+"'
  refute_output --partial '${version}'
  refute_output --regexp '(src|href)="[^"]+\.(js|css)"'

  run sed -n 's/.*src="\(libs\/solr\/index\.js?_[^"]*\)".*/\1/p' "${page}"
  assert_success
  assert_output --regexp '^libs/solr/index[.]js[?]_=[^"[:space:]]+$'
  local client_ref="${output}"

  run curl -fsS --output /dev/null --write-out '%{http_code}' "http://localhost:${SOLR_PORT}/solr/${client_ref}"
  assert_success
  assert_output "200"
}

@test "assert CSP header contains custom connect src URLs" {
  # Set custom CSP connect-src URLs via system property
  local csp_urls="http://example1.com/token,https://example2.com/path/uri1,http://example3.com/oauth2/uri2"

  run solr start -Dsolr.ui.headers.csp.connect-src.urls="${csp_urls}"

  run curl -s -I http://localhost:${SOLR_PORT}/solr/
  assert_output --partial "http://example1.com/token"
  assert_output --partial "https://example2.com/path/uri1"
  assert_output --partial "http://example3.com/oauth2/uri2"
}
