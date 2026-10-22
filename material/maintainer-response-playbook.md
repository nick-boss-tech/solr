# Maintainer response playbook (vm3, 2026-10-22)

Use this when a maintainer comments, reviews, or asks for a change on one of Nick's open PRs on apache/solr. It turns the owner rules and the coordinator handoff into steps. Where a rule is not written here, the source is cited.

## Standing rules that apply

- Live PR title and body edits: no approval needed (Nick, 2026-10-21). Make the edit, read it back, and record it in the receipt or report.
- Pushes to a branch that heads one of Nick's open PRs: standing authorization (Nick, 2026-10-14). Normal patches are fast-forward pushes. A rebuild uses `--force-with-lease` pinned to the tip read in the same session, and the old and new tips are recorded.
- Comments and reviews posted to GitHub: drafted, then posted only after Nick approves (`material/coordinator-handoff.md`, "What stays reserved"). The owner has not yet said whether replies to maintainers are exempt. Ask once, in the summary to Nick, and record the answer here.
- PR openings and draft-to-ready flips: Nick's approval each time. Check first for an existing open PR on the same head.
- Upstream cap (apache/solr main `7892348272`, 2026-10-21): a contributor without write access may have 7 open PRs at a time, drafts included. Count before proposing any new PR.
- Do not poll. Check a PR's threads when Nick asks, or on a scheduled wake that Nick has set up.
- Never run bare `git stash`. Use a WIP commit or a worktree.

## Step 1: Intake

For each PR, list issue comments, reviews, and inline review comments through the REST API. Compare them with the thread log `reports/maintainer-threads/<PR>.md`. Add one row per new item: date (UTC), author, the ask in the maintainer's words, the class from step 2, and the state.

States: NEW, WORKING, ANSWERED, IMPLEMENTED, WAITING-MAINTAINER, NEEDS-NICK (a decision or approval is needed), DONE.

## Step 2: Classify each ask

- **A. Implement as asked.** The default (owner ruling, 2026-10-14). The scope is the scope written.
- **B. A factual question.** Answer it with a citation to code, a test run, or a log.
- **C. One of our listed choices.** Build the option the maintainer picks. Do not argue for ours.
- **D. We think the ask is wrong or costly.** Attempt the design first. Record what it costs. Send the finding to Nick before any reply that declines or narrows the request. Objections go to Nick, not to the reviewer.
- **E. Outside the PR.** Name it in Limits and offer a follow-up on request. Do not expand the PR on our own.

## Step 3: Read before answering

- Read the thread in full and the code around the change: sibling docs, tests, and the pattern the repo already uses (`memory/feedback_review_surrounding_code.md`). A reply that answers only the diff misses the reviewer's point.
- Reproduce the ask on a branch. Feasibility is settled by the attempt, not by reading the code.

## Step 4: Gate and proof

- Run the gate in order: changelog YAML, tidy, Error Prone compile with `--rerun` on `compileJava` and `compileTestJava`, focused tests with counts from the fresh JUnit XML, and `:solr:core:check -x test`.
- If behavior changed, run the fail-before proof: the new tests on the base code, with the outcome recorded. A test that passes on base only pins behavior. Label it that way.
- Write the receipt at `receipts/<TICKET>.md`.

## Step 5: Push and record

- Push as a fast-forward commit with the ticket as the prefix, authored as Nick Shanin with no trailers.
- Write the answer in the thread log: the commit, the test that proves it, and what is still open.

## Step 6: Live text

- If a fact in the live body changed, edit the body and title. Cite the new head SHA. Read it back and compare it byte for byte. Keep the draft under `pr-drafts/<ticket>/`.
- Keep the body near the guide (about 3,500 visible characters). Complex PRs may run longer. Cut repeats, not evidence: every SHA, anchor, test name, and count stays.

## Step 7: Reply draft

- Write the reply to `pr-drafts/<ticket>/reply-<n>.md`. Nick approves before it is posted.
- Shape: the answer first, then the commit and the proof, then what is open. Use plain sentences. No em or en dashes. Name the maintainer's point in their terms.
- Do not add a question unless the choice is open for the maintainer (the decision ladder, rung 4 or 5). Settled choices stay as stated behavior.

## Step 8: Proof before a question to a maintainer

Before asking a maintainer anything, have all of these:
- The option we implemented, tested with its counts.
- The rejected option, run when it is cheap, or a stated reason it was not run.
- The blast-radius checklist below, with the result for each item.
- The named limit that the question depends on.

Settle what the pool can settle. Ask only the choices that belong to the maintainers.

## Blast-radius checklist (run for every change to a file, format, or API)

1. Readers and writers of the artifact: grep tests, packaging, scripts, CLI tools, and the Admin UI.
2. Docs that describe it. Check whether the text covers one storage form only (for example ZooKeeper only).
3. Other storage forms (ZooKeeper versus local file). Do they still have to match? State it if they differ on purpose.
4. Deployment shapes: symbolic links (for example Kubernetes ConfigMap mounts), single-file bind mounts, read-only SOLR_HOME, Windows, Docker volumes.
5. Permissions, owner, ACLs, and extended attributes.
6. Crash leftovers: temporary files, partial writes, what Solr does on restart.
7. Upgrade and downgrade: a new node reading an old file, an old node reading a new file.
8. Reload gating and caches that key on the changed value.
9. Test coverage: which tests fail on base, which only pin behavior.
10. Changelog type and wording.

## Escalation to Nick

- Rung 5 only: the intent is ambiguous, the change widens the approved scope, or the decision commits Nick publicly (`material/decision-ladder.md`).
- A maintainer ask that conflicts with the approved scope, or that we think is wrong (class D).

## Reply patterns

- Implemented: "Done in <sha>. <test> fails on the base code and passes here (<counts>, <date>). <what is left, if anything>."
- Answered with evidence: "<answer>. The code is at <file> line <n> in <sha>, and <test> checks it."
- Choice handed back: "We implemented <option> in <sha>. We also ran <other option> (<result>). Which do you prefer?"
- Out of scope: "That is a separate change. We have named it in the Limits section and can open a follow-up if you want one."

## Current threads

- PR 5118 (SOLR-18010): no maintainer comment or review as of 2026-10-22 00:00 UTC. The one comment is Nick's (2026-10-14). See `reports/maintainer-threads/5118.md`.
- PR 5134 (SOLR-6831): no maintainer comment or review as of the same time. See `reports/maintainer-threads/5134.md`.
