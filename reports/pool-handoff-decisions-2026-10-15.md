# Pool handoff decisions, 2026-10-15

Review-lane record for the handoff that came in on 2026-10-15 after the pr-prepare refactor. Each item states what was checked, then one of three outcomes: **decided** (under a recorded ruling or the decision default), **escalated** (needs Nick, with a recommendation), or **not verified** (could not be checked from this host).

Checked against origin/pr-prepare at 489dba10770 (2026-10-15, UTC). Live PR state was read over REST on 2026-10-15.

## Corrections to the handoff

- The retire-packs commit is 6e0910c5618. The handoff's 66ea9861b16 is not an ancestor of the branch. The other three SHAs in the handoff (559687b3ca0, cac628fdb26, 5a4e43e5751) are on the branch.
- Category convergence round 2 is not unstarted. The five response reports exist (`reports/<category>-convergence-round-2-response.md`), and the close-out W2 claims are DONE (2026-10-14). The settlements assignment is DONE.
- The morning slates file exists (`reports/morning-openings-slates-2026-10-14.md`), and the overnight close-out assignment is DONE.

## 1. PR #5117 body length and fail-before: escalated to Nick

- Live state: head a93f9f6a997e, draft, no comments, no reviews.
- Body is about 5,746 visible characters. The repo's public-text rule is about 3,500, with link URLs not counted.
- Authority: the body was replaced at 18:54:36Z under the strong-review assignment. The workspace AGENTS.md says a live body edit needs Nick's explicit go-ahead for that PR and that edit. Nick should confirm whether the edit stands.
- Fail-before: the body claims a fail-before only for the hits case. Two new tests have no recorded fail-before (gate step 7).
- Recommendation: cut the body to about 3,500 characters, claim only the recorded fail-before, and queue a vm2 fail-before run for the two tests if the wider claim is wanted. Any body edit waits for Nick's go-ahead.

## 2. SOLR-18010 prepared Choice block: decided, removed from the draft

- The block was uncommitted in the Windows worktree and said it waited on the verdict.
- The owner ruling recorded in `receipts/SOLR-18010.md` ("Owner ruling, 2026-10-13", permission-copy item) says no draft change follows; the question is settled in the post-merge rework. The block would pose that question now, so it was removed. The branch was not changed.
- The removed text is kept as a patch in the Windows review session's scratchpad (not on the branch). It can be restored with `git apply` if Nick wants the question posed before merge.

## 3. SOLR-18010 receipt logs: escalated to Nick

- `g18010-gate.log`, `g18010-premise.log` and `g18010-settle.log` are not on the branch. They are also absent from `research/`, `env/`, `reports/`, `material/` and `receipts/` (depth 3). A whole-workspace search timed out, so no claim is made beyond those folders.
- The receipt says the settle log is "found and on disk" and cites it for the base run. That wording is not supported by any record reachable from this host.
- Recommendation: amend the receipt to say the logs are not archived, and do not cite them as evidence. PR #5118's base-failure claims then need either a vm2 re-run with the logs copied into the record, or removal from the body.
- Receipts are the shared copy, so this review did not rewrite the receipt.

## 4. PR #5118 body sync: escalated to Nick

- Live state: head 571307400b6917ce458db989f29febb5cff8a600, draft. Its one comment is Nick's, from 2026-10-14.
- The receipt records that the live body still cites 2b5ad801c9e8.
- Recommendation: point the head references at 571307400b69 now, a fact-only fix. Keep the base-failure sentences out until item 3 is settled. The body edit waits for Nick's go-ahead.

## 5. Search-components PR numbers: explained, no PR action

- Cause: a malformed push in the rewrite lane (an empty source ref) deleted seven fork branches on 2026-10-14. GitHub closed their PRs and refuses to reopen them because the branches were recreated (see the `Incident during the run` line in each receipt).
- Replacements were opened as drafts the same day on Nick's approval (`reports/morning-openings-slates-2026-10-14.md`, replacement-openings note).

