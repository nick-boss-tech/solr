# Skip record: SOLR-14282

- Ticket: SOLR-14282, "/get handler doesn't return copied fields". JIRA status Open, resolution none, fix versions none, created 2020-02-26, updated 2021-01-01. Checked 2026-10-19 UTC.
- Verdict: keep-open.
- Draft branch: solr-14282-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-14282-vm2.md).

## Ticket reading

A document fetched with /get does not include a field that a copyField copies into (the destination is stored, and the same document returned by a query does include it). The reporter's integration test shows the difference. Sixteen comments (2020-10-12 onward). A contributor tested removing the check that drops copy-field targets from /get output, and a maintainer traces the change to SOLR-3743. The discussion turns on whether /get is a store lookup or a read of the update log, and whether the copy targets should be returned.

## Code reading

- solr/core/src/java/org/apache/solr/handler/component/RealTimeGetComponent.java:633, 879 and 917 (origin/main 498a09aea97): three places skip copy-field targets (`schema.isCopyFieldTarget(sf)`). The skip is deliberate in the code; it is what makes /get omit the copied fields.
- The real-time get guide (solr/solr-ref-guide/modules/query-guide/pages/realtime-get.adoc) does not mention copyField (git grep returns no match), so the behaviour is not described in the docs at the pin.

Commands at the pinned head:
- git grep -n 'isCopyFieldTarget' origin/main -- solr/core/src/java/org/apache/solr/handler/component/RealTimeGetComponent.java: lines 633, 879, 917.
- git grep -n -i 'copyField\|copy field' origin/main -- solr/solr-ref-guide/modules/query-guide/pages/realtime-get.adoc: no output.
- git log --oneline --grep='SOLR-14282' origin/main: no output.

## History

- Commits on origin/main naming SOLR-14282: none.
- The change that introduced the skip is attributed in the comments to SOLR-3743; the commit was not traced in the pass.

## Reproduction attempts

Not run. The behaviour is deliberate in the code, so a reproduction would show the designed result. A change to include copy targets would be a design decision, and a run of the real-time get tests after such a change was not made.

## Gap analysis

The /get behaviour is a code-level design choice (copy targets skipped in three places), and the guide does not describe it. Whether /get should return copy targets is an open design question, not a defect with a fix. The documentation gap is a clear, small follow-up, but the verdict depends on the maintainers' decision.

## Verdict justification

keep-open meets the rubric: the pinned head sha is cited, the Jira status and date are read in this pass, code paths are cited with file and line, and the attempts and the missing piece are listed. close-answered does not hold: the answer is in the code, and the real-time get guide does not state it. close-fixed does not hold: no commit names the ticket. close-cannot-reproduce does not apply to a design question. The missing piece is a maintainer decision on the intended /get behaviour, and then either a change or a documented answer in the real-time get guide.

## Limits

This record does not change the behaviour. It does not trace the commit that introduced the skip. It does not run the real-time get tests.
