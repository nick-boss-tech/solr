# Merged PR follow-up promises, ledger, round 1

## Pass dated 2026-10-23, phone-agent-2 (07:35 to 07:39 UTC, pass ad)

Claim: `claims/pool-phone-agent-2-merged-pr-followup-promises-round-1-ad.md`. This pass supersedes earlier factual states; earlier passes remain below as history. External work was read-only. Only this report and its claim are updated on `coordination`. No PR, issue, Jira entry, product branch or email was posted or edited.

### What changed

The merged set is **13**, not 10. Three PRs have merged since the previous pass: #5170 on October 21, then #5030 and #5193 on October 22. The latest merge is #5193 at 2026-10-22T16:46:35Z. All eight seed PRs remain in the merged set.

Three original future promises remain without the specific merge or follow-up opening recorded as their completion condition: #4953, #5001 and #5070. The newly merged #5030 also names its read-side follow-up, but that submission is already present as #5031. Thus this pass tracks four named follow-up items: three open under the evidence-based ledger convention and one already submitted. The SOLR-17393/9637 trigger is not yet due and remains outside the merged-body ledger. The historical owner disposition for #5001 is preserved separately below rather than silently treated as a new technical discharge.

Several old blockers are stale. The custom-language test's independent reviews are satisfied. SOLR-18549 has a current green receipt. The nested-highlighting branch now exists and is gated. The SOLR-13059 six-test changes are on the two open routed-alias PR heads, and their bodies no longer promise a separate follow-up. The old anchor-check offer is not carried by #5180.

### Live census and evidence

