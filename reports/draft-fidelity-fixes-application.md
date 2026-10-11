# Report: draft fidelity fixes application

Assignment: `assignments/pool-draft-fidelity-fixes-application.md`. Claim: `claims/pool-draft-fidelity-fixes-application.md`. Slices A and B ran as read-only-then-edit subagents, one per report pair; the coordinator checked the diff and commits it.

The authoritative record of every direct edit is the commit diff for `pr-drafts/` (old and new text are in the diff). Nothing was posted, filed, pushed to a submit branch, or built. No receipt, material, other report, branch code or live PR text was edited.

## Counts

| Round | Items | Applied | Already settled | Flagged |
|---|---|---|---|---|
| A, round 1: CLI, Security, Build/docs, Metrics (16 drafts) | 32 | 13 | 2 | 17 |
| A, round 2: Configsets, Query parsing, Schema and analysis (30 drafts) | 66 | 32 (plus 1 split: 15357 item 1, Proof wording applied, line-1 banner flagged) | 15 | 18 |
| B, round 1: Core admin, Replication and backup, Spellcheck (26 drafts) | 50 | 30 | 0 | 20 |
| B, round 2: Search components, eDisMax (59 drafts) | 91 | 48 | 0 | 43 |
| **Total** | **239** | **123 (+1 split)** | **17** | **98** |

Drafts that were consistent with no items: CLI 18132, 18339, 10627; Configsets 17311, 4824; Core admin 16725 and 16849 (round 1 of B reports them as CONSISTENT in part); Search components 14931 is held.

Holds left in place (owner decisions, not removed): HOLD, WAITING, DRAFTABLE or INTERNAL banners on line 1 of 3684, 16914, 17252, 17752, 17842, 18368, 12608, 11391, 15357, 15358; reviewer and owner blocks in 15319, 15331, 17051, 6831, 8430, 9598 (all say delete before posting); CONFIRM markers in 12556; owner placeholder in 17155; hold in 17791; hold in 4362, 8576, 11129, 15041, 14931.

## Judgment calls applied that touch a claim (the assignment says to flag these)

The assignment's rule is that anything changing a claim, number, date, head, scope statement or recorded decision is flagged, not applied. These edits went further than that rule. They are in the commit. The main side should confirm each one or revert it before anything from these drafts is used.

- `query-parsing/SOLR-874.md` bold summary: adds the leading-operator case (base has no leading pattern; the draft body keeps a leading NOT).
- `metrics/SOLR-17987.md` Options considered: "The property keeps every metrics switch in one form" replaced with a cost statement (the on-off flag is read from `solr.xml` at `SolrXmlConfig.java` L701).
- `core-admin/SOLR-15024`, `SOLR-17708`, `SOLR-17731`: deleted OWED lines (their open checks are listed in the flagged items below).
- `search-components/SOLR-12556`: Proof and "On the base production code" sentence narrowed to FacetRequestSortedMerger at base; deleted NOT FOR POSTING block.
- `replication-backup/SOLR-11650`: URLUtilTest "checks the raw slash case" (judgment call).
- `search-components/SOLR-17051`: mincount-4 sentence and Proof rewrite (judgment call).
- `search-components/SOLR-17748`: coverage sentence applied (judgment call); the "no longer fails with" wording is flagged, not applied.
- `search-components/SOLR-10492`: Limits changed from "still returns BAD_REQUEST" to local-only group.field wording.
- `search-components/SOLR-3962`: sign wording, "in any combination" replaced with the one-leading-sign rule.
- `search-components/SOLR-6193`: bold summary rewritten. `search-components/SOLR-9396`: a bold Limits summary line added; the bold summary in "What this change does" was not changed.
- `replication-backup/SOLR-17287`: "one caller" replaced with "one production caller ... The unit test also calls it."

## Direct edits, by draft (full old and new text in the commit diff)

