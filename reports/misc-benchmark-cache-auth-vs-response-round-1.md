# Misc benchmark: authorization vs cache validation vs response generation (round 1)

Claim: `claims/misc-benchmark-cache-auth-vs-response.md` (vm6). Item: `assignments/misc-box-round-1.md`, item 1. Measurement only: no Solr source change, no commit of the harness, nothing posted.

## Question

For a read of a core-level v2 API (`GET /api/cores/{core}/schema`), what does each step cost per request: the authorization evaluation, the HTTP cache validation that answers a conditional request with 304, and generating the full response?

## Environment

- Host: vm6, Linux 6.1 aarch64, loopback, one sequential client.
- JDK: 21.0.12.1+1 (LTS), `/workspace/jdk/jdk-21.0.12.1+1`.
- Solr: apache/solr main `13c693d805f6541f7cec684cd27d28eef7b3b614`, built with `:solr:packaging:assemble` (distribution `solr-11.0.0-SNAPSHOT`). Standalone mode (`--user-managed`), heap `-m 1g`, same flags for both conditions.
- Collection `bench`: 10,000 documents (`numFound` 10000), configset `bench_nocache`. It is a copy of `_default` with the top-level `<httpCaching never304="true" />` element removed, so the code default applies. The 200 response carries `ETag` and `Last-Modified`, so the 304 path exists.
- Harness: `scratch-bench/bench.py` in the bench worktree (uncommitted). Python 3.11, `http.client` with one keep-alive connection.

## Requests

- URL: `GET http://127.0.0.1:8983/api/cores/bench/schema`.
- Headers, both conditions: `Accept: application/json`. Authorization on: `Authorization: Basic <user bench>`, password from a local file, not printed.
- Conditional: adds `If-None-Match: "NjAwMDAwMDAwMDAwMDAwMFNvbHI="` (the ETag from the 200 response, same in both conditions).
- Every 304 was checked to carry no body; every 200 was checked for status 200.

## Method

- Warmup 200 requests per path, then 1,000 measured requests per path, for four paths.
- Latency is wall time per request in milliseconds on the client. Median and p95 use the sorted sample (p95 nearest rank).
- Authorization off: server started with no `security.json`, then measured.
- Authorization on: server stopped, `security.json` written (basic auth, rule-based authorization), server restarted with the same flags, then measured.
- No other build or test load on the host during the timed runs (load average about 0.5 before the runs).
- Raw CSV and summaries are listed below.

## Results

| Path | Authorization | Requests | Median (ms) | p95 (ms) | Response bytes |
| --- | --- | --- | --- | --- | --- |
| Full request, response generated (200) | off | 1000 | 1.594 | 2.206 | 21,683 |
| Full request, response generated (200) | on | 1000 | 1.816 | 2.552 | 21,683 |
| Conditional request, answered 304 with no body | off | 1000 | 0.386 | 0.586 | 0 |
| Conditional request, answered 304 with no body | on | 1000 | 0.463 | 0.752 | 0 |

Derived, from the medians and p95 values above (single run per condition):

| Quantity | Median (ms) | p95 (ms) |
| --- | --- | --- |
| Authorization cost on the full path (on minus off) | +0.222 (+14%) | +0.346 (+16%) |
| Authorization cost on the 304 path (on minus off) | +0.077 (+20%) | +0.166 (+28%) |
| Response generation (full minus 304), authorization off | 1.208 | 1.620 |
| Response generation (full minus 304), authorization on | 1.353 | 1.800 |

Reading the table. The 304 path is about a quarter of a full response in both conditions (0.39 of 1.59 ms, 0.46 of 1.82 ms). The authorization cost is larger on the 304 path in percent, but both costs are sub-millisecond on this host. The "response generation" row is a difference, so it also includes sending the 21.7 KB body; it is not a profiler reading.

Microbenchmark of the authorizer call alone: not done. The item allows it "if the harness allows"; this harness does not, so the authorization cost above is the end-to-end delta only.

## Authorization finding (separate from the timing)

While setting up the authorization-on condition, the same user got different results on the v1 and v2 schema reads, in every rule set tried:

- A reader-only user with a `schema-read` grant got 200 on `GET /solr/bench/schema` (v1) only for the any-role grant with no path. The scoped variants (collection `*`, collection `bench`, path `/schema`, path `/cores/*/schema`, path `/**`) gave 403 on v1 as well. On v2, `GET /api/cores/bench/schema` gave 403 in every variant tried.
- The same user got 200 on the v2 route once mapped to the catch-all `all`/`admin` role. The user needs both `reader` and `admin` in the final configuration (`user-role` `bench`: `reader`, `admin`). With `admin` alone, the `schema-read` rule was the governing permission and returned 403.

The source maps the v2 handler (`GetSchema`) to `SCHEMA_READ_PERM` (`solr/core/src/java/org/apache/solr/handler/admin/api/GetSchema.java` line 65), the same permission as v1 (`SchemaHandler.java` line 131). So the v2 result is unexpected. This is an observation from this build and these configurations. It is not a diagnosed defect, and it was not traced further in the code. It matters for anyone who grants `schema-read` to a v2 caller.

Because of this, the benchmark user holds the `reader` and `admin` roles. Every timed request was authorized, as the item requires; no unauthorized request was measured or recorded.

## Limits

- One host, one run per condition, one client thread. No variance estimate across runs.
- The authorization-off and authorization-on conditions were separate server starts with identical flags. Both ran after the same index build.
- Loopback only. No TLS (Solr warned that basic auth is sent without SSL, as expected on loopback).
- The authorization-on user holds two roles, so the rule evaluation path includes the `schema-read` rule before the `all` rule. Another rule layout would change the authorization cost.
- Microbenchmark not done.

## Raw measurement log

- On the host: `/workspace/gates/logs/vm6/misc-benchmark/raw/`.
  - `raw-auth-off-full_200_response_generated.csv`, `raw-auth-off-conditional_304_no_body.csv`, `raw-auth-on-full_200_response_generated.csv`, `raw-auth-on-conditional_304_no_body.csv`: one latency per line, 1,000 each.
  - `summary-auth-off.json`, `summary-auth-on.json`: headers used (Authorization redacted), ETag, counts.
  - `solr-server.log`: server log for the whole session, including the authorization decisions quoted above.
- Build log: `/workspace/gates/logs/vm6/misc-benchmark/build-main-retry-231515.log` (`rc=0`). The first build attempt (`build-main-230519.log`) failed in the web client's npm step because `NODE_ENV=production` was set in the shell, so `npm ci` skipped dev dependencies. The retry ran with `NODE_ENV` unset.
