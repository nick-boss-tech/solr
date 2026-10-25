# SOLR-17268 head reconciliation, round 1

Claim: `claims/solr-17268-head-reconciliation-round-1.md` (vm6). Assignment: `assignments/solr-17268-head-reconciliation-round-1.md`. Records only: no push, no PR, no branch edit, nothing posted.

## Answer

The only reachable head that has the full ticket scope is `77a7e748e9dd` (fork `solr-17268-submit`, top commit 2026-10-18). It is not gated. The GREEN gate ran on `f888c1cea8`, and that commit is no longer reachable. Whatever the owner opens from must be gated at its exact head first.

## The three heads

| Head | Gate record | Reachable now? | Notes |
| --- | --- | --- | --- |
| `9993394741` | NOT GREEN (vm2, 2026-10-17), not relaunched | No: not on the fork, not on apache/solr | Recorded in the archive gate file, `gates/SOLR-17268.md` on `archive/coordination-2026-10-23`. |
| `f888c1cea8` | GREEN (vm3, 2026-10-18, operator-approved), `receipts/SOLR-17268.md` on the archive | No: not on the fork, not on apache/solr | The archive receipt gives scope 5 files, 645 insertions, 4 deletions, against base `714d6ceff852`. |
| `77a7e748e9dd` | Not gated | Yes: fork tip | Top commit "SOLR-17268: fix the arrntv facets test request and the terms value match". |

Checks run from the fork and from apache/solr: `git fetch` of each SHA by name fails on both remotes.

## Evidence for `77a7e748e9dd`

1. Scope: the ticket diff against `714d6ceff852` covers the changelog (`changelog/unreleased/SOLR-17268-jackson-json-arrntv.yml`), `JSONResponseWriter.java` (+1), `JacksonJsonWriter.java` (+15), `JSONWriterTest.java` and `JacksonJsonWriterArrntvCloudTest.java`. That is the scope the gate job expects.
2. Test counts at the tip: `JSONWriterTest` has 20 `@Test` methods and `JacksonJsonWriterArrntvCloudTest` has 4. Both match the gate's focused-run expectation.
3. The archive's test-fix patch (`gates/SOLR-17268-test-fix-proposal.patch`, the change the GREEN head was pushed with) does not apply forward to the tip and does reverse-apply cleanly. So the tip already contains that patch's test changes.
4. The tip is 33 commits behind current apache/solr `main` (`07fdebdda5`). The base `714d6ceff852` is an ancestor of `main`, so the branch is not rebased but has no conflict with the base.

## Unresolved

- The archive receipt counts 645 insertions and 4 deletions for `f888c1cea8`. The tip's diff against the same base, over the same five files, is 398 insertions and 0 deletions. The assignment also says `f888c1cea8` and `77a7e748e9dd` diverged (34 ahead, 7 behind). The most likely reason is that `f888c1cea8` sat on a line that was later rewritten. I cannot confirm this, because the commit is gone and I found no record that names what the 645 count included.
- Because `f888c1cea8` is unreachable, its content cannot be diffed against the tip. The reverse-apply check covers only the patch's own hunks.

## Owner decision needed (route, not a records question)

Opening a PR is a public step, so the route is the owner's.

- Option A: gate `77a7e748e9dd` as it is, then open from it. Fastest. The PR would open 33 commits behind main, and any merge conflict would surface at review.
- Option B: rebase onto current `main` first, then gate the new head, then open from it. Mergeability is better; the gate runs at a new head.

Either way, the gate must run at the head that is opened. Nothing gated before this round covers the tip, so no receipt can be cited for it yet.

Question for the owner: A or B?
