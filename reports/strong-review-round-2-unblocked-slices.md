# Strong review round 2: unblocked slices (vm2)

Run by vm2 on 2026-10-18. Claim: `claims/pool-strong-review-round-2-unblocked-slices-vm2.md`. Assignment: `assignments/pool-strong-review-round-2-unblocked-slices.md`. Coordination tip at the start of the run: `460ee6de9e`. No verdict for the four PRs changed in this run.

## Slice A: SOLR-18541 body rewrite (not run from vm2)

Not run. The slice PATCHes the live body of PR #5117 on GitHub, and this host is not allowed GitHub writes. Nothing was read from or written to the PR from vm2.

On record: `claims/pool-strong-review-round-2-unblocked-slices.md` (DONE 2026-10-15T19:20Z) says the body was replaced by REST PATCH at 2026-10-15T18:54:36Z at head `a93f9f6a997e`, with 0 comments and 0 reviews at the time. The read-back, the check for maintainer comments since round 1, and whether the head has moved since then were not repeated here. The owner or the main side decides whether a further rewrite is needed.

## Slice B1: SOLR-18010 Choice text (blocked: inputs not on the branch)

Not prepared. The inputs are not on `origin/coordination` at `460ee6de9e` or in this worktree:

- `pr-drafts/core-admin/SOLR-18010.md` (the draft the slice writes into)
- `reports/four-new-prs-strong-review-round-1.md` (finding B3, the wording to fix)
- `assignments/pool-four-new-prs-strong-review-round-2.md` (owner decisions and owner items O1 and ruling 3)

The previous claim records that the draft is uncommitted and that its Choice block conflicts with owner ruling 3, which Nick has to decide. Preparing the rewrite from memory of those findings would invent the reviewer's wording, so nothing was written. The Choice text can be prepared once the draft and the round 1 text are on the branch and the ruling 3 question is answered.

## Slice B2: SOLR-18010 changelog title (already discharged, no patch)

The E13 correction is already committed. `origin/solr-18010-submit` at `571307400b` ("SOLR-18010: correct the changelog title partly written file claim") removes ", or leave a partly written file" from `changelog/unreleased/SOLR-18010-security-json-concurrent-edits.yml`. The committed title, verbatim:

> Standalone security.json edits are serialized within one Solr process and written through a temporary file, so concurrent edits can no longer overwrite each other. The file also no longer carries the internal version metadata.

YAML check: the file parses with `yaml.safe_load` (PyYAML), `type: fixed`, one author. No gate patch is needed under `gates/`.

## Owner item O4: records behind `receipts/SOLR-18010.md`

Checked against `/workspace/gates/logs/vm2/` on this host. Receipt paths are relative to `logs/vm2/`. Items 1 to 3 are the three logs O4 names.

1. `g18010-gate.log`: not found. The receipt (line 6) cites a gate that finished 2026-10-05. No file dated 2026-10-05 is present. No reachable record supports this citation.
2. `g18010-premise.log`: not found. The receipt (line 8) says both concurrent-edit tests fail against pre-fix production with a lost assignment. The records here do not show that:
   - `queue-g18010-premise-20261013T165840Z.out`: CHECKOUT FAILED at `2b5ad801c9` and `14c7aac0d1`.
   - `queue-g18010-premise-20261013T165908Z.out`: probe insert failed (anchor count 0).
   - `queue-g18010-premise-r2-20261013T170231Z.out`: head rc=0 and base rc=0, with no test counts or failures.
   - `18010-premise-base.log` and `18010-premise-head.log`: BUILD SUCCESSFUL, no FAILED lines. `18010-premise-xml-base` and `-head` hold only `SecurityConfSymlinkProbeTest` (1 test, 0 failures).
   No reachable record supports the premise result.
3. `g18010-settle.log`: not found. The receipt (line 9) cites the 2026-10-04 takeover log. No file dated 2026-10-04 is present. No reachable record supports this citation.

Other citations with a file on this host (existence checked; contents of the perms and changelog logs not re-read):

- `queue-g18010-changelog-title-gate-20261015T035900Z.out` (line 15): present.
- `queue-g18010-gate-20261013T142306Z.out` (line 26): present.
- `queue-g18010-perms-gate-20261013T143445Z.out` (line 34): present.
- `18010-perms-g1-reverted.log`, `18010-perms-g1-head-rerun.log`, `18010-perms-g2-multiauth.log` (lines 53 to 55): present.
- `18010-review-r1-g4-tidy.log`, `-spotless.log`, `-compile.log`, `-focused.log`, `-check.log` (line 71): present.
- `18010-review-r1-g4-failbefore-parent.log` (line 70): present. `18010-review-r1-g4-junit-parent` shows `SecurityConfHandlerTest` tests=5, failures=1, which matches "4 of 5 pass, 1 failure".
- `18010-review-r1-g4-junit-head`: `SecurityConfHandlerTest` tests=5, failures=0; `V2SecurityAPIMappingTest` and `BasicAuthStandaloneTest` also 0 failures.

Not changed: `receipts/SOLR-18010.md` was not rewritten. Its existing correction note (line 3) already says the three logs are not archived.

## Waiting slices (restated, unchanged)

- SOLR-18539: waits on the QueryComponent real-exit settling run. The branch was not moved and PR #5115 was not touched.
- SOLR-18010 verdict: waits on the corrected parent-baseline gate at the route 1 head and on the G3 record. `gates/SOLR-18010-review-r1-g3.md` is not on `origin/coordination`; the vm2 G3 logs (`18010-review-r1-g3-*`) exist on this host.
- SOLR-18541 score-only exit input: waits on its gate job, if it has not recorded a result.

## Host note

`~/.pool-host` and `~/.pool-env` are missing on vm2 and there is no JDK, so no Gradle build can run here. The host id `vm2` was taken from `/workspace/.pool-host` as the operator directed.
