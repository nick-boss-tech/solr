# Skip record: SOLR-12646

- Ticket: SOLR-12646, Splitting a shard that has been touched by 6x fails with 7x.
- Verdict: keep-open.
- Draft branch: solr-12646-draft-open (new branch from origin/coordination; the verdict changed from close-obsolete, so solr-12646-draft-close is left in place and is not supported).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm5 (pool Linux host), 2026-10-20 UTC. Redo of remediation batch 08, item 8 (`assignments/pool-aaa-skip-verdict-remediation-08.md`).

## Ticket reading

JIRA read 2026-10-20: Open, no resolution, created 2018-08-08, updated 2019-06-08, no fix versions, no links, no comments. The report: a one-shard collection created on Solr 6.6 with several segments, upgraded to Solr 7.x, optimized, and then `SPLITSHARD`. The split never returns; after a restart the Overseer retries the split and fails with a warning. The evidence tool shows only the first 1200 characters of the description, and the warning text is past that point, so the exception class named in the warning is not confirmed here.

## Code reading

Read at the pin:

- A search of the whole tree for the autoscaling variable type named in the earlier reading (`Variable.Type` in `*.java`, the `git grep` used for the close-obsolete basis) returns zero hits. So an autoscaling `Variable.Type` class that the warning might name is absent at the pin.
- The split command path (`SplitShardCmd`) and the Overseer's retry of a failed split were not traced in this pass.
- No commit at the pin names SOLR-12646 (the evidence tool lists none).

## History

- The autoscaling removal commits were not traced in this pass.
- No commit names the ticket.

## Reproduction attempts

Not run. This pass ran no builds or tests. The reproduction is the ticket's steps at the pin: a one-shard index created with several segments, optimized, split; plus a read of the complete warning text from the ticket.

## Gap analysis

- The close-obsolete basis was a grep for a class that the earlier reading linked to the warning. The warning's full text is not available to this pass, so the link from the ticket's failure to the removed class is unverified.
- The split itself never returning is a separate symptom that the removed-class argument does not explain.
- Without the full warning text or a split run at the pin, neither a close nor a fix can be recorded.

## Verdict justification

keep-open: the removed-class argument rests on a warning text this pass could not read, and the "never returns" symptom is not explained. close-obsolete would need the warning to name the removed class and an empty grep for it at the pin; the grep is empty, but the link is not confirmed. close-fixed and close-cannot-reproduce do not fit without a commit or a run.

Missing for this verdict: the complete warning text from the ticket (to confirm the class), and a split run at the pin on a shard touched by a 6.x segment layout, with the outcome recorded.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path, as stated above.
</content>
