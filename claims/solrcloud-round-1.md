# Claim: SolrCloud round 1

Claimed 2026-10-10 by Claude Code (AI agent), working for Nick Shanin.

Scope: `assignments/solrcloud-round-1.md` (commit `30ffe9683ff`). Audit first, then drafts under `pr-drafts/solrcloud/` for the draftable tickets.

Count note: the assignment's header says "thirty-one tickets" but lists thirty. The thirty listed are audited here. SOLR-18391 has two branches, so there are thirty-one branch refs. If a thirty-first ticket was meant, the owner should name it.

The core-admin round (`assignments/core-admin-round-1.md`, commit `e8c31a1ed69`) waits until this round has fully reported. The cap rule says rounds run one after another.

## Heads checked live on 2026-10-10

`git ls-remote`, then a read-only fetch of all 31 refs. Every named head matches its live head.

| Ticket | Branch | Live head | Named in the assignment | Result |
|---|---|---|---|---|
| 3865 | `solr-3865-submit` | `363e8f0651e9` | none (no gate) | audit only |
| 4754 | `solr-4754-submit` | `d2e9038881f6` | none (no gate) | audit only |
| 5813 | `solr-5813-submit` | `90b8baa08aef` | `90b8baa08ae` | matches |
| 7394 | `solr-7394-recovery` | `9857d9ee8029` | `9857d9ee802` | matches; live PR, consistency only |
| 9155 | `solr-9155-submit` | `9f08d0330233` | `9f08d033023` | matches |
| 10234 | `solr-10234-submit` | `16825538a766` | none (no gate) | audit only |
| 10641 | `solr-10641-submit` | `4ab4bd2040f3` | none (no gate) | audit only |
| 11288 | `solr-11288-submit` | `cd094c3c623f` | `cd094c3c623` | matches |
| 11479 | `solr-11479-submit` | `7e538e844c45` | `7e538e844c4` | matches; no gate |
| 12651 | `solr-12651-submit` | `f3131d1ee846` | `f3131d1ee84` (live tip) | matches the live tip; gated at older `90032e274b7`, flagged |
| 12991 | `solr-12991-submit` | `1a86966179e1` | `1a86966179e` | matches |
| 12998 | `solr-12998-submit` | `62a17a116b5b` | none (live PR) | consistency only |
| 13136 | `solr-13136-submit` | `486b38775565` | none (live PR) | consistency only |
| 13186 | `solr-13186-submit` | `b436d90d2a88` | `b436d90d2a8` | matches |
| 13239 | `solr-13239-submit` | `699a1fce368c` | `699a1fce368` | matches; submission-held |
| 13369 | `solr-13369-submit` | `dfa0db5bdf96` | `dfa0db5bdf9` | matches |
| 14919 | `solr-14919-submit` | `84e7bcaeb57d` | `84e7bcaeb57` | matches |
| 15035 | `solr-15035-submit` | `12d7491e82cd` | `12d7491e82c` | matches |
| 15106 | `solr-15106-submit` | `40b7e5d0efa7` | `40b7e5d0efa` | matches |
| 15386 | `solr-15386-submit` | `ca8cb61ee957` | `ca8cb61ee95` | matches |
| 15674 | `solr-15674-submit` | `ba01c83d4c5a` | `ba01c83d4c5` | matches |
| 15863 | `solr-15863-submit` | `f381fd8d4dd1` | `f381fd8d4dd` | matches |
| 16013 | `solr-16013-submit` | `ba26b7028917` | `ba26b702891` (live tip) | matches the live tip; gated at older `e1bd21fd11a`, flagged |
| 16437 | `solr-16437-submit` | `673ae584de03` | none (no gate) | audit only |
| 17281 | `solr-17281-submit` | `ba21ca32791b` | none (parked) | audit only |
| 17292 | `solr-17292-submit` | `e43200b0fb6d` | `e43200b0fb6` | matches |
| 17680 | `solr-17680-submit` | `f4b8ce833654` | `f4b8ce83365` | matches |
| 17733 | `solr-17733-submit` | `636196b7954a` | `636196b7954` | matches |
| 18277 | `solr-18277-submit` | `17c0e6448128` | `17c0e644812` | matches; merged, retire check |
| 18391 | `solr-18391-submit` | `3000eeede7ad` | none (draft live PR) | consistency only |
| 18391 | `solr-18391-graceful-create-submit` | `adcda10b501c` | `adcda10b501` (live tip) | matches; gated at older `3a0ff1262bf`, consistency only |

## Shared rules for every part

