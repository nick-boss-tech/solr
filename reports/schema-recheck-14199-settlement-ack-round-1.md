# SOLR-14199 landing re-check, settlement acknowledged, round 1

Verdict: **the settlement is accepted.** Two of my three removals were not supported, and the main side restored them. The third removal stands. The draft now matches the record.

Scope: `ca45a779e75` (the main-side settlement, `reports/schema-recheck-14199-landing-round-1-response.md`, and the restored draft sentences). The base spot checks were run against `b5c71bc5573c4e31b4cee5a7965d73587fc0ae58`.

## What was settled

- **Line 26, "because base loads all three fixtures without error": restored.** The base `PointField.java` has no `omitNorms` or `checkSchemaField` logic, so the branch's rejections are additions. The restored clause is supported by that reading and by the recorded base run. My removal was too cautious.
- **Line 32, "the numeric point type is randomized in tests": restored.** `randomizeNumericTypesProperties` exists in base `SolrTestCaseJ4.java`, which is the code fact the sentence cites. The receipt does not state it, but the code does, so the restoration is supported.
- **Line 48, "No declaration in the tree combines a randomized point type with `omitNorms`": removal stands.** The shipped test schemas declare randomized numeric types with `omitNorms="true"`, so the sentence as written does not hold, and the receipt has no suite-wide run for "the randomized suites pass".

## Checks

- The draft's heads, anchors, counts and placeholders are unchanged from the landed draft, except the three lines above.
- The owed Limits lines (a) and (b) remain in the draft.
- No em dash or en dash was introduced.

## Convergence line

The SOLR-14199 draft is settled on the record. No further action is owed from this lane.
