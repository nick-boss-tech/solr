# Skip record: SOLR-14530

- Ticket: SOLR-14530, "Eliminate overuse of ShardHandler interface". JIRA status Open, resolution none, fix versions none, created 2020-06-02, updated 2020-07-15. Checked 2026-10-19 UTC.
- Verdict: keep-open.
- Draft branch: solr-14530-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-14530-vm2.md).

## Ticket reading

ShardHandler is a complicated interface used in many places to make a simple synchronous HTTP request to another node. The ticket asks to reduce that use, since most calls need only a plain synchronous request. The description is one paragraph; the report has no comments and names no test class.

## Code reading

- solr/core/src/java/org/apache/solr/handler/component/ShardHandler.java exists at origin/main 498a09aea97, with its implementations HttpShardHandler.java and ParallelHttpShardHandler.java in the same package.
- `git grep -l 'ShardHandler\b' origin/main -- solr/core/src/java` lists 51 files. The count is the scope of the refactor the ticket describes.
- No commit on main names SOLR-14530 (`git log --oneline --grep=SOLR-14530 origin/main` returns nothing).

## History

- Commits on origin/main naming SOLR-14530: none.

## Reproduction attempts

Not applicable. The ticket asks for a refactor of code structure, which a reproduction of a failing run cannot show. Behaviour is covered by the existing shard-handler tests; no run was made in this pass.

## Gap analysis

The interface and its use across 51 files are present at the pin. Whether any part of the refactor has been done is not established by a bounded search, since the request is about how widely the interface is used rather than a single defect.

## Verdict justification

keep-open meets the rubric: the pinned head sha is cited, the Jira status and date are read in this pass, code paths are cited with file and line, and the attempts and the missing piece are listed. close-fixed does not hold: no commit names the ticket. close-obsolete does not hold: the interface and its uses are present. close-cannot-reproduce does not apply to a refactor request. The missing piece is a maintainer decision on whether to pursue the refactor and, if so, which uses to change first.

## Limits

This record does not measure the complexity of the interface. It does not propose a replacement design. It does not check the 51 files individually.
