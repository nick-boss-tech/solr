# SOLR-14919 and SOLR-17292: recheck against the new records, round 1

Trigger: vm2's records on pr-prepare. SOLR-14919 re-gate GREEN at `8f1f4ef91e798f736eb91c9625223a654b59794a` (run 3, focused 1 of 1 from fresh XML, dated 2026-10-13, commit 356abb8ff1e). SOLR-17292 dated run of record at `f614a42fbc8046e47cb7ed216f6b52b8a01f594c` (TestPerReplicaStates 4 of 4 from fresh JUnit XML, run 16:07:45Z to 16:12:58Z, commit dc35a9e38ae). Both were held in batch B on exactly these two items.

Pr-prepare tip checked: `036cb07a5a6` for the reviewers' reads. Each head was checked with ls-remote before a reviewer relied on it. Both match.

Scope: drafts only. No live PR body edited. No fork branch changed. No build, test or Gradle run. Nothing posted.

## Verdicts

| Ticket | Draft | Head | Verdict | Applied | Held |
|---|---|---|---|---|---|
| SOLR-14919 | `pr-drafts/solrcloud/SOLR-14919.md` | 8f1f4ef91e79 | NOT SATISFIED (blocking: the Proof still names the 2026-10-03 ledger date and the 84e7 head) | Dated run in Proof; merge-base named on the two base-code citations; a sentence removed | Changelog title and test comment wording (owner, moves the head); optional scope wording on the update chain |
| SOLR-17292 | `pr-drafts/solrcloud/SOLR-17292.md` | f614a42fbc80 | NOT SATISFIED (blocking: Proof date from the ledger; Limits bullet not matching the callback; the undated revert line) | Dated Proof; Limits bullet with SHA-pinned links; two Section 1 edits | The optional call-site sentence; the undated revert line (owner) |

## SOLR-14919 detail

Checks: the head is 8f1f4ef91e79 (ls-remote). The diff from 84e7bcaeb57 to 8f1f4ef91e79 changes only comments in the processor and its test, the changelog title, and a RecoveryStrategy marker line that nets to zero against the merge-base. Every head link and line range holds at 8f1f4ef91e79. The merge-base citations hold at b5c71bc5573c. No em dashes, no "we". The draft is about 2,600 visible characters.

Applied:
1. Proof: "IgnoreCommitOptimizeUpdateProcessorFactoryTest 1 of 1, 0 failures, 0 errors, 0 skipped; verified 2026-10-13 at head 8f1f4ef91e79." The 2026-10-03 ledger date and the older head are removed.
2. Proof: "On the base processor (merge-base b5c71bc5573c) it fails" names the merge-base.
3. Two citations: ", code at merge-base b5c71bc5573c, before this change" (line 9 and line 29).
4. Removed the sentence "This change does not fix that." from the replication paragraph, since Limits already states it.

Not applied:
- Optional scope wording: the draft says the commits fail "when IgnoreCommitOptimizeUpdateProcessorFactory is in the update chain". The reviewer found this holds for default settings only, because plain commits with ignoreOptimizeOnly=true return before the parse. Owner's call.
- Changelog title ("a leader forwards", "failed with a 400 before") and test comment lines 63-64 ("a leader's fan-out"): imprecise, since any receiving node sets `leaders`. Changing them moves the head and needs a re-run. Owner's call.

## SOLR-17292 detail

Checks: the head is f614a42fbc80 (ls-remote). The merge-base with upstream main is 14c7aac0d151. The dated run is adequate: TestPerReplicaStates 4 of 4, seed 17292C0FFEE17292, fresh JUnit XML. Every head link names f614a42 and every line range holds. The batch B reason for the Limits bullet is confirmed in code for a single per-replica update (the callback in a `finally` at ZkStateWriter, lines 158 to 162 at the merge-base). Choice: no live alternative, so omitting a Choice section is correct. About 2,900 visible characters before the edits, about 3,060 after.

Applied:
1. Proof: "verified 2026-10-13" replaces 2026-10-10 (the ledger date).
2. Limits, second bullet: the Overseer loop's logged failure and the callback in a `finally` are each cited at the merge-base with SHA-pinned links. "Before this change, that removal happened without an error" is kept. "Sustained contention is not tested" is kept from the draft, although the reviewer's replacement dropped it. The disclosure is worth keeping.
3. Section 1: removed "The caller gets no signal. The Jira report asks for the relevant exception to be thrown." The loop sentence stays.
4. Node-down call: the sentence now says the KeeperException skipped the message because the call had no catch of its own, linked at the merge-base.

Not applied:
- Optional wording "These are the call sites that now get the exception:". It repeats the sentence before it, so the draft keeps "These are the call sites:".
- The revert line "With PerReplicaStatesOps.java and ZkController.java reverted to the merge-base, the class runs 4 tests with exactly 1 failure" has no date in the receipt. It stays until the owner decides O1.

## Owner items

1. SOLR-14919: the changelog title and the test comment wording (moves the head; needs a re-run). Also the optional scope sentence on the update chain.
2. SOLR-17292 O1 (blocking for the fail-before line): either vm2 runs a dated revert at `f614a42fbc80`, or Nick accepts the undated base line.
3. SOLR-17292 O2: vm2 should record the checkout HEAD in its log. `ls-remote` does not show which tree a run used, and the log is not in the repo.
4. SOLR-17292 O3: the single-path callback also removes earlier nodes in the same batch (Overseer.java L345 and L361), which may be unflushed. This predates the branch and was read from code, not run. It is not in this ticket's scope unless the owner says so.
5. SOLR-17292 O4: "drafting" in the AI footer (carried from batch B). The footer text is the approved form, so this is left as is.

## Housekeeping

- The local ref `refs/heads/pr-prepare` in the shared repository is stale (c83b02c). The branch on origin is the one to use. The pushes in this round went from the worktree HEAD via `research\push-pr-prepare.ps1`. Nothing was reset or deleted.
- Whether SOLR-14919 or SOLR-17292 has an open PR is not checked in this round. If one is open, its live body needs these same draft edits in a later round, with a read-back after each edit.
