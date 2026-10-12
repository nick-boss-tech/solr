# Replication and backup convergence, round 3: main-side response

Round 3 re-checked the two drafts still open (SOLR-8430, SOLR-9091) and returned NOT CONVERGED, with 3 new findings (RB-R3-01 should-fix; RB-R3-02, RB-R3-03 notes) and the carried RB-R1-01 (blocking) and RB-R2-01 (should-fix) still open in the SOLR-8430 text. By the time round 3 read the records, vm2's re-gate had closed GREEN at the fixture-fixed head, so both carried items stood unblocked with the draft pass owed. This lane is that pass. Every item was verified against the branch at the gated head `0d64c35c5a7d3caffa9981fd1e1a61e1d818edf5`, fetched this pass (round 3 could not fetch under its read-only rule), and all five SOLR-8430 items are applied in the register's text. No finding is rebutted. No round 4 is posted: round 3 was the confirmation vehicle and has reported, so this response states the closure directly. Application method: exact-string replacements, each asserted to occur exactly once before applying; the full diff was then reviewed.

## Counts

| Grade | Items in scope this pass | Applied | Still open |
|---|---|---|---|
| Blocking | 1 (RB-R1-01) | 1 | 0 |
| Should-fix | 2 (RB-R2-01, RB-R3-01) | 2 | 0 |
| Note | 2 (RB-R3-02, RB-R3-03) | 2 | 0 |
| Branch-side carried | RB-R1-03 (SOLR-8430), RB-R1-04 (SOLR-9091) | RB-R1-03 confirmed on the object | RB-R1-04, riding its gate job |

After application, SOLR-8430 is CONVERGED on its text. SOLR-9091 is not (RB-R1-04 open).

## Fact checks before applying

- **Gate state (SOLR-8430):** `gates/SOLR-8430.md` is DONE with GATE GREEN at `0d64c35c5a` (run ended 2026-10-12T04:41:31Z; focused run 4 tests, 0 failures, seed 8430C0FFEE008430; cap leg failed by content at the cap assertion; branch leg a compile failure, inconclusive by construction). `receipts/SOLR-8430.md` is refreshed at that head, and the fork tip for `solr-8430-submit` matches it.
- **Cap code at the gated head:** `ReplicationAPIBase.java` lines 98 to 123 carry the cap comment (line 98), `MAX_SHARED_RATE_LIMITERS` of 64 (line 99), the static `SHARED_RATE_LIMITERS` map (line 101), `rateLimiterFor` (lines 109 to 119: shared limiter when the rate is already in the map, a new `SimpleRateLimiter` when the map is at the cap, `computeIfAbsent` otherwise) and the test-only seam `clearSharedRateLimitersForTest` (line 121). The draft's code link is re-pointed to lines 98 to 119 at the gated head.
- **RB-R1-03 on the object:** the changelog fragment at `0d64c35c5a` is titled "Replication maxWriteMBPerSec throttling is shared by concurrent file requests on a node that use the same rate, instead of each request getting its own limiter", the round 1 replacement word for word. Round 3 had this confirmed on the gate record only; it is now read on the branch itself.
- **RB-R3-03 confirmed against the capped code:** rate 0 is keyed as `Double.MAX_VALUE`. If that key is not yet in the map and the map already holds 64 values, `rateLimiterFor(0)` returns a fresh limiter on each call, so unthrottled streams stop sharing once the map is full. The register's suggested sentence holds and is applied.
- **Test file at the gated head:** `ReplicationRateLimiterTest` has 4 tests, including `testRatesPastTheCapGetPrivateLimiters`, and a `@Before` that clears the shared map through the seam. That matches the receipt's 4 of 4 count.
- **Proof (RB-R3-01):** the separate behavior check ran at `49af21be` on 2026-10-07 only; neither gate run at the later heads lists a re-run of it. The prescribed replacement is applied verbatim, including its closing sentence that the check has not been run at the gated head.
- **Fixture-order Limits item:** no RB item in rounds 1 to 3 prescribes a Limits line about the test fixture order, and none is added. The seam is test-only; its record sits in `gates/SOLR-8430.md` and `receipts/SOLR-8430.md`, and the Proof's cap-leg sentence ("with the new test kept and a test-only hook added") carries the proof shape. The two standing Limits bullets (transfer speed not measured; the follower side and the index fetch path unchanged) are still true at the gated head and stand unchanged.

