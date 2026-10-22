# Gate job: SOLR-18200 review pass 2 (TextProfileSignature produces different signatures for identical input)

Status: DONE (GATE GREEN at f8c4ee22bd, vm4, 2026-10-22T20:18:33Z)

- Branch: solr-18200-textprofile-tiebreak. Head: `f8c4ee22bdd218ddfbf287fe0e389c17a5198f8f` (unchanged since pass 1). Prior gated head: `f8c4ee22bd` (pass 1, vm4, GATE GREEN).
- Intended host: vm4. Capability tags: gate.
- Module: `:solr:core`. Focused class: `org.apache.solr.update.processor.TextProfileSignatureTest`. Seed: `18200C0FFEE18200`.
- Why: pass 2 of the review round for unit 37, regate on the head pass 1 reviewed.

## Checks

- [x] head confirmed by ls-remote
- [x] diff scope unchanged
- [ ] step 0 changelog: not applicable, no changelog file on this branch
- [x] step 1 tidy, rc=0
- [x] step 2 Error Prone compile, rc=0 (20:10:01)
- [x] step 3 proof leg (base `TextProfileSignature.java`): rc=1. Expected failure `testSignatureDoesNotDependOnHashMapResizes` (ComparisonFailure), same as pass 1.
- [x] step 4 focused head run, from fresh XML: TextProfileSignatureTest tests=1, skipped=0, failures=0, errors=0 (ts 2026-10-22T20:16:41.364Z). rc=0.
- [x] step 5 module check `:solr:core:check -x test`, rc=0 (20:18:33).

Logs: `/workspace/gates/logs/vm4/18200/`.

## Deliverable

- Pass 2 result in `reports/newdev-critblock-review-round-1/solr-18200-textprofile-tiebreak.md`.
- Receipt section in `receipts/SOLR-18200.md`, top section.
