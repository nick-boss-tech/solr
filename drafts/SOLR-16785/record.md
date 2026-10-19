# Skip record: SOLR-16785

- Ticket: SOLR-16785, "Solr node is going down automatically and not scaling up." JIRA status read 2026-10-19 (UTC): Open, resolution none, created 2023-05-03, updated 2023-07-21, no fix version, no comments. The description is 622 characters; it has no exception text or stack trace. The config files the reporter attached are not in the ticket text.
- Verdict: keep-open. Unchanged from the earlier record.
- Draft branch: solr-16785-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (2026-10-07). Upstream sha not cited.
- Checked by: vm4, 2026-10-19 14:01-14:02 UTC.

## Ticket reading

On Solr 7.7.1 on Azure, with ZooKeeper, a core node went down and did not recover. The reporter tried recreating the replica (the new replica also went down), redeploying, creating a dummy collection from the same configset (it also failed), raising the heap from 2 GB to 4 GB, and restoring from a snapshot. The ticket has no error text, no log lines, and no reproduction.

## Code reading

Not done at the code level. The ticket has no code pointer and no error message that can be matched to a line at the pin. The one concrete detail (a replica that goes down again when recreated, with a configset that also fails) would point to the startup or configset path, but nothing in the text names a class or message.

## History

- No commit on the pinned head names SOLR-16785 (evidence tool: no hits).
- Not traced: the 7.7.1 to pin changes in the recovery path. The pin is many releases later, so it cannot show what 7.7.1 did.

## Reproduction attempts

Not attempted. The ticket gives no input or configuration to reproduce, and the attached config is not available here. This pass is review-only and runs no builds.

## Gap analysis

The report describes symptoms only. The decisive evidence is the node's own log at the time of failure (the exception that stops the core from loading or recovering, or an OutOfMemoryError if the heap is the cause), and the attached configset. Without those, neither a code path nor a fix can be matched. The reporter's own attempts (new replica also failing, same configset failing, heap raised but no change) suggest a configset or data-directory problem, which is a hypothesis from the description, not a finding.

Missing piece for a verdict change: the Solr log for the failing core at startup or recovery, and the configset attached to the ticket. With those, the record can state the exception and check it at the pin.

## Verdict justification

keep-open meets the rubric: no attempt is run, and the missing piece is named (the node log at failure and the attached configset). A close needs a fix or a non-issue shown, and neither is available. A pr-needed needs a code line that causes the failure, which the ticket does not identify. Solr 7.7.1 is also long out of support, so a fix on the pin would not show the reporter's version was fixed.

## Limits

This record does not establish a cause and does not read the attachments. It does not say whether the pin behaves differently from 7.7.1. It does not post anything to JIRA or GitHub.