## Per-finding dispositions

### SOLR-8430

- RB-R1-01 (blocking): APPLIED, closes. Before: the first Limits bullet read "Limiters stay in memory for the life of the node, one for each rate value that a request has used.", no bound of 64 appeared anywhere in the draft, and the code link, the Proof and the changelog link all pointed at `49af21be`. After: the bullet is replaced with "Past 64 distinct rate values, a new stream gets its own limiter and is not shared."; the body gains "The map holds at most 64 rate values, so a client cannot grow it without limit."; the code link and the changelog link are re-pointed to `0d64c35c5a` (the code link now spans lines 98 to 119), and the Proof is re-pointed by RB-R3-01 below.
- RB-R2-01 (should-fix): APPLIED, closes. The three bounded statements now read: the bold opener "File streams that ask for the same rate now share one limiter on the node, for up to 64 distinct rate values."; the body "one shared limiter for each rate value, up to 64 distinct values"; the Choice "This change keeps one limiter per rate value, up to 64 distinct values."
- RB-R3-01 (should-fix): APPLIED verbatim, closes. Before: the Proof read 3/3 at `49af21be`, verified 2026-10-07, with the separate behavior check "passed at this head", a head at which it never ran. After: the Proof reads 4/4 at `0d64c35c5a`, verified 2026-10-12, states the cap-leg failure on the pre-cap code and the inconclusive-by-construction branch leg, and states the 2026-10-07 behavior check plainly, ending "It has not been run at the gated head."
- RB-R3-02 (note): APPLIED with one adaptation, closes. The prescribed OWNER NOTE listed this pass's own work as its step (1), still owed. The note as applied keeps the prescribed wording and the gated head, records the post-cap text for RB-R1-01 and RB-R2-01 as applied, and leaves only step (2) before posting: "take the choice call (DISCUSS, not taken: one limiter per rate value, or one per node)". The HOLD stands on that call alone.
- RB-R3-03 (note): CONFIRMED on the code (see the fact checks) and APPLIED, closes. The sentence now reads "A rate of 0 still means no throttle, and unthrottled streams share one unlimited limiter until the map is full."
- RB-R1-03 (should-fix, branch-side): CONFIRMED ON THE OBJECT this pass and already landed; closes (see the fact checks). The draft's own references moved with RB-R1-01 above.
- RB-R1-02: confirmed by round 2; no further action.

### SOLR-9091

- RB-R1-04 (should-fix, branch-side): still open, no edit in this lane. The fragment at `e6bfa5c62681` still says a corrupt file "fails the restore (and rolls back)". The fix rides `gates/SOLR-9091-title-fix.md` (OPEN, unclaimed): the title correction with a one-fragment delta check and a re-gate at the new head, and the RB-R2-02 link re-points and line 23 sentence land in that same pass.
- RB-R2-02 (note): conditional, not due until RB-R1-04 lands.

## Items this pass closes

RB-R1-01, RB-R1-03, RB-R2-01, RB-R3-01, RB-R3-02 and RB-R3-03: every open SOLR-8430 item from rounds 1 to 3.

## Verdicts after application

CONVERGED on text: SOLR-8430, at `0d64c35c5a`. Nothing remains for SOLR-8430 but opening: delete the OWNER NOTE, take the choice call it records, and post.

NOT CONVERGED: SOLR-9091 (RB-R1-04), riding `gates/SOLR-9091-title-fix.md`.

No round 4 assignment is posted.
