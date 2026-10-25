# SOLR-17786 / PR #5108 gate preflight blocked

**Disposition: BLOCKED before claim and test gate.** This is a preflight blocker, not a gate verdict. The original gate record remains unchanged and OPEN. No claim or receipt was created. No compile, positive test, mutation test, or module check ran; no PR, Jira, or public comment/state was changed.

## Verified input and board state

- Fork branch `solr-17786-reverse-cursor-submit` was `7ba3b78c45c1c2c59225426ec93e975f1c76f24e`, exactly the pinned gate head. Its sole parent was `32f80171030ca1108cd20292f50232d2729ed930`, exactly the gate's pinned parent.
- Apache Solr PR #5108 was open and unmerged, with live head `32f80171030ca1108cd20292f50232d2729ed930` (`https://api.github.com/repos/apache/solr/pulls/5108`; `refs/pull/5108/head` matched).
- On refreshed `coordination-ui`, `gates/pr-5108-reverse-cursor-round-1.md` was OPEN. No matching SOLR-17786/PR #5108 claim, receipt, or prior gate log was present. No competing gate claim by `phone-agent-2` was present.

## Preflight outcome

The prescribed detached runner was `pool/bin/gate-preflight.sh` at the exact pinned head. Attempt 1 stopped at fetch setup before Gradle because `/workspace/.env` was absent; the runner recorded `fetch failed for 7ba3b78c45c1c2c59225426ec93e975f1c76f24e`. Attempt 2 used the available GitHub credential through the process environment and reached the candidate. The runner's `tidy` command completed successfully as a Gradle task, but returned preflight status 3 because it changed five files in its disposable checkout.

The patch contains formatting-only line wrapping in:

- `solr/core/src/java/org/apache/solr/search/CursorMark.java`
- `solr/core/src/test/org/apache/solr/CursorPagingTest.java`
- `solr/core/src/test/org/apache/solr/cloud/DistribCursorPagingTest.java`
- `solr/core/src/test/org/apache/solr/search/CursorMarkTest.java`
- `solr/solrj/src/java/org/apache/solr/common/params/CursorMarkParams.java`

`preflight-tidy.log` records `BUILD SUCCESSFUL` in 1m 7s and 62 actionable tasks (62 executed); this is formatting preflight only, not compile or test evidence. The patch is 73 insertions and 33 deletions. SHA-256: `4fb6ce9e71d5ddb1c5cc3d8acf20ecce56d16a872b2828ddb7c7eb6c095f3710`.

Immediately before each preflight launch, fresh `df -Pi` showed 25.24% free inodes (211,369 free) on `/`, `/workspace/tmp`, and `/home/ubuntu/.gradle`; `/` had 31 GB available. No Gradle process was active and the `phone-agent-2` lock was available. After the preflight, no Gradle process remained, the lock was available, and `/workspace/solr` remained clean at the pinned remote-tracking head.

## Why execution stopped

The gate record requires that any `tidy` changes be applied as a ticket-scoped formatting commit, pushed to the fork, and followed by a new add-only gate job pinned to the resulting head. This task explicitly excludes product-code repair or branch changes. Therefore the patch was not applied, no source head was changed, and no claim was created; the board workflow requires the detached tidy preflight before claiming. The gate remains OPEN and uncompleted pending a separately authorized formatting repair and superseding gate, or other owner direction.

## Preserved evidence

All evidence is committed alongside this report under `receipts/SOLR-17786-gate-round-1/`:

- Initial fetch failure: `preflight.log` (SHA-256 `ae2e56ee6778ec56b54994e4f970cd4f2066323781134203dbf78a77c16d18de`).
- Attempt 2 status, wrapper output, full tidy log, and unapplied `tidy.patch`: `preflight-attempt-2/preflight.log`, `preflight-attempt-2/preflight-wrapper.log`, `preflight-attempt-2/preflight-tidy.log`, and `preflight-attempt-2/tidy.patch`.
- Attempt 2 tidy-log SHA-256: `00fb29789dd7c785c107cf53b02dd9ef4fb927aedd7a609c716514afbdb051f0`; attempt 2 patch SHA-256: `4fb6ce9e71d5ddb1c5cc3d8acf20ecce56d16a872b2828ddb7c7eb6c095f3710`.
