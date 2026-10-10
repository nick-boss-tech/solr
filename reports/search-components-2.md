# Search components round 1, sub-batch 2 (handler components): round roll-up

Claim: `claims/search-components-2.md` (commit `e4a5dafa8f4`). Assignment: `assignments/search-components-2.md` (commit `12fc625a7d2`). Per-part reports: `reports/search-components-2-h1.md` through `-h6.md`.

Done 2026-10-10 by Claude Code (AI agent), working for Nick Shanin. Six read-only subagents did the audit. No build, Gradle run, or test was run. No submit branch, live PR, JIRA item, or comment was touched. Nothing was posted.

## Heads

Three assignment heads moved and are audited at their live heads, with the move flagged: 9124 (`101e12d2085` to `dfc2518214f`), 10305 (`c21ca8c0e75` to `0cf26e5f331`), and 17055 (`f0c401a290b` to `925a130e2621`). For 17539 the live tip `f9d201a278b` is one docs-only commit past the gated `bce505f45ac`; the difference is flagged, not resolved. The local branches in this checkout are stale.

## Per-ticket verdicts

| Ticket | Verdict | Draft | Live head | What blocks it or what the draft must say |
|---|---|---|---|---|
| SOLR-8009 | Draftable after two checks | `SOLR-8009.md` | `8795661ddc9` | The receipt says 10 tests in `FullSolrCloudDistribCmdsTest`; the file has 9. One id on two shards trips an existing assert in the single-id form |
| SOLR-8767 | Held for the owner's `/get` response-shape decision | none | `3b5f2d23573` | The `/get` response shape changes; the user-facing description is the owner's decision |
| SOLR-8954 | Held; do not land as written | none | `1d981abe700` | Sends unrouted gets to all shards for every router, including `compositeId` with a router field. The existing `FullSolrCloudDistribCmdsTest` lines 300-301 assert such a get returns 0 documents, so by reading this breaks that test, which its gate did not run. It also conflicts with 8009 on merge |
| SOLR-15018 | Draftable | `SOLR-15018.md` | `d0f29b4630c` | The only change since `5d3d36a9ab4` is a test |
| SOLR-13568 | Consistency pass on live PR 5014; no draft | none | `ac5d60c214c` | Branch, receipt, and live PR agree on the head and the 9 of 9 count. The live body has the process word "local gate" and two unmeasured claims ("evict useful ones", "rarely hit"). Squash commits `ce19cf78ae9` and `dfde3c56df8` |
| SOLR-13876 | Draftable after two branch fixes | `SOLR-13876.md` | `2e110473dba` | `TestExpandComponent.java` 944-945 says base XML drops `maxScore`; base writes `maxScore="NaN"` (XMLWriter 160-161). Squash the process-worded commits; `ce19cf78ae9` repeats the wrong claim |
| SOLR-8939 | Draftable, held on the local branch | `SOLR-8939.md` | `a855a2d8965` | The local `solr-8939-submit` differs from the live head and must not be pushed. The Proof cannot count `QueryComponentIdFormatTest` as fail-before, because it does not compile on base |
| SOLR-17748 | Draftable, held for two owner decisions and a commit subject | `SOLR-17748.md` | `dfacaf34766` | Keep or drop the entry-creation branch; which NPE route the reporter hit. The "handoff" subjects on `07d3de7c170` and the head itself need a reword or squash, which moves the head. The receipt's "no known trigger" is wrong for the cause-less `SolrServerException` route |
| SOLR-17976 | Draftable, held for a commit subject and changelog wording | `SOLR-17976.md` | `56ea43c448e` | Commit `7b90b627a9e` has a "handoff" subject. The changelog type should be `changed`, with a shorter title |
| SOLR-6975 | Draftable | `SOLR-6975.md` | `761aa629bb8` | `DistributedQueryComponentOptimizationTest` 10 of 10. Fails on base with only the new test |
| SOLR-7550 | Draftable only after an owner decision on the broad 500 rule | `SOLR-7550.md` | `687165651f9` | The changelog says a 500 is "treated as unreachable", but the code counts it as success. Proof is inconclusive by construction (the new tests do not compile on base) |
| SOLR-8020 | Draftable | `SOLR-8020.md` | `79f790523d2` | `ComponentStageLimitsTest` 5 of 5, discriminating. No Choice owed |
| SOLR-18109 | Draftable, test only | `SOLR-18109.md` | `b19395e1f60` | The production fix is already on main via PR 4779. The title must not say "fix" |
| SOLR-11470 | Draftable; not ready until the history is cleaned | `SOLR-11470.md` | `f44c294da37` | The Proof rests on the `{!bool}` test, 13 of 13. Commits `462345f78e1`, `5912508c530`, and `11857d1325d` carry a Claude `Co-Authored-By` trailer; handoff-doc commits are also in the history |
| SOLR-14451 | Draftable; not ready until the history is cleaned | `SOLR-14451.md` | `e60891d8716` | `DebugComponentTest` 7 of 7 at head. The cloud test is 7 of 7 with no base run recorded. The option question is held for the owner's wording. Handoff-doc commits |
| SOLR-3044 | Parked; audit only | none | `04b877e9de1` | Production work is on main. The test does not compile, and the branch conflicts with main. Retarget is the owner's call |
| SOLR-6759 | No gate; audit only | none | `62974ef8d18` | Premise holds on main. The test does not show the block-collapse loss. The head still carries `SOLR-6759-TESTING.md` |
| SOLR-9124 | No gate; audit only | none | `dfc2518214f` (moved) | Premise holds. The handoff doc was removed; the code is identical to `101e12d2085` |
| SOLR-10305 | No gate; audit only | none | `0cf26e5f331` (moved) | Partial: covers only the no-`uniqueKey` case, not the `store=false` title. The changelog puts the NPE "while merging shard responses", but it occurs in `createMainQuery` (`QueryComponent.java` 793) |
| SOLR-17055 | No gate; audit only | none | `925a130e2621` (moved) | The changelog says topK hits overall with no sort exception; `QueryComponent.java` 1018-1021 shows score-ordered results are capped at topK and an explicit sort returns more than `numFound` |

