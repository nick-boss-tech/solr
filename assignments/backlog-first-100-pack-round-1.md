# Assignment: backlog first-100 pack (H2 unblocked for Bug category, round 1)

Status: OPEN
Tags: draft, records, sweep
Intended host: vm2 for the pack (it holds the six LIVE H2 family claims and built the H1 store); the spot-QA unit is for a second host, not vm2.

Owner direction (2026-10-24): the owner expects to be ready to blast 100 backlog comments, and they can be Bug category. This file supersedes two round-1 settings of `assignments/harmonize-audits-comment-drafts-round-1.md` for this pack only: the line excluding Bugs from every closure family, and the rule that H2 starts only for H1 batches that passed QA. Everything else in the parent assignment stands.

## Why this file exists (measured 2026-10-24)

Across all 16 H1 batches, the closure and ping dispositions hold 292 records: closure-not-a-bug 127, closure-fixed 78, closure-obsolete 68, closure-duplicate 8, ping-stale 11. All 292 are issue type Bug, so under the round-1 exclusion the closure families' eligible population is zero (only the 11 ping-stale records carry a family eligibility). Separately, no second-host H1 QA report has been published, so the QA gate has no output to wait for. The pack below removes both stalls without removing the checks that matter.

## Pack rules

1. Bug-category tickets are eligible for the closure and ping families in this pack, per family:
   - `closure-fixed`: the record cites the fixing version or commit; the draft names it.
   - `closure-not-a-bug`: the record's verdict cites the expected-behavior or by-design basis (ticket text or a maintainer's ruling); the draft cites that basis.
   - `closure-obsolete`: the record cites what was removed or is absent on main; the draft names it.
   - `closure-duplicate`: the target is recorded and is closed/fixed or plainly the live original; the draft names it.
   - `ping-stale`: the Bug shape: whether the problem still reproduces on a supported release, per the round-2 family example.
2. Excluded from this pack regardless: priority Critical or Blocker (their own stores cover them; 2 records), `owner-excluded` records, `implemented` and any record with an open or merged PR, and conflict-flagged records. Conflicts are skipped and listed, never silently resolved.
3. Every filled draft gets a live check at fill time, recorded in the pack index: Jira status still Open, no linked or mentioned open PR, not the owner's ticket, no comment by the owner already on it. A record failing any check is dropped to the overflow list with the reason.
4. QA moves, it does not disappear: drafting and filling do not wait for full-batch QA. Before the pack goes to the owner, a second host spot-QAs 10 pack entries (re-derive each from the sources and the live ticket; any entry that fails is dropped and replaced from overflow). Full-batch H1 QA continues separately and can veto individual records the same way.

## Deliverables

- The six family files under vm2's existing claims: `drafts/jira-comments/<family>.md` (template, fill rule, one filled worked example each).
- Exactly 100 filled drafts at `jira/jira-comment-<KEY>-draft.md`, Jira wiki markup, first person, footer disclosure.
- The pack index: `reports/backlog-first-100-pack-round-1.md` with the 100 in posting order (key, family, draft path, evidence one-liner, live-check time, checks passed), an overflow list of the next 30, and the skipped-conflict list.
- A ledger stub at `receipts/comment-ledger/STUB-backlog-first-100.md`, ready to rename to the posting date when the owner sets it.

## Composition

Closure families first, in this order: closure-not-a-bug, closure-fixed, closure-obsolete, closure-duplicate, then ping-stale. Batches in order within a family. Stop at exactly 100.

Nothing posts. The pack, its family texts, and the spot-QA result go to the owner as one approval; posting follows the daily cap and the ledger when he gives the go.
