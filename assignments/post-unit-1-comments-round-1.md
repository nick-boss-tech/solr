# Assignment: post unit-1 comments (round 1)

Status: OPEN, held. Nothing posts until the owner approves each text.
Tags: post, owner-gate, jira-comment

Post the JIRA comments drafted from the unit-1 review. Each text needs the owner's go before it goes out. No text posts as a batch on a general go.

## Postable set (109 texts)

Use only the file and section listed. Earlier drafts for the same ticket are superseded and must not be posted.

| Group | Count | Source | Notes |
|---|---|---|---|
| Closure (round 1) | 5 | `drafts/unit-1-comments/round-1.md`, Closure section: SOLR-2420, 2516, 3473, 3539, 4558 | Cites main. Owner go per text. |
| Closure (round 2) | 3 | `round-2.md`, Closure section: SOLR-4859, 7070, 10030 | The other five round-2 closures are superseded (see below). |
| Not-a-bug (round 3) | 19 | `round-3.md`, Not-a-bug section | Not code-checked against main; the draft cites the ticket text only. SOLR-17838 (updated 2026-01-01) and SOLR-18444 (updated 2026-09-22) need a re-read before posting. |
| Fix possible, ask if still needed | 34 | `drafts/unit-1-comments/round-4-buckets.md`, sections A and B | Section A: 28. Section B (closure candidates): 6, including SOLR-4277. |
| Re-filed and new (round 5) | 10 | `drafts/unit-1-comments/round-5-completion.md`, section 1 (SOLR-6099, 6712 ask) and section 2 (SOLR-8067, 3721 close; SOLR-8311, 8709, 9477, 9648, 9978, 1557 ask) | SOLR-3721's close draft has no commit cited. Verify it before posting. |
| Maintainer questions (rung 5 only) | 8 | `round-2.md`, Maintainer-call section: SOLR-5235, 5962, 6364, 6714, 7511, 8634, 9226, 10112 | Reword before posting: the re-triage (`reports/maintainer-questions-retriage-round-1.md`) confirmed each one is still open. |
| Candidate | 21 | `drafts/unit-1-comments/candidates-and-closes-round-1.md`, Candidate section | Ask the ticket creator whether the named behaviour is still wanted. The PR and debate come later. |
| Close (re-triaged) | 9 | Same file, Close section: SOLR-2855, 4100, 4983, 4996, 5095, 6837, 8543, 9223, 11927 | Owner go per text. |

Superseded, do not post: round-1 `Maintainer-call` drafts (SOLR-2855, 3008, 3744, 4023, 4100, 4674); round-2 `Maintainer-call` drafts other than the eight listed above; round-2 closures SOLR-6099, 6712, 7198, 8770, 11292. Their replacements are in the table.

## Excluded (not in this assignment)

- **Information requests, held (6):** SOLR-7198, 8770, 11292, 7521, 8368, 11583. Hold until the ticket comments are checked for the detail, then reword. Posting them is a later decision.
- **Waiting on a probe (2):** SOLR-3744, 4674. No text until the probe runs.
- **No comment text (15):** SOLR-6813, 7427, 9925, 7404, 7758, 8641, 8741, 8966, plus the seven duplicate candidates below.
- **Duplicate candidates (7):** SOLR-3765, 5772, 7721, 9089, 11034, 11787, 13470. No recorded duplicate target, so no text.

## Method per text

1. Owner go on the exact text, recorded in the posting log before the post.
2. Read the ticket once, the same day. If `updated` has moved since the code read (2026-10-26), re-read the ticket and the comments before posting. If the ticket now has a fix, a resolution, or a reporter reply, drop the text and note why.
3. Post with the footer the owner approves. The earlier drafts carried "reviewed by Nick Shanin", which is only true after the owner has reviewed that text. Confirm the wording before the first post.
4. Log the posted text, the ticket key, and the posted comment's URL in the daily comment ledger, under the unit-1 stream.

## Blockers

- **JIRA credentials:** this host has no JIRA login. The REST API is read-only here. Posting needs an account and a route for it; the owner decides which.
- **Footer wording:** see method step 3.
- **Shallow clone:** citations are to the tree at `b73f03976fa`. Rung-1 history is incomplete; any text that cites a commit SHA needs that SHA verified first.

## Deliverable

`drafts/unit-1-comments/posting-log-round-1.md`: one row per text with the owner's go, the posted URL or the reason it was dropped.
