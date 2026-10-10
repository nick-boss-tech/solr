# Search components round 1, sub-batch 3 (doc transformers, writers, loaders): round roll-up

Claim: `claims/search-components-3.md` (commit `e4a5dafa8f4`). Assignment: `assignments/search-components-3.md` (commit `77b6b51a7fc`). Per-part reports: `reports/search-components-3-w1.md` through `-w6.md`.

Done 2026-10-10 by Claude Code (AI agent), working for Nick Shanin. Six read-only subagents did the audit. No build, Gradle run, or test was run. No submit branch, live PR, JIRA item, or comment was touched. Nothing was posted.

## Heads

Every named head matches its live branch. The local `solr-<ticket>-submit` branches in this checkout are stale, and some carry unpushed notes commits. Do not push from them. Drafts name the live origin heads.

## Per-ticket verdicts

| Ticket | Verdict | Draft | Live head | What blocks it or what the draft must say |
|---|---|---|---|---|
| SOLR-4374 | Draftable | `SOLR-4374.md` | `801c62290f7` | The test covers `1001_s`, not the ticket's field `1001`. The rename form `x:1001` is not covered. Both go in Limits. The Proof names the failing test but not its assertion message, since the receipt has none |
| SOLR-7390 | Draftable | `SOLR-7390.md` | `7463dd006a7` | The "identical code" claim checks out against the earlier commit. Owner: re-run at the live head, or accept the Proof wording. The fail-fast behavior is a Choice |
| SOLR-9396 | Draftable after two fixes | `SOLR-9396.md` | `a5ab2eda4e6` | Squash the two handoff commits (`f0e03ca25da`, `a5ab2eda4e6`); their subjects would show on the PR. Drop the `\{?` branch of the row-reference regex in `SubQueryAugmenterFactory.java` 307, since nothing dereferences `${row.x}` |
| SOLR-14678 | Draftable, held | `SOLR-14678.md` | `5d94e6cf398` | `/get` with several ids transforms every document before writing, so only the last keeps nested children (`RealTimeGetComponent.java` 366-375). The draft's Limits name this. The owner decides whether to fix it here. The changelog title overclaims: narrow it to search results. The receipt's single premise failure cannot come from base code, so the draft has no fail-before sentence |
| SOLR-8240 | Draftable after one doc fix and a landing decision | `SOLR-8240.md` | `fad7a1dd8e2` | The ref guide at line 68 says "supplies both", which misses inherited values. Land `SOLR-10424` before or with it: alone, 8240 turns every `f` request to the techproducts sample into a 400. The claim's line 53 says 8240 makes JsonLoader honor `f`; the branch rejects `f` with a 400, as the Jira proposes |
| SOLR-10424 | Draftable | `SOLR-10424.md` | `e629ab8bb29` | The Proof matches the fixed test only: `TechproductsJsonDocsParamsTest` 1 of 1 at the head, and base `cabedd1d968` fails on the `mapUniqueKeyOnly` assertion. Choice: drop the default, or a named id-only set |
| SOLR-11364 | Draftable, held | `SOLR-11364.md` | `562d3e7e685` | Commits `0e49d9dc424`, `e7dbf1a2f61`, and `8788b294650` carry a Claude `Co-Authored-By` trailer. `0e49d9dc424` says "Hypothetical, unrun" and points to a removed `SOLR-11364-TESTING.md`. Squash to one clean commit on a new branch name, with no force push. The changelog title overstates the alias case: `fl=mydv:a3` still drops `a3`, and `fl=a3:a1` can return a raw `a3` value when `a1` is missing. Owner: fix, or keep it in Limits |
| SOLR-16155 | Audit only; the flag stands | none | `0881de1ed68` | Upstream PR #1151 is open and covers the ticket with broader scope. Owner decides whether anything is submitted. No file or code-path overlap with 11364 |
| SOLR-7498 | Draftable | `SOLR-7498.md` | `2050d8e447a` | The run date is a placeholder. Open owner question: trace the SolrJ missing-size path before submitting |
| SOLR-9148 | Draftable after a docs fix | `SOLR-9148.md` | `30f0d7a42d5` | `sql-query.adoc` line 454 says the JDBC driver "cannot set filter queries". The JDBC info reaches `SolrTable` as the same properties, so the sentence overstates. Replace with "The JDBC driver adds no filter queries of its own." TestSQLHandler counts 35 at the tip (34 at base) |
| SOLR-9864 | Draftable | `SOLR-9864.md` | `b5826e466b9` | In `SolrQueryTest.java` 128-131 the `testGetSortImmutable` comment now sits above the new test. Move the new method above the comment. The counts check statically: 17 + 5 + 5 = 27 |
| SOLR-8003 | Held, audit only | none | `f1c99a44961` | No gate. The branch carries a code commit (6 files) beyond the handoff doc. Textual conflict with 14678 in `DocTransformers.java` |
| SOLR-18356 | Retired, audit only | none | `c179cd35713` | Upstream `e11a34a5141` has the identical `DocsStreamer` removal (blob-equal). Only the branch deletion is open, and that is the owner's call |
| SOLR-11153 | Draftable, held for three fixes and an owner call | `SOLR-11153.md` | `093df0d65bd` | The changelog title names only the `wt=schema.xml` path. The comments overstate the version case. Handoff commits are in the history. Owner: omit or warn |
| SOLR-12543 | Draftable as a partial fix, held | `SOLR-12543.md` | `88d236db6b7` | Three commits carry Claude `Co-Authored-By` trailers, and handoff commits sit in the history. Both need an authorized rewrite. The draft says the fix is partial |
| SOLR-13245 | Draftable, held for the owner's scope call | `SOLR-13245.md` | `16e62ab6542` | Node-local or collection-wide. The changelog must also state that a start replaces a same-named daemon on a sibling replica |
| SOLR-15041 | Held | `SOLR-15041.md` | `55fca0a7c24` | In `CSVLoaderBase.java` 227-235, a blank line inside a split value loses a line break (read from code, not run). It needs a code fix and a focused test |

