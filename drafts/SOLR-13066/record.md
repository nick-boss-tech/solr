# Skip record: SOLR-13066

- Ticket: SOLR-13066, "A failure while reloading a SolrCore can result in the SolrCore not being closed." JIRA status Open, no resolution, fix versions empty, checked 2026-10-18 (UTC).
- Verdict: close-fixed.
- Draft branch: solr-13066-draft-close.
- Pinned head: fork origin/main at 498a09aea9. Fix commit on upstream: 7de72c9bc7 (December 12, 2018). Backport to branch_7x: 4bcad18084.
- Checked by: pool Linux host, coordinator session, 2026-10-18 (UTC).

## Ticket reading

The summary is the whole symptom: a failure while reloading a core leaves the new core open. The description is empty. Two comments are bot notices of the commit to master and to branch_7x. Linked issue SOLR-13067 (Resolved) hardens BasicAuthIntegrationTest and is not the fix.

## Code reading

- solr/core/src/java/org/apache/solr/core/CoreContainer.java:2014: `newCore = core.reload(coreConfig);` is assigned before the registration and the cluster checks.
- solr/core/src/java/org/apache/solr/core/CoreContainer.java:2054: `if (!success && newCore != null && newCore.getOpenCount() > 0) { IOUtils.closeQuietly(newCore); }` runs in the `finally` block. A failed reload closes the new core.
- The commit changed CoreContainer.java (23 lines), SolrCore.java (98 lines) and solr/CHANGES.txt.

## History

- 7de72c9bc7, 2018-12-12, "SOLR-13066: A failure while reloading a SolrCore can result in the SolrCore not being closed.": ancestor of the pinned fork head (checked with `git merge-base --is-ancestor`).
- 4bcad18084 on branch_7x (JIRA bot comment): backport. Not checked on the fork.

Evidence command: `python3 -I /workspace/.claude/skills/skip-ticket-evidence/evidence.py SOLR-13066 --ref origin/main --commit 7de72c9bc7`.

## Reproduction attempts

Not attempted. The verdict rests on the fix commit and on the code path at the pinned head. A reproduction would need a test that makes reload fail after registration, which was not written for this record.

## Gap analysis

Before the fix, a reload that threw after the new core was registered left that core open, because nothing closed it. At the pinned head, the `finally` block closes the new core when the reload did not succeed and the core is still open. The guard matches the reported failure. The commit adds no regression test.

## Verdict justification

close-fixed. Minimum evidence met: the fix commit is an ancestor of the pinned head, and the changed code is still present at lines 2014 and 2054.

## Limits

No test was run. The record does not check the released versions the fix shipped in, because JIRA lists no fix version.
