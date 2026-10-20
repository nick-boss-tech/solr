# Skip record: SOLR-13066

- Ticket: SOLR-13066, "A failure while reloading a SolrCore can result in the SolrCore not being closed."
- Verdict: close-fixed.
- Draft branch: solr-13066-draft-close (verdict unchanged; this record replaces the earlier record on this branch).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm5 (pool Linux host), 2026-10-20 UTC. Redo of remediation batch 07, item 15 (`assignments/pool-aaa-skip-verdict-remediation-07.md`).

## Ticket reading

JIRA read 2026-10-20: Open, no resolution, created 2018-12-12, updated 2019-06-08, no fix versions, description empty, linked SOLR-13067 (Resolved, a test-hardening issue, not the fix). Two comments (2018-12-13, jira-bot) name the fix commit `7de72c9bc7` on master and `4bcad18084` on branch_7x. The ticket's open status does not change the fix: the verdict depends on the fix being on main, not on the ticket's resolution field.

## Code reading

Read at the pin:

- `solr/core/src/java/org/apache/solr/core/CoreContainer.java:2014`: `newCore = core.reload(coreConfig);` assigns the new core before registration and cluster checks.
- `CoreContainer.java:2054-2056` (in the `finally` block): `if (!success && newCore != null && newCore.getOpenCount() > 0) { IOUtils.closeQuietly(newCore); }`. A failed reload closes the new core.
- The commit `7de72c9bc7` introduced this structure: its `CoreContainer.java` hunk adds `SolrCore newCore = null;`, moves the reload assignment out of the `try`, sets `success = true` on the success path, and adds the `finally` close. The same lines are present at the pin.

## History

- `7de72c9bc7` (2018-12-12, "SOLR-13066: A failure while reloading a SolrCore can result in the SolrCore not being closed."): ancestor of the pin (`git merge-base --is-ancestor 7de72c9bc7 498a09aea97aac86c2cc7ac114113b946d249942`); its subject cites SOLR-13066.
- `4bcad18084` (branch_7x backport, named in the ticket's bot comment): not checked on the fork in this pass; not needed for the main verdict.

## Reproduction attempts

Not run. A reproduction would need a test that fails the reload after registration; none was written. close-fixed needs the fix commit, its ancestry, and the changed code at the pin, which are recorded above.

## Gap analysis

- The fix commit is named by the ticket and its subject, it is an ancestor of the pin, and its changed lines are present.
- The commit also changes `SolrCore.java` (98 lines) and the `CHANGES.txt` entry; the verdict relies on the `CoreContainer` close path, which is the part that addresses the reported symptom.
- No regression test for the failed-reload close was identified in this pass.

## Verdict justification

close-fixed: a commit on main (`7de72c9bc7`) fixes the reported problem (the new core is closed when a reload fails), the commit is an ancestor of the pin, and the changed code is present at the pin (`CoreContainer.java:2054-2056`).

Missing for this verdict: none for close-fixed; a regression test would strengthen it but is not required by the rubric.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title and code path, as stated above.
</content>
