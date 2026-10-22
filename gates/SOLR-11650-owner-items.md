# Gate job: SOLR-11650 owner-items round 1 (item 2, leaderUrl redaction)

Status: DONE

- Branch `solr-11650-submit`. Fix commit `4de476d091d` on top of `e4f5e941cd`. Claim: `claims/pool-owner-items-solr-11650.md` (rung 4 choice recorded there).
- Host: vm5. Logs: `/workspace/gates/logs/vm5/solr-11650-owner-items/`.

## Steps (on the tree that became `4de476d091d`)

1. Tidy: rc=0 (formats the new test lines).
2. `:solr:core:compileTestJava` and `:solr:core:test --tests IndexFetcherLeaderUrlRedactionTest`: rc=0. XML: 3 tests, 0 failures, 0 errors, including `testLeaderUrlWithRawSlashOrQuestionMarkInPasswordIsWithheld`.
3. `:solr:solrj:test --tests URLUtilTest`: rc=0 (redaction pins unchanged).
4. Mutation (rung 2): `IndexFetcher` messages switched back to `URLUtil.redactUserInfo` (no withholding). Result: `testLeaderUrlWithRawSlashOrQuestionMarkInPasswordIsWithheld` FAILED, rc=1. File restored and checked clean before commit.

## Result

GATE GREEN at `4de476d091d`, with the mutation proving the new test discriminates.
