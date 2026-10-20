# Skip record: SOLR-13216

- Ticket: SOLR-13216, Trouble restoring a collection.
- Verdict: keep-open.
- Draft branch: solr-13216-draft-open (verdict unchanged; this record replaces the earlier record on this branch).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm5 (pool Linux host), 2026-10-20 UTC. Redo of remediation batch 07, item 8 (`assignments/pool-aaa-skip-verdict-remediation-07.md`).

## Ticket reading

JIRA read 2026-10-20: Open, no resolution, created 2019-02-04, updated 2019-06-08, no fix versions, no links, no comments. The report: a `RESTORE` from a production cluster to a staging cluster proceeds normally and then, at the end, the restored data is dumped together. The ticket gives the restore request and no error text, log lines, stack trace, or collection size.

## Code reading

Read at the pin:

- `solr/core/src/java/org/apache/solr/handler/RestoreCore.java:145-244`: the restore copies every file in the backup into the restore index directory, then on success moves it into place and deletes the old non-snapshot index files (lines 236-246 `if (success)`), and on failure rolls back with an exception (`"Exception while restoring the backup index"`).
- The text "Copying file ... to restore directory" that the ticket reporter may have seen was rewritten by `33d16b570c` (SOLR-13608, "Incremental backup file format"), an ancestor of the pin (checked with `git merge-base --is-ancestor`). The restore path itself was not replaced.
- The code shows what a restore does at the end; it does not show which step produced the dumped data in the reporter's cluster, because the ticket has no logs or error text.

## History

- `33d16b570c` (SOLR-13608): ancestor of the pin, changes the backup file format and the copy messages.
- No commit at the pin names SOLR-13216 (the evidence tool lists none), and no commit addresses a missing-segment or end-of-restore failure.

## Reproduction attempts

Not run. This pass ran no builds or tests. The missing piece is the reporter's logs or the error text at the end of the restore, which the ticket does not provide; without them, a run would have to guess the failing step.

## Gap analysis

- The ticket has no error text, no stack trace, no logs, and no comments, so the failing step cannot be named from the material.
- The code path is present and rolls back on failure, which the reporter's description does not match, so the cause is not identified by reading.
- The advisory's shape 3 (structurally cannot finish) would apply only after a full redo with the same gap. This is the first redo, so the verdict is keep-open, with the gap named, and the item is not balked.

## Verdict justification

keep-open: the code path is present and the failure has no identified cause; the ticket does not contain the material that would identify the failing step.

Missing for this verdict: the reporter's restore logs or the error text from the end of the restore, and then a run that reproduces the failing step.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path, as stated above.
</content>