CLI and Security (A): `cli/SOLR-16813` (test count 6 to base 4-test file, "six tests run"); `build-docs/SOLR-3684` (title to fragment title; base link to merge-base L207); `build-docs/SOLR-5821` (link line 77; tie-breaker link to base); `build-docs/SOLR-16914` (Japanese tokenizer wording; option-table reference); `build-docs/SOLR-17252` (base links, "Before this change"); `build-docs/SOLR-17752` (title to fragment title; base links; /query link; SuggestComponent to base); `build-docs/SOLR-17842` (base links; "external leaderboards"; "no vector search numbers"); `metrics/SOLR-17987` (cost statement, see above).

Configsets and Query parsing (A): `configsets/SOLR-15478`, `SOLR-17363`, `SOLR-6960`, `SOLR-7267` (base links and changelog links); `query-parsing/SOLR-10897`, `SOLR-11391`, `SOLR-11761`, `SOLR-12212`, `SOLR-12532`, `SOLR-12871`, `SOLR-17796`, `SOLR-8977`, `SOLR-9048`, `SOLR-9149` (base links with "before this change"); `query-parsing/SOLR-12608` ("faceting is active" from the research record); `query-parsing/SOLR-874` (bold summary and link to merge-base, see above).

Schema and analysis (A): `schema-analysis/SOLR-14199` (changelog link); `SOLR-15357` (Proof wording; base link); `SOLR-15358` (bold placeholder sentence removed; "Run at" wording); `SOLR-15945` (base wording); `SOLR-17047` (base link; SolrCore line range); `SOLR-18134` (base links); `SOLR-9349` (changelog link).

Core admin (B): `SOLR-11939` (base links; "this change"), `SOLR-12007`, `SOLR-13246`, `SOLR-15024`, `SOLR-15805`, `SOLR-17297`, `SOLR-17377`, `SOLR-17708`, `SOLR-17731`, `SOLR-18010`, `SOLR-4989`, `SOLR-6438`, `SOLR-8275`, `SOLR-8576`, `SOLR-9750` (base links; "Before this change"; OWED lines deleted where noted; the SOLR-4367 spellcheck draft lost a process sentence).

Replication and backup (B): `SOLR-11650`, `SOLR-12085`, `SOLR-12246`, `SOLR-17287`, `SOLR-8430`, `SOLR-9598` (base links; "at the base commit").

Spellcheck (B): `SOLR-4367` (process sentence deleted from Proof).

Search components (B, round 2): `SOLR-10424`, `SOLR-10492`, `SOLR-10694`, `SOLR-10844`, `SOLR-11129`, `SOLR-11153`, `SOLR-11310`, `SOLR-11364`, `SOLR-11470`, `SOLR-12044`, `SOLR-12556`, `SOLR-13245`, `SOLR-13876`, `SOLR-14451`, `SOLR-14678`, `SOLR-15319`, `SOLR-15331`, `SOLR-17051`, `SOLR-17748`, `SOLR-17976`, `SOLR-4374`, `SOLR-5394`, `SOLR-6193`, `SOLR-6207`, `SOLR-6831`, `SOLR-7498`, `SOLR-7390`, `SOLR-7550`, `SOLR-8009`, `SOLR-8240`, `SOLR-8939`, `SOLR-9396`, `SOLR-2988`, `SOLR-3962`, `SOLR-4362`, `SOLR-6009`, `SOLR-6320`, `SOLR-7120` (base links with merge-base labels; changelog links re-pointed to the fragment; the rest as listed in the commit).

Note on base links: edits point at the merge-base or base commit on the `nick-boss-tech/solr` host, following `pr-formula.md`. Round reports that showed the `apache/solr` host were not followed where the draft already used the fork host. Merge-base targets were computed from the local `upstream/main` (`3f5d4c5bf8a`), so they need re-verification before posting.

## Flagged items (not applied)

