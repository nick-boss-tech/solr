# Assignment: full-scan Jira comment readiness, remaining leads (round 1)
Status: DONE
Completed: 2026-10-25. Claim `claims/phone-agent-jira-full-scan-comment-readiness-round-1.md` and report `reports/phone-agent-jira-full-scan-comment-readiness-round-1.md` are on the board; cleanup record in `reports/vm4-open-work-discussion-round-1.md`.
- Capability tags: `review`, `draft`.
- Intended host: `phone-agent` (Phone Agent), not `phone-agent-2`. Claim this assignment under `claims/` before starting. Phone-agent creates its own claim; this posting creates no claim.
- Posted by: Cue at Nick Shanin's direction, 2026-10-24.
- One claim covers the seven candidate/warm-lead reviews and the consolidated report. Draft only ticket comments that current evidence supports.
- Scope: fresh watch-list comments in the full Jira scan on `coordination-ui` for 2026-10-14 through 2026-10-23, excluding work already assigned or otherwise covered below.

## Source basis and limits
Read each date's `receipts/YYYY-MM-DD/receipt.md` and associated `target-comments.json`. The dated Oct. 23 folder is canonical; `receipts/jira-day-scan-2026-10-23.md` is the historical starting-scan receipt and agrees with it. The receipts determine which comments were fresh on each UTC day and what evidence-first follow-up shape, if any, the scan identified. The scan itself did not verify live Jira state, code, branches, PRs, revert state, sponsorship, or reproduce reported behavior. Treat each lead as unverified.

The assignment includes only receipt-identified follow-up candidates or warm leads that are not already assigned or covered. The complete fresh-comment inventory and receipt disposition is below, so absence from the assigned set is deliberate. Source folders for each row are named by date; all comments link to their exact Jira comment IDs.

## Fresh watch-list comment inventory and disposition

