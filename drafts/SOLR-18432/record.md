# SOLR-18432 closeout record

Verdict: keep-open (draft branch `solr-18432-draft-open`).

- Pinned head: fork `origin/main` `498a09aea97aac86c2cc7ac114113b946d249942`. Upstream history is not cited.
- JIRA checked 2026-10-19: status Open, no resolution, no fix versions, created and updated 2026-09-09. Description read in full (about 900 characters). One comment (2026-09-09, janhoy), read in full: a maintainer reply that calls the items "improvements worth doing" and says they have not been seen as issues.
- Audit verdict on record: `confirmed-skip` (audit-1, 2026-10-04): "Improvement program for SolrVersion prerelease handling; not a single bug." The audit note also cites `research-notes/SOLR-18432.md`, which is not on `coordination` or in the pool worktree. It was not read for this record.

## Code paths read at the pinned head

- `solr/api/src/java/org/apache/solr/client/api/util/SolrVersion.java:32`: `LATEST_STRING = "11.0.0"` is a hardcoded constant. Line 35 derives `LATEST` from it.
- `dev-tools/scripts/addVersion.py:25-40`: the release script rewrites that constant with the release version. It does not add a qualifier.
- `build.gradle:56`: `versionSuffix` defaults to `SNAPSHOT`, so between releases the build version carries a suffix and `LATEST_STRING` does not. This matches the ticket's mismatch.

## Reproduction

None attempted. The mismatch is visible in the code paths above. The ticket asks for a broader change to how the version is carried, not a single defect with a test that would fail today.

## What the verdict does not cover

- Whether `LATEST_STRING` should carry the build suffix, and what that changes for code that compares versions or for the release script's rewrite. That is a design choice; it is not decided on the ticket.
- The contents of the research note cited by the audit.

## What discharges keep-open

A decision on the design (carry the suffix or not, and where the release rewrite happens). Then the work can be split into a concrete PR with a test. The owner decides whether to take the design.