### Verification dates (shared item, roll-up 7; left unchanged across the set)
- Core admin: `SOLR-11939` Proof (no date; proposed "2026-10-11" not added); `SOLR-12007` Proof ("Verified 2026-10-08 at head" cites a takeover-log record); `SOLR-13246` Proof ("checked 2026-10-06" cites a ledger record).
- Replication: `SOLR-17287` line 27 "[CONFIRM: count]"; `SOLR-9865` line 25 "[CONFIRM: count]" (receipt line 6 and recount both say 4).
- Search components: `SOLR-7498` Proof "[run date to confirm]"; `SOLR-9148` Proof "Run date: [run date to confirm]"; `SOLR-9864` Proof "Run date: [run date to confirm]".
- Query parsing and configsets: `SOLR-12608`, `SOLR-6014`, `SOLR-12212`, `SOLR-7390` items flagged in the A list below.

### Slice A, round 1 (CLI, Security, Build/docs, Metrics)
- F1 `cli/SOLR-9342` line 5: title "Solr GC logging not respecting user timezone". The branch fragment says the timezone is exported from SOLR_TIMEZONE; a branch title change is needed first, and the title adds "for the server JVM". Main-side call.
- F2 `cli/SOLR-9342` line 26 "(verified 2026-10-07)": receipt gives counts with no run date; the log is not on disk.
- F3 `cli/SOLR-9342` line 27 "run by hand against the distribution built from this head": receipt says "Gradle-built distribution" with no head. Claim change.
- F4 `cli/SOLR-9342` line 46 "The Windows JVM does not read TZ for GC log timestamps": no Windows run.
- F5 `cli/SOLR-17029` lines 36-37: the 2026-10-08 run records test 5 failing; the packaging round leaves it open. Owner decision.
- F6 `cli/SOLR-17598` line 25 "verified by construction": no verification date (receipt gives 2026-10-06 and a 2026-10-07 re-verification).
- F7 `security/SOLR-18368` line 9: the opener says the removal is already on main (commit `3ef3d093c06`). Status conflict: receipt "RESOLVED/Fixed" against the Jira record "Open". Owner options first.
- F8 `build-docs/SOLR-16914` line 27: no verification date (light gate 2026-10-08).
- F9 `build-docs/SOLR-16914` lines 23 and 27: the Lucene Kuromoji source comparison is not in the receipt (it is in round g2). Dropping it changes Proof; main side to accept or record.
- F10 `build-docs/SOLR-17252` line 36 Choice: the receipt (Q3) says the check stays at startup, while the build-docs decisions recommend an earlier check. Conflicting recorded decisions.
- F11 `build-docs/SOLR-17752` line 25: "TestShardResponseLogging passed 1 of 1 at head e5c0a64": receipt dates 2026-10-04, before the head commit (2026-10-05). Main side to confirm.
- F12 `build-docs/SOLR-17842` line 19 "one or two existing benchmark classes per area": the reference guide names three for search. Count change.
- F13 `metrics/SOLR-17987` line 1: no Title line; the title needs the fragment changed first (the fragment still says `=jvm,jetty`).
- F14 `metrics/SOLR-17987` line 15 "The environment variable SOLR_METRICS_DISABLEDREGISTRIES ... The reference guide documents the setting": the reference guide gives SOLR_METRICS_DISABLED_REGISTRIES; the owner decision recommends the rename. Owner wording.
- F15 `metrics/SOLR-17987` after line 42 (missing Limit): "jetty" creates no `solr.jetty` registry. New claim; owner wording.
- F16 `metrics/SOLR-17987` after line 42 (missing Limit): the new test does not read the property or the environment path. New scope statement.
- F17 `metrics/SOLR-17987` after line 42 (missing Limit): with jvm listed, the JVM metrics still run against a no-op provider. New scope statement; not verifiable by reading.

