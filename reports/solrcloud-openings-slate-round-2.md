# SolrCloud openings slate, round 2

Workstream: W3 of `assignments/pool-category-closeout-overnight-round-1.md`. Claim: `claims/pool-category-closeout-w3-slate-round-2.md`.
Starting records: `reports/solrcloud-openings-slate-round-1.md` and `reports/category-closeout-w3-w5-round-1.md`.
Record tip read: pr-prepare `5205dda997a` (origin/pr-prepare and this worktree's HEAD).
Live fork tips: `git ls-remote origin`, taken during this review.

Scope: read-only. No gate, build, test or Gradle run on this host. No draft, gate job or receipt edited. No commit, push, checkout, PR, comment or live body change. The only file written is this report.

## Slate

Opening head = the live fork tip when the ticket's gate is green. For the two tickets with a pending gate, the head is the pushed tip, and the gate confirms it.

| Ticket | Live fork tip (ls-remote) | Receipt at that tip | Opening head | State |
|---|---|---|---|---|
| SOLR-17680 | `52b641ca1a26` | GATE GREEN at `52b641ca1a26` | `52b641ca1a263589afa4f1ef0ec7b68f53c84565` | READY FOR OWNER APPROVAL (unchanged) |
| SOLR-9155 | `29bf373828` | GATE GREEN at `29bf373828` | `29bf373828994c622ac478a968d6cff4f4c45fa5` | READY FOR OWNER APPROVAL (unchanged) |
| SOLR-17733 | `cf1b283272641` | none at this tip; last green at `636196b7954` | `cf1b283272641e8e4944cf5a80773cb336be8e0c` if green | Blocking: vm2 gate, then draft and receipt head refresh |
| SOLR-14919 | `483b3885f6439` | none at this tip; last green at `8f1f4ef91e798` | `483b3885f6439391a3128e6c61be34fc47122514` if green | Blocking: vm2 gate, then receipt and draft head refresh, then owner item (c) |
| SOLR-15035 | `12d7491e82c` | GATE GREEN (hardened) at `12d7491e82c` | `12d7491e82cd660ad55063cb85633b7e05e1918c` | Blocking: owner's HOLD to READY settlement line, and the changelog YAML gate (owed, not filed) |
| SOLR-11479 | `6f739cfbbd38` | carry-over green at `6f739cfbbd38` | `6f739cfbbd383f08e7393ea38358b7fc77254e58` | READY TO OPEN after SOLR-15035. Blocking: SOLR-15035 settlement line (owner) |
| SOLR-15106 | `40b7e5d0efa7` | dated run 2026-10-13, 1 of 1, at `40b7e5d0efa` | `40b7e5d0efa70d61b74b9fa9ac04beb1ed397637` | Blocking: owner's two checks (1200-message claim; length) |
| SOLR-15386 | `ca8cb61ee957` | dated run 2026-10-13, 3 of 3, at `ca8cb61ee957` | `ca8cb61ee957ed728034a2e6c90532e6082ef3ff` | Blocking: owner's confirmation of the single-check Choice, and length |

## Per ticket

### SOLR-17680: READY FOR OWNER APPROVAL

- Live tip equals `52b641ca1a26`. The receipt records GATE GREEN at that tip: changelog parse, tidy, Error Prone compile and module check pass; CreateRoutedAliasTest 17 of 17 and CreateAliasAPITest 13 of 13 from fresh JUnit XML; both new tests fail at the merge-base production.
- Unchanged in this round.

### SOLR-9155: READY FOR OWNER APPROVAL

- Live tip equals `29bf373828`. The receipt records GATE GREEN at that tip (ZkControllerGetLeaderTest 1 of 1, XML 2026-10-13T05:51:35Z; module check rc 0).
- Unchanged in this round.

### SOLR-17733: gate owed at vm2

- Live tip `cf1b283272641` is one commit on `636196b7954` (parent verified), one file changed: `changelog/unreleased/SOLR-17733.yml`, one line. The title at the tip is identical to the "Title after" text in the gate job.
- `gates/SOLR-17733-title-narrowing-round-1-gate.md` is still OPEN with nothing ticked. It says the landed head does not exist yet, and records no landed SHA.
- No gate result exists at `cf1b283`. `receipts/SOLR-17733.md` ends at `636196b7954`. No commit on pr-prepare after `10647e52204` touched `gates/`, `receipts/`, `pr-drafts/solrcloud/` or `reports/`.
- Blocking: vm2 runs the gate job (steps 1 to 7 at the landed head). After a green run, the draft `pr-drafts/solrcloud/SOLR-17733.md` (it cites `636196b` at lines 15 to 17, 23, 35 and 38) and the receipt are refreshed to the landed head.

### SOLR-14919: gate owed at vm2; owner item (c)

- Live tip `483b3885f6439` is one commit on `8f1f4ef91e798` (parent verified), two files: the changelog title (one line) and the comment in `IgnoreCommitOptimizeUpdateProcessorFactoryTest.java` (lines 63 to 66). The title and comment text match the gate job's "after" text.
- `gates/SOLR-14919-precision-round-1-gate.md` is still OPEN with nothing ticked, and says the moved head does not exist yet.
- No gate result exists at `483b388`. `receipts/SOLR-14919.md` ends at `8f1f4ef91e798`. The draft cites `8f1f4ef91e` at lines 16, 22 and 32.
- Blocking, in order: (1) vm2 runs the gate job (steps 0 to 9, including the focused run at the landed head); (2) receipt and draft head refresh; (3) owner item (c).
- Owner item (c): the draft headline says forwarded commits fail whenever the processor is in the update chain. With `ignoreOptimizeOnly=true`, a plain commit returns before the parse, so the scope of the headline is the owner's call. The item is named here and not settled.

### SOLR-15035: settlement and changelog gate owed

- Live tip `12d7491e82c` matches the receipt. The receipt records GATE GREEN (hardened) at that head, the base run (`java.lang.AssertionError: expected:<2> but was:<null>`, 2026-10-13, merge-base `b5c71bc5573c`), and the head-production run on base tests (3 of 3, 2026-10-13).
- Done since the round 1 slate: the draft's Proof quotes the base failure line; the receipt and gate file rename the 2026-10-13 run to "Head-production run on base tests", so "alone" keeps the ADDREPLICA-only meaning; `gates/SOLR-15035-base-run.md` is DONE with its checklist ticked and the stale line removed.
- Blocking step 1, owner: the settlement line that moves SOLR-15035 from HOLD to READY. The record that would carry it, `reports/solrcloud-hold-first-rung-round-1.md`, is not on pr-prepare. `reports/solrcloud-decisions-round-5.md` still lists 15035 as HOLD.
- Blocking step 2, owed: the changelog YAML parse of `changelog/unreleased/SOLR-15035.yml` at `12d7491e82c`. No gate job file exists for it under `gates/`. The review coordinator files it, and vm2 runs it.

### SOLR-11479: READY TO OPEN after SOLR-15035

- Live tip `6f739cfbbd38` matches the receipt's carry-over section. The tree `eb90a93c38c7` is identical to the gated head `360dca60fcfa`. Parent `7e538e844c45` is the same as the gated head's parent. All five commits show author and committer Nick Shanin, with no trailers. The changelog parses. The carry-over job is DONE.
- Blocking: the SOLR-15035 settlement line (owner). Batch A says this PR opens only after SOLR-15035 and lands after it.

### SOLR-15106: owner's two checks

- Live tip `40b7e5d0efa7` matches the receipt. The receipt records the base compile failure verbatim (`OverseerProcessorExitTest.java:36: error: cannot find symbol`, `symbol: method getCollectionProcessorThread()`, repeated at line 46) and the dated head run (1 of 1, 0 failures, XML 2026-10-13T04:41:12Z). `gates/SOLR-15106-premise-base.md` is DONE. The hold-first-rung check, whether the run record has landed, is answered: it has.
- The Limits wording is fixed: the "which the report names" text is gone, and bullet 3 now reads that the returns and interrupts after the runner starts are not changed in `OverseerTaskProcessor.java`.
- Owner check 1, the 1200-message claim: the draft's "What happens today" repeats the ticket's "more than 1200 queued messages". No file on pr-prepare for this ticket contains 1200 apart from that sentence, and the changelog at `40b7e5d0efa` does not either. The branch has no packet for the number. The ticket is the only source. The owner decides whether to keep it, cite the ticket, or drop the number.
- Owner check 2, length: the draft measures 4,671 bytes with link targets and 3,440 bytes with them removed. The guide is about 3,500 characters. The W3 round 1 record gives 4,695 characters, which does not match either measure, so the owner should choose one measure for the check.

### SOLR-15386: owner's confirmation

- Live tip `ca8cb61ee957` matches the dated run, NodeMutatorTest 3 of 3, 0 skipped, 0 failures, XML 2026-10-13T05:01:08Z. `gates/SOLR-15386-dated-run.md` is DONE, though its checklist is still unticked (a tidy item).
- Choice: the phrase "live node entries carry no data" is gone. The clause now says live node entries carry JSON data (Solr version, node name, roles), set at registration, and that a restart creates the entry again at version 0. Checked at `ca8cb61ee957`: `createEphemeralLiveNode` (ZkController.java lines 1310 to 1331) writes the data from `buildLiveNodeData` (lines 1333 to 1341), and no `setData` call writes to the live node path.
- Seed: the draft's Proof says the dated run pins no seed, which matches its XML. The receipt still cites `15386C0FFEE15386` at line 14, from the earlier undated run. That is a receipt correction for the next refresh. It does not change the draft text.
- Owner checks: (1) confirm the single-check Choice; (2) length, 3,680 bytes with link targets removed (4,935 with them), against the guide of about 3,500.

## Owner items, collected

1. SOLR-15035: the HOLD to READY settlement line. No record file exists yet.
2. SOLR-14919: item (c), the headline's scope against `ignoreOptimizeOnly=true`.
3. SOLR-15106: the 1200-message claim, and the length.
4. SOLR-15386: the single-check Choice confirmation, and the length.

Not owner items: the vm2 gates for SOLR-17733 and SOLR-14919, the SOLR-15035 changelog gate (to be filed), and the head refreshes after each gate.

## Owed records (not blocking the owner's approval)

- The SOLR-17733 and SOLR-14919 gate job files still say the moved heads do not exist. They should record `cf1b283` and `483b388` before vm2 starts. Not edited in this round.
- `receipts/SOLR-15386.md` line 14: the undated seed.
- `gates/SOLR-15386-dated-run.md`: checklist unticked.
- Length, for reference only: SOLR-9155 measures 3,566 bytes with link targets removed, slightly over the guide. It stays READY FOR OWNER APPROVAL as settled in round 1.

## Verification log

- Live tips (`git ls-remote origin`): solr-17680-submit `52b641ca1a263589afa4f1ef0ec7b68f53c84565`; solr-9155-submit `29bf373828994c622ac478a968d6cff4f4c45fa5`; solr-17733-submit `cf1b283272641e8e4944cf5a80773cb336be8e0c`; solr-14919-submit `483b3885f6439391a3128e6c61be34fc47122514`; solr-15035-submit `12d7491e82cd660ad55063cb85633b7e05e1918c`; solr-11479-submit `6f739cfbbd383f08e7393ea38358b7fc77254e58`; solr-15106-submit `40b7e5d0efa70d61b74b9fa9ac04beb1ed397637`; solr-15386-submit `ca8cb61ee957ed728034a2e6c90532e6082ef3ff`.
- Pushed commits, read by `git diff --stat` and `git show` on the local object store: `cf1b283` parent `636196b7954`, one changelog line; `483b388` parent `8f1f4ef91e7`, the changelog title and the test comment.
- Gate and record activity since the pushes: no change to `gates/`, `receipts/`, `pr-drafts/solrcloud/` or `reports/` between `10647e52204` and the record tip.
- `reports/solrcloud-hold-first-rung-round-1.md`: absent on the record tip.
- Worktree note: `reports/narrowing-and-fail-before-round-1.md` shows as modified in this worktree. It is another session's change and was not touched or staged.
