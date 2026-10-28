# Claim: SOLR-18432 prerelease normalization test follow-up, round 1

Status: CLAIMED. Immutable implementation claim; completion is derived from the report and gate job below.

- Claimant: windows, Codex desktop; tags implementation, review, records.
- Started: 2026-10-28 03:44:47 UTC (October 27, Eastern).
- Authority: Nick's direct request after reviewing PR 5254: "Try to produce additional tests then hand it off to the gate agent".
- Board snapshot reviewed: e768687d9514e4c3ffba60282499929e23dfc980. No active assignment, claim or gate for this slice. Read WORKFLOW.md, START-HERE.md, UI-PLAYBOOK.md, host capabilities, decision map, decision ladder and owner complaints at that snapshot.
- Original contributor head O: d59169c9cc96dd60c976079d18ef4da56024bb26, dsmiley/solr:SOLR-18432-solrversion-prerelease. Live PR head and discussion refreshed; unchanged and no comments/reviews.
- Local worktree: C:/Users/shaninna/dev/Solr-issues/wt/SOLR-18432. New local branch wt-solr-18432-submit, fork destination solr-18432-submit. Remote fork branch and local worktree/branch are absent at intake; use O directly because branch-work new assumes an existing fork branch.
- Slice: add tests in solr/api/src/test/org/apache/solr/util/TestSolrVersion.java for normalized exact range boundaries, preserved metadata and original object, already released versions/idempotence, and numeric release ordering. No production behavior, version constant, contributor changelog, or public PR edits.
- Decision ladder: rung 1, existing SolrVersion delegation and semver4j 6.0.0's immutable modifier preserve the numeric tuple and build metadata; rung 3, the ticket explicitly keeps strict comparison and excludes a cluster injection seam. Test the public version API without adding such a seam.
- Proof boundary: Windows static diff review only. Build, formatting, focused/neighbor tests, and discriminating mutations run on the Linux gate host. No new assertion is called proven before that run.
- Deliverables: reports/pr-5254-normalization-tests-implementation-round-1.md and gates/pr-5254-normalization-tests-round-1.md, with pushed exact head, required checks, original/synthetic mutation bases and receipt destination. Commit and plain-push the one-file test patch to the fork in this session.
- Public communication: none requested; no GitHub/JIRA comment, PR edit, review submission or contributor branch update is part of this claim.
