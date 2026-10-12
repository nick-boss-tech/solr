# Pool assignment: moved-heads draft re-check, round 1

Capability tags: `review`. Intended host: windows. Staffing: up to 3 subagents in parallel, one draft each. Claim: this round runs under the standing coordinator claim, claims/coordinator-review-rounds.md.

## Purpose

Six branches moved after their PR drafts were written: a fix landed, a fixture was corrected, a branch was rebased, or a commit-message rewrite produced a new head on an identical tree. Each draft must agree with the gate receipt at the branch's new head. Under the owner's patch-loop directive of 2026-10-12, this round patches as well as checks: where the new receipt makes the needed edit mechanical (head citations, Proof counts and dates, changelog link re-points), the review side applies the edit directly to the draft, commits it on pr-prepare, and then re-reviews its own patch in the second half of the assignment. Anything that needs judgment beyond the receipt is not patched; it goes in the report as a finding with exact replacement text.

Heads below are as recorded in receipts/ and verified by ls-remote when this assignment was posted. Verify the branch tip again at claim time; if a tip has moved since, check the draft against the receipt at the live tip and say so in the report.

## Items with a draft on record

- **SOLR-11939, pr-drafts/core-admin/SOLR-11939.md.** Gated head cdb9903506e9c20c32aa4ec2be77f500235399ba (packaged head; docs shape, site build at the head). An earlier repoint round already moved the draft's citations to this head. Verify every citation: the change links at cdb9903506e9, the pre-change links at base cabedd1d968059215188f4e7563fb303241899ed, the changelog link, and the Proof statement (site builds at the head, verified 2026-10-12; changelog fragment parses and passes the changelog lint). Then check the Limits against the nuance recorded in receipts/SOLR-11939.md on 2026-10-12: for CREATE the fallback is absent, but ADDREPLICA does fall back to property.name when its name parameter is blank (AddReplicaCmd lines 351 to 356 at the base). The draft must not claim the fallback is absent for ADDREPLICA; its wording stays scoped to CREATE, or states the ADDREPLICA fallback plainly, with the follow-up offer standing. Patch mechanically if any citation or the Limits wording is off.
- **SOLR-17731, pr-drafts/core-admin/SOLR-17731.md.** Fixed head 18165d35a7e3761afb11ecb26218a4b3c9525dbe (pre-fix head f2b4ba164f). The draft already cites the new head. Verify: change links at 18165d35a7, pre-change links at base e2cdb2d7e8ae0be4cf6cf0606206c67d89e7278f, Proof counts against receipts/SOLR-17731.md (V2ResourcePathOverlapTest 2 of 2 and ListAliasesAPITest 2 of 2 at the head, from fresh JUnit XML on 2026-10-12; proof leg fails with the two HTTP 405 responses), and that the change description matches the fix commits' actual delta (comments, the two license header lines and the changelog title only, per the receipt's delta check), with no claim left over from the pre-fix draft. Patch mechanically where the receipt settles the text.
- **SOLR-16437, pr-drafts/solrcloud/SOLR-16437.md.** Rewritten head f72a9edd0a8b1aa1dded6af6c0c792fbb6179aa2. The tree is identical to the previously gated head aa2a6b8afb6 (tree cd87c72b946e5bae22b96c2a4f60dbd8be319cbc at both), so the expected outcome is a head re-point only: no count, claim or wording should change. The draft's links already point at f72a9edd0a8b. Two named suspects in the Proof: the short head string printed there, and its recorded date, both to be checked character by character against receipts/SOLR-16437.md (focused CollectionsAPISolrJTest 25 tests, 1 skipped, 0 failures at the head, run 2026-10-12; proof leg exactly 1 failure; caller-class runs at the head the same day). Patch the head string and date mechanically if they do not match the receipt.

## Items with no draft on record (scope as corrected at posting)

A filename and content search of pr-drafts/ at the posting tip finds no draft for the three tickets below. There is nothing to patch. For each, confirm the absence from your own reading of pr-drafts/, and record the verdict NO DRAFT ON RECORD in the report, with the receipt facts listed so a later draft commission starts from them. Draft writing is not part of this round.

- **SOLR-6711.** New gated head 73c5e6e0d243a9886da0a25efe0acc7f1bbf7ec2 after the fixture fix (the earlier gate at 7ddf5dc6f4 failed on a test fixture defect; the fix allows the test's URL through in @BeforeClass and clears it in @AfterClass, fixture config unchanged). Receipt facts: TestPersistedPollDisabled 1 run, 0 failures at the head; premise shown by content at the merge base; module check passes.
- **SOLR-17612.** New gated head 3a456c27dda52061e95eeabf9bd228cc48e1ea47 after the fixture fix (one line in the new test block, rebuilding the spellcheck dictionary after the fixture documents commit). Receipt facts: DistributedSpellCheckComponentTest 1 run, 0 failures at the head; the proof leg fails by content at the distributed suppression (the merged response suppresses the suggestion while the control returns it), not at the control-data validation.
- **SOLR-16013.** Fixed head 966bb4c4eb449853994594b8474ccf24eec59d87 (one commit on ba26b7028917: the interrupt flag is restored after the state reader and ZooKeeper client close). Receipt facts: OverseerCloseOrderingTest 2 of 2 at seed 2E201C17D50DCEAA and 2 of 2 at seed 16013C0FFEE16013; pre-fix proof passes; full gate green at the head. A later draft for this branch must describe the interrupt-restore fix and carry these counts.

## Patch-loop rules

- Patch only what the receipt settles: head SHAs in citations and links, Proof counts, run dates, and changelog link targets. Keep the draft's voice and structure; change nothing else.
- Drafts are public-bound text: no internal process vocabulary in any patched line, and keep each section's bold one-line summary shape per the PR formula on pr-prepare.
- Commit draft patches on pr-prepare in the same push discipline as the report (claim first, heartbeat, one push per finished slice is fine).
- Second half of the assignment: re-read each patched draft against its receipt line by line, as a fresh check. The report states both passes: what was patched, and what the re-check found afterward.

## Out of scope

The SOLR-16013 fixture assignment (assignments/solr-16013-overseer-fixture.md) is superseded by the fix at 966bb4c4eb44 and is not part of this round. No branch is edited, built or gated in this round; build work stays with the gate jobs. No new draft is written. Settled convergence verdicts for these categories stand and are not re-opened beyond the moved-head checks above.

## Deliverable

reports/moved-heads-draft-recheck-round-1.md: verdict first, then one section per ticket with its verdict: PATCHED AND RE-CHECKED (naming each edit made), CONVERGED UNCHANGED, NOT CONVERGED (with findings graded blocking, should-fix or note, register prefix MH-R1, and exact replacement text for anything left unpatched), or NO DRAFT ON RECORD for the three corrected items. The draft patches themselves are committed on pr-prepare by the review side under the patch-loop rules above.

Rules: WORKFLOW.md binds (claim before work, hourly heartbeats, subagent cap of 6 to 7 across all hosts, and mark the claim DONE in the same push as the deliverable). No em dashes in authored text.