| Scan date (UTC) | Fresh comment(s) | Receipt disposition and assignment decision |
|---|---|---|
| 2026-10-14 | [SOLR-17946, David Smiley, 18126875](https://issues.apache.org/jira/browse/SOLR-17946?focusedCommentId=18126875) | Candidate: verify the queue behavior under `SOLR_NODE_ROLES="data:on,overseer:disallowed"` before asking whether it supports changing the default. Excluded because already assigned in `assignments/phone-agent-solr-17946-18502-comment-readiness-round-1.md`. |
| 2026-10-14 | [SOLR-18502, Jason Gerlowski, 18127176](https://issues.apache.org/jira/browse/SOLR-18502?focusedCommentId=18127176) | Candidate: verify the revert and characterize the reported test failure before asking whether corrective follow-up is wanted. The scan did not inspect the branch or reproduce the failure. Excluded because already assigned in `assignments/phone-agent-solr-17946-18502-comment-readiness-round-1.md`. |
| 2026-10-15 | None | No fresh watch-list comments; no candidate follow-ups. |
| 2026-10-16 | None | No fresh watch-list comments; no candidate follow-ups. |
| 2026-10-17 | None | No fresh watch-list comments; no candidate follow-ups. |
| 2026-10-18 | None | No fresh watch-list comments; no candidate follow-ups. |
| 2026-10-19 | [SOLR-18551, Jason Gerlowski, 18128711](https://issues.apache.org/jira/browse/SOLR-18551?focusedCommentId=18128711) | Candidate: the note reports `mmap failed` after switching to TLOG/PULL replicas and points to Lucene issue 15068, `IOContext.DEFAULT`, `/proc/<pid>/maps`, and a heap dump as diagnostic context. Include for evidence-first review only; the scan recommends no implementation. |
| 2026-10-20 | [SOLR-18450, David Smiley, 18129023](https://issues.apache.org/jira/browse/SOLR-18450?focusedCommentId=18129023) | Candidate: verify the two 10.1 backports/reverts and whether anything remains. Excluded because this is covered by completed item 1 of `assignments/rc3-delta-audit-round-1.md`; `reports/rc3-delta-audit-round-1.md` records the check as CLEAN at RC3. This is coverage of the backport/revert question, not a claim about current Jira status. |
| 2026-10-20 | [SOLR-18266, Jan Høydahl, 18129034](https://issues.apache.org/jira/browse/SOLR-18266?focusedCommentId=18129034) | Candidate: an AWS nightly-benchmark proof of concept is conditioned on a sponsor for storage and egress. Include as a sponsorship-check warm lead; the scan did not check costs or sponsorship. |
| 2026-10-20 | [SOLR-18147, Jan Høydahl, 18129013](https://issues.apache.org/jira/browse/SOLR-18147?focusedCommentId=18129013) | Receipt classifies this as a deferred direction and infers no additional candidate. The receipt table excerpt and JSON excerpt differ; do not turn it into a lead without live verification. Excluded. |
| 2026-10-20 | [SOLR-18284, Jason Gerlowski, 18129173](https://issues.apache.org/jira/browse/SOLR-18284?focusedCommentId=18129173) | Warm lead: the fresh comment asks Mark Miller whether the ticket should be closed. The receipt says this is a sponsor ticket-state decision and implies no technical work. Include only to check the live thread/status and whether anything beyond the already-asked question merits a comment; do not repeat the question. |
| 2026-10-21 | [SOLR-18546, Eric Pugh, 18129618](https://issues.apache.org/jira/browse/SOLR-18546?focusedCommentId=18129618) | The comment records positive dev-list feedback; the receipt says no separate follow-up candidate can be inferred. Excluded. |
| 2026-10-21 | [SOLR-18549, Eric Pugh, 18129907](https://issues.apache.org/jira/browse/SOLR-18549?focusedCommentId=18129907) | Candidate: verify which endpoints are meant to work in standalone and, if useful, reproduce the reported failure before asking whether the evidence supports graceful error behavior. The scan recommends no implementation. |
| 2026-10-22 | [SOLR-18407, David Smiley, 18130097](https://issues.apache.org/jira/browse/SOLR-18407?focusedCommentId=18130097) | Technical correction about Solr 10's Java 21 minimum; the receipt records it only and infers no separate follow-up. Excluded. |
| 2026-10-22 | [SOLR-17971, David Smiley, 18130281](https://issues.apache.org/jira/browse/SOLR-17971?focusedCommentId=18130281) | Acknowledgement of reviewers only; no follow-up candidate. Excluded. |
| 2026-10-22 | [SOLR-17780, David Smiley, 18130382](https://issues.apache.org/jira/browse/SOLR-17780?focusedCommentId=18130382) | The comment states a 9.12 backport decision; receipt marks it settled. Excluded. |
| 2026-10-22 | [SOLR-18267, David Smiley, 18130383](https://issues.apache.org/jira/browse/SOLR-18267?focusedCommentId=18130383) | The comment states 9.12 delivery; receipt marks it settled. Excluded. |
| 2026-10-22 | [SOLR-18568, Jason Gerlowski, 18130390](https://issues.apache.org/jira/browse/SOLR-18568?focusedCommentId=18130390) | Candidate: verify whether the reported shutdown hangs share a failure path, then ask which direction the evidence supports (inspect shutdown cleanup or evaluate the timeout idea). The scan produced no reproduction and recommends no implementation. |
| 2026-10-23 | [SOLR-14306, David Smiley, 18130678](https://issues.apache.org/jira/browse/SOLR-14306?focusedCommentId=18130678) | Warm lead only: the comment says sub-task SOLR-16116 was converted to a normal issue and linked as superseding this issue, and that Smiley is inclined to close this as won't-do. The receipt says any follow-up starts as a sponsor conversation, not a build. Include only to verify live relevance and assess whether a concise sponsor question is useful. |
| 2026-10-23 | [SOLR-18571, Eric Pugh, 18130653](https://issues.apache.org/jira/browse/SOLR-18571?focusedCommentId=18130653) and [18130654](https://issues.apache.org/jira/browse/SOLR-18571?focusedCommentId=18130654) | Candidate: Eric is actively discussing whether the feature should be on by default. The receipt identifies an engaged owner and a staged-ask candidate. |
| 2026-10-23 | [SOLR-17895, David Smiley, 18130679](https://issues.apache.org/jira/browse/SOLR-17895?focusedCommentId=18130679) | Design question to Amrit about Curator recipes; receipt says watch, no candidate. Excluded. |
| 2026-10-23 | [SOLR-18569, Jason Gerlowski, 18131023](https://issues.apache.org/jira/browse/SOLR-18569?focusedCommentId=18131023) | The receipt ties the branch_9x note directly to PR 5211 and says no new unit because that review is already in flight. Excluded as covered by `assignments/pr-5211-test-review-implementation-and-gate-round-1.md`, its claims and review deliverables. |
| 2026-10-23 | [SOLR-18297, Jan Høydahl, 18131158](https://issues.apache.org/jira/browse/SOLR-18297?focusedCommentId=18131158) | Jan stated the status himself (missed 10.1, ready for 10.2); the receipt marks it settled. Excluded. |

Receipt and target-comments JSON sources by date:
- 2026-10-14: `receipts/2026-10-14/receipt.md` and `receipts/2026-10-14/target-comments.json`.
- 2026-10-15: `receipts/2026-10-15/receipt.md` and `receipts/2026-10-15/target-comments.json`.
- 2026-10-16: `receipts/2026-10-16/receipt.md` and `receipts/2026-10-16/target-comments.json`.
- 2026-10-17: `receipts/2026-10-17/receipt.md` and `receipts/2026-10-17/target-comments.json`.
- 2026-10-18: `receipts/2026-10-18/receipt.md` and `receipts/2026-10-18/target-comments.json`.
- 2026-10-19: `receipts/2026-10-19/receipt.md` and `receipts/2026-10-19/target-comments.json`.
- 2026-10-20: `receipts/2026-10-20/receipt.md` and `receipts/2026-10-20/target-comments.json`.
- 2026-10-21: `receipts/2026-10-21/receipt.md` and `receipts/2026-10-21/target-comments.json`.
- 2026-10-22: `receipts/2026-10-22/receipt.md` and `receipts/2026-10-22/target-comments.json`.
- 2026-10-23: `receipts/2026-10-23/receipt.md` and `receipts/2026-10-23/target-comments.json`. The historical starting-scan file `receipts/jira-day-scan-2026-10-23.md` agrees with the dated folder.

The existing two-ticket assignment is not repeated here.

## Assigned candidate and warm-lead reviews

For each of these seven tickets, read the live issue and recent comments first. Confirm current status, resolution, assignee, latest activity, and whether the scan lead remains relevant. Inspect relevant code, PR, revert, or related-ticket history only as needed to verify the lead. Distinguish checked facts from hypotheses and cite exact comment IDs, source links, commit IDs, and code locations as appropriate. Do not treat scan excerpts as verified current behavior.

1. **SOLR-18551:** Determine whether the ticket's proposed solution covers the TLOG/PULL-specific paths described in comment 18128711. The scan's possible question is whether the verified explanation captures the reported failure; do not propose implementation from the scan alone.
2. **SOLR-18266:** Check whether a sponsor for the AWS storage and egress costs is identified in the live discussion. Do not infer a commitment or estimate costs without evidence. Recommend whether a sponsor question would add value now.
3. **SOLR-18284:** Check whether the sponsor answered the existing closure question in comment 18129173 and whether the issue's current state makes any further note useful. Do not repeat Jason's question or make a ticket-state change.
4. **SOLR-18549:** Verify the standalone support expectations for the named endpoints and the reported failure, if evidence is available without running tests. State what is confirmed and what remains a hypothesis; no implementation recommendation is implied.
5. **SOLR-18568:** Verify the reported shutdown examples and whether they plausibly share a failure path before weighing shutdown-cleanup inspection against the timeout idea. The scan performed no reproduction; do not present either explanation as established.
6. **SOLR-14306:** Verify the live relationship and status of SOLR-16116 and SOLR-14306. Treat this as a sponsor-conversation warm lead only; no build, code work, or ticket closure is authorized.
7. **SOLR-18571:** Re-read the two fresh comments and current thread to identify the specific default-behavior question that remains open. The receipt calls this an engaged-owner staged-ask candidate, not permission to start work.

For each ticket recommend whether a Jira comment is warranted now. If warranted, state the exact information or single question it should convey and its addressee based on current activity. Draft only when checked evidence supports a useful comment. Follow `comment-formula.md` and Jira wiki markup. A multi-sentence Jira comment uses the footer disclosure. If a draft is created, label it `DRAFT (not posted)`, identify its shape and addressee, and update `jira/COMMENTS.md` under the phone-agent's single-writer rule.

## Deliverables
- `reports/phone-agent-jira-full-scan-comment-readiness-round-1.md`: one section per included ticket, recording live state and exact sources, whether the scan lead remains live, recommendation, the precise question/information if a comment is warranted, and missing evidence. State clearly when no draft is appropriate.
- If evidence supports a comment, add the corresponding `jira/jira-comment-SOLR-<key>-draft.md` file(s), labeled `DRAFT (not posted)`. Do not create placeholder drafts.

## Limits
- Do not post anything to Jira or GitHub, open a PR, change Jira state, create or modify code, or run tests. Read-only source/PR/history inspection is allowed when needed to verify a lead.
- Do not create a claim on behalf of phone-agent. Phone-agent must claim this assignment itself before work.
- No comment may be posted until Nick gives explicit approval. A recommendation or draft is not posting authorization.
- Keep scope to the seven included tickets. Do not revive excluded comments or invent a follow-up from routine acknowledgements, settled backports, or technical notes the receipt says do not imply follow-up.
- Commit as Nick Shanin with no trailers and no em dash in authored text.
