# Search components round 1, sub-batch 1 (facets): round roll-up

Claim: `claims/search-components-1.md` (commit `e4a5dafa8f4`). Assignment: `assignments/search-components-1.md` (commit `e903baf1359`). Per-part reports: `reports/search-components-1-f1.md` through `-f6.md`.

Done 2026-10-10 by Claude Code (AI agent), working for Nick Shanin. Six read-only subagents did the audit. No build, Gradle run, or test was run. No submit branch, live PR, JIRA item, or comment was touched. Nothing was posted.

## Heads

Every named head matches its live branch. The local `solr-<ticket>-submit` branches in this checkout are stale or carry unpushed notes commits; every verdict below uses the live origin heads. Do not push from the local branches.

## Per-ticket verdicts

| Ticket | Verdict | Draft | Live head | What blocks it or what the draft must say |
|---|---|---|---|---|
| SOLR-5394 | Draftable as drafted | `SOLR-5394.md` | `967445622f9` | Choice: default thread count 1, or honor `facet.threads` on the fcs path. The fcs path still ignores `facet.threads`, and the draft says so |
| SOLR-10844 | Draftable after two text fixes | `SOLR-10844.md` | `e87515c56d04` | The comment at `SimpleFacets.java` 799-804 says the collector needs SORTED docValues. Lucene 10.4.0 uses SORTED_SET for a multi-valued facet field. The "empty counts" claim is in no receipt, and the changelog title repeats it |
| SOLR-6193 | Held for the owner's scope call | `SOLR-6193.md` (pivot-only; post only if the ticket is narrowed) | `ec94bf50c80` | The branch changes only distributed `facet.pivot` merging. The ticket's `facet.field` example still resolves to the request-level offset. The changelog title overclaims local `facet.offset`. The shard request is sized from request-level `facet.limit`, so a local limit above 100 or -1 can truncate |
| SOLR-10492 | Draftable after history cleanup and two wording items | `SOLR-10492.md` | `ecf21e2192c` | Handoff-doc add and remove pairs in the history. The round 28 blocker ("no assertion") is wrong: `query()` compares against the single-node control. Two wording items need the owner |
| SOLR-11129 | Held | `SOLR-11129.md` (HOLD comment) | `6c1356bdff7` | `FacetComponent.java` 1403 reads a local `facet.offset`, but the shards also apply it, so the offset is applied twice. Replace with the request-level read and add a focused test. The receipt's scope note is confirmed |
| SOLR-6831 | Draftable after two fixes | `SOLR-6831.md` | `96b33ba5f6f8` | The changelog must name `memAllowed`, since the check covers it. The test's control assertion assumes all five values land on one shard of a three-shard collection, which the record does not show |
| SOLR-12556 | Held, not postable | `SOLR-12556.md` | `033ec65a0e1b` | The changelog title's first sentence is true only for buckets known before refinement; its second sentence describes no change from base. The receipt says 13 tests, the file has 12 `@Test` methods, and the fail-before test name is not in any checkable record |
| SOLR-17051 | Draftable; owner confirms the contract | `SOLR-17051.md` | `148544e9ed5` | Owner confirms "screen only above mincount 1". The base-failure sentence is bracketed because the record does not supply it |
| SOLR-15331 | Draftable as INCONCLUSIVE; not postable until the trailer is fixed | `SOLR-15331.md` | `6769b4cd8a1` | Proof is inconclusive by construction; no pass claim. Commit `a9f5ecf9d0c` carries a Claude `Co-Authored-By` trailer. A "testing handoff" commit (`85e81363e7b`) is in the history |
| SOLR-16290 | Held, audit only | none | `ff760120c81` | Disposition is the owner's: pin PR, fund the real fix, or bank |
| SOLR-18482 | Consistency pass on live PR 5009: the Proof names an older head | none | `49ca9099d8e` | The Proof names head `8ef833b64cb` (2026-10-04). The live head is `49ca9099d8e`. The 2026-10-06 confirmation at the tip gives the same counts, and no Java source changed since `8ef833`. Also: "unsorted" is inaccurate for range buckets; bare file citations need blob links; the Choice lacks its closing question; Limits lacks the follow-up offer. Replacement text is in part f6 |

## Landing order and interactions

- **5394 and 10844:** no overlap. Trial merges are clean in both orders and onto upstream main. Land 5394 first.
- **10492:** trial merges clean with the other facet branches.
- **6193 and 11129:** no shared file or hunk, but both use the same precedence rule for per-field local params. The 11129 comment at `FacetComponent.java` 1396 overstates precedence. The 6193 pivot merge has the same offset question as 11129's line 1403.
- **12556, 17051 and 15331:** three layers of the JSON facet path. 17051 and 12556 share a merger class with no code overlap; trial merges clean. If 17051 lands first, 15331's Javadoc needs one sentence, because the server can then omit the key.
- **16290 and 12556:** the pinned failure is in the same exclusion-recompute area. Noted, not audited.

## Owner decisions

1. SOLR-5394: default thread count 1, or honor `facet.threads` on the fcs path.
2. SOLR-10844: confirm the SORTED_SET correction in the comment, and whether to keep the "empty counts" claim (not in any receipt; the draft drops it).
3. SOLR-6193: narrow the ticket to pivot merging, or hold. The changelog title is replaced in the pivot-only draft.
4. SOLR-11129: fix the offset double-apply before any draft is posted.
5. SOLR-10492: squash the handoff pairs; settle the two wording items.
6. SOLR-17051: confirm the contract (screen only above mincount 1).
7. SOLR-15331: rewrite the trailer commit and the handoff commit before any PR. Needs your direction for the history rewrite.
8. SOLR-16290: disposition (pin PR, fund the real fix, or bank).
9. SOLR-18482: update the Proof to the current head, with replacement text in part f6.

## Pre-post cleanup in drafts

- `SOLR-11129.md`: the HOLD comment must come out, and the draft cannot post until the offset fix lands.
- `SOLR-6193.md`: pivot-only draft; post only if the owner narrows the ticket.
- `SOLR-17051.md`: the bracketed base-failure sentence needs the owner's text.

## Corrections to the record

- The receipt for 12556 says 13 tests; the file has 12 `@Test` methods.
- The 18482 Proof names an older head.
- The changelog for 10844 repeats the SORTED docValues claim.
- 15331's live head carries a Claude trailer on `a9f5ecf9d0c`.

## Not done

No build, test, Gradle run, `gh` write call, fetch, commit, or post. Live JIRA was not read. Gate logs named in the receipts are not on disk, so all counts are receipt-only. Lucene 9.x and 10.x claims were not rechecked in this sub-batch.