- Read only, except the report file and any draft named for your part. No commit, push, checkout, reset, merge, stash, or fetch of anything new. `git show`, `git log`, `git diff`, `git merge-tree`, `git rev-parse`, `git cat-file -e`, `git grep`, `git ls-tree`, and `git merge-base` are fine. Trial merges with `git merge-tree --write-tree` are fine if they write no ref.
- No builds, no Gradle, no tests, no test runs of any kind. No `gh` write calls.
- `gh pr view`, `gh pr list`, and `gh pr checks` are allowed for read-only consistency passes, through `C:\Users\shaninna\dev\Solr-issues\research\gh.ps1` (quote a JSON field list as one argument). Nothing that writes.
- Post nothing anywhere. No PR, no comment, no JIRA. No new Jira ticket unless someone asks.
- Proof numbers come only from the receipt named for the ticket. Gate logs are often not on disk; say so. Where a receipt says inconclusive by construction, or a pin, the draft says that, and does not claim a failing base run.
- Drafts follow `pr-formula.md`: the AI header as line 1, the Jira link line, a bold one-line summary in each section, "What happens today", "What this change does", "Proof", "A choice to check" (only with a live alternative and a pointed question), "Limits", the Changelog line with a link, and the AI assistance footer. Length guide about 3,500 characters. Each draft names the head it was written against in its Proof.
- Public text carries no internal process vocabulary (gate, receipt, ledger, handoff, top-up, takeover, audit, rc=0, fresh JUnit XML, pre-fix proof as a label, "premise run", internal log names). Plain words and short sentences. No em dash and no en dash in anything you write.
- Lucene version claims: main and branch_10x pin Lucene 10.4.0, and branch_9x pins 9.12.3. If one version is named in a draft, name every version that applies.
- A moved tip, or a diff that differs from the receipt, is flagged, not silently adopted.
- Every finding: file and line, evidence (SHA and line, receipt line, or command output), and exact replacement wording. Mark FIX or NOTE. If you cannot check something, say so under "Not checked". Do not guess.

## Parts

Six subagents, all launched together (the cap is six). Reports go to `reports/solrcloud-round-1-<part>.md`. Drafts go to `pr-drafts/solrcloud/`.

**Part p1: mzxid pair and gated singles (SOLR-15674, 5813, 17292, 12991).**
- 15674: gated (hardened), `IndexSchemaFactoryCacheTest` 1 of 1, proof inconclusive by construction. Paired with SOLR-15478 (configsets round 1, which is at `0478bdf0ac5`). Both replace a data-version freshness check with the znode mzxid. State the pairing and landing order in the report and in the draft. Check whether the draft's wording about version resets must match the configsets draft's wording.
- 5813: gated, `CloudDescriptorTest` 3 of 3. Choice: default to the core name, or reject the empty name. Limits: the unaddressed SOLR-5811 knock-on.
- 17292: gated (hardened), `TestPerReplicaStates` 4 of 4. The behavior change (persist now propagates failures that callers never saw) must be stated plainly.
- 12991: gated, focused class 1 of 1. Choice: WARN against keeping ERROR. Limits.
Draft each draftable one.

**Part p2: AddReplicaCmd and backup/restore (SOLR-11479, 15035, 12651, 15863).**
- 11479: no gate, audit only, no draft. The branch's code commit rejects `property.coreNodeName` when several replicas would share it. Note the overlap with 15035 (both change `AddReplicaCmd.java`) and the likely landing order, without auditing 15035 twice.
- 15035: gated (hardened), `AddReplicaTest` 4 of 4. The receipt says the banked patch alone was a no-op on current main. Draft if draftable.
- 12651: gated at older `90032e274b7` (`TestLocalFSCloudBackupRestore`, 2 tests); the live tip `f3131d1ee84` has moved since. Audit against the gated head, flag the move, and note that a re-gate at the live tip is main-side work owed before any opening. The ticket-thread question (whether restore cleanup should be optional for retry and resume) goes in the draft's description.
- 15863: gated at the live tip (`BackupCmdTest` 4 of 4, `BackupCoreAPITest` 6 of 6, `LocalFSCloudIncrementalBackupTest` 7 of 7). Premise grounded at the earlier gated head; inconclusive by construction at this head. The receipt owes one Choice and Limits lines. Check the shared test class with 12651.
Draft each draftable one.

