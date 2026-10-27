# Jira comments: the scoped list

Every comment for a ticket on this list is scoped through `comment-formula.md` (same folder; the coordination branch root copy governs) before anything posts. Posting still needs Nick's go, per comment. New tickets join the list when a comment is first contemplated, not after it posts.

## Formula review pass (DONE 2026-10-23; review at `formula-review-2026-10-23.md` in this folder)

Reviewed every Jira comment posted on 2026-10-23 against the new formula. Result: no edits proposed. All seven carry the header disclosure, which the formula grandfathers (posted text stands; the footer applies forward). On substance the four status notes and the 5754 closure pass; the 6213 ask is the reconfirmation beat the formula allows, and its mechanism bullet was verified accurate against the PR #5210 diff; SOLR-18470 is the pre-staged specimen, left as posted because the thread is live. One open decision for Nick: whether to apply the footer swap retroactively anyway (recommendation in the review: no).

## Drafts in hand, not posted

- **SOLR-17861 platform harmonization** (SOLR-17861, UI checksum classifiers): combined status draft replying to Christos Malliaridis's comment 18133004 on OS coverage. Windows 11 x64 run and Linux x86_64 gate both PASS at commit `b0b0b39f7e62` on `solr-17861-ui-license-check` (105 checksum files, all five Skiko runtime classifiers, regeneration unchanged, `:desktopApp:test` NO-SOURCE on both). No macOS host run; the draft asks whether the two host results address his concern or a macOS run is still wanted. Draft at `jira-comment-SOLR-17861-platform-harmonization-draft.md`; claim `claims/SOLR-17861-windows-linux-comment-harmonization-round-1-phone-agent.md`. The LICENSE/NOTICE scope question stays in the separate closing drafts, untouched. Waits on Nick's go.

- **SOLR-17845** (new UI OAuth): status note drafted by a pool agent on the old board, `jira-comment-SOLR-17845-draft.md`. The ticket still shows Patch Available though the OAuth work landed in PR #3754; two acceptance items remain open (token refresh, ID-token identity). The draft asks Christos whether to resolve it with those filed as sub-tasks. New footer form; ready for Nick's go.
- **SOLR-17988 follow-up**: owed and now structured. The pool property audit (DONE, on the new board at `reports/solr-17988-property-audit-round-1.md`) found real undocumented gaps and states in terms that the posted note's "looks complete" does not hold for the environment variables. The work is a parent assignment with subtasks on the new board, `assignments/solr-17988-subtasks-round-1.md` (one claim per ranked gap, verification first, then assembly into one docs PR); the follow-up is drafted after the PR opens, as item 1 of `assignments/pr-followups-round-1.md`. Delivery vehicle (Nick, 2026-10-23): the follow-up is an EDIT of the posted note (comment 18131215), not a second comment, because the ticket's only watchers are Eric and Nick; the edit replaces the "looks complete" passage with the verified findings and the PR pointer. The verification gate does not change: nothing is edited until the subtask chain confirms the gaps.
- **SOLR-17676** (Enable wasmJs tests): staged status note, `jira-comment-SOLR-17676-draft.md`. The pool no-browser probe (vm6, 2026-10-24, fork main 498a09aea9) is DONE: with no usable browser the build hard-fails at :shared:wasmJsBrowserTest (Karma launcher error), it does not skip. The draft now carries the checked fact in its ladder. Posting HELD at the owner's direction (2026-10-24, sleep on it); nothing posts without his fresh go.
- **SOLR-11787** (duplicate of SOLR-6213): draft on branch `solr-11787-draft-close` at `drafts/SOLR-11787/jira-comment.md`. Waits on Nick's decision on the duplicate link.

## To draft