The [closed-PR API search](https://api.github.com/search/issues?q=repo%3Aapache%2Fsolr%20author%3Anick-boss-tech%20is%3Apr%20is%3Aclosed%20base%3Amain&per_page=100) returned 26 closed PRs. Each was read through `GET /repos/apache/solr/pulls/{number}`; only author `nick-boss-tech`, base `main` and non-null `merged_at` were retained. This yields 13 merged PRs. The [open-PR search](https://api.github.com/search/issues?q=repo%3Aapache%2Fsolr%20author%3Anick-boss-tech%20is%3Apr%20is%3Aopen&per_page=100) returned 89 PRs, all individually read. Neither search was truncated or marked incomplete. Every merged description below is the live closed PR body, not a draft. All-author repository searches for SOLR-18549, SOLR-17393 and SOLR-9637 returned no PR; SOLR-4841 returned only #5070.

### Current merged-body ledger

| PR and ticket | Merged at (UTC) | Follow-up state |
| --- | --- | --- |
| [#4939](https://github.com/apache/solr/pull/4939), SOLR-18135 | 2026-09-24T13:24:24Z | No follow-up promise in the description |
| [#4953](https://github.com/apache/solr/pull/4953), SOLR-8291 / SOLR-13217 | 2026-09-30T20:04:41Z | OPEN: verified test-only #5004 remains unmerged |
| [#4995](https://github.com/apache/solr/pull/4995), SOLR-18196 | 2026-10-03T02:06:45Z | No follow-up promise in the description |
| [#4959](https://github.com/apache/solr/pull/4959), SOLR-18277 | 2026-10-05T10:46:07Z | No follow-up promise in the description |
| [#5001](https://github.com/apache/solr/pull/5001), SOLR-18317 | 2026-10-05T14:58:58Z | OPEN on the specific server-PR evidence; historical OWNER RULED KEPT noted below |
| [#4965](https://github.com/apache/solr/pull/4965), SOLR-10198 | 2026-10-07T13:19:33Z | No follow-up promise in the description |
| [#5012](https://github.com/apache/solr/pull/5012), SOLR-13202 | 2026-10-07T15:03:48Z | No follow-up promise in the description |
| [#5070](https://github.com/apache/solr/pull/5070), SOLR-4841 | 2026-10-10T10:46:38Z | OPEN: test and reviews ready; follow-up submission absent |
| [#5062](https://github.com/apache/solr/pull/5062), SOLR-18523 | 2026-10-17T00:11:16Z | No follow-up promise in the description; companion anchor-check offer noted below |
| [#5027](https://github.com/apache/solr/pull/5027), SOLR-18391 | 2026-10-17T00:24:06Z | No follow-up promise in the description |
| [#5170](https://github.com/apache/solr/pull/5170), SOLR-18368 context, explicitly no ticket of its own | 2026-10-21T18:08:16Z | No follow-up promise in the description |
| [#5030](https://github.com/apache/solr/pull/5030), SOLR-15823 | 2026-10-22T11:38:48Z | DISCHARGED as a submission: named levels-GET follow-up #5031 is open |
| [#5193](https://github.com/apache/solr/pull/5193), SOLR-18565 | 2026-10-22T16:46:35Z | No follow-up promise in the description |

#### #4953: separate SOLR-16130 test-only PR

Promise, verbatim: "This also fixes SOLR-16130, I plan to submit a separate, test-only PR for that."

State: **OPEN under the ledger's recorded merge condition**. [#5004](https://github.com/apache/solr/pull/5004) remains open at `3c48dec4b79faed625c4fb79833d0975a55f3ed7`. Its live body links the fixed root cause to #4953. Its current file list contains only `CrossCollectionJoinQueryTest.java` and the ccjoin test configset's `solrconfig.xml`, confirming the separate test-only submission already exists. `receipts/SOLR-16130.md` records the gate and fail-before proof.

The October 14 pass explicitly retained OPEN until #5004 merges; later passes offered Nick the narrower interpretation that submission alone suffices. This pass does not silently change that recorded convention. Concrete next step: maintainer review and merge of the existing PR, not another test branch or PR. Whose call: maintainers for merge; Nick only if changing the ledger's completion convention.

#### #5001: separate server-side hardening PR

Promise, verbatim: "The server-side hardening that degraded such calls to the local node was split out at reviewer request and is preserved intact on the fork branch `solr-18317-server-submit`, to be opened as its own PR."

State: **OPEN on evidence of the specific server-side opening**. The named branch remains at `ae918a03fa7b622853aa1c5ba3e53ab567e2fef7`; `solr-18317-ready` remains at `c5d63b1a67c1842a70dd4fdec39f39e1bcfd9b16`. The newer standalone proxy fix is `solr-18549-submit` at `b89664d164db46cdac432668e2d9a07ce95fbd37`. `receipts/SOLR-18549.md` records green proof at that tip, with the Java gate at `cf4b86934697` and the later title-only changelog adjustment revalidated. `pr-drafts/solr-18549/PR-draft.md` is explicitly not opened and requires Nick's approval. No SOLR-18549 PR was found.

Historical disposition: the October 14 pass records **OWNER RULED KEPT**, based on the SOLR-15823 GET/PUT pair, and later passes distinguish that ruling from the separate server-side gap. #5030 is now merged; #5031 is open. These facts advance that pair but do not show a separate server-hardening PR was opened. The original promise's degrade-to-local behavior is also not the same as #5030's documented standalone `nodes` rejection with 400. This pass preserves the owner's historical disposition and records the factual gap without making a new owner ruling or claiming a fresh code-coverage audit.

Concrete next step: use the existing gated SOLR-18549 change and draft for the server-side submission, subject to Nick's approval. No new ticket or replacement branch is needed merely because the old SOLR-18317 ticket is resolved. Whose call: Nick for the public opening. No action is executed here.

#### #5070: test through a custom language identifier

Promise, verbatim: "A follow-up submission is planned for a test that runs language detection through a custom identifier."

State: **OPEN as a submission, implementation/review SATISFIED**. `solr-4841-langid-followup-submit` remains at `bf1b01d0dc0ecb0455a19c2cb3276f5a1318d437`. `receipts/SOLR-4841-followup.md` records green proof. Independent review rounds 2 and 3 are already SATISFIED in `reports/SOLR-4841-followup-round-1.md`; the October 22 status scrub explicitly retains ticket filing and PR opening as Nick's public actions (`reports/owner-items-scrub-2026-10-22.md`, second-pass row and public-actions list). A SATISFIED implementation header does not prove the promised submission happened.

The original SOLR-4841 Jira ticket is now Resolved / Fixed, unlike earlier passes. That resolves the original constructor change, not the distinct follow-up submission. The all-author SOLR-4841 PR search still returns only #5070, and no open PR uses the follow-up head.

Concrete next step: approve and file the distinct follow-up ticket, update its packaging references as required by the recorded direction, and approve opening the ready follow-up PR. Do not repeat completed reviews. Whose call: Nick for ticket filing and opening.

#### #5030: named read-side follow-up already submitted

The final body names the levels-GET follow-up as already open. Verbatim:

> Our plan for the read side: the levels GET, which is the read half of the screen this PR migrates and the one place V1 with `nodes=all` already aggregates, is open as [#5031](https://github.com/apache/solr/pull/5031), a separate follow-up PR stacked on an earlier head of this PR; the messages GET and the threshold PUT stay out unless maintainers ask for them, and the follow-up PR names them in its Limits.

State: **DISCHARGED as a named submission**, not merged. [#5031](https://github.com/apache/solr/pull/5031) remains open at `251460b7c14fa4764c04096a69c2c7d279f9d2d2`. Its live body explicitly says it was restacked onto current main after #5030 merged and reports the updated head's tests. This corrects #5030's older description of it as stacked on an earlier head. No new submission is required to fulfill that named plan; maintainer review/merge remains outstanding. The messages GET and threshold PUT are conditional on maintainer requests, not unconditional future promises.

#### Why the other descriptions do not add promises

#4939 and #4995 contain no later-work plan. #4959's unmeasured rollback window, #4965's exclusions and API tradeoffs, and #5012's missing-`v` limitation describe scope or behavior, not planned later work. #5027's "Follow-up at adcda10b501." describes work already in that PR; "Happy to open a follow-up ticket and PR for them on request." is conditional. #5062 says "Happy to open a follow-up ticket for it." and refers to the companion's anchor-check offer; it does not commit to implementing that check. #5170 says "I can add one here, or in a follow-up, on request." about an upgrade note, an offer rather than a commitment. #5193 lists limited investigation and test coverage, with no follow-up plan.

### Pending and conditional shapes outside the merged ledger

- **#5100 / SOLR-18532**, open at `7dfd3d98d0a2f016c520139cbca96ea4a49f6689`. Promise, verbatim: "The SOLR-9865 change edits the same rollback lines and writes back only the previous directory name. Once this change lands, I will follow up on that branch by dropping its rollback edit, so the two do not conflict." **NOT YET DUE / pending merge**. `solr-9865-submit` remains `4937608bb181efae104c0d6f0257f445af50bf52`. After #5100 merges, the concrete next step is removing the overlapping rollback edit and revalidating the existing branch before opening. Nick approves the opening; implementation hosts can carry out the scoped branch work when assigned. This audit changes nothing.
- **#5104 / SOLR-2681**, open at `a3b1ea7994d932cdf78667f799542d7d823f7d49`. Promise, verbatim: "A follow-up pull request is planned for the nested forms." **PENDING MERGE**, outside this round's merged ledger. The old "no branch" note is obsolete: `solr-2681-nested-submit` exists at `cec42ef4f061ff7b81a4610a91ee76aa83b806ff`, with a green receipt in `receipts/SOLR-2681-nested.md`. `assignments/pool-solr-2681-nested-forms-round-1.md` records the separate follow-up; its ticket and PR are not filed/opened. Next step is Nick's approval for those public actions, not rebuilding the nested fix.
- **#5061 / SOLR-18119**, closed without merge at 2026-10-18T18:50:40Z, head `660faedd026ddc77f8736a4d1ab5cb89a4253a4a`. Exact old offer: "A follow-up change can restore the anchor check; it is not in this PR." **CLOSED UNMERGED / outside the ledger**, not pending an imminent merge. [#5180](https://github.com/apache/solr/pull/5180) is open at `89c8e184fe7102bebfa946b82b52a8425ab604a5`; its current body concerns release-only Changes.html generation and Jenkins preparation. It contains no anchor-check follow-up wording. It therefore does not establish a replacement for the old anchor offer. Next step, if restoring that coverage is wanted: Nick selects and approves a scoped follow-up; none is created by this audit.
- **#5089 / SOLR-13943**, open at `aa14590303d059025fe7b5baaaf6ad784ad53787`. This remains the correct PR, not the assignment's #5090. The previous "A follow-up submission for those six tests is planned." is no longer in its live body. The current exact text is: "The six tests in `TimeRoutedAliasUpdateProcessorTest` run in normal mode on this head. No further change is planned." It incorporates #5088's `960355fce36f` changes. **OLD PENDING PLAN REPLACED BY WORK IN EXISTING OPEN PRs**, not a merged discharge. Both PRs remain open; the SOLR-13059 receipt records their proof. No separate `solr-13059-submit` ref exists, but its absence no longer means the six-test work is missing. Maintainers review/merge the existing PRs; no new follow-up is proposed here.
- **#5088 / SOLR-13696**, open at `960355fce36ff254611190b21b8ceaf3fc887a0e`. Its old promise to restore commitWithin coverage is also gone from the live body. It now explicitly leaves an honest commitWithin check out and says "Maintainers should decide." This is a disclosed coverage tradeoff, not an unconditional submission plan. The old pending promise is not imported into the merged ledger.
- **#5090 / SOLR-14262**, open at `1e8d2b0075d7fded72ac19a049db27123a46ec10`. No SOLR-13059 follow-up promise in its body; no follow-up promise in the description.
- **SOLR-17393 then SOLR-9637**: no merged description carries this plan, and all-author searches return no PR for either ticket. Branches remain `626241e647e5fcc5ac6cc485f6a96d4ffe12e00a` and `c50fa4ffd932ede86c36b0748ee6aabe34d30e0a`. The earlier ledger attributes this sequence to drafts, but those two historical draft paths are absent from the current coordination tree, so their old quotations are not presented as newly verified draft evidence. No live merged body names this promise. **NOT YET DUE**. The SOLR-17393 receipt is green at its existing head. Nick approves opening that first PR; only after its merge does the SOLR-9637 rebase/opening sequence become due.

### Public Jira and fork verification

Public Jira REST reads used `GET /jira/rest/api/2/issue/{key}?fields=summary,status,resolution,updated,issuelinks`. The first attempt used GitHub's media type and returned 406; retrying with `Accept: application/json` succeeded for all 13 keys. No unavailable state is carried forward as a successful read.

| Public Jira | Current state |
| --- | --- |
| [SOLR-4841](https://issues.apache.org/jira/browse/SOLR-4841) | Resolved / Fixed, changed from prior ledger |
| [SOLR-18317](https://issues.apache.org/jira/browse/SOLR-18317) | Resolved / Fixed |
| [SOLR-18549](https://issues.apache.org/jira/browse/SOLR-18549) | Open |
| [SOLR-16130](https://issues.apache.org/jira/browse/SOLR-16130) | Open |
| [SOLR-17393](https://issues.apache.org/jira/browse/SOLR-17393) | Open |
| [SOLR-9637](https://issues.apache.org/jira/browse/SOLR-9637) | Open |
| [SOLR-18532](https://issues.apache.org/jira/browse/SOLR-18532) | Open |
| [SOLR-9865](https://issues.apache.org/jira/browse/SOLR-9865) | Open |
| [SOLR-18119](https://issues.apache.org/jira/browse/SOLR-18119) | Open |
| [SOLR-13059](https://issues.apache.org/jira/browse/SOLR-13059) | Open |
| [SOLR-13943](https://issues.apache.org/jira/browse/SOLR-13943) | Open |
| [SOLR-2681](https://issues.apache.org/jira/browse/SOLR-2681) | Open |
| [SOLR-15823](https://issues.apache.org/jira/browse/SOLR-15823) | Open |

Fork heads above came from the [matching-ref API](https://api.github.com/repos/nick-boss-tech/solr/git/matching-refs/heads/solr-), which returned 1,239 refs, including the newly recorded nested and SOLR-18549 heads. Relevant live PR bodies and receipts establish the distinctions between built, gated, submitted and merged. No code was rebuilt, no arbitrary fork head was treated as a discharge, and no new owner ruling was inferred from a satisfied implementation status.

### Concrete next steps, none executed

1. Keep #5004 as the single test-only candidate and wait for its review/merge under the existing ledger convention.
2. Put the existing gated SOLR-18549 draft before Nick for opening approval; preserve the historical #5001 owner disposition alongside the factual server-PR gap.
3. Put the already-reviewed SOLR-4841 follow-up before Nick for ticket/opening approval; do not repeat its completed review or gate.
4. Track #5031 as an already-submitted follow-up awaiting review/merge, not as missing work.
5. Keep #5100 and SOLR-17393 dependent work behind their actual merge triggers. Keep the gated nested fix ready for approved public filing/opening. Do not create redundant SOLR-13059 or anchor-check submissions from stale text.

---

## Pass dated 2026-10-19, vm4 (overnight recurrence check)

Claim: `claims/pool-vm4-merged-pr-followup-promises-round-1-overnight.md`. Read-only: GitHub API reads only. Nothing posted, filed or edited.

Merged set: 10, re-derived with the search API (`repo:apache/solr author:nick-boss-tech is:pr is:merged`), same set as the last pass; latest #5027 merged 2026-10-17T00:24:06Z. No new merge.

Change since the last pass: #5061 (SOLR-18119, changelog-to-HTML converter in Java, head `660faedd026d`) is closed, not merged, closed at 2026-10-18T18:50:40Z. It was carried as pending-merge for the fragment-anchor promise. That promise now has no open PR. Reopening, resubmitting or dropping it is the owner's call; this pass does not change its state. The SOLR-18119 release-path work continues in #5180, which is a different change; whether #5180 carries the fragment-anchor promise has not been checked.

Not re-read in this pass: the other open PRs (#5004, #5088, #5089, #5090, #5100, #5104). Their heads are as recorded in the pass below.

## Pass dated 2026-10-18, vm5 (16:38Z recurrence check, pass k)

Claim: `claims/pool-vm5-merged-pr-followup-promises-round-1-f.md`. Read-only: GitHub API reads, `ls-remote` on the fork, and JIRA REST reads. Nothing posted, filed or edited.

Counts for this pass: merged PRs 10 (re-derived with the search API, `is:pr is:merged author:nick-boss-tech`, same set as pass j; latest #5027 merged 2026-10-17T00:24:06Z). Promises found in merged bodies 3, discharged 0, open 3, not yet due 1. No PR has merged since pass j.

Checked this pass:

- Open PRs: #5004 `3c48dec4b79f`, #5061 `660faedd026d`, #5088 `6914eae1c2ed`, #5090 `1e8d2b0075d7`, #5100 `7dfd3d98d0a2`, #5104 `a3b1ea7994d9`, all still open at the heads recorded in pass j. #5089 moved from `ca443f6f6806` to `33fa8d13819a` (pushed 2026-10-18T16:36:33Z to `solr-13943-submit`: a merge of the SOLR-13696 head with the SOLR-13059 test fix). It is still open. None of the ledger's open or not-yet-due promises is about #5089, so the move discharges nothing.
- Fork heads unchanged from pass j: `solr-4841-langid-followup-submit` `bf1b01d0dc0e`, `solr-18317-server-submit` `ae918a03fa7b`, `solr-18317-ready` `c5d63b1a67c1`, `solr-16130-test-followup` `3c48dec4b79f`, `solr-9865-submit` `4937608bb181`, `solr-17393-submit` `626241e647e5`, `solr-9637-submit` `c50fa4ffd932`. `solr-13059-submit` is still absent.
- JIRA states re-read this pass: SOLR-4841 Open, SOLR-18549 Open, SOLR-18317 Resolved, SOLR-17393 Open, SOLR-9637 Open, SOLR-18532 Open, SOLR-18119 Open, SOLR-13059 Open, SOLR-9865 Open, SOLR-13943 Open. Unchanged from pass j.
- Open promises (SOLR-4841 follow-up, SOLR-18549 server-side promise, #4953) and the not-yet-due SOLR-17393/9637 item are unchanged in state, with the discharge paths as recorded in pass g.

## Pass dated 2026-10-18, vm2 (12:57Z recurrence check, pass j)

Claim: `claims/pool-vm2-merged-pr-followup-promises-round-1-l.md`. Read-only: GitHub API reads, `ls-remote` on the fork, and JIRA REST reads. Nothing posted, filed or edited. Host id from `/workspace/.pool-host`.

Counts for this pass: merged PRs 10 (re-derived with the search API, `is:pr is:merged author:nick-boss-tech`, each confirmed base `main` with `merged_at` set), promises found in merged bodies 3, discharged 0, open 3, not yet due 1. The seed PRs #4939, #4953, #4959, #4965, #4995, #5001, #5012, #5070 all came back merged. No PR has merged since #5062 (2026-10-17T00:11Z) and #5027 (2026-10-17T00:24Z).

Checked this pass:

- Open PRs, heads unchanged from pass i: #5004 `3c48dec4b79f`, #5061 `660faedd026d`, #5088 `6914eae1c2ed`, #5089 `ca443f6f6806`, #5090 `1e8d2b0075d7`, #5100 `7dfd3d98d0a2`, #5104 `a3b1ea7994d9`. All still open.
- Fork heads unchanged from pass i: `solr-4841-langid-followup-submit` `bf1b01d0dc0e`, `solr-18317-server-submit` `ae918a03fa7b`, `solr-18317-ready` `c5d63b1a67c1`, `solr-16130-test-followup` `3c48dec4b79f`, `solr-9865-submit` `4937608bb181`, `solr-17393-submit` `626241e647e5`, `solr-9637-submit` `c50fa4ffd932`. `solr-13059-submit` is still absent.
- JIRA states re-read this pass: SOLR-4841 Open, SOLR-18549 Open, SOLR-18317 Resolved, SOLR-17393 Open, SOLR-9637 Open, SOLR-18532 Open, SOLR-18119 Open, SOLR-13059 Open, SOLR-9865 Open, SOLR-13943 Open, SOLR-2681 Open, SOLR-16130 Open. No new tickets for the open promises.
- Open promises (SOLR-4841 follow-up, SOLR-18549 server-side promise, #4953) and the not-yet-due SOLR-17393/9637 item are unchanged in state, with the discharge paths as recorded in pass g. No discharge is executed.

## Pass dated 2026-10-18, vm2 (12:49Z recurrence check, pass i)

Claim: `claims/pool-vm2-merged-pr-followup-promises-round-1-j.md`. Read-only: GitHub API reads and `ls-remote` on the fork. Nothing posted, filed or edited. Host id from `/workspace/.pool-host`.

Counts for this pass: merged PRs 10 (re-derived with the search API, `is:pr is:merged author:nick-boss-tech`), promises found in merged bodies 3, discharged 0, open 3, not yet due 1. No PR has merged since #5062 (2026-10-17T00:11Z) and #5027 (2026-10-17T00:24Z). The merged set is unchanged: #4939, #4953, #4959, #4965, #4995, #5001, #5012, #5027, #5062, #5070.

Checked this pass:

- Open PRs, heads unchanged from pass h: #5004 `3c48dec4b79f`, #5061 `660faedd026d`, #5088 `6914eae1c2ed`, #5089 `ca443f6f6806`, #5090 `1e8d2b0075d7`, #5100 `7dfd3d98d0a2`, #5104 `a3b1ea7994d9`. All still open.
- Fork heads unchanged from pass h: `solr-4841-langid-followup-submit` `bf1b01d0dc0e`, `solr-18317-ready` `c5d63b1a67c1`, `solr-16130-test-followup` `3c48dec4b79f`, `solr-9865-submit` `4937608bb181`, `solr-17393-submit` `626241e647e5`, `solr-9637-submit` `c50fa4ffd932`. `solr-13059-submit` is still absent.
- Open promises (SOLR-4841 follow-up, SOLR-18549 server-side promise, #4953) and the not-yet-due SOLR-17393/9637 item are unchanged in state, with the discharge paths as recorded in pass g. JIRA states were not re-read this pass.

## Pass dated 2026-10-18, vm4 (12:05Z recurrence check, pass h)

Claim: `claims/pool-vm4-merged-pr-followup-promises-round-1.md`. Read-only: GitHub API reads and `ls-remote` on the fork. Nothing posted, filed or edited. The host id for this pass came from the operator, because `~/.pool-host` is missing on this VM.

Counts for this pass: merged PRs 10 (re-derived with the search API, `is:pr is:merged author:nick-boss-tech`), promises found in merged bodies 3, discharged 0, open 3, not yet due 1. No PR has merged since #5062 (2026-10-17T00:11Z) and #5027 (2026-10-17T00:24Z). The merged set is unchanged: #4939, #4953, #4959, #4965, #4995, #5001, #5012, #5027, #5062, #5070.

Checked this pass:

- Open PRs match the heads recorded in pass g: #5004 `3c48dec4b79f`, #5061 `660faedd026d`, #5088 `6914eae1c2ed`, #5089 `ca443f6f6806`, #5090 `1e8d2b0075d7`, #5100 `7dfd3d98d0a2`, #5104 `a3b1ea7994d9`. All still open.
- Fork heads match pass g: `solr-4841-langid-followup-submit` `bf1b01d0dc0e`, `solr-18317-ready` `c5d63b1a67c1`, `solr-16130-test-followup` `3c48dec4b79f`, `solr-9865-submit` `4937608bb181`, `solr-17393-submit` `626241e647e5`, `solr-9637-submit` `c50fa4ffd932`.
- Open promises (SOLR-4841 follow-up, SOLR-18549 server-side promise, #4953) and the not-yet-due SOLR-17393/9637 item are unchanged in state. The discharge paths are as recorded in pass g and are not restated here.

## Pass dated 2026-10-18, vm2 (10:37Z recurrence check, pass g)

Claim: `claims/pool-vm2-merged-pr-followup-promises-round-1.md`. Read-only: GitHub API reads, branch-ref reads on the fork, and public Apache JIRA reads. Nothing posted, filed or edited. No PR has merged since #5062 (2026-10-17T00:11Z) and #5027 (2026-10-17T00:24Z), so this pass re-checks states without adding entries.

Counts for this pass: merged PRs 10 (re-derived with the search API, `is:pr is:merged author:nick-boss-tech`), promises found in merged bodies 3, discharged 0, open 3, not yet due 1 (SOLR-17393/9637, see below).

Checked this pass:

- Merged set unchanged at 10: #4939, #4953, #4959, #4965, #4995, #5001, #5012, #5027, #5062, #5070. All eight seed PRs came back merged. The final bodies of all ten were re-read; the promise sentences in #4953, #5001 and #5070 match the ledger verbatim, and the Limits and choice lines in #4959, #4965, #5012, #5027 and #5062 are unchanged in kind (scope exclusions and offers on request, not promises).
- Named shape for SOLR-17393 and SOLR-9637: no merged body names the per-shard top-count cut or either ticket. The only merged body that refers to SOLR-18119 is #5062, and that is the anchor-checking item below. SOLR-17393 has a fork branch (`solr-17393-submit`, `626241e647e5`) but no PR in the open or merged set, so the trigger has not happened.
- Open PRs, heads unchanged from the vm3 09:28Z pass except where noted: #5004 `3c48dec4b79f`, #5061 `660faedd026d` (first recorded in this pass), #5088 `6914eae1c2ed`, #5089 `ca443f6f6806`, #5090 `1e8d2b0075d7`, #5100 `7dfd3d98d0a2`, #5104 `a3b1ea7994d9`. All still open.
- Fork heads unchanged: `solr-18317-server-submit` `ae918a03fa7b`, `solr-17393-submit` `626241e647e5`, `solr-9637-submit` `c50fa4ffd932`, `solr-9865-submit` `4937608bb181`, `solr-16130-test-followup` `3c48dec4b79f`, `solr-4841-langid-followup-submit` `bf1b01d0dc0e`, `solr-18317-ready` `c5d63b1a67c1`. `solr-13059-submit` is still absent (404).
- JIRA states unchanged: SOLR-4841 Open, SOLR-16130 Open, SOLR-18317 Resolved, SOLR-18549 Open, SOLR-17393 Open, SOLR-9637 Open, SOLR-18532 Open, SOLR-18119 Open, SOLR-13059 Open, SOLR-9865 Open, SOLR-13943 Open, SOLR-2681 Open.

Correction to the named shapes: the assignment cites #5090 for the SOLR-13943 follow-up. On apache/solr, #5089 is SOLR-13943 (`solr-13943-submit`) and #5090 is SOLR-14262. The ledger tracks #5089. Nothing in #5090 changes the ledger.

Open promises, unchanged in substance from the vm4 00:45Z pass: SOLR-4841 follow-up (#5070), SOLR-18549 server-side promise (#5001), and #4953 (open #5004 as a candidate discharge, Nick's ruling). The discharge paths are as recorded there and are not restated here.

## Pass dated 2026-10-18, vm3 (09:28Z recurrence check, pass f)

Claim: `claims/pool-vm3-merged-pr-followup-promises-round-1.md`. Read-only: GitHub API reads, `ls-remote` on the fork, and public Apache JIRA reads. Nothing posted, filed or edited. No PR has merged since #5027 (2026-10-17T00:24Z), so this pass re-checks states without adding entries.

Counts for this pass: merged PRs 10 (re-derived with the search API, `is:merged`), promises found in merged bodies 3, discharged 0, open 3, not yet due 0.

Checked this pass:

- Merged set unchanged at 10. All eight seed PRs (#4939, #4953, #4959, #4995, #5001, #4965, #5012, #5070) came back merged. The bodies of all ten merged PRs were scanned for follow-up, Limits, choice, planned and submit lines. The promise sentences in #4953, #5001 and #5070 match the ledger verbatim. No new promise line appears in the other seven.
- Open PRs #5004, #5061, #5088, #5089, #5090, #5100 and #5104 are still open, heads unchanged from pass e (#5088 at `6914eae1c2ed`).
- Fork heads unchanged: `solr-18317-server-submit` `ae918a03fa7b`, `solr-17393-submit` `626241e647e5`, `solr-9637-submit` `c50fa4ffd932`, `solr-9865-submit` `4937608bb181`, `solr-16130-test-followup` `3c48dec4b79f`, `solr-4841-langid-followup-submit` `bf1b01d0dc`, `solr-18317-ready` `c5d63b1a67`. `solr-13059-submit` is still absent.
- JIRA states unchanged: SOLR-4841 Open, SOLR-16130 Open, SOLR-18317 Resolved, SOLR-18549 Open, SOLR-17393 Open, SOLR-9637 Open, SOLR-18532 Open, SOLR-18119 Open, SOLR-13059 Open, SOLR-9865 Open, SOLR-13943 Open, SOLR-2681 Open.
- Coordination since 07:00Z: no new report, gate, receipt or PR-draft entry for SOLR-4841, SOLR-18549 or the SOLR-18317 server branch.

## Pass dated 2026-10-18, vm5 (07:12Z recurrence check, pass e)

Claim: `claims/pool-vm5-merged-pr-followup-promises-round-1-e.md`. Read-only: GitHub API reads. Nothing posted, filed or edited. No PR has merged since #5027 (2026-10-17T00:24Z), so this pass re-checks states without adding entries.

Counts for this pass: merged PRs 10 (re-derived with the search API, `is:merged`), promises found in merged bodies 3, discharged 0, open 3, not yet due 0.

Checked this pass:

- Merged set unchanged at 10. #4953, #5001 and #5070 are still merged with the same merge dates.
- Open PRs #5004, #5061, #5088, #5089, #5090, #5100 and #5104 are still open, heads unchanged from pass d (#5088 at `6914eae1c2ed`).
- Fork heads unchanged from pass d: `solr-18317-server-submit` `ae918a03fa7b`, `solr-17393-submit` `626241e647e5`, `solr-9637-submit` `c50fa4ffd932`, `solr-9865-submit` `4937608bb181`, `solr-16130-test-followup` `3c48dec4b79f`, `solr-4841-langid-followup-submit` `bf1b01d0dc`. `solr-13059-submit` is still absent.

## Pass dated 2026-10-18, vm5 (07:06Z recurrence check, pass d)

Claim: `claims/pool-vm5-merged-pr-followup-promises-round-1-d.md`. Read-only: GitHub API reads, `ls-remote` on the fork, and public Apache JIRA reads. Nothing posted, filed or edited. No PR has merged since #5027 (2026-10-17T00:24Z), so this pass re-checks states without adding entries.

Counts for this pass: merged PRs 10 (re-derived with the search API, `is:merged`), promises found in merged bodies 3, discharged 0, open 3, not yet due 0.

Checked this pass:

- Merged set unchanged at 10. The promise lines in the bodies of #4953, #5001 and #5070 were re-read and match the ledger verbatim.
- Open PRs #5004, #5061, #5088, #5089, #5090, #5100 and #5104 are still open, heads unchanged from pass c (#5088 at `6914eae1c2ed`).
- Fork heads unchanged: `solr-18317-server-submit` `ae918a03fa7b`, `solr-17393-submit` `626241e647e5`, `solr-9637-submit` `c50fa4ffd932`, `solr-9865-submit` `4937608bb181`, `solr-16130-test-followup` `3c48dec4b79f`, `solr-4841-langid-followup-submit` `bf1b01d0dc`. `solr-13059-submit` is still absent.
- JIRA states unchanged: SOLR-4841 Open, SOLR-16130 Open, SOLR-18317 Resolved (Fixed), SOLR-17393 Open, SOLR-9637 Open, SOLR-18532 Open, SOLR-18119 Open, SOLR-13059 Open, SOLR-9865 Open, SOLR-18549 Open, SOLR-15823 Open.

## Pass dated 2026-10-18, vm5 (07:02Z recurrence check, pass c)

Claim: `claims/pool-vm5-merged-pr-followup-promises-round-1-c.md`. Read-only: GitHub API reads, `ls-remote` on the fork, and public Apache JIRA reads. Nothing posted, filed or edited. No PR has merged since #5027 (2026-10-17T00:24Z), so this pass re-checks states without adding entries.

Counts for this pass: merged PRs 10 (re-derived with the search API, `is:merged`), promises found in merged bodies 3, discharged 0, open 3, not yet due 0.

Checked this pass:

- Merged set unchanged at 10. All eight seed PRs (#4939, #4953, #4959, #4995, #5001, #4965, #5012, #5070) came back merged.
- Open PRs #5004, #5061, #5089, #5090, #5100 and #5104 are still open, heads unchanged from the 05:27Z pass.
- #5088 is still open, but its head moved from `da4fa6df1178` to `6914eae1c2ed` (branch `solr-13696-submit`). It carries no promise from a merged body, so the ledger is unchanged.
- Fork heads unchanged: `solr-18317-server-submit` `ae918a03fa7b`, `solr-17393-submit` `626241e647e5`, `solr-9637-submit` `c50fa4ffd932`, `solr-9865-submit` `4937608bb181`, `solr-16130-test-followup` `3c48dec4b79f`, `solr-4841-langid-followup-submit` `bf1b01d0dc`. `solr-13059-submit` is still absent.
- JIRA states unchanged: SOLR-4841 Open, SOLR-16130 Open, SOLR-18317 Resolved (Fixed), SOLR-17393 Open, SOLR-9637 Open, SOLR-18532 Open, SOLR-18119 Open, SOLR-13059 Open, SOLR-9865 Open, SOLR-18549 Open, SOLR-15823 Open.

## Pass dated 2026-10-18, vm5 (05:27Z recurrence check, pass b)

Claim: `claims/pool-vm5-merged-pr-followup-promises-round-1-b.md`. Read-only: GitHub API reads, `ls-remote` on the fork, and public Apache JIRA reads. Nothing posted, filed or edited. No PR has merged since the 03:45Z pass below, so this pass re-checks states without adding entries.

Counts for this pass: merged PRs 10 (re-derived with the search API, `is:merged`), promises found in merged bodies 3, discharged 0, open 3, not yet due 0.

Checked this pass:

- Merged set unchanged at 10. All eight seed PRs (#4939, #4953, #4959, #4995, #5001, #4965, #5012, #5070) came back merged.
- Open PRs #5004, #5061, #5088, #5089, #5090, #5100 and #5104 are still open, with heads matching the 03:45Z pass.
- Fork heads unchanged: `solr-18317-server-submit` `ae918a03fa7b`, `solr-17393-submit` `626241e647e5`, `solr-9637-submit` `c50fa4ffd932`, `solr-9865-submit` `4937608bb181`, `solr-16130-test-followup` `3c48dec4b79f`, `solr-4841-langid-followup-submit` `bf1b01d0dc`. `solr-13059-submit` is still absent.
- The bodies of #4953, #5001 and #5070 were re-read. The promise text matches the ledger.
- JIRA states unchanged: SOLR-4841 Open, SOLR-16130 Open, SOLR-18317 Resolved (Fixed), SOLR-17393 Open, SOLR-9637 Open, SOLR-18532 Open, SOLR-18119 Open, SOLR-13059 Open, SOLR-9865 Open, SOLR-18549 Open, SOLR-15823 Open.


## Pass dated 2026-10-18, vm5 (current, 03:45Z recurrence check)

Claim: `claims/pool-vm5-merged-pr-followup-promises-round-1.md`. Read-only: GitHub reads, `ls-remote` on the fork, and JIRA reads. Nothing posted, filed or edited on GitHub or JIRA. No PR has merged since the vm4 pass below (00:45Z), so this pass re-checks states without adding entries.

Counts for this pass: merged PRs 10 (re-derived with `is:merged`), promises found in merged bodies 3, discharged 0, open 3, not yet due 0.

Changes from the vm4 pass below:

- #5070 (SOLR-4841): the follow-up branch `solr-4841-langid-followup-submit` now exists on the fork at `bf1b01d0dc`, and `gates/SOLR-4841-langid-followup.md` records GATE GREEN at that head (vm4, `receipts/SOLR-4841-followup.md`). The vm4 pass below says the branch and gate file were missing; that is no longer true. The promise stays OPEN: review rounds 2 and 3 by a reviewer other than the builder are not done, no follow-up JIRA ticket exists (the only SOLR-4841 hit is the 2013 ticket, still Open), and no PR is open.
- JIRA re-queried: SOLR-18549 Open, SOLR-18317 Resolved (Fixed), SOLR-15823 Open, SOLR-16130 Open, SOLR-13059 Open, SOLR-17393 Open, SOLR-9637 Open, SOLR-18532 Open, SOLR-18119 Open, SOLR-4841 Open. No new tickets.
- Open PR heads match the ledger: #5004 `3c48dec4b79f`, #5061 `660faedd026d`, #5089 `ca443f6f6806`, #5100 `7dfd3d98d0a2`, #5104 `a3b1ea7994d9`. #5088 (`da4fa6df1178`) and #5090 (`1e8d2b0075d7`) are open with no promise carried in the merged set.
- Fork heads unchanged: `solr-18317-server-submit` `ae918a03fa7b`, `solr-17393-submit` `626241e647e5`, `solr-9637-submit` `c50fa4ffd932`, `solr-9865-submit` `4937608bb181`, `solr-16130-test-followup` `3c48dec4b79f`. `solr-13059-submit` is still absent.

## Pass dated 2026-10-18 (vm4, 00:45Z)

Assignment: `assignments/pool-merged-pr-followup-promises-round-1.md`. Claim: `claims/pool-merged-pr-followup-promises-round-1.md` (vm4 took over the stale windows claim on 2026-10-18). Read-only: GitHub reads, `ls-remote` on the fork, and JIRA reads. Nothing posted, filed or edited on GitHub or JIRA. This ledger now lives on `coordination`; the 2026-10-13 to 2026-10-15 passes were on `pr-prepare` and are kept below.

Focus for this pass, requested by Nick: the SOLR-4841 language-detection follow-up and SOLR-18549.

Merged set, re-derived with `repo:apache/solr author:nick-boss-tech is:pr is:merged`: 10 PRs, all base `main`, all with `merged_at` set. Two are new since the 2026-10-15 pass: #5027 (SOLR-18391, merged 2026-10-17T00:24Z) and #5062 (SOLR-18523, merged 2026-10-17T00:11Z). No seed PR failed to come back merged.

Counts for this pass:

- Merged PRs: 10.
- Promises found in merged bodies: 3 (#4953, #5001, #5070), unchanged.
- Discharged: 0. Open: 3. Not yet due: 0.
- Named shapes outside the merged set: unchanged, all pending merge or not yet due.

### Focus items

#### SOLR-4841 language-detection follow-up (#5070, merged 2026-10-10)
Promise, verbatim from the merged body: "A follow-up submission is planned for a test that runs language detection through a custom identifier."
State: NEEDS-OWNER (2026-10-18T19:45:04ZZ, vm5). Review rounds 2 and 3 are SATISFIED (reports/SOLR-4841-followup-round-1.md). Nick files the ticket and approves the PR. Earlier check (vm5, 03:45Z):
- The fork has `solr-4841-langid-followup-submit` at `bf1b01d0dc`. `gates/SOLR-4841-langid-followup.md` records GATE GREEN at that head (vm4). Review rounds 2 and 3 by a reviewer other than the builder are still needed before SATISFIED.
- No new JIRA ticket exists. A search for "language detection custom identifier" and "DetectedLanguage" finds only the 2013 SOLR-4841. It is still Open.
- No PR is open from the follow-up branch.
Next step, in order: (1) Nick approves and files the new JIRA ticket, which is a public write and his call. (2) A reviewer other than the builder runs review rounds 2 and 3 to SATISFIED. (3) Gate: done at `bf1b01d0dc`. (4) Nick approves opening the PR, which discharges the promise.
Whose call: Nick for the ticket and the PR opening. Review is the reviewer's under the assignment.

#### SOLR-18549 for the SOLR-18317 server-side promise (#5001, merged 2026-10-05)
Promise, verbatim from the merged body: "The server-side hardening that degraded such calls to the local node was split out at reviewer request and is preserved intact on the fork branch `solr-18317-server-submit`, to be opened as its own PR."
State: NEEDS-OWNER (2026-10-18T19:45:04ZZ, vm5). Gate green at `cf4b869346` on the new branch `solr-18549-submit` (apache main base; receipts/SOLR-18549.md). The draft PR is at pr-drafts/solr-18549/PR-draft.md. Nick approves opening the PR, and says whether the banked `solr-18317-server-submit` branch (HELD) stays held. Earlier check (2026-10-18):
- SOLR-18549 exists in JIRA, status Open, reporter nickshanin, created 2026-10-16. Its summary is "Standalone node throws NPE for nodes or node parameters in proxy-capable admin endpoints". It names SOLR-18317 as the Admin UI half. Its expected behavior covers the v1 logging, v1 system info, v2 node system and v1 metrics endpoints.
- No PR on apache/solr mentions SOLR-18549. No gate file exists for it.
- Two fork heads carry SOLR-18317 work, and the records do not say which one the 18549 PR should use:
  - `solr-18317-server-submit` at `ae918a03fa`, the branch the merged body names. Its tip commits are the testing handoff notes and their removal, plus the proxy and system-info changes.
  - `solr-18317-ready` at `c5d63b1a67`, with the commit "SOLR-18317: Treat standalone nodes=all as local", on top of two commits that change build configuration. This branch was not in the 2026-10-15 records. Its base and content were not analyzed in this pass.
Next step: Nick picks the head and approves opening the PR. The pool gates the chosen head under SOLR-18549 and files the gate job, since the server branch has no gate today. When the PR opens with the ticket link, the promise is discharged.
Whose call: Nick for the head, the ticket and the PR. The gate is the pool's.

### Merged PRs

#### #4939 (SOLR-18135), merged 2026-09-24 (UTC)
No follow-up promise in the description.

#### #4953 (SOLR-8291, SOLR-13217), merged 2026-09-30 (UTC)
Promise, verbatim: "This also fixes SOLR-16130, I plan to submit a separate, test-only PR for that."
State: NEEDS-OWNER. The PR is ready and needs no draft: #5004 (SOLR-16130) is open with a gated head `3c48dec4b79f` (`receipts/SOLR-16130.md`). Question for Nick: does an open PR satisfy "I plan to submit"? If yes, the promise is discharged; if no, the pool has nothing else to do here.

#### #4959 (SOLR-18277), merged 2026-10-05 (UTC)
No follow-up promise in the description.

#### #4965 (SOLR-10198), merged 2026-10-07 (UTC)
No follow-up promise in the description.

#### #4995 (SOLR-18196), merged 2026-10-03 (UTC)
No follow-up promise in the description.

#### #5001 (SOLR-18317), merged 2026-10-05 (UTC)
Promise as above. State: OPEN, see the focus section.

#### #5012 (SOLR-13202), merged 2026-10-07 (UTC)
No follow-up promise in the description. The Limits line is a scope exclusion.

#### #5027 (SOLR-18391), merged 2026-10-17 (UTC), new this pass
No follow-up promise. The body says "Follow-up at adcda10b501." and then describes a test added in that commit. That points to work inside the PR, not to future work. The body also says "Happy to open a follow-up ticket and PR for them on request." That is an offer on request, not a promise.

#### #5062 (SOLR-18523), merged 2026-10-17 (UTC), new this pass
No follow-up promise. "Happy to open a follow-up ticket for it." is an offer on request. The body also says the companion PR for SOLR-18119 offers anchor checking as a follow-up change. That is the same SOLR-18119 anchor item as #5061 below, and it stays pending merge.

#### #5070 (SOLR-4841), merged 2026-10-10 (UTC)
Promise as above. State: OPEN, see the focus section.

### Seed promise shapes (open PRs or no merged carrier)

Checked against live bodies and open status on 2026-10-18. None is in the merged set, so none is a ledger entry yet.

- #5100 (SOLR-18532), open. Promise unchanged; `solr-9865-submit` is still at `4937608bb1`. PENDING MERGE.
- #5104 (SOLR-2681), open. Unchanged: the nested-form plan is still unconfirmed. PENDING MERGE, Nick to confirm or reword.
- #5061 (SOLR-18119), open. The anchor follow-up has no named plan. PENDING MERGE.
- #5089 (SOLR-13943), open. The six-test follow-up has no branch and no PR. PENDING MERGE.
- #5088 (SOLR-13696), open. Unchanged, not re-read this pass. PENDING MERGE.
- SOLR-17393 and SOLR-9637: not re-checked this pass. NOT YET DUE; the trigger is the SOLR-17393 PR merging.

### Discharges proposed, none executed

- SOLR-4841 follow-up: Nick files the ticket, the pool builds, reviews and gates, and Nick approves the PR.
- SOLR-18317 promise: Nick picks the head, the pool gates it under SOLR-18549, and Nick approves the PR linking the ticket.
- #4953: Nick rules whether open #5004 satisfies the promise, or it waits for its merge.

### Not verified this pass

- The content of `solr-18317-ready` beyond its commit subjects.
- Whether commit `adcda10b501` (cited by #5027) is reachable from main.
- Owner rulings from earlier passes, carried as recorded.

Everything below this heading is history: the 2026-10-15 pass, the round 2 pass of 2026-10-14 and the 2026-10-13 pass, kept as recorded on `pr-prepare` at `335be1be51`.

## Pass dated 2026-10-15

Pass dated 2026-10-15 (pr-prepare tip at pass start `c5a6b5e7ca0`). Assignment: `assignments/pool-merged-pr-followup-promises-round-1.md`. This run updates the ledger in place. The file already held the 2026-10-13 pass and the round 2 pass of 2026-10-14; both stay below, unchanged, as history. Where this pass differs from them, this pass is current. The assignment is still named round 1, but this is the third recorded pass, so the round numbering is for the owner to settle.

Merged set, re-derived this pass with `research\gh.ps1 pr list --repo apache/solr --author nick-boss-tech --state merged --limit 200`: total 8, all base `main`, all with `mergedAt` set (UTC). It matches the seed list, and no seed PR failed to come back merged.

Open set, re-derived with `--state open --limit 200`. A first attempt without the limit returned only 30 PRs (the gh default), so it was discarded; the census below uses the full list.

Counts for this pass:

- Merged PRs: 8.
- Promises found in merged bodies: 3 (#4953, #5001, #5070).
- Discharged: 0. Open: 3. Not yet due: 0.
- Named shapes outside the merged set: 6 entries, all pending merge or not yet due (see the seed section). Absent from the set: #5100 and #5104 are open, #5090 is open with no promise, and #5061 is open (pending merge, out of ledger).

### Merged PRs

#### #4939 (SOLR-18135), merged 2026-09-24 (UTC)
No follow-up promise in the description.

#### #4953 (SOLR-8291, SOLR-13217), merged 2026-09-30 (UTC)
Promise, verbatim: "This also fixes SOLR-16130, I plan to submit a separate, test-only PR for that."
The same body also says "If the tests are not needed, I can remove them." That is an offer about this PR's tests, moot after merge, so it is not counted as a follow-up.
State: OPEN. Candidate discharge: #5004 (SOLR-16130, "Add cross-collection export regression test"), open, author nick-boss-tech, head `solr-16130-test-followup` at `3c48dec4b79f`, which matches the fork branch. Its live body says it "contains no production change" and that the root cause "was fixed and merged in #4953 (SOLR-8291)". The 2026-10-14 pass verified its file list is test-only.
Open question for Nick: whether an open PR satisfies "I plan to submit", or whether the promise waits for #5004 to merge. This ledger keeps it open until he rules.
Next step: no pool action. Nick rules on the wording. Merging #5004 is a maintainer call.

#### #4959 (SOLR-18277), merged 2026-10-05 (UTC)
No follow-up promise in the description. The sentence "The size of the rollback window was not measured." is a limit, not a promise.

#### #4965 (SOLR-10198), merged 2026-10-07 (UTC)
No follow-up promise in the description.

#### #4995 (SOLR-18196), merged 2026-10-03 (UTC)
No follow-up promise in the description. (In Eastern time the merge falls on 2026-10-02; the UTC date is used here.)

#### #5001 (SOLR-18317), merged 2026-10-05 (UTC)
Promise, verbatim: "The server-side hardening that degraded such calls to the local node was split out at reviewer request and is preserved intact on the fork branch `solr-18317-server-submit`, to be opened as its own PR."
State: OPEN. The fork branch exists at `ae918a03fa7b`, matching the 2026-10-14 pass. No open or merged PR has that head (open census). The discussion thread on #5001 gives the context: Nick's comment of 2026-10-04 says "The server-side proxy changes are preserved on a separate branch for a later PR." Eric Pugh's comment of 2026-10-05 13:38:52Z asks whether Nick would take "a follow up to this PR" to move to the V2 logging APIs and points to SOLR-15823.
Owner ruling, recorded in the 2026-10-14 pass and not re-checkable on GitHub: Nick ruled the promise kept, relying on the SOLR-15823 pair #5030 and #5031 (both open). The 2026-10-14 pass also records SOLR-18317 as Resolved in Jira; Jira was not queried this pass.
Next step: Nick chooses one of two paths. (a) Open a PR from `solr-18317-server-submit`, which needs his go-ahead and a ticket decision, because SOLR-18317 is resolved. (b) Confirm the ruling in writing, with the SOLR-15823 pair as the discharge. The coverage check is in `reports/solr-18317-server-coverage-round-1.md`. Whose call: Nick.
Update, 2026-10-15: the coverage check found the server-side gap live on main and not covered by #5030 or #5031, which contradicts the 2026-10-14 ruling above as a discharge. Path (b) is not recommended. The recommendation is path (a), with a new ticket first. The reasoning and the draft ticket text are in `reports/pool-handoff-decisions-2026-10-15.md`. The promise stays open until Nick confirms the path.

#### #5012 (SOLR-13202), merged 2026-10-07 (UTC)
No follow-up promise in the description. The Limits line, "A missing `v` parameter (the query being joined) is not checked by this change and is not covered by the new tests," is a scope exclusion, not a promise.

#### #5070 (SOLR-4841), merged 2026-10-10 (UTC)
Promise, verbatim: "A follow-up submission is planned for a test that runs language detection through a custom identifier."
State: OPEN. The follow-up branch `solr-4841-langid-followup-submit` is on the fork at `bf1b01d0dc`, and its gate is GREEN at that head (vm4, `gates/SOLR-4841-langid-followup.md`, 2026-10-18 vm5 pass). `ci/4841-langid-r28` is the CI copy of the merged change (2026-10-14 pass). Nick ruled on 2026-10-14 that the promise is to be executed.
The assignment (`assignments/pool-solr-4841-langid-followup-round-1.md`) also records Jan Høydahl's direction (reported by Nick, 2026-10-14) that the follow-up get a new Jira ticket, with packaging re-pointed at that ticket. No such ticket exists yet.
Next step: review rounds 2 and 3 by a reviewer other than the builder, then the new ticket, then Nick's approval to open the PR. Whose call: the review is the pool's under the assignment; the ticket and the opening are Nick's.

### Summary of merged PRs

| Merged PR | Ticket | Merged (UTC) | Promise state |
|---|---|---|---|
| #4939 | SOLR-18135 | 2026-09-24 | none |
| #4953 | SOLR-8291, SOLR-13217 | 2026-09-30 | open (#5004 open and test-only; discharges on merge or Nick's ruling) |
| #4959 | SOLR-18277 | 2026-10-05 | none |
| #4965 | SOLR-10198 | 2026-10-07 | none |
| #4995 | SOLR-18196 | 2026-10-03 | none |
| #5001 | SOLR-18317 | 2026-10-05 | open (branch at `ae918a03`, no PR; owner ruling kept, recorded 2026-10-14) |
| #5012 | SOLR-13202 | 2026-10-07 | none |
| #5070 | SOLR-4841 | 2026-10-10 | open (follow-up branch `bf1b01d0dc` gate GREEN; review rounds 2 and 3, new ticket and PR still needed) |

### Seed promise shapes (open PRs or no merged carrier)

These are checked against live bodies. None is in the merged set, so none is a ledger entry yet. Each enters the ledger when it merges.

#### #5100 (SOLR-18532), open, head `7dfd3d98d0a2`
Promise, verbatim: "The SOLR-9865 change edits the same rollback lines and writes back only the previous directory name. Once this change lands, I will follow up on that branch by dropping its rollback edit, so the two do not conflict."
State: PENDING MERGE. `solr-9865-submit` is still at its gated head `4937608bb181`, so the rollback edit has not been dropped.
Next step: after #5100 merges, drop the rollback edit on `solr-9865-submit`, re-gate, and open. Whose call: Nick.

#### #5104 (SOLR-2681), open, head `a3b1ea7994d9`
Promise, verbatim: "A follow-up pull request is planned for the nested forms."
State: PENDING MERGE. No fork branch for the nested forms exists (`ls-remote`). The plan is not confirmed in the records. `reports/highlighting-answers-check-2681.md` (lines 25 to 31) says no record it read shows a plan, and asks Nick to confirm before the sentence stands.
Correction: the 2026-10-13 pass cited `material/highlighting-round-1-answers.md` line 9 as the source of the plan. That file has no "nested" text, so the citation is withdrawn.
Next step: Nick confirms the nested-form plan, or the Limits line is reworded to an offer "on request", as the check suggests. Whose call: Nick.

#### #5061 (SOLR-18119), open, head `660faedd026`
Out of this round's ledger, because #5061 is open. It enters the ledger when it merges.
Named follow-up, verbatim from the body: "A follow-up change can restore the anchor check; it is not in this PR."
Record: the anchor follow-up has no named plan or owner (`reports/build-docs-misc-round-1-g6.md` line 42, re-read this pass).
Next step: before merge, Nick names the plan (which PR, by whom). Whose call: Nick.

#### SOLR-13943 and SOLR-13059 (the assignment's "#5090")
The SOLR-13943 PR is #5089, not #5090. #5089 (open, head `ca443f6f6806`) carries the promise, verbatim: "A follow-up submission for those six tests is planned." The full sentence in the Limits section reads: "The six tests left in `TimeRoutedAliasUpdateProcessorTest` keep the class-level `@AwaitsFix`, so they stay skipped in normal mode. `testPreemptiveCreation` is one of them. A follow-up submission for those six tests is planned."
State: PENDING MERGE. No fork branch and no open PR for the SOLR-13059 six-test follow-up (`ls-remote` and open census).
Next step: Nick schedules the six-test follow-up. Whose call: Nick.

#### #5090 (SOLR-14262), open
Checked as the seed "#5090". The live body has no SOLR-13059 promise and no planned follow-up. Its Limits name scope gaps (SolrCloud, docs, autocommit overlap, "This branch does not address that overlap"), which are limits, not promises.
Record: No follow-up promise in the description. Not merged.

#### #5088 (SOLR-13696), open (new this pass, not a seed)
Promise, verbatim from the Limits section: "A follow-up submission to restore that coverage is planned." The coverage is commitWithin coverage, which the change removes.
State: PENDING MERGE. It enters the ledger when #5088 merges.

#### SOLR-17393 and SOLR-9637 (no merged carrier)
Assignment wording: the merge-only scope on 17393 names the per-shard top-count cut as a follow-up, and 9637 follows as its own PR once 17393 lands, rebased onto it.
Search result: no merged description carries this. The eight merged bodies do not mention either ticket, and a search of all apache/solr PRs for SOLR-17393, SOLR-9637 and SOLR-13059 returns no PR for either ticket.
The text exists only in unmerged drafts. `pr-drafts/suggester/SOLR-17393.md`: "The per-shard fix is a planned follow-up submission." `pr-drafts/suggester/SOLR-9637.md`: "This PR opens after SOLR-17393 lands, rebased onto the landed tip."
Fork branches `solr-17393-submit` (`626241e647e5`) and `solr-9637-submit` (`c50fa4ffd932`) exist. No PR is open for either.
State: NOT YET DUE. The trigger is the SOLR-17393 PR merging, and that PR is not yet opened.
Next step: Nick opens the 17393 PR. After it lands, Nick rebases 9637 onto it and opens that PR. Whose call: Nick.

### Discharges proposed, none executed

- #4953: Nick rules whether an open #5004 satisfies "I plan to submit", or the promise waits for its merge.
- #5001: Nick chooses between opening from `solr-18317-server-submit` and confirming the SOLR-15823 pair as the discharge.
- #5070: review rounds 2 and 3 under `assignments/pool-solr-4841-langid-followup-round-1.md` (the gate is done at `bf1b01d0dc`); Nick approves the opening and the new ticket.

### Not verified this pass

- Owner rulings from 2026-10-14 (#5001 kept; #5070 to be executed). The #5070 direction appears in the assignment text. The #5001 ruling is not in any file read this pass; it is carried from the 2026-10-14 pass.
- Jira status (SOLR-18317 Resolved, SOLR-15823 Open, SOLR-13059, SOLR-17393, SOLR-9637). Jira was not queried this pass.
- The SOLR-16130 receipt (GATE GREEN) and the #5004 file list are carried from the 2026-10-14 pass, not re-read.
- The nested-form plan for #5104: no record confirms it.
- The anchor-check plan for #5061: none named.
- Fork branches for the nested forms and for the SOLR-13059 follow-up: none found on the fork.

Everything below this heading is history: the 2026-10-13 pass and the round 2 pass of 2026-10-14, kept as recorded.

## Earlier pass, 2026-10-13

Pass dated 2026-10-13 (pr-prepare tip `1faa41f34d2`). Assignment: `assignments/pool-merged-pr-followup-promises-round-1.md`. Later rounds update this ledger in place.

Merged set, derived from the GitHub search API (`repo:apache/solr author:nick-boss-tech is:pr is:merged`, total 8, all base `main`, all with `merged_at` set). The seed list matches the search exactly: no seed is missing and no extra merged PR is returned. Closed without merge and out of scope: #4967 (SOLR-18134).

Assignment corrections, for the main side: the assignment's seed-list note says "#5090 (SOLR-13943)". Live titles show #5089 is the SOLR-13943 PR and #5090 is SOLR-14262. Neither is in the merged set, so the SOLR-13943 entry below is pending merge. Also, #5100, #5104 and #5061 are open, so their promises enter the ledger only when they merge.

Reading method: each body read as it stands now on the closed PR, through the API `pulls/<n>` `body` field. Promises quoted verbatim.

## Ledger

### #4939 (SOLR-18135), merged 2026-09-24
No follow-up promise in the description.

### #4953 (SOLR-8291, SOLR-13217), merged 2026-09-30
Promise, verbatim: "This also fixes SOLR-16130, I plan to submit a separate, test-only PR for that."
State: OPEN. A candidate, #5004 (SOLR-16130), is open and unmerged. It has not been verified as the planned test-only PR.
To discharge: confirm that #5004 is the test-only PR for SOLR-16130. If it is, the promise rests on an open PR and discharges when that PR merges. Whose call: Nick's. Any public PR needs his go-ahead.

### #4959 (SOLR-18277), merged 2026-10-05
No follow-up promise in the description.

### #4965 (SOLR-10198), merged 2026-10-07
No follow-up promise in the description.

### #4995 (SOLR-18196), merged 2026-10-03
No follow-up promise in the description.

### #5001 (SOLR-18317), merged 2026-10-05
Promise, verbatim: "The server-side hardening that degraded such calls to the local node was split out at reviewer request and is preserved intact on the fork branch `solr-18317-server-submit`, to be opened as its own PR."
State: OPEN. The fork branch exists (head `ae918a03`). No PR has been opened from it.
To discharge: open the PR from `solr-18317-server-submit`. Whose call: Nick's.

### #5012 (SOLR-13202), merged 2026-10-07
No follow-up promise in the description. One scope-gap line about the missing `v` parameter is a near-miss, recorded here and not counted as a promise.

### #5070 (SOLR-4841), merged 2026-10-10
Promise, verbatim: "A follow-up submission is planned for a test that runs language detection through a custom identifier."
State: OPEN. No matching fork branch was found. The branch `ci/4841-langid-r28` was not inspected.
To discharge: inspect `ci/4841-langid-r28` to see whether it is the planned test. Whether to submit it is Nick's call.

## Named shapes from the assignment

These promises are named in the assignment, and each sits in an open PR or in no merged PR. They enter the ledger when their PR merges.

### #5100 (SOLR-18532), open, not in the merged set
Promise, named in the assignment: a Limits line naming the follow-up on SOLR-9865, whose branch drops its overlapping rollback hunk when prepared for opening, because #5100 lands first.
State: PENDING MERGE. #5100 is open (head `7dfd3d98`). The fork branch `solr-9865-submit` is still at its gated head `4937608b`, so the rollback hunk has not been dropped. No SOLR-9865 PR exists.
To discharge: after #5100 merges, drop the rollback hunk on `solr-9865-submit`, re-gate, and open the PR. The drop is the packaging decision Nick has already recorded. Opening the PR is Nick's call.

### SOLR-17393 and SOLR-9637
Promise, named in the assignment: the merge-only scope on 17393 names the per-shard top-count cut as a follow-up, and 9637 follows as its own PR once 17393 lands, rebased onto it.
State: NOT YET DUE. No merged PR body carries this promise. No apache/solr PR, of any author, mentions either ticket. The fork branches `solr-17393-submit` (`626241e6`) and `solr-9637-submit` (`c50fa4ff`) exist.
To discharge: Nick opens the 17393 PR. After it lands, rebase 9637 onto it and open that PR. Whose call: Nick's.

### #5104 (SOLR-2681), open, not in the merged set
Promise, named in the assignment: a Limits line on the nested form, with a follow-up pull request planned.
State: PENDING MERGE. #5104 is open. No nested-form branch exists on the fork. The plan is in `material/highlighting-round-1-answers.md` at line 9. `reports/highlighting-answers-check-2681.md` (lines 25 to 31) asks Nick to confirm it.
To discharge: Nick confirms the nested-form plan, or rewords the Limits line to "on request". The branch follows after #5104 merges.

### #5061 (SOLR-18119), open, not in the merged set
Promise, named in the assignment: fragment anchor validation carried as a named follow-up promise, rather than code.
State: PENDING MERGE. #5061 is open (head `660faedd`). The anchor follow-up has no plan or owner (`reports/build-docs-misc-round-1-g6.md`, line 42).
To discharge: before #5061 merges, Nick names the anchor plan in one sentence (which PR, by whom). Whose call: Nick's.

### SOLR-13943 and SOLR-13059 (assignment cites #5090; see the correction above)
Promise, named in the assignment: a Limits promise tied to the SOLR-13059 follow-up, the test-only fix for `TimeRoutedAliasUpdateProcessorTest`.
State: PENDING MERGE. The SOLR-13943 PR is #5089, which is not merged. The SOLR-13059 six-test follow-up is OPEN: no branch and no PR.
To discharge: Nick schedules the six-test follow-up. A fail-before run is needed first. Whose call: Nick's.

## Summary

| Merged PR | Ticket | Merged | Promise state |
|---|---|---|---|
| #4939 | SOLR-18135 | 2026-09-24 | none |
| #4953 | SOLR-8291, SOLR-13217 | 2026-09-30 | NEEDS-OWNER (Nick rules whether open #5004 counts) |
| #4959 | SOLR-18277 | 2026-10-05 | none |
| #4965 | SOLR-10198 | 2026-10-07 | none |
| #4995 | SOLR-18196 | 2026-10-03 | none |
| #5001 | SOLR-18317 | 2026-10-05 | NEEDS-OWNER (gate green at cf4b869346; Nick approves the PR) |
| #5012 | SOLR-13202 | 2026-10-07 | none |
| #5070 | SOLR-4841 | 2026-10-10 | NEEDS-OWNER (review SATISFIED; Nick files the ticket and approves the PR) |

Open promises from merged PRs: three (#4953, #5001, #5070). All three are waiting on Nick's call. Named shapes that sit in open PRs or in no merged PR: five, none discharged.

No discharge in this ledger has been executed. No PR was opened, closed or changed, no comment was posted, and no ticket was filed.

## Round 2 hold

`assignments/pool-four-new-prs-strong-review-round-2.md` did not start in this round. Its own rule waits for its seven gate inputs to be DONE or for the owner to direct a subset. At this pass, six of the seven are OPEN. The symlink premise is DONE. Its two owner rulings are recorded in the assignment and are not reopened here.

## Round 2, 2026-10-14

Pass dated 2026-10-14 (pr-prepare tip at pass start `72d4f126d0`). The standing rule (Nick, 2026-10-13) re-runs this audit whenever one of our PRs merges; this pass also re-checks every round 1 state.

Merged set, re-derived from the GitHub search API with round 1's query: total still 8, identical to round 1's set. The latest merge is #5070 on 2026-10-10, which round 1 already covered. No PR has merged since round 1, so round 2 newly covers no merged PR and extracts no new promises.

### State re-checks of round 1 items (all read live on 2026-10-14)

#### #4953 promise (SOLR-16130 test-only PR): candidate verified, promise still open

Promise, verbatim from round 1: "This also fixes SOLR-16130, I plan to submit a separate, test-only PR for that."

Round 1 left the candidate #5004 unverified. Verified now, from three independent reads: #5004's head branch is `solr-16130-test-followup` at `3c48dec4b79f`; its file list is test-only (`CrossCollectionJoinQueryTest.java` and the ccjoin test configset's `solrconfig.xml`); and its body states the root cause "was fixed and merged in #4953 (SOLR-8291)" and "This PR contains no production change; it adds the regression test for the reported scenario." Its receipt (`receipts/SOLR-16130.md`) records GATE GREEN at that head. State: OPEN, now resting on a verified PR; the promise discharges when #5004 merges. #5004 is open and unmerged at this pass. The verification item round 1 left open is discharged; the promise itself is not.

#### #5001 promise (server-side SOLR-18317 PR): owner ruled kept, 2026-10-14

Promise, verbatim from round 1: "The server-side hardening that degraded such calls to the local node was split out at reviewer request and is preserved intact on the fork branch `solr-18317-server-submit`, to be opened as its own PR."

Disposition: OWNER RULED KEPT (Nick, 2026-10-14). Nick considers the promise kept: the paired follow-up work for the GET/PUT side was built and awaits Pugh's review. Factual note, recorded alongside the ruling so the ledger stays honest: the server-side branch `solr-18317-server-submit` stands built at `ae918a03fa7b`, the round 1 head, and no separate PR has been opened from it as of this round 2 audit; a main-side API check on 2026-10-14 confirms only #5001 names SOLR-18317. If that branch is ever to open as its own PR, it needs Nick's word like any opening.

Addendum (2026-10-14, later): the link behind the ruling is verified. On #5001, Eric Pugh commented at 2026-10-05T13:38:52Z: "Okay, @nick-boss-tech I took a stab at trying to move us to using the V2 Logging APIs, but we don't yet properly handle everything there... So, I was wondering if you would be interested in a follow up to this PR to tackle that work? I'm going to add more detail to https://issues.apache.org/jira/browse/SOLR-15823". That follow-up is the SOLR-15823 pair, both open: #5030 (the PUT side: V2 logging level changes broadcast to named nodes, the Admin UI migrated to V2) and #5031 (the GET side: nodes broadcast on the V2 logging levels listing). SOLR-15823 ("Split v2 /node/logging API into separate GET and PUT APIs and deal with single node/all nodes logic") is still Open in Jira; SOLR-18317 is Resolved. The split-out server branch was never opened, and with SOLR-18317 resolved it cannot be opened under that ticket now. Whether its standalone nodes=all narrowing is fully covered by current main plus the SOLR-15823 pair is an open code question; Nick has been offered a check on it. Update (2026-10-14): the coverage check is assigned as `assignments/pool-solr-18317-server-coverage-round-1.md`, with per-item verdicts against current main and the SOLR-15823 pair as its deliverable. Nick's recorded expectation is that a new ticket will likely be needed for whatever gap remains; the assignment includes a drafted ticket text for that case, which waits for his decision and is not filed by the assignment.

#### #5070 promise (detection-running test): no change; round 1's open inspection item resolved

Promise, verbatim from round 1: "A follow-up submission is planned for a test that runs language detection through a custom identifier."

Round 1 left the branch `ci/4841-langid-r28` uninspected. Inspected now: its tip is `41c0087c8a29` and it is the CI copy of the merged change, carrying the same `CustomLanguageIdentifierTest` that merged in #5070. That test builds a `DetectedLanguage` from another package and checks the two getters; #5070's own Limits says of it, "It does not run language detection." No fork branch carries a test that runs language detection through a custom identifier. State: OPEN, unchanged, still owed. Whether to submit it is Nick's call.

Addendum (2026-10-14, later): Nick ruled the promise is to be executed: the SOLR-4841 ticket is still open and the follow-up was never built. State: OPEN, assigned to the review agent on 2026-10-14 under `assignments/pool-solr-4841-langid-followup-round-1.md`, which directs building the detection-running test on branch `solr-4841-langid-followup-submit`, review rounds to SATISFIED, and a gate job (`gates/SOLR-4841-langid-followup.md`) for a Linux host. The assignment does not authorize an opening; the branch joins an openings slate for Nick's approval once gated and satisfied.

#### Named shapes: no state changes

- #5100 (SOLR-18532): still open at `7dfd3d98d0a2`; `solr-9865-submit` still at `4937608b`, so the rollback hunk has not been dropped. PENDING MERGE, unchanged.
- SOLR-17393 and SOLR-9637: branches unchanged (`solr-17393-submit` at `626241e6`, `solr-9637-submit` at `c50fa4ff`); neither has an open PR yet. NOT YET DUE, unchanged.
- #5104 (SOLR-2681): still open at `a3b1ea7994d9`. PENDING MERGE, unchanged.
- #5061 (SOLR-18119): still open at `660faedd026e`. PENDING MERGE, unchanged.
- #5089 (SOLR-13943) and SOLR-13059: #5089 still open at `ca443f6f6806`; the SOLR-13059 follow-up still has no fork branch and no PR. PENDING MERGE, unchanged.

### Round 2 summary

Newly covered merged PRs: none (no merges since round 1). Open promises from merged PRs: three recorded (#4953, #5001, #5070), of which #5001 was ruled KEPT by the owner on 2026-10-14 (see its item above), leaving #4953 and #5070 still owed. One state change since round 1: the #4953 candidate is verified as the planned test-only PR, so that promise now rests on #5004 and discharges at its merge. One open item resolved with no state change: `ci/4841-langid-r28` is not the #5070 follow-up test. Every discharge remains Nick's call. Addendum (2026-10-14, later): the #5070 promise is now assigned to the review agent for execution (see its subsection above); the #4953 promise is unchanged.

No discharge in this ledger has been executed. No PR was opened, closed or changed, no comment was posted, and no ticket was filed.
