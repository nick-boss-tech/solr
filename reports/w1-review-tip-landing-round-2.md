# Workstream W1: review-tip wording landed on pr-prepare, round 2

Assignment: `assignments/pool-category-closeout-overnight-round-1.md`, W1. Claim: `claims/pool-branch-review-verdicts-landing-w1-round-2.md`. Starting tip `c8dbf8b53ce`.

Scope: the 20 drafts that were marked "No" (the review-tip wording was not on pr-prepare). Each draft was copied byte-for-byte from its verdict branch, after checking that the verdict was issued against that text. The three query-parsing OPEN (gate) drafts (SOLR-6014, SOLR-12608, SOLR-17796) were not copied, because the patch loop has already worked them on pr-prepare. No live PR body, PR, comment or claim touched. No build, test or Gradle run.

Verdict branches, verified by ls-remote:
- Search components: `assignment-search-components-review` at `289d5d3ad5fe34b21852a4187657b03796bbc5e7`.
- Query parsing: `assignment-query-parsing-review` at `03fbf23793f0ab23d46dce01709a3afec01a2fe6`.

## Landed (20 drafts)

Search components (10): SOLR-6975, SOLR-7550, SOLR-8009, SOLR-10492, SOLR-11364, SOLR-13245, SOLR-13876, SOLR-14451, SOLR-15479, SOLR-17051. Each blob matches the verdict-branch version. The live submit tips match the receipt heads and the draft citations.

Query parsing (10): SOLR-4824, SOLR-9048, SOLR-9149, SOLR-11391, SOLR-12212, SOLR-12532, SOLR-12871, SOLR-15615, SOLR-16267, SOLR-17280. Each blob matches the verdict-branch version. Receipt head, live tip and draft head agree, and the counts match the receipts.

## Corrections to the landing report's own counts

- SOLR-13876: the landing report and the search-components sub-batch 2 table say 2 edits. The diff shows 1. The summary's total is unaffected.
- SOLR-17280: the landing report says 3 edits. The diff shows 2, because one "we" sentence appears twice in the q3 table.

These are recorded here, and the landing report stays as it was landed.

## Receipt versus tip

- SOLR-17051: the receipt says the missing-bucket screen runs "only at the coordinator". The tip (`148544e9ed54`, `FacetFieldProcessor.java` lines 534 to 542) also screens single-node requests. The tip is believed, and the draft is unchanged. The draft still needs its wording checked against the tip before any opening; that is a later step.

## Open flags carried forward

- SOLR-11391: the receipt records GATE GREEN at `b0f15a22856`. The round 1 assignment said "NO GATE". The query-parsing summary records this as decision D4, and the main side is asked to confirm it.
- SOLR-16570: two owners (search components and query parsing). Not decided here.
- SOLR-6014 and SOLR-12608: the verdict-branch copies of these drafts are not used. Their pr-prepare versions carry the patch-loop edits, and the holds are in `reports/branch-review-verdicts-landing-round-1.md`.

## Not done in this round

- The W1 deliverable's ready lists (step 4): the drafts verified READY at their live tips, with the current receipt named for each, are not built here. The 20 landed drafts are candidates; their live-tip and receipt checks are done for head and count, but the ready list needs the packaging-step check per draft.
- The live PR bodies for the 20 drafts are not edited. Each body update waits for the draft to be re-read and approved.
