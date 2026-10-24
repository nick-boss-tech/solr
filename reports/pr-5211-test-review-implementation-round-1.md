# PR 5211 test review, implementation, phase I (round 1)

Claim: `claims/pr-5211-test-review-implementation-round-1.md` (vm6). Assignment: `assignments/pr-5211-test-review-implementation-and-gate-round-1.md`. Phase G (Linux proof) is a separate claim and report.

## Pins

- Target PR: apache/solr #5211 (SOLR-18569, Jason Gerlowski), open, not draft, checked live 2026-10-24.
- Original PR head: `6f72e0cac151d139626299a680f90a7c9c123ceb`, unchanged at the time of the claim.
- Merge base: `79b23df58daac14b92cc35f23b8b81f72098b4ba`.
- Work branch: `solr-18569-pr5211-test-review` on nick-boss-tech/solr. Created from the PR head fetched from `apache/solr` `refs/pull/5211/head`. The branch was absent before this run.
- New head: `f00c7bde83bf5d82ac4607ecf086a4397eca563b`, author Nick Shanin <nick.boss.us@gmail.com>, one commit, no trailers. Pushed as a new branch; no force.
- Read-back: `git ls-remote` on the fork returns `f00c7bde83bf5d82ac4607ecf086a4397eca563b` for `refs/heads/solr-18569-pr5211-test-review`.

## Files

Only `solr/core/src/test/org/apache/solr/cloud/ZkControllerTest.java`. Diffstat: 1 file, 9 insertions, 5 deletions. No changelog fragment, per the assignment.

## Rationale and citations

At the reviewed head, `testStartupWaitsForLeftoverEphemeralNodesToDisappear` (lines 226-257) joined the deleter thread in `finally` (line 251) and only then asserted that the leftover paths were gone (lines 253-255). Production `checkForExistingEphemeralNode` (`solr/core/src/java/org/apache/solr/cloud/ZkController.java` lines 1181 onward) blocks on deletion watches and returns only after the leftovers are gone. So the honest path passes either way. The weakness is in the other direction: if the check returned early, the join would still wait for the deleter to remove the leftovers, and the later assertion would pass. The test would not detect the early return.

The change, in the test only:

- Moves the absence assertion to run immediately after `checkForExistingEphemeralNode()` returns, before the join. An early return now fails with the leftover still present.
- Joins the deleter with a 30 second bound, so a stuck deleter cannot hang the test.
- Captures a deleter failure in an `AtomicReference` and asserts on it after the join. The old code threw `RuntimeException` inside the thread, where the test runner would not see it.
- Asserts that the deleter thread has finished.

Kept as is: the 1000 ms deleter delay. A coordinated worker/check handshake would need an observable point inside the production wait, and the assignment rules out a production hook. The sensitivity therefore depends on the 1 second margin between the check returning and the deleter's first delete. Phase G measures that margin with the mutation leg.

## Patch

Plain patch of the incremental delta only (`git diff 6f72e0cac151 f00c7bde83bf`): `reports/pr-5211-test-review-implementation-round-1.patch`.

## Compare

Reviewed PR head to new head: https://github.com/nick-boss-tech/solr/compare/6f72e0cac151d139626299a680f90a7c9c123ceb...f00c7bde83bf5d82ac4607ecf086a4397eca563b

## Not done in phase I

- No compile, tidy, Error Prone or test run. These belong to the phase G gate at the pinned head `f00c7bde83bf`. Phase I did not claim them.
- The gate file `gates/SOLR-18569-pr5211-test-review-f00c7bde83bf.md` is written under the phase G claim, not here.
- No public comment, review, PR, or JIRA write. The upstream PR is unchanged.
