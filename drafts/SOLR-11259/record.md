# Skip record: SOLR-11259

- Ticket: SOLR-11259, SharedFSAutoReplicaFailoverTest: reproducible seeds failure: "Timeout waiting for all live and active". JIRA was not re-read for this record.
- Verdict: close-obsolete.
- Draft branch: solr-11259-draft-close.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Source: A2 flake locate round 1 (reports/2026-10-18-skip-repro/A2-flake-locate-round-1.md). Checked here from that report; the runs were not repeated.

## Ticket reading

The summary as recorded in the queue: SharedFSAutoReplicaFailoverTest: reproducible seeds failure: "Timeout waiting for all live and active". Test class from the report: not named.

## Code reading

Not read for this record; the verdict rests on the run data and the grep result given below.

## History

No commit history was used for this record.

## Reproduction attempts

Grep at the pinned head: `git grep -l SharedFSAutoReplicaFailoverTest 498a09aea9` returns no files. No run was made because the class is absent.

## Gap analysis

The class is absent at the pinned head and no reference remains; the report does not apply to the current code.

## Verdict justification

close-obsolete: the test class the ticket names does not exist at the pinned head, and a repository-wide grep finds no reference to it.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub.