**Part p3: overseer lifecycle and ZkController (SOLR-13186, 15106, 16013, 15386, 9155).**
- 13186: gated, `OverseerElectionContextTest` 1 of 1. PR-ready; Limits lines only.
- 15106: gated, `OverseerProcessorExitTest` 1 of 1. PR-ready. The untested processor-exit versus session-expiry race stays a Limits line with a follow-up offer.
- 16013: gated at older `e1bd21fd11a` (`OverseerCloseOrderingTest` 1 of 1); live tip `ba26b702891` matches the live tip, but the gated head is older. Flag, and note that a re-gate at the live head is main-side work owed.
- 15386: gated, `NodeMutatorTest` 3 of 3, proof inconclusive by construction. An owner question (cluster-state snapshot sufficiency) is open; put it in the report's owner list.
- 9155: gated, `ZkControllerGetLeaderTest` 1 of 1. Choice: SolrException with the interrupt flag restored, against a declared InterruptedException. Limits.
- Check the four overseer and ZkController branches (13186, 15106, 16013, 15386, 9155) for shared files and conflicting assumptions about overseer shutdown and election. Check 9155, 15386 and 16013 hunks in `ZkController`.
Draft each draftable one.

**Part p4: collections, aliases, file store, recovery (SOLR-11288, 13239, 13369, 14919, 17680, 17733, 18277).**
- 11288: gated, `TestCollectionAPI` 4 of 4. PR-ready but partial for its audit ticket: the escaping concern is unaddressed; the Limits name it with a follow-up offer.
- 13239: gated as-is, but submission-held regardless. Audit verifies the record only. No draft.
- 13369: gated, test only, `TriLevelCompositeIdRoutingTest` 1 of 1. Its proof was NOT obtained by a run: the premise stands by construction on the ticket's Jenkins record and the router reading. The draft's Proof must say that, and must not claim a failing base run.
- 14919: gated, `IgnoreCommitOptimizeUpdateProcessorFactoryTest` 1 of 1. The branch also touches `RecoveryStrategy.java`. Check its hunks against SOLR-7394's `solr-7394-recovery` branch (consistency only).
- 17680: gated, `CreateRoutedAliasTest` 15 of 15, `CreateAliasAPITest` 13 of 13. The draft must also say that the v2 aliases endpoint with two routers starts working, which the changelog title does not say.
- 17733: gated, `TestDistribFileStore` 1 test. The draft must state the contract change (an API delete now removes the ZooKeeper entry) and note that the ticket's sync complaint was not reproduced.
- 18277: gated (hardened), and its pull request has already merged on apache/solr. Retire candidate: confirm the merged state and report. No draft.
Draft each draftable one.

**Part p5: audit only (SOLR-3865, 4754, 10234, 10641, 16437, 17281).**
- 3865 and 4754: no gate, fresh arrivals; premise unverified; each branch still carries a `TESTING.md` note. Audit only; no draft. State what the premise run and first gate must show.
- 10234: no gate, premise unverified. Audit only; no draft.
- 10641: no gate; a sweep classified the tip as tidy-only drift (one `assertEquals` line join in its test). Audit only. Whether a passing pin ships as its own PR is an open owner call; state it as an owner decision. No draft.
- 16437: no gate; the only test evidence is a GitHub corroboration run at its head, which does not settle gate state. Audit only; no draft.
- 17281: no gate; parked under review on Nick's side. Audit only; no draft.

**Part p6: live PR consistency (SOLR-7394 `solr-7394-recovery`, 12998, 13136, 18391 both branches).**
- Use `gh pr list --repo apache/solr --head <branch>` and `gh pr view` (read only) to find each live PR and its head. Check each live PR's head against its receipt and against the branch's live head. Report any drift. Do not draft replacement PR text.
- 7394: gated at the live tip; already a live PR. Consistency only.
- 12998 and 13136: gated at older heads; already live PRs. Consistency only. Report the head difference; do not treat either head as wrong.
- 18391: two branches. `solr-18391-submit` is a draft PR; `solr-18391-graceful-create-submit` is gated green at the older `3a0ff1262bf`, with live tip `adcda10b501`. Consistency only for both. The alias-failure deletion question is already posed in the live PR's Choices, with a recommendation on record (keep the delete). Do not re-open it.
- Check the RecoveryStrategy hunks of 7394 against those of 14919 (report for 14919's draft; p4 holds the 14919 check).

## Deliverables

1. `reports/solrcloud-round-1-p1.md` through `-p6.md`. The lead writes `reports/solrcloud-round-1.md`, with per-ticket verdicts, interaction results, and owner decisions.
2. Drafts in `pr-drafts/solrcloud/` for the draftable tickets, each naming its head.

Not in scope: opening PRs, posting comments, editing branches or live PR descriptions, builds, Gradle, and test runs.

---
Status: DONE. Marked 2026-10-10 by vm1 (main agent) at Nick's direction, because the completed claim had not been marked. Completion verified from the deliverable on this branch: reports/solrcloud-round-1.md. The work was performed by the original claimant; this mark is a record correction, not a new claim.
