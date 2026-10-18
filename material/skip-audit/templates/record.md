# Skip record: KEY

- Ticket: KEY, summary, JIRA status and resolution at check, date checked (UTC).
- Verdict: one verdict from `material/skip-audit/verdict-rubric.md`.
- Draft branch: solr-NUMBER-draft-STATUS.
- Pinned head: fork origin/main sha, plus the upstream sha if cited.
- Checked by: the host's tracking name, date and time (UTC).

## Ticket reading

What the ticket asks, the symptom it gives, the comments that matter, and the linked issues with their status.

## Code reading

Each code path read, with file and line, and what it shows at the pinned head. Include the grep commands and their results.

## History

Commits found on the pinned head that name the ticket or touch the code path. For each: sha, date, subject, and whether it is an ancestor of the pinned head.

## Reproduction attempts

One entry per attempt, in order:

- Command, with the head sha and any seed or input.
- Result: pass or fail, with the exact failing line or output.
- Log path.
- Conclusive: yes or no, and why.
- If an attempt was inconclusive, the next variant tried and why.

If no attempt was made, write "Not attempted" and the reason.

## Gap analysis

Why the reported behavior happens, or why it no longer happens, in plain terms. Name the uncertainty that remains.

## Verdict justification

The verdict, and the minimum evidence from the rubric that it meets. Name any requirement not met.

## Limits

What this record does not establish.
