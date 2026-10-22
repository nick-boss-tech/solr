# Gate job: SOLR-18458 owner-items round 1 (item 6, debug=results narrowing)

Status: DONE

- Branch `solr-18458-submit`. Commit `326184849e3` on `344485485f`.
- Host: vm5. Logs: `/workspace/gates/logs/vm5/solr-18458-owner-items-2/`.

## Steps

1. Tidy: rc=0.
2. `:solr:core:compileTestJava` and `:solr:core:test --tests TestReRankQParserPlugin`: rc=0. XML: 14 tests, 0 failures. `testDebugResultsIgnoredWithoutResponseBuilder` (was `testDebugResultsWithoutResponseBuilder`, asserted the widened behavior) and new `testDebugAllWithoutResponseBuilder`.

An earlier run at `344485485f` failed on the old assertion, which pinned `debug=results` to disable caching through the fallback. That is the widened behavior the decision removes, so the expected value was changed. This is part of the decided implementation, not a gate repair.

## Result

GATE GREEN at `326184849e3`.
