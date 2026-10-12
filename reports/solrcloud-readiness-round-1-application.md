# SolrCloud readiness round 1: application of the verdicts

Verdict. The five READY drafts are confirmed. The four Part 2 drafts are applied. The three Part 3 items are reconciled or have their settling information named. Five fork branches were changed and pushed, and each needs a gate at its new head before its draft is ready. Nothing is opened, posted, or merged.

Source: `assignments/pool-solrcloud-readiness-round-1-application.md`. Claim: `claims/pool-solrcloud-readiness-round-1-application.md`. Nick's direction on 2026-10-12 also asked for the recommended changes to be implemented, so the branch edits below were made here. The assignment's rule against review-side branch work was set aside for that direction. Each changed branch is handed to a Linux gate job before it counts as ready.

Live tips were re-read with `git ls-remote` on this run. Every tip below matches the value in its receipt or draft, except the branches listed under "Branch edits", which moved on purpose.

## Part 1: the five READY TO OPEN drafts

- **SOLR-5813** (tip `90b8baa08aef`, receipt line 4): READY CONFIRMED. No patch. The Proof counts match the receipt (3 of 3, and 1 base failure, receipt lines 6 and 7). The reject route is posed as a Choice (draft lines 23 to 27). The SOLR-5811 Limit matches receipt line 8. The changelog link names the tip.
- **SOLR-12991** (tip `1a86966179e1`, receipt line 4): READY CONFIRMED. No patch. The test class `RecoveryStrategyLeaderUnreachableLogTest` exists on the branch. It waits 60 seconds and asserts an IOException in the cause chain, as the draft says. The WARN Choice and the two Limits match receipt lines 6 to 8.
- **SOLR-16437** (tip `f72a9edd0a8b`, receipt line 5): READY CONFIRMED. No patch. The draft counts (25 tests, 1 skipped, 0 failures, recorded 2026-10-12) match receipt lines 3 and 9. The base failure is `testAddReplicaPropRejectsUnknownReplica` (receipt line 8). Note for the owner: the head's subject, "remove the handoff note (packaging for the first gate)", stays as it is under the adopted rule in `gates/SOLR-16437.md` line 8, which keeps subjects. It shows on the PR's commit list.
- **SOLR-17292** (tip `f614a42fbc80`, receipt line 4): READY CONFIRMED. No patch. Counts match receipt line 6 (4 of 4). The base failure matches line 7. The node-down remedy is the head commit (receipt line 8), and the draft's call-site paragraph and duplicate-ERROR Limit match it.
- **SOLR-11479** (tip `360dca60fcfa`, receipt line 4): READY CONFIRMED after one patch. Patch: draft line 22 had "Both new tests pass.- Base commit" run together. It is now two bullets. The branch has no `SOLR-11479-TESTING.md`, and its changelog title already names the one-replica limit (checked on the fork tip). The landing order after SOLR-15035 is stated (draft line 38). It opens after SOLR-15035.

## Part 2: the four DECISION drafts with a recorded adoption

- **SOLR-14919** (tip `c1fa37e33c1`): DRAFT APPLIED, BRANCH DONE, GATE OWED. The draft states the processor-only route as settled. The Choice section is removed. Limits name the recovery 403 that this route leaves in place, and the marker as a follow-up with its own test. The branch has three commits on top of the gated head: `e5eafd26064` removes the recovery marker and fixes the test comment, `c1fa37e33c1` fixes the processor javadoc, and the changelog title now matches the route. Gate job: `gates/SOLR-14919-processor-only-gate.md`. The material file says the opening stays held until the owner rules on the route. This route follows Nick's standing instruction to adopt recorded recommendations. Nick should confirm it before the opening.
- **SOLR-11288** (tip `cbf0c678f277`): DRAFT APPLIED, BRANCH DONE, GATE OWED. The lead of the Choice now states that a blank value means the same as an omitted one. The question to the maintainer stays, as the record says. The branch changelog title now names the blank-only behavior (replacement from `reports/solrcloud-round-1-p4.md`, finding 1). Gate job: `gates/SOLR-11288-title-regate.md`. The Proof and code citations stay at the gated head `cd094c3c623`.
- **SOLR-15386** (tip `ca8cb61ee957`, no branch change): APPLIED AND RE-CHECKED, one correction not applied. The Choice now opens with the adopted position. The question to the maintainer stays. The Proof is already in formula order. The correction not applied: the record asks for the Proof date from the takeover log (round 35, 2026-10-07). A takeover-log citation is not a date source under the readiness rules, so no date was added. The date comes in with a dated gate record.
- **SOLR-16013** (tip `408317d7e241`, receipt lines 5 to 10): APPLIED AND RE-CHECKED, no patch. The draft cites `408317d7e241` and no old head or old title (grep, no matches for the old head `966bb4c4eb44` or the co-author trailer). The history rewrite is already executed and re-gated green. See Note A.