- **SOLR-18119**: short note that PR #4999 was closed because PR #5180 carries the work.
- **SOLR-18249**: withdrawal comment (PR #4969 already closed).

## Posted

- **SOLR-18470**: plan comment posted 2026-10-23 (comment 18131208). Text: `jira-comment-SOLR-18470-draft.md`. Watching for a reply.
- **SOLR-18240, SOLR-17988, SOLR-12833, SOLR-16450**: Blocker status notes posted 2026-10-23 (comments 18131214, 18131215, 18131218, 18131220). Texts: `jira-notes-stale-blockers-draft.md`. SOLR-17988 has a pool property-transition audit in flight; a follow-up note only if the audit finds real gaps.
- **SOLR-6213**: clarification comment posted 2026-10-23 (18131122), edited the same day to add the review ask to Smiley. No file; the text lives on the ticket.
- **SOLR-5754**: closure recommendation posted 2026-10-23; PR #5073 closed with a pointer. No file; the text lives on the ticket.

## Dropped

- **SOLR-18297**: note dropped 2026-10-23. Jan Høydahl had already stated the status on the ticket that day; the note added nothing.

## Examples

Verbatim comments, each labeled with its shape. Posted examples keep the disclosure line they went out with; the formula's footer rule applies to new comments.

### Staged status note (SOLR-17676, draft)

[~malliaridis], I checked the current state of the wasmJs tests against this ticket.

The tests were disabled in [PR #3195|https://github.com/apache/solr/pull/3195] (February 2025), with the {{enabled = false}} override in the old {{solr/ui}} build file. In [PR #4650|https://github.com/apache/solr/pull/4650] that build file went away, and {{ui/shared/build.gradle.kts}} now declares the {{wasmJs}} browser target with the default test setup and no disable. I verified the result in the UI check workflow: in [this run|https://github.com/apache/solr/actions/runs/39835983878] (October 22) {{:shared:wasmJsBrowserTest}} executed alongside {{:shared:jvmTest}} and the build succeeded.

Is that run sufficient evidence for the ticket's CI criterion, for the shared module?

As I see the ladder from there:

 * Close on the shared-module enablement, treating the rest as follow-ups.
 * Stay open for {{ui/webApp}}, whose build file still sets {{enabled = false}} for its browser tests.
 * Stay open for the two criteria I have not verified either way: a local run on a machine without a browser, and the Jenkins agents.

My recommendation is the first: the shared tests are the bulk of the suite and they are running in CI again. If you want webApp tracked separately, I can file it as a follow-up.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_

### Plan comment (SOLR-18470, posted 2026-10-23; predates the staged form and the footer rule)

[~dsmiley], [PR #5202|https://github.com/apache/solr/pull/5202] covers SOLR-18446 only: the {{collection}} parameter can no longer override the path on distributed requests. It deliberately does not attempt this ticket.

Taking your advice on the dev list to line up a reviewer before starting work: before building anything here, I wanted to check whether this is a direction you want taken up, and whether you would be willing to review it.

The plan I have in mind:
 * Add path scope as a first-class characteristic of {{SolrQueryRequest}}: the collection or core named in the URL path, with an alias or comma-separated list resolved to its set of collections where that applies.
 * Let each handler judge whether it can honor that scope; a collection handler that cannot should return HTTP 400 rather than act on the core or node that happens to serve the request.
 * Start with collection handlers. Treat the core-path behavior, including {{SearchHandler}} searching the whole collection in SolrCloud mode, as a separate slice if you agree it is too much for one change.
 * Parameters such as {{shards}} may narrow the path scope, never expand it.

Three questions: is that first slice the right size, should the core path be in or out of it, and is there anything in the description you would now narrow or drop?

### Status note answering a stale question (SOLR-16450, posted 2026-10-23)

Answering [~gus]'s question from 2024: yes, this is complete, and the version is 9.1.0. The timeline on the ticket: the original fix resolved it for 9.1 on 2022-10-14; [~hiteshkhamesra] then flagged one more place in {{ZkStateReader}}, and [~ichattopadhyaya] reopened it for that follow-up on 2022-10-18. The follow-up fix ("waitForState to cleanly unregister watches if there is a failure") landed the same day on branch_9_1 as 74f09064947 and on main as 32e0f68a9bc, so both fixes are in the 9.1.0 release. The ticket's fixVersion was removed in January 2023 while it sat reopened; restoring 9.1.0 and closing as Fixed matches the record.

## Follow-up draft, not posted (SOLR-18470, vm6, 2026-10-24)

- **SOLR-18470 follow-up**: short new comment, not an edit of 18131208, drafted at `jira-comment-SOLR-18470-followup-draft.md`. It states that PR #5202 does only the narrowing step, that removal or deprecation for Solr 11 is the project's call, and that no further work happens without a reviewer who said yes. Analysis in `reports/solr-18470-framing-reconciliation-round-1.md`. Waits on Nick's go.

## Staged ask, not posted (SOLR-17861, vm6, 2026-10-24)

- **SOLR-17861 staged ask**: asks [~malliaridis] whether the evidence meets the ticket's bar and offers Options A to C, recommending A (a licenses check inside `ui`, after #4650). Draft at `jira-comment-SOLR-17861-draft.md`; record in `reports/ui-volunteer-drafts-round-1.md`. Waits on Nick's go.

## Staged ask, not posted (SOLR-17909, vm6, 2026-10-24)

- **SOLR-17909 staged ask**: asks [~malliaridis] whether the evidence meets the ticket's bar and offers Options A to C, recommending A (composable over a fixed list with preview, Decompose check in the same change). Draft at `jira-comment-SOLR-17909-draft.md`; record in `reports/ui-volunteer-drafts-round-1.md`. Waits on Nick's go.

## Staged ask, not posted (SOLR-17911, vm6, 2026-10-24)

- **SOLR-17911 staged ask**: the component already exists on fork branch `solr-17911-file-content-viewer` (8047b99ef0), so the ask is about opening the PR, not about starting. Asks [~malliaridis] whether the evidence meets the bar, offers Options A to C, recommends A. Draft at `jira-comment-SOLR-17911-draft.md`; correction to the index-query report recorded in `reports/ui-volunteer-drafts-round-1.md`. Waits on Nick's go.

## Staged ask, not posted (SOLR-17849, vm6, 2026-10-24)

- **SOLR-17849 check with Jane Sandberg**: one question to [~sandbergja] on whether she is still working on the ticket, given Nick's implementation on fork branches `solr-17849-index-query-header` and `solr-17849-index-query-screen`. Draft at `jira-comment-SOLR-17849-draft.md`. Waits on Nick's go.


## Critical batch 01 drafts, not posted

SOLR-4180, SOLR-6420, SOLR-5821, SOLR-7351, SOLR-7517, SOLR-5850, SOLR-6683, SOLR-8331, SOLR-8417, and SOLR-7926 now have individual comment drafts at `jira/jira-comment-SOLR-<KEY>-draft.md`. The closure-ready drafts are SOLR-6683, SOLR-7517, and SOLR-8417. See `reports/critical-comments-round-1-batch-01.md` for the evidence and `reports/closure-ready-drafts-index-round-1.md` for the running closure list. All remain unposted pending Nick's go.

## vm6 drafts, 2026-10-27 (not posted)

Drafts from vm6's pass over Eric Pugh's open tickets with no live PR. Each one carries the header disclosure and a single question to the reporter. Nothing here is posted. Posting needs Nick's go per comment.

- **SOLR-18138**: CLI message when ZooKeeper is unreachable. Text: `jira-comment-SOLR-18138-draft.md`. Report: `reports/solr-18138-cli-zk-unreachable-round-1.md`.
- **SOLR-18145**: queryUUID and taskUUID. Text: `jira-comment-SOLR-18145-draft.md`. Report: `reports/solr-18145-taskmanagement-names-round-1.md`.
- **SOLR-18405**: SLF4J lazy logging, measurement. Text: `jira-comment-SOLR-18405-draft.md`. Report: `reports/solr-18405-slf4j-lazy-logging-round-1.md`.
- **SOLR-18428, 18429, 18430, 18431**: v2 shard, replica and replica-property reads. Texts: `jira-comment-SOLR-18428-draft.md` through `jira-comment-SOLR-18431-draft.md`. Report: `reports/solr-18428-18431-v2-read-round-1.md`.
- **SOLR-18461**: curl examples on techproducts. Text: `jira-comment-SOLR-18461-draft.md`. Report: `reports/solr-18461-curl-examples-round-1.md`.
- **SOLR-18546**: gettingstarted to techproducts. Text: `jira-comment-SOLR-18546-draft.md`. Report: `reports/solr-18546-gettingstarted-round-1.md`.
- **SOLR-18553**: plurality and consistency. Text: `jira-comment-SOLR-18553-draft.md`. Report: `reports/solr-18553-plurality-audit-round-1.md`.
- **SOLR-18351**: HOLD. Open PR #5234 (move ContentStream to solr-core) and backport #5235 are not yet considered in the draft. Text: `jira-comment-SOLR-18351-draft.md`. Report: `reports/eric-fresh-scan-round-1.md`.
- **SOLR-18449**: HOLD. Open PR #4913 (standard test properties naming) is not yet considered in the draft. Text: `jira-comment-SOLR-18449-draft.md`. Report: `reports/solr-18449-test-sysprops-audit-round-1.md`.

Posting path: vm6 has no Jira credentials. Nick posts each text by hand, or the phone agent posts it with Nick's go. Each posting is recorded the day it posts in `receipts/comment-ledger/<YYYY-MM-DD>.md`, per `assignments/scan-streams-and-comment-cap-round-1.md`.
