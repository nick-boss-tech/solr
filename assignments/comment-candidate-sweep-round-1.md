# Assignment: comment candidate sweep (round 1)

Status: OPEN, not started. Nothing posts until the owner gives a go per batch.
Tags: draft, sweep, owner-gate

Find open Solr tickets that can take one of the three comment types below, and draft the comment. This extends the unit-1 review to tickets not yet covered. It does not repeat the unit-1 set, and it does not replace the H1/H2 backlog pack (`assignments/backlog-first-100-pack-round-1.md`), which is vm2's.

## Comment types

Use the same wording rules as the posted unit-1 comments: start with "From code reading against apache/main b73f03976fa,", full-path citations (`solr/...`, line numbers checked), one question at the end, no first person about a reproduction, footer `🤖 AI-generated 🤖`.

- **Close candidate:** the code on main gives the intended behaviour (documented, by design, or removed/absent), so closure is proposed. The question asks whether closure is OK. Basis must be a code path, a documented rule, or a maintainer ruling in the ticket.
- **Candidate fix:** the code path plausibly shows the reported problem, and one specific change is defensible. The question asks whether that change or the problem is still wanted. Name only changes we would actually make.
- **Maintainer question:** only when the decision ladder (`material/decision-ladder.md`) cannot settle the choice. Rung 4 choices become candidate fixes, not questions. Each question states the two options and asks which one.

## Candidate pool

JQL, run live on the day of the batch:

    project = SOLR AND status = Open AND issuetype in (Bug, Improvement) AND priority not in (Blocker, Critical) AND affectedVersion in (<all 4.x, 5.x and 6.x versions on JIRA>)

Use the version names from `project/versions` for the affected range; do not hard-code them.

Exclude: any key already in `drafts/unit-1-comments/`, any key in the posting log `drafts/unit-1-comments/posting-log-round-1.md`, any ticket the owner has listed as excluded, any ticket with a linked or mentioned open PR, and any ticket with a comment by the owner (nickshanin) that is not a closing note.

## Method per ticket

1. Read the ticket: summary, description, status, resolution, affected versions, fix versions, comments, `updated`.
2. Check the affected-version rule: the ticket must have at least one affected version in 4.x to 6.x. A ticket with no affected version recorded is held, not drafted, and listed as such. This is the 4.x-6.x applicability check the owner asked for.
3. Find the code on main that decides the reported behaviour, at `b73f03976fa` (`git show`, `git grep`). Decide: close candidate, candidate fix, maintainer question, or no comment.
4. Cite full paths and check each line range. If the decision is not supported by code or by a ruling in the ticket, no comment.
5. Draft the comment with the wording rules above.
6. Live check at fill time: Jira status still Open, `updated` date recorded, no open PR referencing the key (search apache/solr), no owner comment already posted on the ticket that answers the question.
7. Log the ticket in the batch index with the decision, the citations, and the live-check result. Holds and drops get a reason.

## Batch shape

- 10 tickets per batch, in JQL order (last updated first).
- Deliverable per batch: `drafts/comment-sweep/batch-<n>.md` with a header for the batch, one section per ticket (key, type, affected versions, draft, citations, live check), and a list of holds and drops with reasons.
- The owner gives a go per text. Posting is then logged in `drafts/unit-1-comments/posting-log-round-1.md` (or a sweep log if the owner prefers a separate file).

## QA

A second host spot-checks 3 entries per batch: re-derive the citation from the source and re-run the live check. Any failing entry is dropped and listed.

## Blockers

- The JIRA token in `/workspace/.env` (`JIRA_PAT`) can post, so the sweep can post once approved, but nothing posts without the owner's go on each text.
- The affected-version field is empty on many older tickets (56 of the 117 unit-1 texts). Those tickets are held under step 2, not drafted.
