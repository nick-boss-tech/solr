# Assignment: ready-draft conversion and rebase follow-up questions, round 1

Status: OPEN

Posted 2026-10-28 by vm5 for phone-agent, to route the questions raised while running `assignments/ready-draft-conversion-round-1.md` and the rebase follow-up. Each question is a decision for Nick Shanin. Answer each with one line; do not act on them until Nick answers.

## Questions

1. **#5136 conflict resolution.** `solr/core/src/test/org/apache/solr/search/ReturnFieldsTest.java` conflicted between PR #5136's expectThrows block and main's SOLR-11364 (#5188) version of the same block. vm5 kept the PR side (unknown `fl` transformer is a 400). Merge commit `48a79df2e3` is pushed to `solr-7390-submit`, compile of `:solr:core:compileTestJava` is green, and the focused test has not been run. Question: is keeping the PR side right, or should the test be re-checked against main's behavior before Nick accepts the merge?
2. **Rebase vs merge.** The two dirty PRs (#5004, #5136) were brought up to main with a merge commit, not a rebase, because a rebase needs a force-push, and AGENTS.md forbids force-pushes. Question: does Nick want true rebases for these, and if so, is a force-push with lease approved for these two fork branches?
3. **Maintainer comments.** #5031 (epugh, 2026-10-22), #5192 (epugh 2026-10-22, sigram 2026-10-27), and #5202 (epugh, 2026-10-23) were left ready, not drafted. #4996 (dsmiley, 2026-10-02) and #5009 (janhoy, 2026-10-06) were drafted. Question: confirm the three held PRs stay ready for now, and confirm the two older-comment PRs stay drafted.
4. **Unit 2 claim order.** The claim `claims/ready-draft-conversion-unit-2-vm5.md` was posted after the unit's edits ran (see the note in that claim). Question: is that acceptable as recorded, or should the unit's record be marked as run before claim?
5. **"Unstable" mergeable state.** 59 converted PRs report `mergeable_state: unstable`. The head checks seen on the first sample are only the labeler check, so CI may not run on fork heads. Question: does Nick want a check of why CI does not run on these heads, and is that a separate assignment?

## Deliverable

One record `reports/ready-draft-followup-answers-round-1.md` with Nick's answer to each question, or the question marked open. No code, no PR or Jira posts, no force-pushes.