## Part 3: the three open items

- **SOLR-12651**: the record governs, and the readiness report misread it. The record adopts delete-on-failure, and the retry question stays in the draft's Choice (`material/solrcloud-round-1-answers.md`, SOLR-12651, the adopted lines). The record also adopts the landing order, 9598 before 12651 (`material/replication-backup-round-1-answers.md`, line 92). Neither item is open. Done here: the branch changelog title now states the cleanup as it covers it (commit `404f13b501c`, finding 9), and the draft's changelog link names that head. The draft's Proof is not bracketed; that correction was already in. Still open: the gate at the new head, and SOLR-9598's own default call, which the record leaves with its owner (`material/replication-backup-round-1-answers.md`, line 152).
- **SOLR-13369** (tip `dfa0db5bdf96`): INFORMATION NAMED, and the record governs. The record adopts the test-side rule, as drafted (`material/solrcloud-round-1-answers.md`, SOLR-13369). The readiness report's "no recorded recommendation" was a misreading. The Choice stays a question to the maintainer, and no draft edit was made, as the assignment asks. The fact that would settle the Choice for the maintainer is a run where one route prefix really lands in two shards and the test's one-shard rule fails. The ticket's failing run is named in its summary (`research/jira-context/SOLR-13369.json`), but the log is not in the packet. The packet cannot be read here as JIRA is not reachable. A seed sweep on a Linux lane that reproduces a split prefix would settle it. Six seeds on base did not (the draft says so).
- **SOLR-9155** (tip `9f08d0330233`): INFORMATION NAMED. The ticket text settles the direction. `research/jira-context/SOLR-9155.json` line 4 says `getLeader` "does not handle InterruptedException, and instead rethrows it as a SolrException". That names the wrapping as the problem. The declared route, `throws InterruptedException` on `getLeader`, follows the ticket more closely than the implemented route. Still needed before that route can be built: the enclosing method of the register caller at `solr/core/src/java/org/apache/solr/cloud/ZkController.java` line 1485 (on the branch), to see whether it already handles a checked exception. The rejoin caller at line 2608 already sits inside a `catch (Exception e)`. This is not implemented here. Nothing can be compiled on this host, so the change waits for that read and a gate. The recorded date (2026-10-06) is a ledger citation, and it needs a dated gate line.

## Branch edits (pushed)

| Ticket | New head | Previous gated head | Change | Gate job |
|---|---|---|---|---|
| SOLR-15863 | `78dff1d52f6c` | `f381fd8d4dd1` | changelog title capitalized | `gates/SOLR-15863-title-regate.md` |
| SOLR-11288 | `cbf0c678f277` | `cd094c3c623f` | changelog title names the blank-only behavior | `gates/SOLR-11288-title-regate.md` |
| SOLR-12651 | `404f13b501c9` | `f3131d1ee846` | changelog title states the cleanup as it covers it | `gates/SOLR-12651-title-regate.md` |
| SOLR-14919 | `c1fa37e33c1e` | `84e7bcaeb57` | recovery marker removed; test comment and processor javadoc corrected; changelog title | `gates/SOLR-14919-processor-only-gate.md` |
| SOLR-17680 | `ce59bbe112d0` | `f4b8ce83365` | message-only rewrite of the head commit body (tree identical); forced with lease | `gates/SOLR-17680-message-regate.md` |

