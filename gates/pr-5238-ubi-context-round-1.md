# Gate: adjacent UBI recording context regression, round 1

Status: OPEN

## Assignment and exact revisions

- Tags: gate, implementation, review, records.
- Intended host: any available capable Linux gate host with JDK 21 and the branch's build dependencies, serialized behind its local build lock. The prior vm4 UBI contract host has useful context but no exclusive claim to this job.
- Source: Nick explicitly requested a real adjacent patch on top of the tested branch, followed by handoff to a gate agent. Windows implementation claim: `claims/pr-5238-ubi-context-windows-round-1.md`.
- Fork branch: `nick-boss-tech/solr:solr-11994-ubi-context-submit`.
- Candidate F: `09343928e7d2ba5cb5e7def1d0a550337b67a7d2`, independently verified as the fork tip after a normal push.
- Unfixed prerequisite I: `3a362dcbcb036a4cfe9a6df413057d90666cd070`.
- Existing tested example C: `c2c387064a83def34cece4163eccded28318caeb`. I's parent is C and F's parent is I. The old example branch remains unchanged.
- Author's listener head underneath C: `aa03c9544968f1d9de9b6b36e2823bcbe76268b9`.
- Real UBI source provenance: `apache/solr` draft PR 2452 at `6402b4b528d3ec8daa134772abc84532380e3f7c`. I imports only `LoggingStream.java` and `UBIComponent.java`, byte-identical in Git to that revision. No global registration, default-handler enablement, docs, fixtures, or unrelated draft changes were imported.

This is a new adjacent candidate, not a superseding proof of the test-only example. Do not overwrite `receipts/PR-5238-ubi-contract.md` or cite its green runs as proof of F.

## Premise and implementation

The real draft's component-wide StreamContext stores each request's `ubi-query`. The nested UBIQueryStream retains the context reference and fetches that key later, during read. If A stops after setStreamContext, B can replace the value and complete, then A records B's payload despite returning A's response identity. A concurrent map would not solve sharing one logical payload slot.

F creates a fresh StreamContext for every recording, copies the read-only initialization entries and preserves the container-owned SolrClientCache. The core and cache remain shared managed resources; request payload and stream context maps are private. It adds no production test seam, lock, asynchronous queue, hook change, or listener migration.

The test subclasses the real UBIComponent only to pause at its existing protected getTuple seam, after context assignment and before opening the real stream. It delegates to the actual getTuple, UBIQueryStream, and LoggingStream and checks the resulting JSONL file. It reuses cloud-minimal and ConfigRequest on a single-node, single-shard collection, with both requests reaching the same component instance. Distributed cases use shortCircuit=false and require shards.info, rather than silently taking the local shortcut. This proves coordinator processing on one shard, not multi-shard merging.

## Required proof legs

Keep stdout/stderr, exit codes, exact command lines, JDK/tool versions, fresh XML copies and output logs under a host-owned path, for example `/workspace/gates/logs/<host>/pr5238-ubi-context-round-1/`. Never reuse old XML. No Gradle was run on Windows.

1. Perform detached preflight at F according to WORKFLOW before claiming. Review I..F and C..F separately. Confirm imported-source provenance and clean `git diff --check`.
2. **Fail before:** create an isolated detached worktree at I and overlay only `solr/core/src/test/org/apache/solr/handler/component/UBIRecordingContextTest.java` from F. Do not overlay UBIComponent or LoggingStream from F. Run `:solr:core:test --tests org.apache.solr.handler.component.UBIRecordingContextTest -Ptests.seed=11994C01`. The expected result is five executed tests: three controls pass, and both concurrent tests fail specifically at `Recorded query ids must match both successful responses in recording order`, expected `[query-B, query-A]`, actual `[query-B, query-B]`. Both successful search response identities/results are asserted before this check. Capture assertion stacks and the JSONL records (or equivalent diagnostic contents). A compile failure, timeout, absent file, malformed JSON, missing shards.info, skipped test, or different assertion is not fail-before proof; investigate and repair the harness or premise first. C has no real UBI source and is not a valid negative baseline.
3. **Pass after:** on a clean F checkout run the entire UBIRecordingContextTest with seed `11994C01` and then `11994C02`, preserving separate logs/XML. Expect five tests each, zero skips/failures/errors. Tests: concurrent local, concurrent forced-distributed, sequential local/distributed, generated identity, and disabled recording. The recorded tuples must preserve query_id, user_query, and doc_ids.
4. **Existing behavior:** at the same F, run SearchRequestListenerUbiTest (expected 9 tests) and SearchRequestListenerTest (expected 19 tests), seed `11994C01`. Run neighbors TestSolrConfigHandler and TestSubQueryTransformerDistrib, same seed. All must execute with zero skips/failures/errors. Inspect actual counts; historical counts are not evidence of execution.
5. **Formatting and checks:** require clean tidy at the offered head and root `check -x test`. Unset NODE_ENV for the latter as required by this host's earlier successful proof, and record that material execution condition. Preserve the final exit code/log. This does not waive any other required root check.
6. Verify the final fork ref, worktree cleanliness, and complete test/check proof at that exact offered SHA. Confirm the listener hook production files are unchanged from C.

Use the supported serialized runner and this checkout's Gradle wrapper. The commands above specify tasks, filters, and seeds, not authorization to skip the runner or host lock. The negative worktree's intentional test-only overlay must be recorded; the positive worktree must be clean.

## Repair and delivery

The gate host is authorized to repair compilation, test setup, or formatting within these three outbound files, preserving the real race and the request-scoped fix. Investigate any executed failure before retrying; preserve the first result. Do not weaken the association checks, remove one of the two requests, substitute fake UBI preparation/recording, or replace the intended negative assertion with a timeout. A substantive change in premise or broader feature scope returns a report to Nick.

If any repair moves F, commit it as Nick Shanin with no attribution trailer and no PR number in the commit message, normal-push this fork branch, verify the remote SHA, and add a superseding gate record naming the repaired head. Re-run all required positive legs and the negative leg using the exact final test file; never claim F's earlier result as proof of a repaired head. Tidy may touch the two imported prerequisites as well as the new test, but no unrelated files may enter the branch.

Finish with an add-only `reports/pr-5238-ubi-context-linux-round-1.md` (or a clearly named superseding round), and a new gate-owned `receipts/PR-5238-ubi-context.md` with revision table, exact proof totals, failed-before evidence, command/log/XML paths, limits, and final offered SHA. State Implemented, Proof Pending until all legs pass. Do not edit the original claim or gate job.

Do not post a GitHub comment or JIRA update from this gate. Return the result for review and a concise offer explicitly framed as adjacent and potentially outside PR 5238's scope. A green result would establish a UBI draft recording defect and this bounded repair; it would not establish that the listener hook is defective or that UBI must adopt it. It would not prove async ingestion, performance, redaction, core reload, multi-shard merge behavior, general persistent-sink durability, Windows execution, or readiness of the entire UBI draft.
