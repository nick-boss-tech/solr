# PR #5154 (SOLR-6759) access-control scenario, with #5138 (SOLR-7520), round 1

- Host: vm5. Claim: `claims/pool-vm5-pr5154-access-control-scenario-round-1.md`. Assignment: `assignments/pool-pr5154-access-control-scenario-round-1.md`.
- Heads checked by ls-remote at claim time (2026-10-23 04:20 UTC): `solr-6759-submit` at `bdeb07d03b9`, `solr-7520-submit` at `15cc9ba5a63`. Both still match the assignment.
- Final heads: `solr-6759-submit` at `7bd6cf52b1` (one test commit), `solr-7520-submit` at `bb5d30124d` (one test commit).

## Finding on the critique

The critique says the expand path could leak an expanded document past an access-control boundary. The probe does not show a leak. With a post filter that holds every document and releases only the permitted ones from `complete()`, the base code returns an empty expanded section. No denied document appears in the base response. The base failure is missing permitted expansions (fail-closed), not a leak. The PR body for #5154 says so.

## Scenario test, #5154 (rung 2 probe)

- Test: `TestExpandComponent.testExpandReleasesOnlyPermittedDocsFromPostFilterCompletion`. Six documents in two groups; three are permitted (`test_i` below 10) and three are denied. The request is `q=*:*`, `fq={!collapse field=group_s}`, `fq={!releaseoncomplete}`, `expand=true`. Assertions: two main results, two expanded documents, and no denied document in the expanded section.
- Helper: `ReleaseOnCompleteQParserPlugin` (test only, registered in `solrconfig-collapseqparser.xml`). It holds documents in `collect` and releases the permitted ones from `complete()`.
- Head `7bd6cf52b1`: `TestExpandComponent` 11 of 11, from fresh JUnit XML.
- Base mutation: `ExpandComponent.java` reverted to `cabedd1d968`, tests kept. The new test fails at `count(//lst[@name='expanded']//doc)=2` with an empty expanded section. The main results (1 and 4) are correct on base, since the main search completes. Denied documents 2 and 3 do not appear. The two count tests from the PR also fail on base, as before.
- Restored `ExpandComponent.java` from git afterwards.

## Scenario test, #5138 (rung 2 probe)

- Test: `AnalyticsMergeStrategyTest.testGroupedReleasesOnlyPermittedDocs`. Shard 0 holds `sort_i` 5, 50 and 1. The request is `q=*:*`, `fq={!releaseoncomplete}`, `group=true`, `group.field=sort_i`, `group.distributed.first=true`, `group.ngroups=true`. The first phase should report `groupCount` 2 and top groups `5` and `1`.
- Helper: `ReleaseOnCompleteQParserPlugin` (test only, package `org.apache.solr.search`, registered in `solrconfig-analytics-query.xml`), same release rule on `sort_i`.
- Base `14c7aac0d15` (`CommandHandler.java` reverted, tests kept): fails with `expected:<2> but was:<0>`.
- Head `bb5d30124d`: 3 of 3 from fresh JUnit XML (the other two methods in the class are the PR's existing analytics checks).
- A plain `group=true` request without `group.distributed.*` passed on base in an earlier probe, so it does not reach the path #5138 fixes. The test uses `group.distributed.first`.

## Plugin shape

The two helpers differ in one way. In #5154 the leaf collector is created in `complete()`. In #5138 it is created in `doSetNextReader`, as `DelegatingCollector` does by default, and the held documents are grouped per segment. The first shape made the grouped first phase throw `docs were sent out-of-order` (`TrieField`'s value source), so #5138 uses the second shape. #5154 keeps the first shape because its gate was already green at `7bd6cf52b1` and it passes there.

## Gates

- #5154 at `7bd6cf52b1`: tidy clean; `:solr:core:compileTestJava` rc 0; `TestExpandComponent` rc 0 (11 of 11); `:solr:core:check -x test` rc 0. Log: `gates/logs/vm5/SOLR-6759-pr5154-gate/`.
- #5138 at `bb5d30124d`: tidy clean; `:solr:core:compileTestJava` rc 0; `AnalyticsMergeStrategyTest` rc 0 (3 of 3, fresh XML at 06:02 UTC); `:solr:core:check -x test` rc 0. Log: `gates/logs/vm5/SOLR-7520-pr5138-gate/`.
- Error Prone was not run (disabled outside CI), in both gates.
- Upstream CI on both PRs is not a gate and is not waited for.

## PR descriptions

- #5154: the Proof section now lists the release test and its base and head results. The Limits section now says the count tests check `complete()` calls, the release test checks the expanded output, and the access-control shape shows a missing expansion on base, not a leak. Edited through the API and read back.
- #5138: the Proof section now lists the grouped scenario test, its base and head results, and the note about plain `group=true`. Edited through the API and read back.

## Not done

- No new PR, comment, or JIRA item. Requesting review from Smiley is still an owner decision.
- The 5154 critique's "leak" wording is not supported by this probe. The owner may want to decide whether the ticket text or the critique should change. This is a record, not a question for the owner.
