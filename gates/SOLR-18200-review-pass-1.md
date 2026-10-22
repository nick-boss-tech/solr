# Gate job: SOLR-18200 review pass 1 (TextProfileSignature produces different signatures for identical input)

Status: DONE (GATE GREEN at f8c4ee22bd, vm4, 2026-10-22T20:00:37Z)

- Branch: solr-18200-textprofile-tiebreak. Head: `f8c4ee22bdd218ddfbf287fe0e389c17a5198f8f` (ls-remote 2026-10-22T19:29:36Z, from the claim). Prior gated head: `f8c4ee22bd` (none on this unit).
- Intended host: vm4. Capability tags: gate.
- Module: `:solr:core`. Focused class: `org.apache.solr.update.processor.TextProfileSignatureTest`. Seed: `18200C0FFEE18200`.
- Why: pass 1 of the review round for unit 37.

## Checks

- [x] head confirmed by ls-remote
- [x] diff scope confirmed
- [ ] step 0 changelog: not applicable, no changelog file on this branch
- [x] step 1 tidy, rc=0, status file empty
- [x] step 2 Error Prone compile `:solr:core:compileTestJava -Pvalidation.errorprone=true`, rc=0 (19:46:35)
- [x] step 3 proof leg (base `TextProfileSignature.java` in worktree `proof-18200`, test at head): rc=1. Expected failure `testSignatureDoesNotDependOnHashMapResizes`: `ComparisonFailure`, the signature before and after the HashMap resize differ on base. Tests 1, failed 1.
- [x] step 4 focused head run, from fresh XML: TextProfileSignatureTest tests=1, skipped=0, failures=0, errors=0 (ts 2026-10-22T19:57:09Z). rc=0.
- [x] step 5 module check `:solr:core:check -x test`, rc=0 (20:00:37).

Logs: `/workspace/gates/logs/vm4/18200/`.

## Deliverable

- Pass 1 result in `reports/newdev-critblock-review-round-1/solr-18200-textprofile-tiebreak.md`.
- Receipt section in `receipts/SOLR-18200.md`, top section.
