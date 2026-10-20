# Work-store spot check, 2026-10-20

Source records were read from coordination tip `f889878389a`. Checks were read-only; no existing claim or deliverable was edited.

At that tip, `queue-current.json` had 1,027 ticket records and `remediation-current.json` had 459 open entries.

## Store 1: follow-ups and reviews

`SOLR-16130` is a useful check of a follow-up that remains open without being unattended. The promise ledger says the test-only promise rests on PR #5004 and is discharged only when that PR merges (`reports/merged-pr-followup-promises-round-1.md`). A live read of #5004 returned state OPEN, head `3c48dec4b79faed625c4fb79833d0975a55f3ed7`, and no merge timestamp. `receipts/SOLR-16130.md` records a green gate at that head. The ledger's state is consistent: the gate passed, while the promise remains open pending merge.

## Store 2: skip list

`SOLR-10407` appears in `material/skip-audit/queue-current.json` with the earlier `close-obsolete` record and confirmed-skip audit. It also appears in `material/skip-audit/remediation-current.json` with status `open`. The batch 04 re-audit report explains why: `availableSubPaths` still appears in `PathTrie.java` and the v2 API guide, so the old close claim needs correction or the ticket remains open (`reports/skip-verdict-audit-batch-04-reaudit.md`). The open remediation correctly keeps this from being treated as handled or taken as a fresh skip closeout.

## Store 3: CSV backlog

The latest completed slice at this tip, slice 0179, has exactly two rows. `SOLR-3653` is `stale` from the CSV metadata. `SOLR-3657` is labeled `has-pr`, but it has substantial prior work: the skip queue records branch `solr-3657-submit` and an older unrun-test note; `receipts/SOLR-3657.md` records a later green gate at `14edaca577c0` with 17 `DocumentBuilderTest` tests, 11 `TolerantUpdateProcessorTest` tests, and module check rc=0. A live fork-ref read confirmed that the branch still points at the receipt head. The slice output does not expose that overlap.

All 179 completed slice files contain exactly two data rows. Of 2,039 planned slices, 1,860 remained uncompleted at this tip. A key scan found 55 of the 358 distinct completed CSV keys in `queue-current.json`; 20 also had `open` remediation records. These counts identify records to inspect, not 55 individually verified duplicate investigations. The confirmed `SOLR-3657` case shows why each exact match must be opened and compared with its current claim, report, gate, receipt, and promise state. Existing slices and claims remain as recorded; the new lookup rule applies to future slices.

## Changes made from this check

The CSV assignment now requires a per-key lookup across both skip queues, earlier CSV slices, the priority-round reports, and ticket records. It keeps overlapping rows in the two-row slice, labels them `overlap`, and records the exact path and status. It also distinguishes metadata triage from the deeper evidence work required by the completed Critical and Blocker round. The `records` capability is now listed for Linux and Windows pool hosts.
