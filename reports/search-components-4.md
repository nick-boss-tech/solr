# Search components round 1, sub-batch 4 (search core, grouping, stats, REST, managed resources): round roll-up

Claim: `claims/search-components-4.md` (commit `e4a5dafa8f4`). Assignment: `assignments/search-components-4.md` (commit `a2fdce70c2c`, capped by `51082bc9a37`). Per-part reports: `reports/search-components-4-s1.md` through `-s6.md`.

Done 2026-10-10 by Claude Code (AI agent), working for Nick Shanin. Six read-only subagents did the audit. No build, Gradle run, or test was run. No submit branch, live PR, JIRA item, or comment was touched. Nothing was posted.

## Heads

Three assignment heads moved and are audited at their live heads, with the move flagged: 8051 (`44588ce6719` to `55b24f64a8b`), 8088 (`2398c9bea08` to `567efa9b78c`), 9595 (`7ff1350ab7b` to `78f5524476a`), and 10694 (`093d90c62de` to `64e86811548`). SOLR-12044 moved since the assignment, and its new receipt names the live head `c92bbf9349b1`. Every other named head matches.

## Per-ticket verdicts

| Ticket | Verdict | Draft | Live head | What blocks it or what the draft must say |
|---|---|---|---|---|
| SOLR-6207 | Draftable | `SOLR-6207.md` | `b099a9f1be5` | A documentation test that passes on base too. The Proof says so |
| SOLR-7520 | Draftable as is | `SOLR-7520.md` | `10b6931e1c0` | Settle the "handoff" commit subject first |
| SOLR-8051 | Audit only; not draftable | none | `55b24f64a8b` (moved) | The new null check sits after `res.getException()` at `ExactStatsCache.java` 117. `SolrResponse.getException()` dereferences the null body, so `ExactStatsCacheMergeTest` throws an NPE on its own branch (read, not run). Move the null check above the exception check. The code is unchanged since `44588ce6719`; only a handoff doc was removed |
| SOLR-8088 | No gate; audit only | none | `567efa9b78c` (moved) | `TESTING.md` removed at the live head. Premise run still owed |
| SOLR-9595 | No gate; audit only | none | `78f5524476a` (moved) | The ticket's `MultiDocValues` is still uncached |
| SOLR-10694 | No gate; audit only | none | `64e86811548` (moved) | The change is CSV only |
| SOLR-11310 | Draftable, held | `SOLR-11310.md` | `e38ddec5279` | Owner confirms the field-sort boost (the Choice). Narrow the changelog title to match the code |
| SOLR-12044 | Draftable | `SOLR-12044.md` | `c92bbf9349b` | The changelog type is `optimized`, which is not a valid type. Change it to `changed` before any PR |
| SOLR-13851 | Audit only; held | none | `b60f4d642d1` | Conflicts with 12044 in `TestIndexSearcher.java`. Submission held regardless |
| SOLR-14381 | Draftable after a comment fix and owner calls | `SOLR-14381.md` | `a28b3f672cb` | The compat test (`TopGroupsResultTransformerCompatTest.java` 44-46, 85, 111) says the old coordinator casts per-group `totalHits` to Integer. At base `b5c71bc5573` it reads them as `Number` (`TopGroupsResultTransformer.java` 144). Only matches and the top-level `totalHitCount` are cast. Conflicts textually with 17155; land 17155 first |
| SOLR-14931 | Draftable after two stray-character fixes | `SOLR-14931.md` | `1a7d678d9a1` | The two fixes change the head and need a new gate |
| SOLR-15144 | Held as an owner decision | none | `68b0fc31e05` | Options (a), (b), (c). Under (a), the changelog title (`changelog/unreleased/SOLR-15144.yml` line 1) and the comment at `TimeAllowedLimit.java` 99-100 claim end-to-end behavior that the round 35 receipt says does not hold. Use unit-level wording. The fetch request now runs with no time limit. Option (b) widens to `SearchHandler`, which crosses into sub-batch 2 |
| SOLR-15319 | Draftable, held | `SOLR-15319.md` (reviewer notes below a separator; delete before pasting) | `4bda91f46fc` | Landing order with 8051 (both rewrite the same lines of `ExactStatsCache.java`). The changelog title says "and its subclasses", but the subclass tests are not gated. The proof covers recorded runs only. The same null-body hazard is at line 120, and needs the same guard |
| SOLR-15479 | Draftable, not ready | `SOLR-15479.md` | `57bd53ce4d0` | History cleanup: `4a6ffa87358` carries a Claude co-author trailer; `b21e1b940fa` and `81602df492f` have handoff subjects. Three text fixes. The owner's maxScore scope call. Any rewrite moves the head, so the Proof must name the new SHA |
| SOLR-15895 | Draftable with owner checks | `SOLR-15895.md` | `02930909397` | The Proof is inconclusive by construction, and no pass is claimed. The head commit `02930909397` carries a Claude `Co-Authored-By` trailer; remove it |
| SOLR-16444 | Draftable | `SOLR-16444.md` | `8ffee94a5e7` | The Limits say the reporter's path is not confirmed |
| SOLR-17155 | Draftable with an owner Choice | `SOLR-17155.md` | `1413237f7a7` | Choice on the non-stored unique-key scope. Land before 14381 |
| SOLR-17372 | Failed gate; audit only | none | `048862fda8a` | The record shows no reproduction on base, so there is no fail-before proof. Commit subjects carry process words |
| SOLR-17791 | Draftable but held | `SOLR-17791.md` | `a39c1c97376` | A code fix: `ManagedFeatureStore.java` line 179 treats an empty child id (from a trailing slash) as a real store, creating a store named `""`. Change the check to `childId == null || childId.isEmpty()`, add a test, and re-prove at the new head. A merge fix (conflicts with main in the upgrade-notes adoc). Commit cleanup. Owner's API-shape call |
| SOLR-17841 | Retire candidate; audit only | none | `478731e6760` | The fix already landed on main via PR #4724 (`3beb0dc5f28`). The branch has no production change |
| SOLR-18196 | Merged; audit only | none | `f3248fe2310` | Already merged via PR #4995. Retire candidate |
| SOLR-18506 | Consistency pass on live PR 5029 | none | `77c019e1ff0` | The head matches. The live PR shows a failing Crave test run (`37384974639`), and the Jira packet still has the superseded v1 text. Hold public replies; read the failing run before replying there |

