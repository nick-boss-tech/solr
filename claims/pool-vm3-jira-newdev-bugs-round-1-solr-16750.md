# Claim: pool-vm3-jira-newdev-bugs-round-1, slice SOLR-16750 (item 9 only)

- Claimant host: vm3, matching `hosts/vm3.md`.
- Status: CLAIMED. Slice: item 9 only, SOLR-16750. Other items are not claimed by vm3.
- Job or assignment: `assignments/pool-jira-newdev-bugs-round-1.md`, item 9.
- Base: apache/main at `c8ab71430f`.
- Capability tags used: implementation, gate.
- Started: 2026-10-19T19:25Z (UTC).

Heartbeat: 2026-10-19T19:25Z

Result (vm3, 2026-10-19T19:57Z): STOPPED at reproduction; no branch pushed. Item 9 needs a decision before a fix.

- Reproduced at base `c8ab71430f` with a regression test in `EnumFieldTest` (`testGroupingOnEnumFieldReturnsLabels`, seed `16750E51`, log `/workspace/gates/logs/vm3/solr-16750-repro/focused-run4-seed51-ngroups-assert.log` and the run 5 `focused.log`). Single-shard `group=true&group.field=severity` returns `groupValue` `'3'` for the label `High`, matching the enum-index symptom. Run 5 status: `test_exit=1`, `mismatch: 'High'!='3' @ grouped/severity/groups/[0]/groupValue`.
- Seeds `16750E50` skipped the test: `solr.tests.numeric.dv=false`, which `EnumFieldType` does not allow. The suite's `testEnumSchema` uses the same `assumeFalse` guard, which the test now copies.
- Multi-shard site, from reading: `solr/core/src/java/org/apache/solr/search/grouping/distributed/command/GroupConverter.java` line 86 casts `fieldType.toObject(...)` to `Integer` in the `INTEGER` branch. `EnumFieldType.getNumberType()` returns `INTEGER`, so an `EnumFieldValue` hits `ClassCastException`. Not reproduced live (no distributed run).
- Single-shard site, from reading: `solr/core/src/java/org/apache/solr/search/grouping/endresulttransformer/GroupedEndResultTransformer.java` line 81 rebuilds the value with `createFields(group.groupValue().utf8ToString())` and adds `toObject`. The writer prints the number, not the label; the exact path that yields `3` was not traced.
- Decision needed (owner or main agent): the conversion for enum group values. Option A: emit the label, via `EnumFieldType.indexedToReadable` (the same conversion `TopGroupsResultTransformer` uses), for enum fields only. Option B: keep `EnumFieldValue` and fix the writer path. Either choice also needs the multi-shard `Integer` cast fixed in `GroupConverter.toMutable`, which makes this more than a minimal fix under the assignment's scope rule.
- Test patch kept outside the repo: `/workspace/tools/solr-16750-test.patch` (on the base, not on any branch).

DONE: 2026-10-19 by vm3, stopped. No branch. Deliverable: this claim (result section). Heartbeat: 2026-10-19T19:57Z