The SOLR-17680 force-push is the first use of the allowance Nick granted on 2026-10-12. It is recorded in AGENTS.md under "Force-push allowed on fork submit branches".

Note A (history rewrites): SOLR-16437 (message-only, tree identical, `--force-with-lease`, receipt line 5) and SOLR-16013 (trailer removal, `--force-with-lease`, receipt line 10) were rewritten earlier, and SOLR-17680 is rewritten here. AGENTS.md says the fork is never force-pushed except under the lease rule. Nick should confirm all three.

## Gate and record jobs posted (unclaimed)

- Re-gates at the new heads: `SOLR-14919-processor-only-gate.md`, `SOLR-17680-message-regate.md`, `SOLR-15863-title-regate.md`, `SOLR-11288-title-regate.md`, `SOLR-12651-title-regate.md`.
- Dated records owed: `SOLR-13186-dated-run.md` (focused run at `b436d90d2a88`), `SOLR-17733-dated-run.md` (focused run at `636196b7954a`), and `SOLR-15106-premise-base.md` (base compile failure text, then a dated run at `40b7e5d0efa7`).

## Other drafts edited

- SOLR-15674 (tip `ba01c83d4c5a`): the Choice section is replaced by a settled statement that the check uses the mzxid alone, as the record adopted. The question to the maintainer is gone. The length is not reduced: the draft is about 4,984 characters with links, and each link backs a claim in the Proof or the changes. Owner call: accept the length, or ask for a further trim.
- SOLR-17680 (`f4b8ce83365` to `ce59bbe112d`): links re-pointed to the rewritten head. The tree is identical, so the code citations are unchanged. The Proof sentence still names the gated head `f4b8ce83365`. The unsourced "10 before, 11 now" count from the slate is already gone from the draft.
- SOLR-12651 and SOLR-15863: changelog links re-pointed to the new heads. Their Proof sentences still name the gated heads.

## Readiness report: correction note (not edited in place)

Replacement text for `reports/solrcloud-readiness-round-1.md`, to be applied by the main side:

> Correction, 2026-10-12. SOLR-12651: the Choice on optional restore cleanup is ADOPTED (delete-on-failure stays; the question stays in the draft), and the landing order after SOLR-9598 is ADOPTED (9598 before 12651). Neither is open; the readiness hold is the gate at the new head 404f13b501c and SOLR-9598's own default call. SOLR-13369: the test-side rule is ADOPTED (`material/solrcloud-round-1-answers.md`); the Choice stays a maintainer question. SOLR-17680: the slate's "10 before, 11 now" count is no longer in the draft. SOLR-15386 and SOLR-11288: the Choice stays a maintainer question with a settled lead. SOLR-5813, SOLR-12991, SOLR-16437, SOLR-17292 and SOLR-11479 are unchanged in verdict.

## Owner items (not done here)

- SOLR-15035: the JIRA summary check. JIRA is not reachable from this session.
- SOLR-9598: the RESTORE default call (record recommendation: wait by default with an opt-out).
- SOLR-9155: the register-caller read, then the declared-route decision.
- SOLR-13369: the failing-run or seed evidence for the Choice.
- SOLR-14919: confirm the processor-only route before the opening; read the 2018 reason (SOLR-12933).
- SOLR-15674: accept the length or ask for a further trim.
- Note A: confirm the three history rewrites.
- SOLR-16437: the commit subject on the PR's commit list (the adopted rule keeps it).

## Not done

- Nothing was opened, posted, or merged, and no PR description was edited.
- No build, test, Gradle run, or JIRA call was made. Every branch edit is text-only or a revert plus a comment, and each one waits for its gate.