## Landing order and interactions

- **4374 and 7390:** no dependency. Both orders give the same merged tree (`20679cf27977`) with a clean textual merge. The merged tree is not built.
- **9396 and 14678:** no file overlap. 9396's assumptions hold at 14678's head. Both need a combined focused run before the second lands.
- **8240 and 10424:** land 10424 first (see the table).
- **11364 and 16155:** no overlap.
- **8003 and 14678:** textual conflict in `DocTransformers.java`.

## Owner decisions

1. SOLR-7390: re-run at the live head, or accept the Proof wording. The fail-fast Choice.
2. SOLR-4374: the schema-lookup Choice.
3. SOLR-14678: fix the `/get` multi-id transform here, or leave it in Limits; per-document versus request-wide.
4. SOLR-8240 and SOLR-10424: the landing order (10424 first).
5. SOLR-11364: the alias-case changelog title, and the history rewrite (new branch name, no force push).
6. SOLR-16155: whether anything is submitted, given the open upstream PR #1151.
7. SOLR-7498: trace the SolrJ missing-size path before submitting.
8. SOLR-11153: omit or warn.
9. SOLR-13245: node-local or collection-wide.
10. SOLR-18356: branch deletion.
11. Rewrites for 12543, 11153, and 11364: authorize the history rewrite.

## Pre-post cleanup in drafts

- Handoff commit subjects on `SOLR-9396`, `SOLR-11364`, `SOLR-11153`, `SOLR-12543`: squash or reword before posting. Any rewrite moves the head and needs a fresh gate.
- `SOLR-9148.md`: replace the sentence about the JDBC driver before posting.
- `SOLR-7498.md`: fill in the run date.

## Corrections to the record

- The 9148 sql-query sentence overstates the JDBC driver's behavior.
- The 8240 claim (line 53) says the change honors `f`; the branch rejects it.
- The 11364 changelog title overstates the alias case.
- The 14678 changelog title overclaims.

## Not done

No build, test, Gradle run, `gh` write call, fetch, commit, or post. Gate logs named in the receipts are not on disk, so counts are receipt-only. Live JIRA was not queried.
