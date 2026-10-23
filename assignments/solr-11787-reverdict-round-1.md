# Pool assignment: SOLR-11787, re-check the close-obsolete verdict against SOLR-6213

Status: DONE

- Capability tags: review, draft.
- Category: Logging and leader election.
- Intended host: any pool host. Reading and drafting only; no build is needed.
- Posted by: vm2, 2026-10-23, at the direction of Nick.

## Why this is posted

The recorded verdict for SOLR-11787 is close-obsolete (`claims/skip-SOLR-11787-vm2.md`, draft branch `solr-11787-draft-close`). It rests on the log4j 1.x frames in the stack trace no longer existing. The deep frames in that trace are `PeerSync.sync`, `SyncStrategy.syncReplicas`, `ShardLeaderElectionContext.runLeaderProcess`. They are the rejoin loop that SOLR-6213 describes, and that loop is still on main. On `b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb`, a test that forces failed syncs shows the stack growing 12 frames over 4 rejoins (`pr-drafts/SOLR-6213/jira-comment.md`). The log4j frames were where the stack ran out, not the cause. A 2024 comment on SOLR-11787 also asks whether it duplicates SOLR-6213, and the record says that was not checked.

## Items

1. Read `pr-drafts/SOLR-6213/jira-comment.md` and `pr-drafts/SOLR-6213/pr-description.md`, then reread the SOLR-11787 trace and `drafts/SOLR-11787/record.md`.
2. Decide between close-obsolete and keep-open as a duplicate of SOLR-6213. State the deciding facts in a replacement `record.md`.
3. Replace `drafts/SOLR-11787/jira-comment.md` with a comment that matches the new verdict, following `comment-formula.md`. Update `material/skip-audit/queue-current.json`.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Verdict | default: duplicate of SOLR-6213, keep open | change only if the trace cannot reach the rejoin path on main, and say why in the record | Nick |
| Post to Jira | decided: no | drafts only; Nick approves any post | Nick |
| Link the ticket to SOLR-6213 in Jira | decided: no | no ticket edits | Nick |

No row may leave the choice to the claimant.

## Deliverable

The updated `drafts/SOLR-11787/record.md` and `jira-comment.md` on the draft branch, the queue entry, and a claim marked DONE in the same push.

## Limits

- No Jira or GitHub posts. No force push.
- Commit as Nick Shanin with no trailers. No em dashes in anything authored.
