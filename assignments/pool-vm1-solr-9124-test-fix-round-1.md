# Assignment: SOLR-9124 head test fix, round 1

Status: CLAIMED

Capability tags: [implementation, gate]. Intended host: vm1. Posted by the main side 2026-10-16, after the live-head gate at 07b5f39e35 failed at step 4 (gates/SOLR-9124-live-head-gate.md, receipts/SOLR-9124.md top section).

Claim first: claims/pool-vm1-solr-9124-test-fix-round-1.md.

## What failed

- Head run, seed 9124C0FFEE9124, fresh JUnit XML: 5 tests, 3 failures (TestExactStatsCache 1 of 2, TestLRUStatsCache 1 of 1, TestExactSharedStatsCache 1 of 1; TestDefaultStatsCache 1 of 1, whose checkGroupedScores hook is empty). All three failures are the identical server error "can not use FieldCache on multivalued field: grp_s" from the head commit's new checkWithinGroupOrder (TestBaseStatsCache.java:111). The production change in 07b5f39e35 is not implicated: in TestExactStatsCache the grouped-scores assertion passed at the head before the new check ran.
- Root cause, from the harness schemas: the check groups on grp_s. grp_s resolves through the *_s dynamic field, which is multivalued in the schema the harness loads (solr/core/src/test-files/solr/collection1/conf/schema.xml, version 1.0: at schema versions below 1.1 a field is multivalued unless it carries multiValued="false", and *_s carries none) and in schema12.xml and schema15.xml (both set *_s multiValued="true" explicitly). Only schema11.xml (version 1.1) leaves *_s single-valued. Same trap as this branch's first gate with shard_i (the *_i dynamic field is multivalued under the same version rule).

## Decision (recorded before the code push, per the main side's brief)

Fix in test code only; production files stay byte-identical to 07b5f39e35. The within-group check groups on a new doc field grp_s1, indexed with the same g0/g1 values grp_s carries today, via the *_s1 dynamic field:

- schema.xml (version 1.0, the file the harness loads; core.properties schema default, 26 of 26 "Loaded schema test/1.0" lines in the failed gate's log): *_s1 is defined with multiValued="false" explicitly (line 720), which overrides the below-1.1 multivalued default that grp_s falls under.
- schema12.xml (version 1.4): *_s1 multiValued="false" (line 740); *_s is multiValued="true" there.
- schema15.xml (version 1.6): *_s1 multiValued="false" (line 593); *_s is multiValued="true" there.
- *_s1 is a string field, indexed and stored, so it passes the FieldCache source check the same way grp_s would have had it been single-valued (indexed, uninvertible at these schema versions, StrField uninversion).

So in every variant where *_s is multivalued, *_s1 is defined and single-valued. (schema11.xml defines no *_s1; it is also the one variant where *_s is already single-valued, and this harness does not load it: no stats test sets a schema override.) Grouping on id instead was rejected: id is the only field single-valued in all four files, but it gives one group per doc, which collapses the check (the assertion expects exactly 2 groups and compares the top doc inside multi-doc groups).

## Fix shape

- TestDefaultStatsCache.java: the two index_specific calls index "grp_s1" with the same grp value instead of "grp_s".
- TestBaseStatsCache.java: checkWithinGroupOrder sets group.field to grp_s1. Query, sorts, group.limit and every assertion stay unchanged (q "a_t:two a_t:five", main sort "id asc", group.sort "score desc,id asc", exactly 2 groups, top doc id per group equal between control and shards).
- Commit as Nick Shanin, no trailers. Push plainly (fast-forward) to fork solr-9124-submit. If the tip has moved past 07b5f39e35, stop and report instead.

## Re-gate

Fresh gate job gates/SOLR-9124-test-fix-gate.md at the new head, full hardening gate on vm1 (blocking flock on ~/workspace/tools/.solr-test-queue.lock, ~/workspace/tools/solr-gradle.sh): changelog YAML parse; tidy with the GATE INVALID guard (tidy the final tree before committing; restore gradle/libs.versions.toml); Error Prone compile; proof leg against merge-base production cabedd1d9680 judged by content (premise: TestExactStatsCache grouped-scores divergence at checkGroupedScores; the other classes' base behavior must be premise-consistent, not the grp_s server error); focused run of TestExactStatsCache, TestLRUStatsCache, TestExactSharedStatsCache, TestDefaultStatsCache counted from fresh JUnit XML at seed 9124C0FFEE9124; module check :solr:core:check -x test.

## Stop conditions

- Green: refresh receipts/SOLR-9124.md with a new top section, mark this assignment, the claim and the gate job DONE naming the new head, one takeover log line, push records and verify by read-back. PR body and pr-drafts edits are the main side's; flag falsified sentences, do not edit them.
- Red or wrong fix shape: record exactly where in the gate job, mark records accordingly, and do not push a fix commit whose gate is not green.