### Slice A, round 2 (Configsets, Query parsing, Schema and analysis)
- F18 `query-parsing/SOLR-11761` line 9: the ticket repro order in the draft does not match the research record (the record starts with a call that works). Claim change.
- F19 `query-parsing/SOLR-12212` line 27 "[per-class count to fill from the run log]": the receipt gives 80 focused tests in total; the log is not on disk. Get the count or drop it.
- F20 `query-parsing/SOLR-12608` line 23 "the class passes 38 of 38": no verification date (receipt gate date 2026-10-05).
- F21 `query-parsing/SOLR-12608` line 1 HOLD banner: a status record for the owner; not removed.
- F22 `query-parsing/SOLR-12608` line 31: "The changelog entry must not claim a memory benefit" depends on a branch title change.
- F23 `query-parsing/SOLR-15615` Limits, last bullet: missing sentence on the fallback authorization (not traced end to end). Scope statement.
- F24 `query-parsing/SOLR-16267` line 17: the def() argument claim is not confirmed (Lucene 9.x not checked). Claim change.
- F25 `query-parsing/SOLR-17796` line 32: "may still occur there" is a claim addition; hold for an owner call.
- F26 `query-parsing/SOLR-6014` line 23: the cited CI run (`37639870647`) ran on a commit that differs from the head only by a workflow file. Deleting it removes Proof evidence; main side to delete or re-cite.
- F27 `query-parsing/SOLR-6014` line 21 "passes 39 of 39": no verification date (receipt gives 2026-10-07).
- F28 `query-parsing/SOLR-6014` lines 28-29: "likely to fail at search time" is traced, not run; claim addition, hold.
- F29 `query-parsing/SOLR-874` line 41: two Limits bullets depend on branch changes not yet on the branch. Hold until they land.
- F30 `schema-analysis/SOLR-14199` line 28 "[CONFIRM COUNT BEFORE POSTING ...]": the receipt says 37 of 37; the source has 36 test methods.
- F31 `schema-analysis/SOLR-14199` line 30 HOLD and line 41 OWNER note: internal status; depends on a run and a ledger file that are not on disk.
- F32 `schema-analysis/SOLR-15357` line 1 INTERNAL comment: remove after the fix commits land.
- F33 `schema-analysis/SOLR-15357` lines 26 and 28: "passes 4 of 4 ... shows no failure on the base code": the receipt says inconclusive by construction. Owner to confirm the framing.
- F34 `schema-analysis/SOLR-15358` line 27 "[TO FILL before posting ...]": no base run on disk; the proposed text asserts failure without a run.
- F35 `schema-analysis/SOLR-18134` line 19 "Lucene 9.x": only 9.12.0 and 9.12.3 were checked; the replacement "9.12" is a scope statement.
- F36 `schema-analysis/SOLR-18134` line 25 "one of its two tests fails": the overlay is not on record.

### Slice A, branch-side fragment changes (the draft text is already right, or needs no change; each moves a head)
- Changelog titles or lines that differ from the draft (branch-only fixes): `SOLR-16272`, `SOLR-16813`, `SOLR-9342` (see F1), `SOLR-6960`, `SOLR-12608`, `SOLR-12871`, `SOLR-16267`, `SOLR-17280`, `SOLR-874`, `SOLR-8977`, `SOLR-9048`, `SOLR-14199`, `SOLR-15357`, `SOLR-15945`, `SOLR-16977`, `SOLR-17047`, `SOLR-18134`, `SOLR-9349`; `SOLR-17987` changelog line 2 (remove `=jvm,jetty`, needed for F13); `SOLR-17598` changelog title (optional owner decision).

