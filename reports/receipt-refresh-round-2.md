# Receipt refresh round 2: verdicts for six tickets

Claim: `claims/solrcloud-answers-round-1.md` (commit `f52f0504fc8`), part b1 and part b2. Reports: `reports/receipt-refresh-round-2-b1.md` and `reports/receipt-refresh-round-2-b2.md`.

Done 2026-10-10 by Claude Code (AI agent), working for Nick Shanin. Read-only checks, with no build, Gradle run, or test. No submit branch, live PR, or comment was touched. Nothing was posted.

## First pass was invalid, and the rechecks replace it

The first pass ran against stale local refs. Six branches (8051, 10305, 9124, 6759, 8003 and 10694) were not fetched when it started. Its holds on 6759 and 8003 were based on a missing head, and its holds on 8051, 10305 and 9124 were based on stale refs. Both reports were then re-run against the fetched `origin/` heads, and the verdicts below are from those rechecks.

Heads at the fetched refs match the live heads and the receipts' gated heads: `e50a2aa3437c` (8051), `c43de86d4c13` (10305), `17a279d4dce1` (9124), `42f03eb52376` (6759), `17c516a607a8` (8003), `64e86811548b` (10694).

## Per-ticket verdicts

| Ticket | Verdict | Draft | Head | What blocks it |
|---|---|---|---|---|
| SOLR-8051 | Held | none | `e50a2aa3437c` | The null-body check now sits above the `getException` check (`ExactStatsCache.java` 117-126). Still held: no real path to a body-less shard response is shown (owner decision). The changelog title says "if a shard returned no response". Test javadoc line 27 says "no live replica" |
| SOLR-10305 | Held | none | `c43de86d4c13` | The 400 check and the test-only allow-list hook are present. Held for text and scope: the changelog line 2 says the NPE happens "while merging shard responses", but it is in `createMainQuery`, before any response. The test javadoc (line 76) names `mergeIds`. In default standalone the 403 comes first, so the 400 applies only in SolrCloud, or with the allow list disabled |
| SOLR-9124 | Held | none | `17a279d4dce1` | The grouped `sendGlobalStats` and grouped `updateStats` are present and match the receipt. Held for one text fix: the changelog's "match the ungrouped ones" (the test compares grouped with grouped), and the `LRUStatsCache` naming (only `ExactStatsCache` fails without the fix). The receipt says `shard_i` is multivalued, which `schema.xml` line 713 does not support |
| SOLR-6759 | Held | none | `42f03eb52376` | The handoff note is gone. The changelog title claims a lost-group fix the test does not show: the test's post filter holds nothing back and only counts `complete()` calls. Title needs call-level wording on the branch. The try/finally for partial results is an owner call |
| SOLR-8003 | Held | none | `17c516a607a8` | The handoff note is gone. The tidy commit is test-only, and the count (3 of 3) and proof match the code. The textual conflict with SOLR-14678 in `DocTransformers.java` still holds: keep both overrides. The changelog title needs the JSON writer limit. Held for owner decisions on glob-level design and landing order |
| SOLR-10694 | Draftable | `pr-drafts/search-components/SOLR-10694.md` | `64e86811548b` | The tidy commit is javadoc-only, the count is 4 of 4, and the draft names its head. The CSV-only scope is named in Limits; you confirm it. The test link should be `L359-L396`. The draft is about 10 percent over the length guide. The failing-test name is inferred, and needs confirming from the gate log, which is not on disk |

## Owner decisions

1. SOLR-8051: is there a real path to a body-less shard response? If not, the fix is a Choice or is dropped.
2. SOLR-10305: text and scope (where the NPE happens, and the 403 precedence).
3. SOLR-9124: approve the changelog wording fix (grouped against grouped), and the `LRUStatsCache` naming.
4. SOLR-6759: the try/finally for partial results.
5. SOLR-8003: the glob-level design, and the landing order against SOLR-14678.
6. SOLR-10694: confirm the CSV-only scope.

## Not done

No build, test, Gradle run, `gh` write call, fetch of anything new beyond the six refs named above, commit, or post. Gate logs are not on disk, so the proof counts are from the receipts only.