## Landing order and interactions

- **RealTimeGet family (8009, 8767, 8954, 15018):** 8009 first, then 15018, then 8767 last. 8954 does not land as written. The receipts name the duplicate-result behavior that 8009 inherits.
- **ExpandComponent pair (13568, 13876):** both change `ExpandComponent.java` and share `TestExpandComponent`. Trial merges are clean in either order, to the same tree. 13568 first, since it is already live.
- **QueryComponent family (8939, 17748, 17976):** no hunk overlap; trial merges are clean pairwise and against upstream main. Suggested order: 8939, 17748, 17976. 17976's receipt names a tie-order change and a new `ShardDoc` field.
- **6975 and 17976:** clean trial merge.
- **14451 and 18109:** clean trial merge, but `DebugComponentTest` counts change once both land.
- **11470:** neighbors 15479 and 11310 on the rerank path. Noted, not audited here.

## Owner decisions

1. SOLR-8954: close it, or make it a test-only follow-up. Recommended: 8009 carries the all-shards fix.
2. SOLR-8767: the `/get` response-shape change and its user-facing description.
3. SOLR-17748: keep or drop the entry-creation branch; which NPE route the reporter hit.
4. SOLR-7550: the broad 500 rule (the changelog must say what the code does).
5. SOLR-18109: confirm that the title says "test", not "fix".
6. SOLR-11470 and SOLR-14451: authorize the history rewrite. Rewrites the fork branches.
7. SOLR-3044: retarget, or leave parked.
8. SOLR-17539: the live PR's "pre-fix proof" label, which needs your go-ahead to edit.
9. SOLR-8009: the receipt's count (10) against the file (9); confirm the count before posting.

## Pre-post cleanup in drafts

- `SOLR-17748.md`, `SOLR-17976.md`, `SOLR-11470.md`, `SOLR-14451.md`: the handoff commit subjects must be reworded or squashed before posting. Any rewrite moves the head and needs a fresh gate.

## Corrections to the record

- The 8009 receipt's test count (10) does not match the file (9).
- The 10305 changelog puts the NPE in the wrong method.
- The 17055 changelog overstates the topK behavior.
- The 17539 receipt's live-tip run ID does not appear in the current checks.

## Not done

No build, test, Gradle run, `gh` write call, fetch, commit, or post. Live JIRA was not queried. Gate logs named in the receipts are not on disk, so counts are receipt-only. The 13568 and 17539 live bodies were read, not edited.