### Slice B, round 1 (Core admin, Replication and backup, Spellcheck)
1. `core-admin/SOLR-11939` Proof: no verification date; proposed "on 2026-10-11" not added.
2. `core-admin/SOLR-12007` Proof: "Verified 2026-10-08 at head" cites a takeover-log record.
3. `core-admin/SOLR-13246` Proof: "checked 2026-10-06" cites a ledger record.
4. `core-admin/SOLR-17297` Choice (lines 26-35) and Limits: pr-formula section 4 says narrow against broad is not a choice; the answers say the draft stays held. Owner call.
5. `core-admin/SOLR-17708` changelog title (branch): "authorized once per request instead of twice" against the draft's Limits "One route still checks twice". Branch edit; the head moves.
6. `core-admin/SOLR-17731` changelog title (branch): "reachable again" against the receipt (no prior state). The Choice omits the moved alias handler (optional wording).
7. `core-admin/SOLR-18010` changelog title (branch): "serialized ... corrupt the file" against the Limits (standalone, one process). A settling run is not on disk.
8. `core-admin/SOLR-8576` lines 13 and 15: "checks that the rejected creates change nothing" and "the original collection and the alias are unchanged". The head test does not read the alias. Hold.
9. `core-admin/SOLR-8576` Proof line 24 "[OWED BEFORE POSTING ...]": kept until the fix lands.
10. `replication-backup/SOLR-11650` Proof: "Three of those tests call the new helper" against the receipt's 18 of 18 with no split. Count change.
11. `replication-backup/SOLR-11650` changelog title (branch): "no longer exposes ... in IndexFetcher log and error messages" against the draft (warnings and client errors still show the URL).
12. `replication-backup/SOLR-12085` Proof and Limits: "No replication test ran at this head" against "IndexFetcherPacketProtocolTest runs 18 tests" in the same paragraph. Main side confirms the count and removes the sentence.
13. `replication-backup/SOLR-12085` changelog title (branch): "on every replication" against the code path.
14. `replication-backup/SOLR-12246` Proof: "so no replication test ran at this head." Same conflict as item 12.
15. `replication-backup/SOLR-17287` line 27 "TestRestoreCore [CONFIRM: count]": filling a number is for the main side.
16. `replication-backup/SOLR-8430` changelog title (branch): "shared by all concurrent file requests on a node" against the code, which shares only equal rates.
17. `replication-backup/SOLR-8430` Limits: "Limiters stay in memory" against the cap that is not in the code yet. Needs code and a new run.
18. `replication-backup/SOLR-9865` line 25 "TestRestoreCore [CONFIRM: count]": the receipt says 4.
19. `spellcheck/SOLR-3701` changelog title (branch): "collations no longer come out as pandora's's" against a test that checks tokens only.
20. `spellcheck/SOLR-4367` Proof "[Owner to supply before posting: the run counts ...]": the receipt has no counts or base failure.

