# solr-13943-submit

- Branch: origin/solr-13943-submit (the PR branch; ci/13943-datemath has the same test code plus a fork-only workflow commit)
- Head: a24849b7707
- Base: upstream/main at 9d7cc2884e8 (merge-base c3cdf7b46e8, 26 commits behind, 4 ahead)
- Scope: TimeRoutedAliasUpdateProcessorTest.java (removes @AwaitsFix, replaces the latch with a 30 s poll), changelog fragment. No fork-only files.
- Verdict: Close, pending the changelog question
- Reviewer: spot-check, 2026-10-07

## Findings (ranked)

- **MEDIUM (hypothesis):** The change un-mutes a test that was marked @AwaitsFix. The root cause in the commit message (the test's watcher fires before the provider's ZkStateReader refreshes) is not demonstrated. If the poll is the wrong fix, the test becomes a flaky gate on every build.
- **LOW (verified):** The changelog entry is a test description. Solr changelog entries describe user-visible changes, so this fragment probably should not exist. Check the changelog policy before PR.
- **Checked, OK:** the poll is bounded (30 s TimeOut) and it parses the value before accepting it.
- **Housekeeping:** 26 commits behind upstream/main. Rebase before PR.

## Not checked

Nothing was compiled or run. Did not reproduce the race. Did not confirm that the `aliasUpdate` latch field is still used or can be removed.