| Closed | Replacement | Branch |
|---|---|---|
| #5133 | #5154 | solr-6759-submit |
| #5143 | #5155 | solr-8051-submit |
| #5148 | #5156 | solr-9396-submit |
| #5149 | #5157 | solr-9595-submit |
| #5151 | #5158 | solr-10305-submit |
| #5152 | #5159 | solr-10424-submit |
| #5153 | #5160 | solr-10492-submit |

- The review records still cite the closed numbers. Read them with this table. No record was rewritten here.
- Process note for Nick: branch deletion is Nick's action under WORKFLOW.md. The deletion was accidental. A guard against empty refspecs in the rewrite lane would prevent a repeat.

## 6. Merged-PR follow-up ledger (`reports/merged-pr-followup-promises-round-1.md`)

- **#4953 (SOLR-8291, SOLR-13217): decided.** Open #5004 does not discharge the promise. It discharges when #5004 merges. This is the harder reading under the decision default; Nick may reverse it.
- **#5001 (SOLR-18317): escalated.** Opening a PR from `ae918a03fa7b` is a PR opening, which needs Nick's approval. Whether the SOLR-15823 pair counts as the discharge is Nick's call.
- **#5070 (SOLR-4841): escalated.** A new JIRA ticket is a public write. Nick directs it.
- **#5104 (SOLR-2681): escalated.** The nested-form follow-up is a public commitment. Nick confirms the plan.

## 7. Retire conditions

- **SOLR-18119 and PR #4999: decided.** Keep #4999 open until PR #5061 lands or is accepted, as the stated condition requires.
- **SOLR-17882: escalated.** The premise-dead call needs Nick's acceptance.
- **SOLR-18277: not verified.** The cached Jira packet (`research/jira-context/SOLR-18277.json`, fetched 2026-06-06) reads Open. The current Fixed status needs a live read, which this session did not do.
- **SOLR-17722: decided.** The only probe record is on vm3. VM3 is down by standing rule, so no re-probe is possible. The item stays open and unverified.

## Still open, not started by this review

- Four-PR strong review round 2 is blocked on VM2 gates. Nothing for the Windows lane until those land.
- `assignments/pool-vm2-open-decisions-round-1.md` (OPEN, unclaimed) is the next Windows review item. Three of its four items need Nick: the SOLR-17055 maxScore contract, the SOLR-17842 fork push, and the SOLR-4374 include-or-follow-up call.

## Update, 2026-10-15: Nick accepted the recommendations

Done, each verified by a read-back against the approved text:

- **PR #5117 body** replaced with the cut version (about 3,490 visible characters, links not counted). Head `a93f9f6a997e` unchanged, still a draft, no comments or reviews. The cut claims a fail-before only for the seven first-version tests, which is what the record shows. The two later exactness tests have no fail-before: their gate run passed on the merge-base, so the body makes no claim for them.
- **PR #5118 body**: the head references already cite `571307400b69`, so no head sync was needed. The receipt's note that the body still cites `2b5ad801c9e8` was out of date. The two base-failure sentences in the Proof section were removed. Head `571307400b69` unchanged, still a draft, one comment (Nick's), no reviews.
- **SOLR-18010 receipt** (commit `62d6006d58a`, pushed fast-forward): the three cited logs are recorded as not archived on any reachable record. The "found and on disk" wording is corrected, and the results attributed to those logs are marked unverified from a log.
- **SOLR-18010 draft**: the prepared Choice block was removed under the 2026-10-13 ruling (see item 2).
- **Branch history**: nothing was rewritten, and the commits from this review stay in place. This review will not rewrite `pr-prepare`.

Still open on the PR #5118 body: its Choice paragraph poses the permission-copy question to maintainers. The 2026-10-13 ruling says that item waits for the post-merge rework. Nick should decide whether the paragraph stays.

Process note: the rewrite lane's empty-ref guard was accepted in principle. This review did not locate the rewrite lane's script from this host, so the guard is not implemented here.

