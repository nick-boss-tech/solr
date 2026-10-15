# Gate job: SOLR-15674 history scrub (remove the co-author trailer) and re-gate

- Branch: solr-15674-submit. Fork tip at posting: ba01c83d4c5ad03c7af63fd4061df119a8146d49 (verified by ls-remote 2026-10-12; matches receipts/SOLR-15674.md).
- Intended host: any capable Linux host (vm1 or vm2), under the WORKFLOW.md claim rules. Windows never claims build work.
Status: DONE
- Capability tags: gate.
- Job type: history scrub (trailer removal only, no code change), then the full hardening gate at the scrubbed head.
- Source: reports/solrcloud-pr-critical-review-round-1.md marks the SOLR-15674 draft NOT READY for two reasons: a Claude co-author trailer in the branch history, and draft length. This job covers the trailer only. The draft text is a separate review-side item.

## The trailer, verified at posting

Checked with git log --format over the branch range (merge-base with main b5c71bc5573c4e31b4cee5a7965d73587fc0ae58 to the fork tip). Exactly one commit carries the trailer:

- 9b51865402dfa18f09aa00342b531ef77c7eb5c8, "SOLR-15674: compare mzxid instead of version+czxid for the cached schema/config; drop sentinel and 2-arg constructor; cache test and changelog" (2026-10-03). Its body is only the trailer line: `Co-Authored-By: Claude Sonnet 5.5 <noreply@anthropic.com>`.

The other two branch commits carry no trailers (their %(trailers) output is empty):

- 4cc98731784366149a00486258d1b917cd83d110, "SOLR-15674: include znode czxid in schema config cache freshness check" (2026-10-01).
- ba01c83d4c5ad03c7af63fd4061df119a8146d49 (the tip), "SOLR-15674: add assumeWorkingMockito to the cache test" (2026-10-03).

Re-check this at claim time against the then-live tip. If the tip has moved past ba01c83d4c5, stop and record it here instead of scrubbing: the trailer census has to be redone on the moved history first.

## The rebuild

Use the commit-tree rebuild pattern already used for the SOLR-15805 and SOLR-16013 scrubs:

1. Rebuild each branch commit in order onto the merge-base, reusing each commit's exact tree, subject, author name, author email and author date.
2. The only message change anywhere is deleting the trailer line `Co-Authored-By: Claude Sonnet 5.5 <noreply@anthropic.com>` from 9b51865402's body. No other subject or body text changes in any commit.
3. Verify before pushing: the rebuilt tip's tree is identical to ba01c83d4c5's tree; each rebuilt commit's tree is identical to its source commit's tree; a grep for "claude" (case-insensitive) over the rebuilt range's full messages returns nothing; no rebuilt commit carries any Co-Authored-By trailer.
4. Push with --force-with-lease pinned to the fork tip read in the same run (expected ba01c83d4c5ad03c7af63fd4061df119a8146d49 unless the re-check above found it unchanged at a later read; pin to the value actually read). If the lease fails because the tip moved, skip the push and record it here.

## Gate at the scrubbed head

Run the full hardening gate at the scrubbed head, following the original gate record in receipts/SOLR-15674.md (log g15674-harden.log):

- Changelog YAML parse on the branch's fragment.
- Tidy, with the GATE INVALID post-tidy guard and gradle/libs.versions.toml restored.
- Error Prone compile (-Pvalidation.errorprone=true).
- Focused tests from fresh JUnit XML: IndexSchemaFactoryCacheTest (1 of 1 at the pre-scrub head, per the receipt). Grep the branch for the class's exact package before composing the run spec; do not construct the package from the class name.
- Module check for the module the branch touches, as in the original gate.
- Proof state carries over and is recorded as such: the receipt records the proof as INCONCLUSIVE by construction (the branch's tests do not compile against unpatched code, because the branch changes the cache comparison to the znode mzxid and updates both in-repo VersionedConfig callers). The scrub changes no code, so no new proof leg is owed; the refreshed receipt restates the inconclusive-by-construction record.

If the gate fails on tests it executed, stop. Record the failing test, seed and reproduce line in this file and in the receipt, and do not relaunch; the failure goes to the main side for investigation. An environment refusal (no tests executed) follows the standing queue rules.

## On green

- Refresh receipts/SOLR-15674.md: new gated head (the scrubbed tip), the scrub record (source tip ba01c83d4c5, the one trailer commit 9b51865402 and its rebuilt hash, tree-identical verification), the gate counts from fresh JUnit XML, and the carried-over proof record. Keep the receipt's pairing note: this branch pairs with SOLR-15478 (both switch a freshness check from the data version to the mzxid); the scrub does not change the pairing.
- Mark this job DONE and the claim DONE in the same push sequence.

## Step checklist

- [x] claim filed under WORKFLOW.md
- [x] trailer census re-checked against the live fork tip
- [x] history rebuilt with the trailer line removed and nothing else changed
- [x] tree-identical verification passed (tip tree and per-commit trees)
- [x] force-with-lease push pinned to the tip read in the same run
- [x] full hardening gate green at the scrubbed head (counts from fresh JUnit XML)
- [x] receipts/SOLR-15674.md refreshed at the scrubbed head
- [x] job and claim marked DONE
