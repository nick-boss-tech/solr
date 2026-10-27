# Claim: SOLR-16139 JSON stat facet over SortableTextField, premise run then implementation, round 1 (vm4)

- Claimant host: vm4, matching `hosts/vm4.md`.
- Status: DONE. Branch pushed; gate requested (stop point reached).
- Job or assignment: `assignments/critical-impl-16139-round-1.md` (OPEN, not edited). Source triage: `reports/critical-implementation-triage-round-1.md`, rank 3.
- Slice: the whole assignment (premise, narrow fix on `solr-16139-submit`, gate request at the stop point).
- Capability tags used: implementation, premise-run.
- Started: 2026-10-27T09:59:26Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat. Attempts: 1. Idempotency key: critical-impl-16139@012f513a29.
- Links: Ticket: SOLR-16139. Category: implementation (`pool/work-map.md`). Siblings: SOLR-12069 premise (closed, vm4). Supersedes: none.

Heartbeat: 2026-10-27T09:59:26Z

Plan: standalone shallow clone of apache/solr main, `:solr:packaging:assemble` through `pool/bin/sweep-run.sh` with `env -u NODE_ENV`, then index a SortableTextField and run a nested avg stat facet over JSON facets on the packaged Solr. If the premise fails, write the premise report and stop with no branch. If it reproduces, implement narrowly on `solr-16139-submit`, add tests on SolrTestCase or SolrCloudTestCase, push, and request the gate with `pool/bin/request-gate.py`. No PR, JIRA, or GitHub comment.

Result: pending.

Run (premise setup): `s16139-assemble-ee2f1bf-20261027T100633Z`, `:solr:packaging:assemble` on apache/solr main `ee2f1bf2e0109956145d7fd995da63c4091a589f` (standalone clone `/workspace/worktrees/s16139-clone`), started 2026-10-27T10:06:33Z with `env -u NODE_ENV`. Logs `/workspace/gates/logs/vm4/critical-impl-16139/`.
Heartbeat: 2026-10-27T10:06:49Z

Premise result (2026-10-27T10:28:46Z): REPRODUCES on main ee2f1bf2e0. Nested terms facet on SortableTextField gives avg/sum 0.0 and min null; the same nested stats on a string field are correct (15, 40, 7). Report `reports/critical-impl-16139-premise-round-1.md`. Solr process 72898 is a zombie (PID 1 does not reap orphans); port 8986 closed, nothing running. Step 2 (implementation on solr-16139-submit) next.
Heartbeat: 2026-10-27T10:28:46Z

Probe (before any edit, on main ee2f1bf2e0, packaged Solr 11.0.0-SNAPSHOT):
- Nested `avg/sum/min(price)` under a terms facet, all terms methods (default, dv, dvhash, stream, enum, smart, uif): correct on `plain_s` (string), 0.0 / 0.0 / null on `sortable_text` (SortableTextField). Counts are correct on both.
- Filter probes: `fq=sortable_text:"Alpha grp"` 2 hits; `fq={!term f=sortable_text}Alpha grp` 0 hits (parsed `sortable_text:Alpha grp`); `fq={!term f=sortable_text}alpha` 2 hits; `fq={!term f=plain_s}Alpha grp` 2 hits.
- Cause (code + probe, rung 2): `FacetFieldProcessor.makeBucketQuery` (line ~541) builds the bucket domain with `sf.getType().getFieldTermQuery`. SortableTextField inherits `TextField.getFieldTermQuery`, an exact `TermQuery` on the raw value. The index holds analyzed tokens for `SortableTextField`, so a multi-token bucket value matches nothing. The bucket count comes from the terms enumeration, which is why counts are right.
- Planned narrow fix (rung 2, from the same index structure): for a SortableTextField with docValues, the bucket domain uses `SortedDocValuesField.newSlowSetQuery(field, bucketValue)`, the exact full-value doc-values form that `SortableTextField.createFields` already writes. Used only in makeBucketQuery; the term parser is unchanged. Tests: a SolrTestCase reproduction with the nested avg over a multi-token SortableTextField, plus the string-field control.
- Solr 8986 killed (pid 75830); port closed. Clone `/workspace/worktrees/s16139-clone` stays until implementation is done.
Heartbeat: 2026-10-27T10:35:04Z

Fail-before (run `q16139-failbefore-20261027T105238Z`, branch `solr-16139-submit` at main `ee2f1bf2e0`, new test only, seed 16139C0DE, `-Ptests.workDir=/tmp/q16139-tests`, env -u NODE_ENV): BUILD FAILED rc 1 in 3m 42s. XML `logs/vm4/critical-impl-16139/q16139-failbefore-20261027T105238Z.xml/TEST-org.apache.solr.search.facet.SortableTextNestedStatsCloudTest.xml`: 1 test, 1 failure, 0 errors. Failure: `AssertionError: avg grp_sortable Alpha grp expected:<15.0> but was:<0.0>` at the grp_sortable call (test line 76, assertion at line 97). The string-field control (line 75) passed. Matches the premise.
Heartbeat: 2026-10-27T11:09:34Z

Implementation results:
- Fail-before (main `ee2f1bf2e0`, new test only, seed 16139C0DE): run `q16139-failbefore-20261027T105238Z`, 1 test, 1 failure. `AssertionError: avg grp_sortable Alpha grp expected:<15.0> but was:<0.0>`. The string control passed.
- Pass (fix applied, same test and seed): run `q16139-pass-20261027T111013Z`, 1 test, 0 failures, 0 errors. Tidy runs `q16139-tidy-20261027T111013Z` and `q16139-tidy2-*` rc 0, no other file changed.
- Branch `solr-16139-submit` pushed to the fork as a new branch. Commit `3c719c14d0` (fix and test, Nick Shanin, no trailers), then `3feb4c7` (changelog `changelog/unreleased/SOLR-16139.yml`). Head `3feb4c757370e05ae7c77011511b47af7c80d9e2`, verified by ls-remote. Base `ee2f1bf2e0`. 138 insertions, 0 deletions.
- Gate request posted: `gates/SOLR-16139.md` on coordination at `7490a09421`, read back OK.

Limits:
- The distributed refinement path (`FacetFieldProcessor` near line 1138, `ft.getFieldTermQuery` for a bucket value) still uses the exact term query. The fix covers the nested domain in `makeBucketQuery`, not refinement. A multi-shard refinement case was not tested; the test is single-shard.
- The new test's one string literal is over 100 characters; the formatter does not split string literals, and tidy left the tree unchanged.
- Only the techproducts-style `collection1` schema fields (`*_sortable`, `*_s`) were tested, on one main tip.
- No PR, JIRA, or GitHub action. The gate host owns the rest.

DONE: 2026-10-27T11:33:46Z. Deliverables: fork branch `solr-16139-submit` at `3feb4c7`; gate job `gates/SOLR-16139.md`; premise report `reports/critical-impl-16139-premise-round-1.md`.

