# Family: status-note (implemented, never told)

Status: DRAFT ONLY. Prepared from rev4 records of `material/harmonized-backlog/batches/h1-NNN-rev4.json`. H1 QA by a second host has not been published, so no text here is approved for use. Nothing is posted to Jira.

Eligibility (assignment rules): bugs are barred from the four closure families; Critical and Blocker are barred from all families; a key with no metadata row is not eligible.

Purpose: a ticket where an owner PR or gated branch exists and the Jira ticket has not yet mentioned it. Eligible tickets are listed in `material/harmonized-backlog/families/status-note-eligible.tsv`.

## Eligible now: 69 tickets

Counts: pr-open 57, gated-branch (no PR number, branch named) 11, pr-merged 1. A "told" check was run live against every Jira comment: a comment naming the PR number, or a comment by the owner account, marks the ticket as told. 33 candidates were told and are excluded. The check is a heuristic. A second read of each comment before posting is part of the fill rule.

## Template (Jira wiki markup, first person)

```
[~{REPORTER}] I wanted to point you to the work on this issue. {PR_LINE} {PR_SUMMARY}

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
```

`PR_LINE` is one of:
- pr-open: "A pull request is open for it: https://github.com/apache/solr/pull/{PR_NUMBER}."
- gated-branch: "The fix is on a branch, {BRANCH}, that has passed the local checks. A pull request is not open yet."
- pr-merged: "The fix is merged in pull request https://github.com/apache/solr/pull/{PR_NUMBER}."

`PR_SUMMARY` must be one sentence taken from the pull request's own opening text, read at fill time. It is not filled from the record. A record without that sentence is not fillable.

## Placeholders and fill rule

- `REPORTER`: Jira reporter, from the metadata CSV `Reporter` or the Jira field.
- `PR_NUMBER` and `PR_LINE`: from the record's `citations.pr` (PR number) or `branch_state` and `citations.branch`.
- The record's `branch_state` must still match the live PR state at fill time. A PR that closed since rev4 is not fillable.

## Worked example (SOLR-2681)

Record: rev4 batch `h1-001`, key SOLR-2681, `branch_state` pr-open, `citations.pr` = apache/solr PR #5104 from `solr-2681-submit`. Metadata: Bug, Major, Updated 2026-10-11, summary "Lucene highlighting unable to extract terms from sub-query o...". PR #5104 is open, not a draft, and its opening text says: "With the original highlighter, a query wrapped in a function query is not highlighted."

```
[~{REPORTER}] I wanted to point you to the work on this issue. A pull request is open for it: https://github.com/apache/solr/pull/5104. With the original highlighter, a query wrapped in a function query is not highlighted.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
```

`REPORTER` stays a placeholder because the Jira reporter was not looked up for this example.