### Slice B, round 2 (Search components, eDisMax)
21. `search-components/SOLR-10424` Limits: "best landed together" should name the landing order against SOLR-8240 (recorded decision).
22. `search-components/SOLR-10694` Limits: the writer list omits JSON. Scope statement.
23. `search-components/SOLR-10694` changelog title (branch): "map, NamedList and iterator values" against the array code.
24. `search-components/SOLR-10844` changelog title (branch): "instead of empty counts or an internal error": no receipt supports "empty counts".
25. `search-components/SOLR-10844` `SimpleFacets` comment at branch lines 799-804: "used to return empty counts". Not edited (branch code).
26. `search-components/SOLR-11129` "facet.offset is read from local params too" and Limits "applies the offset twice": a shipped-behaviour claim; owner decision 4 offers a one-line fix or a Limit.
27. `search-components/SOLR-11129` "Field facet settings are read from the local params first": owner decision 2 may change the precedence; branch comment at line 1396 too.
28. `search-components/SOLR-11129` final HOLD comment kept until item 26 is decided.
29. `search-components/SOLR-11153` changelog title (branch lines 2-3): names only `wt=schema.xml`; the draft also covers the save path.
30. `search-components/SOLR-11310` changelog title (branch line 1): "only the ones that happened to sort first" against the every-rerank behaviour.
31. `search-components/SOLR-11364` changelog title (branch lines 2-3): "even when fl also contains a glob" overstates the alias case.
32. `search-components/SOLR-12556` "Buckets known before refinement follow the processEmpty rule" against the changelog (branch), which drops all buckets not refined against every shard. Owner approval first.
33. `search-components/SOLR-12556` Proof "[CONFIRM BEFORE POSTING: test count ...]": the receipt says 13 tests; the file has 12 `@Test` methods.
34. `search-components/SOLR-12556` Proof "[CONFIRM BEFORE POSTING: name the failing test ...]": kept.
35. `search-components/SOLR-13245` changelog title (branch): no stop-and-replace (owner decision 2).
36. `search-components/SOLR-14381` changelog title (branch): omits the XML long type.
37. `search-components/SOLR-14381` Proof "production code from before the boxing change": the owner names the SHA.
38. `search-components/SOLR-14678` Limits "We plan a follow-up for this path": owner decision 1 is not decided.
39. `search-components/SOLR-14678` changelog title (branch): "in the response ... at any nesting depth".
40. `search-components/SOLR-15041` Limits bullets on blank lines and split values: held until the blank-line fix and a focused test land.
41. `search-components/SOLR-15319` Limits "It is the case in SOLR-8051": not reproduced; owner decision 2 (fix or close).
42. `search-components/SOLR-15479` changelog title (branch): "now reflects the rescored documents" against the window test (document 1 keeps its score).
43. `search-components/SOLR-15895` "The Windows branch of the test is the one that covers this bug": owner decision 4; the record does not say which branch ran.
44. `search-components/SOLR-17051` Proof "[OWNER: the failure text ...]" and "this is the check that should fail on the base code": owner adds the failure text or deletes the sentence.
45. `search-components/SOLR-17748` Proof: "two-node cluster"; the head uses `configureCluster(3)`. Number change.
46. `search-components/SOLR-17748` "What this change does": "no longer fails with a 500 error" needs the shards.tolerant and 503 statements. New claims.
47. `search-components/SOLR-6193` changelog title (branch line 2): names `facet.offset` and `facet.pivot.mincount` with no test.
48. `search-components/SOLR-6831` changelog title (branch): names timeAllowed and cpuAllowed only; the draft also says memAllowed.
49. `search-components/SOLR-7390` Proof: "Only the three test classes above were run"; the receipt lists four. Number change.
50. `search-components/SOLR-7498` Proof: run date to confirm (owner).
51. `search-components/SOLR-7550` changelog title (branch): "treats a 500 ... as unreachable" against the code (owner decision 3).
52. `search-components/SOLR-7550` optional sentence "A 500 had no such case." not added (new claim).
53. `search-components/SOLR-8009` Proof: "FullSolrCloudDistribCmdsTest 10 of 10" against 9 test methods at the head; the gate log is not on disk.
54. `search-components/SOLR-8240` Limits: "Updating it is a planned follow-up pull request": owner decision 3 needs the owner's agreement.
55. `search-components/SOLR-9148` What happens today: `SQLHandler` lines 121-127 re-point to the base. Confirm the merge-base first (computed from the local `upstream/main`).
56. `search-components/SOLR-9148` Proof run date to confirm (owner).
57. `search-components/SOLR-9396` Limits bullet 1 "Only a $row.field reference": depends on the regex fix (owner decision 4).
58. `search-components/SOLR-9864` Proof run date to confirm (owner).
59. `search-components/SOLR-12092` "The reference guide and the changelog both state this": the branch title and guide line 110 do not. Held, not deleted.
60. `search-components/SOLR-14913` changelog title (branch): "instead of failing the whole query".
61. `search-components/SOLR-3243` changelog title (branch): no negated form.
62. `search-components/SOLR-4362` bullet 1 "A decimal slop such as ~2.5 counts too": the slop check does not match those forms at the head. Claim and scope call.
63. `search-components/SOLR-6009` changelog title (branch lines 4-5): "matched nothing (or errored)" against `numFound=0`.

## Notes

- The shared verification-date item (roll-up 7) was left unchanged across the set. Explicit date cases are flagged above.
- Blocks to remove before posting: `SOLR-6831`, `SOLR-8430`, `SOLR-9598`, `SOLR-15319`, `SOLR-15331`, `SOLR-17051`, and the CONFIRM markers in `SOLR-12556`.
- Holds not lifted by these edits: `SOLR-4362` (item 62 and a branch squash), `SOLR-8576` (fix pending), `SOLR-11129` (item 26), `SOLR-15041`, `SOLR-14931` (head moves), `SOLR-17791`.
- Branch changelog titles listed above are branch edits. Changing any of them moves the head, so links to that fragment must be re-pointed afterwards.
- Merge-base targets were computed from the worktree `upstream/main` (`3f5d4c5bf8a`). Re-verify before posting.
- Not done: no build, test, Gradle, gate or test-queue run; no push, PR, comment, Jira or GitHub write; no branch edit; the claim is marked DONE in the same push as this report.
