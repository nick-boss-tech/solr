# Category close-out, overnight round 1: workstreams W3 and W5

Assignment: `assignments/pool-category-closeout-overnight-round-1.md`. Claims: `claims/pool-category-closeout-overnight-w3-round-1.md`, `claims/pool-category-closeout-overnight-w5-round-1.md`, `claims/pool-pr-prepare-tip-ae5e520-round-1.md`. Starting tip `ae5e520ba53`.

Scope: drafts, records, gate job files, two fork branch pushes (plain fast-forwards, owner-allowed), and one unposted reply draft. No PR opened, closed or flipped. No comment posted. No live body edited. No build, test or Gradle run.

Decisions made here under the decision default (a recorded recommendation adopted; otherwise the harder option), each recorded in the place it acts:
1. SOLR-15386: the dated run at the head is the evidence, and it sets no seed. The receipt's seed `15386C0FFEE15386` came from an earlier, undated run that conflicts, so it is dropped as evidence from the Proof.
2. SOLR-15035: the Proof quotes the base failure line the receipt records. The run that applied both production files is renamed "Head-production run on base tests", because the earlier name "banked patch alone" was wrong about its content.
3. SOLR-14919 and SOLR-17733: the wording corrections follow the records. "A leader forwards" becomes "a node forwards", and the title of SOLR-17733 is narrowed to the DELETE without `localDelete=true`.

## W3: SolrCloud slate completion

| Ticket | Step | State |
|---|---|---|
| SOLR-15106 | Limits bullet 3 replaced ("runner starts are") | Done in the draft |
| SOLR-15386 | Choice clause reworded to the code: live node znodes carry JSON data (Solr version, node name, roles), set once; a restart recreates the entry at version 0 | Done in the draft. Read at `ZkController.java` 1316 to 1341 at `ca8cb61ee957` |
| SOLR-15386 | Seed conflict resolved (decision 1) | Done |
| SOLR-15035 | Proof quotes `java.lang.AssertionError: expected:<2> but was:<null>` (merge-base `b5c71bc5573c`) | Done, from the receipt |
| SOLR-15035 | Run renamed (decision 2); stale line removed; checklist ticked against the receipt | Done in the receipt and the gate file |
| SOLR-15035 | Changelog YAML parse at opening | Held: a gate job is owed, not yet filed |
| SOLR-17733 | Title narrowed on `solr-17733-submit`, pushed as `cf1b2832726` (plain fast-forward from `636196b7954`) | Pushed. Gate job `gates/SOLR-17733-title-narrowing-round-1-gate.md` OPEN for vm2 |
| SOLR-17733 | Draft and receipt head refresh | Waits for the gate to go green |
| SOLR-14919 | Title "a node forwards" and the test comment reworded, pushed as `483b3885f64` (plain fast-forward from `8f1f4ef91e7`) | Pushed. Gate job `gates/SOLR-14919-precision-round-1-gate.md` OPEN for vm2 |
| SOLR-14919 | Draft and receipt head refresh | Waits for the gate to go green |
| SOLR-14919 | Item (c), the draft headline's scope against `ignoreOptimizeOnly=true` | Owner's call, left as it is |
| SOLR-11479 | Included in the round 2 slate with its state | Not written in this round. It needs the SOLR-15035 settlement line, and the slate report waits on that |

Flags for the owner:
- The SOLR-15386 draft is 3,680 bytes without link targets, above the 3,500 guide. A cut is the owner's call.
- The SOLR-15386 receipt still cites the seed at line 14. It is not a named file, and it should be corrected with the receipt refresh.
- The SOLR-15106 draft is 4,695 characters with URLs. The ticket's 1200-message claim has no packet on the branch. Both are the owner's checks.

## W5: PR 5106 reply, reconstructed

The reviewer's request, from the two comments on PR #5106 (SOLR-16630): replace more `Thread.sleep` calls with latches. The sketch is a wait on a latch before the NRT stop, and a count-down right before the phase 1 NRT queries.

The reply draft is `material/pr-5106-reply-draft-reconstructed.md`. It is marked RECONSTRUCTED. It is not posted, and the PR is not edited. Posting stays with the main agent after you see the text.

Gaps:
- The two reasons for keeping the remaining sleeps appear only in the workstream text. They are not checked against code.
- The later of the two reviewer comments was edited after it was written, and the earlier text is not on record.

## Not done

- W1 (verdict landing completion), W2 (convergence round 2 completion for build-docs, CLI, streaming and SolrJ) and W4 (morning slates assembly) are not started. W4 depends on the others.
- The SOLR-18539 narrowing patch is still unpushed. The push wrapper's branch rule does not accept `review-r1-solr-18539`.
- The worktree `wt/fork-14919` could not be removed (permission denied). It is a detached worktree at `483b3885f64`, and it can be deleted once nothing holds it.
