# Gate job: SOLR-17844 (partition-safe expected-hit oracle)

Status: DONE (GATE GREEN at 08ecc90ff7b5505e576985c8dcb514393314ce2b, phone-agent-2)

- Branch: solr-17844-restore-sort-assertions-phone-agent-2. Head: `08ecc90ff7b5505e576985c8dcb514393314ce2b`.
- Base: `b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb`. Previous gate at `0a7ffa07a40c123bd541df3068548247f40055d7` failed and remains recorded as HANDED BACK in `gates/SOLR-17844-phone-agent-2-round-1.md`.
- Claiming host: phone-agent-2, under the existing directed round claim. Capability tags: implementation, review, gate.
- Module: `:solr:core`. Focused class: `org.apache.solr.uninverting.TestFieldCacheSortRandom`. Seed: `1BB02D3762B56611`.
- Diff scope: only the named test class. Expected-hit observations are deduplicated by global docID, retaining count/order/value checks and checking repeated observations agree.

Links: Ticket SOLR-17844. Category Test quality, secondary Search and query components. Siblings SOLR-17631 and SOLR-17840, no changed-file overlap. Supersedes the failed candidate's oracle implementation, not its gate evidence.

## Why a new implementation head

Lucene 10.4.0 bytecode independently confirms leaf partition dispatch invokes scorerSupplier for each partition, while this test's supplier records the full leaf each time. Therefore raw oracle observations are not distinct document matches. Commit 08ecc90 corrects that oracle construction without using actual returned hits or weakening assertions. This is implementation rework, not a claimed gate repair of expected values. Rung 1 source/bytecode evidence plus the seeded count failure settle the correction. Every gate step runs again on the new head; previous proof/compile successes are not reused.

## What to run

Runner `/workspace/tools/phone-agent-2-hardening-gate.py`, configuration `/workspace/phone-agent-2-logs/gate-17844-rework-1.json`, logs `/workspace/phone-agent-2-logs/gate-17844-rework-1`. Shared build lock and disk guard apply. Run serially, never alongside the LTR diagnostic.

0. Verify test-only changelog exemption against the complete diff (`validate-changelog.yml:63-75`).
1. Root tidy.
2. Core compileJava and compileTestJava with validation.errorprone=true.
3. Invert only the actual SortField reverse flag, retain expected ordering; require executed `Unexpected document at position` assertion failures, archive XML, then restore head.
4. Clean test results, run both methods in the focused class at the historical seed, archive fresh XML counts.
5. Core module check, `:solr:core:check -x test`.

## Decisions

The corrected oracle remains independent of actual hits. No production sorting change is justified. Any failed proof shape is not accepted as green. Gate repairs and one tree-changing repair retry follow WORKFLOW; a failed retry is HANDED BACK. No additional implementation loop will be hidden inside this job.

## Checklist

- [x] fork head read back
- [x] changelog exemption verified
- [x] tidy
- [x] Error Prone compile
- [x] discriminating mutation
- [x] fresh head XML counts
- [x] module check
- [x] receipt and terminal outcome recorded

No external posts, PR opening, ready flip or force push. Commits as Nick Shanin without trailers.

Completed 2026-10-23T00:10:05Z. All steps passed. Mutation proof: 2 executed, 2 expected assertion failures, 0 skipped/errors. Restored head: 2 tests, 0 skipped/failures/errors. Core module check rc=0. Source clean, fork head read back unchanged. Full new top receipt in `receipts/SOLR-17844.md`; failed original gate preserved below it.
