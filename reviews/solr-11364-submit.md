# solr-11364-submit

- Branch: origin/solr-11364-submit (the PR branch; ci/11364-dv has the same solr/ code plus a fork-only workflow commit)
- Head: 8b3fc5c8880
- Base: upstream/main at 9d7cc2884e8 (merge-base 14c7aac0d15, 45 commits behind, 5 ahead)
- Scope: SolrDocumentFetcher.java, TestUseDocValuesAsStored2.java, changelog fragment. No fork-only files.
- Verdict: Close
- Reviewer: spot-check, 2026-10-07

## Findings (ranked)

- **LOW (verified):** `getNonStoredDVs(false)` is called inside the `for (String fl : requestedNames)` loop. Hoist it out of the loop.
- **LOW (hypothesis):** The test covers `fl=id,a*,a3` only. It does not cover an explicit name that is also matched by the glob, so the no-duplicates behaviour is untested.
- **Housekeeping:** 45 commits behind upstream/main. Rebase before PR.

## Not checked

Nothing was compiled or run.
