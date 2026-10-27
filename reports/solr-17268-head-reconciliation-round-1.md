# SOLR-17268 head reconciliation (round 1)

- Assignment: `assignments/solr-17268-head-reconciliation-round-1.md`. Claim: `claims/solr-17268-head-reconciliation-round-1-phone-agent.md`.
- Executed by: phone agent, 2026-10-26, records only. No pushes, no PR, no branch edits.

## Settled head

The PR vehicle is `77a7e748e9ddf30fe7b0957796d6de151d13d2fa`, the live fork tip of `solr-17268-submit`. It is the only head that should be opened from, and the only head a future gate needs to certify.

## How the three heads resolve

The branch exists as two lines carrying the same work:

- Gated line (old base, merge-base `714d6ceff852`): `e97dacd223` → `9993394741` ("fix arrntv test fixtures and cloud routing", 2026-10-17) → `f888c1cea8` ("fix the arrntv facets test request and the terms value match", 2026-10-18T01:30:58Z).
- Live line (fresher base, `63f2459f3b5`): the production and test commits, a tidy commit (`047784bef37`), `da41f06711` ("fix arrntv test fixtures and cloud routing"), then `77a7e748e9dd` with the same message and the same author timestamp as `f888c1cea8`.

`f888c1cea8` and `77a7e748e9dd` are twin commits: the same fix applied on the two lines. `9993394741` is not a third candidate; it is the parent of `f888c1cea8`, and its NOT GREEN re-gate verdict was answered by its own child commit on the gated line and by the twin on the live line.

## Tree comparison, ticket files only

All five files in the gate job's diff scope are byte-identical between `f888c1cea8` and `77a7e748e9dd` (compared through the GitHub contents API at each SHA):

| File | Result |
| --- | --- |
| `changelog/unreleased/SOLR-17268-jackson-json-arrntv.yml` | identical, 195 bytes |
| `solr/core/src/java/org/apache/solr/response/JSONResponseWriter.java` | identical, 9947 bytes |
| `solr/core/src/java/org/apache/solr/response/JacksonJsonWriter.java` | identical, 8928 bytes |
| `solr/core/src/test/org/apache/solr/response/JSONWriterTest.java` | identical, 24856 bytes |
| `solr/core/src/test/org/apache/solr/response/JacksonJsonWriterArrntvCloudTest.java` | identical, 4942 bytes |

The 211-file divergence in the `f888c1cea8...77a7e748e9dd` compare is base drift between the two lines, not ticket content.

## Gate provenance

The GREEN gate ran on `f888c1cea8`: the archive board's `gates/SOLR-17268.md` records "DONE 2026-10-18 (vm3 re-gate GREEN at f888c1cea8, operator-approved run on the patched head; receipt in receipts/SOLR-17268.md)". No gate record names `77a7e748e9dd`.

## Openability of the settled head

- It is the live fork tip of `solr-17268-submit` (verified by `git ls-remote` equivalent fetch on 2026-10-26), so it is pushable and openable as it stands.
- Base freshness: merge-base with `origin/main` is `63f2459f3b5`; main is 14 commits ahead of that base as of 2026-10-26.
- Changelog entry present and identical to the gated head's.
- Tidy state at `77a7e748e9dd` is not certified by any record; the line carries a tidy commit from before the two fix commits. The owed gate's tidy step settles it.

## Step still owed

One exact-head gate on `77a7e748e9dd` before the branch is called validated. The ticket content is byte-identical to the head vm3 proved GREEN, so the run is a confirmation at the live head under the factory's exact-head rule, not new proof work. The route decision itself (PR, handoff, or hold) stays with the owner; this report settles only which head it would use.