## Landing order and interactions

- **8051 and 15319:** the same lines in `ExactStatsCache.java`. Both need the null-check move. Landing order is an owner call.
- **7520, 14381, 17155:** land 17155 first, then 14381 (textual conflict in `TopGroupsResultTransformer.java`). 7520 is independent.
- **11310 and 15479:** no shared file; trial merge clean. maxScore does not depend on landing order.
- **15895 and 16444:** merge cleanly onto main and with each other.
- **17791:** conflicts with main only in the upgrade-notes adoc.
- **12044 and 13851:** conflict in `TestIndexSearcher.java`.
- **15144 option (b):** crosses into sub-batch 2's handler components.

## Owner decisions

1. SOLR-8051 and 15319: the landing order, and the null-check fix in both (move the check above `getException()`).
2. SOLR-15144: options (a), (b), or (c).
3. SOLR-17791: the API-shape call, and the empty-child-id fix.
4. SOLR-17155: the non-stored unique-key scope Choice.
5. SOLR-11310: the field-sort boost Choice.
6. SOLR-15479: the maxScore scope.
7. SOLR-14381: owner calls on the compat-test wording.
8. SOLR-17841 and SOLR-18196: retire calls.
9. SOLR-17372: the failed gate's three owner options (from the receipt).
10. History rewrites for 15479, 15895, 15319, 17791, 7520 and 17372: authorize them. Rewrites move the heads.

## Pre-post cleanup in drafts

- `SOLR-15319.md`: delete the reviewer notes below the separator before pasting.
- `SOLR-15895.md`, `SOLR-17791.md`, `SOLR-15479.md`: history cleanup before any PR.
- `SOLR-12044.md`: change the changelog type to `changed`.

## Corrections to the record

- The 8051 null check is in the wrong place, so the fix does not work.
- The 14381 compat test misstates the cast.
- The 15144 changelog title and comment overstate end-to-end behavior.
- The 17841 branch's fix is already on main via PR #4724.
- The 18196 branch is already on main via PR #4995.
- The 17791 empty-child-id check misses the trailing-slash case.

## Not done

No build, test, Gradle run, `gh` write call, fetch, commit, or post. Gate logs named in the receipts are not on disk, so counts are receipt-only. The failing GitHub run for PR 5029 was not reopened. Live JIRA was not queried.
