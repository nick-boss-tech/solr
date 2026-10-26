# Assignment: comment readiness for SOLR-17946 and SOLR-18502

- Status: DONE.
- Completed: 2026-10-25. Claim `claims/phone-agent-solr-17946-18502-comment-readiness-round-1.md` and report `reports/phone-agent-solr-17946-18502-comment-readiness-round-1.md` are on the board; cleanup record in `reports/vm4-open-work-discussion-round-1.md`.
- Capability tags: `review`, `draft`.
- Intended host: `phone-agent` (Phone Agent), not `phone-agent-2`. Claim under `claims/` before starting. Phone-agent creates its own claim; this assignment post does not create one.
- Posted by: Cue at Nick Shanin's direction, 2026-10-24.
- One claim covers both ticket reviews and the consolidated report.
- Scope: review current Jira state and decide whether a comment is warranted now. This is not authorization to post.

## Background at handoff

The source is the Jira day-scan receipt at `receipts/2026-10-14/receipt.md`, especially its candidate-work notes. The scan recorded activity on 2026-10-14 but did not inspect code, branches, or PRs and did not run tests. Treat both leads as unverified until checked.

### SOLR-17946: Disabled Overseer shouldn't create ZK queues

Live Jira metadata checked 2026-10-24: Open, unresolved, unassigned, last updated `2026-10-14T00:08:05.080+0000`. The issue has one comment. David Smiley's only comment hypothesizes that `SOLR_NODE_ROLES="data:on,overseer:disallowed"` may leave no such queues and suggests that disabling the Overseer could imply changing the default Overseer node role. This remains a hypothesis, not verified behavior.

Sources: [live ticket](https://issues.apache.org/jira/browse/SOLR-17946), [David Smiley's comment 18126875](https://issues.apache.org/jira/browse/SOLR-17946?focusedCommentId=18126875), and `receipts/2026-10-14/receipt.md`.

### SOLR-18502: INSTALLSHARD (and RESTORE) should skip write.lock when copying index files

Live Jira metadata checked 2026-10-24: Open, unresolved, assigned to Jason Gerlowski, last updated `2026-10-14T15:43:20.975+0000`. The four-comment thread records the original change commit `f3a798e424bb933ba988c8d316d0e8f35c8b6e0f`, a revert commit `f586eda8ba14d4e96a80656d20c32c2c73e6bdaa`, Chris Hostetter's report that `S3InstallShardTest.testInstallSkipsWriteLockFile` failed with `Test data should include a write.lock`, and Jason's statement that he reverted the change and could not address it immediately. The live thread has no later Jira update than Jason's October 14 comment. Do not assume the original change or failure state is still current without checking the live source.

Sources: [live ticket](https://issues.apache.org/jira/browse/SOLR-18502), [Chris Hostetter's failure report, comment 18126824](https://issues.apache.org/jira/browse/SOLR-18502?focusedCommentId=18126824), [revert record, comment 18127175](https://issues.apache.org/jira/browse/SOLR-18502?focusedCommentId=18127175), [Jason Gerlowski's comment 18127176](https://issues.apache.org/jira/browse/SOLR-18502?focusedCommentId=18127176), [original change](https://github.com/apache/solr/commit/f3a798e424bb933ba988c8d316d0e8f35c8b6e0f), [revert](https://github.com/apache/solr/commit/f586eda8ba14d4e96a80656d20c32c2c73e6bdaa), and `receipts/2026-10-14/receipt.md`.

## Work

For each ticket separately:

1. Re-read the live issue and recent thread. Confirm current status, resolution, assignee, latest activity, and whether the scan lead is still relevant. If the ticket is resolved or the lead is stale, report that and do not draft a redundant comment.
2. Inspect relevant code, PR, or revert history only as needed to substantiate a possible comment. Keep hypotheses distinct from verified facts, cite exact source links and code locations or commit IDs, and state what evidence is still missing.
3. Recommend whether a comment is warranted now. If it is, state the precise information or single question it should convey and who should be addressed based on current ticket activity. If not, explain why.
4. Draft only when the evidence supports a useful comment. Follow `comment-formula.md` and Jira wiki markup. Use the appropriate status-note or staged-ask shape, keep it concise, include the Jira disclosure footer for a comment longer than one sentence, and update `jira/COMMENTS.md` under the phone-agent's single-writer rule when a draft is created.

## Deliverables

- `reports/phone-agent-solr-17946-18502-comment-readiness-round-1.md`: one section per ticket with current state and sources, whether the scan lead remains live, recommendation, exact question or information if a comment is warranted, and missing evidence. Record clearly when no draft is appropriate.
- If warranted, separate draft(s): `jira/jira-comment-SOLR-17946-draft.md` and/or `jira/jira-comment-SOLR-18502-draft.md`. Mark each `DRAFT (not posted)` and identify its shape and addressee.

## Limits

- Do not post anything to Jira or GitHub, open a PR, change Jira state, implement code, or run tests.
- Do not create claims on behalf of phone-agent. Phone-agent claims this assignment itself before work.
- No comment is to be posted until Nick gives explicit approval. A draft or recommendation is not posting authorization.
- Keep scope limited to SOLR-17946 and SOLR-18502. Do not revive other findings from the scan.
- Commit as Nick Shanin with no trailers. No em dashes.