### #5001 (SOLR-18317): recommended path (a), new ticket and PR

The promise, verbatim from the ledger: the server-side hardening "is preserved intact on the fork branch `solr-18317-server-submit`, to be opened as its own PR."

The SOLR-15823 pair (#5030 and #5031) does not carry that hardening. It adds the V2 logging broadcast and migrates the Admin UI. It does not change `RemoteRequestProxy`, `GenericV1RequestProxy`, `V2SolrRequestBasedProxy`, `SystemInfoHandler`, `MetricsHandler`, or the v1 logging tail (`reports/solr-18317-server-coverage-round-1.md`). The gap is live on main for four endpoints.

- **Path (b)**, confirming the pair as the discharge, would close the promise on a PR that lacks the promised change. Not recommended.
- **Path (a)** is recommended:
  1. Nick opens a new JIRA ticket for the gap, using the text below.
  2. The server branch gets a gate at its head under the new ticket key. It has none today (`receipts/SOLR-18317.md`).
  3. Nick approves the PR opening. The PR links the new ticket, which discharges the promise.

The ledger's 2026-10-14 entry records an owner ruling that keeps the promise on the pair. That ruling's text is not in any record here, and the coverage check of the same date contradicts it. This recommendation supersedes it once Nick confirms path (a). The cached Jira packet for SOLR-18317 (`research/jira-context/SOLR-18317.json`, dated 2026-07-24) reads Open, so the ledger's "Resolved" is also unverified.

Choices made for the draft ticket, each a default Nick can change:

1. Standalone `nodes=all` is answered by the node on the v1 logging, v1 system info and v2 node system endpoints. The V2 logging endpoint keeps the 400 that SOLR-15823 gives it, and the ticket asks that question openly.
2. Metrics with `node=all` or an explicit node name return 400 in standalone mode.
3. File the ticket now, so the SOLR-15823 author has a target for the v1 tail.

Draft ticket text (for Nick to post; not posted):

> **Title:** Standalone node throws NullPointerException for nodes or node parameters in proxy-capable admin endpoints
> **Type:** Bug. **Priority:** Major (as SOLR-18317).
>
> 🤖 _AI text below_ 🤖 _(posted on behalf of Nick Shanin)_
>
> Found by reading the code at main 7767d36d29f. The endpoints were not run, so the exact status codes are not confirmed.
>
> Related: SOLR-18317 (the Admin UI half of the same report), SOLR-15823 (open; V2 logging broadcast).
>
> The {{nodes}} and {{node}} parameters on a standalone node (not running in SolrCloud) fail with a NullPointerException, or with a 500 error that wraps one, in four endpoints:
> * {{/admin/info/logging}} (v1), for reads and for {{set}}
> * {{/admin/info/system}} (v1)
> * {{/api/node/system}} (v2)
> * {{/admin/metrics}} (v1), with {{node}}
>
> For example, {{GET /solr/admin/info/logging?nodes=all}} fails. A {{set}} request with {{nodes=all}} applies the level change on the node and then fails the same way. The shared proxy code (RemoteRequestProxy.validateNodeNames and GenericV1RequestProxy.shouldProxy) reads the ZooKeeper controller without checking that one exists. The Admin UI no longer sends {{nodes=all}} in standalone mode, but other clients, older UIs and direct calls still get the error.
>
> Expected behavior:
> * {{/admin/info/logging}}, {{/admin/info/system}} and {{/api/node/system}}: {{nodes=all}} is answered by the node itself. Other {{nodes}} values return 400 with a message that the parameter requires SolrCloud mode.
> * {{/admin/metrics}}: {{node=all}} and explicit node names return 400 with the same kind of message.
>
> Outside this ticket: the V2 logging endpoint ({{/api/node/logging/levels}}). SOLR-15823 gives it a 400 for {{nodes}} in standalone mode. Open question: should the V2 logging endpoint answer {{nodes=all}} locally too, for consistency with v1? This ticket does not decide it.
