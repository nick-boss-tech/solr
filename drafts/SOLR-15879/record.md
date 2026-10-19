# Skip record: SOLR-15879

- Ticket: SOLR-15879, "Memory leak in QueryResultsCache". JIRA status Open, resolution none, fix versions none, created 2021-12-24, updated 2022-10-26. Duplicate SOLR-15878 (Resolved). Checked 2026-10-19 UTC.
- Verdict: close-obsolete.
- Draft branch: solr-15879-draft-close.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-15879-vm2.md).

## Ticket reading

The reporter used a queryResultCache of class solr.FastLRUCache with maxRamMB="1048" and size="5000" on Solr 7.5. The cache grew to 320K entries and 9 GB, with only inserts and no evictions, because the RAM limit was not enforced. Removing maxRamMB and keeping size bounded the cache. The only comment (elyograg, 2021-12-24) notes that Solr 7.5 is in maintenance mode and asks whether the problem persists in 8.x. No test class is named.

## Code reading

- The cache class named in the report, FastLRUCache, is not in origin/main. `git grep -n 'FastLRUCache' origin/main -- solr/core/src/java solr/core/src/test` returns no matches. The only references are historical entries in CHANGELOG.md (for example line 4716, SOLR-9633, which added the maxRamMB parameter to FastLRUCache).
- It was removed by commit b4fe911cc8e (SOLR-13817: Remove legacy SolrCache implementations, 2019-11-14). That commit is an ancestor of the pinned head (git merge-base --is-ancestor).
- The maxRamMB parameter is still supported, on the cache class that replaced the legacy ones. solr/core/src/java/org/apache/solr/search/CaffeineCache.java:123-125 reads maxRamMB and sets maxRamBytes; lines 163-164 apply it as the cache's maximumWeight, so the RAM limit is enforced by the cache itself. This is the reading from the code, not a test run.

Commands at the pinned head:
- git grep -n 'FastLRUCache' origin/main -- solr/core/src/java solr/core/src/test: no output.
- git grep -n 'MAX_RAM_MB_PARAM\|maxRamMB' origin/main -- solr/core/src/java: SolrCache.java:36 (the parameter name) and CaffeineCache.java:123-125, 163-164, 360-367.
- git log --format='%h %ad %s' --date=short -S 'class FastLRUCache' origin/main: b4fe911cc8e is the removal.

## History

- b4fe911cc8e (2019-11-14): SOLR-13817, removal of the legacy SolrCache implementations, including FastLRUCache. Ancestor of the pinned head.
- Commits on origin/main naming SOLR-15879: none.

## Reproduction attempts

Not attempted. The reported class does not exist at the pinned head, so the reported run cannot be repeated. A reproduction of the maxRamMB limit on the replacement cache (CaffeineCache) would test a different class; it was not run in this pass.

## Gap analysis

The reported class is gone, so the report describes behaviour that cannot occur on this code. The replacement cache enforces maxRamMB through its weight limit, which the code shows. Whether CaffeineCache's limit holds under the reporter's load was not tested here. The ticket's question (whether the problem persists in 8.x) is answered for the named class by its removal; it is not answered for the replacement by a run.

## Verdict justification

close-obsolete meets the rubric: the code the ticket is about (FastLRUCache) was removed (b4fe911cc8e, ancestor of the pinned head), and a search of main finds no remaining use in code or tests (the empty grep above). The rubric's minimum evidence for this verdict is the grep command and its empty result at the pinned head, which is recorded here. Requirement not met in full: the replacement cache's behaviour under this load was not run; the verdict rests on the removal.

## Limits

This record does not test CaffeineCache's RAM limit. It does not check the reporter's Solr version. It does not establish that CaffeineCache is free of the reported leak.
