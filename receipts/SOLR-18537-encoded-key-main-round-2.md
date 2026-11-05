# Receipt: SOLR-18537 encoded-key gate, round 2 (phone agent)

Status: GATE GREEN at the exact head.

- Job: `gates/solr-18537-encoded-key-main-round-2.md`, applying the round 1 job's terms (`gates/solr-18537-encoded-key-main-round-1.md`) unchanged. Claim: `claims/solr-18537-encoded-key-main-round-1-phone-agent.md`. Executed 2026-11-05T04:57Z to 05:12Z (attempt 2; attempt 1 at 04:31Z lost its test legs to the Gradle journal lock held by the preceding check run's daemon and is not counted). Seed `5110EAC0DE`, JDK 21, Gradle 9.7.0 via `~/workspace/tools/solr-gradle.sh`, one build at a time under the host build lock. Test legs in clean worktrees at the exact head; root `check -x test` in a normal clone (not a worktree) whose checkout was verified by recorded `rev-parse HEAD`, because a local clone does not carry commits that exist in the source repo only as fetched objects; the clone fetched the candidate SHA explicitly.
- Candidate head: `a676e7a392244e7cf4a35719fc64383b65cd947f` on `solr-18537-encoded-key-followup` (round 1 head `7b83425fe7` plus the formatting-only tidy repair commit, +2/-1 in `NodeTargetProxyFilterTest.java`). Baseline: upstream main `07ccc73cd24e26e59da169ca35b3fd5f64c35986`. Live fork head re-verified through the GitHub API after the run: `a676e7a392`.

## Checks, independently

- Tidy preflight at the candidate head: rc 0, tracked tree clean (no diff).
- Positive focused suites: `NodeTargetProxyFilterTest` 19 of 19 and `NodeTargetProxyFilterAuthTest` 6 of 6, zero failures, errors, or skips, matching the job's expected counts.
- Positive neighbors: `HttpSolrProxyTest` 2 of 2 and `SolrRequestParserTest` 9 of 9, zero failures, errors, or skips.
- Fail-before (isolated baseline checkout with only the two candidate test files overlaid; overlay diff retained): all four named methods executed and failed at their intended assertions: `testEncodedQueryParamProxiesGet` returned the local node instead of the target (ComparisonFailure on the node name); `testEncodedQueryParamAndHeaderAreRejected` got 200 instead of the conflict 400; `testEncodedQueryDirectiveConsumedWhenForwardingToReplica` found the directive retained instead of absent (assertFalse at line 238); `testEncodedQueryParamProxiesWhenAuthenticated` returned the local node instead of the target. As the job notes, the GET method loops over path and key variants and stops at its first baseline assertion; the remaining variants are proven by the positive leg, not independently in the negative leg.
- Root `check -x test` in the clone at verified head `a676e7a392`: BUILD SUCCESSFUL in 8m 39s, rc 0, tracked tree clean afterward. The combined Hatch CA truststore was supplied to the Gradle JVM for downloads through the local relay, per the host's recorded egress CA condition.
- Final remote read: fork branch unchanged at `a676e7a392`.

## Round 1 summary (for the chain of evidence)

At round 1 head `7b83425fe7` the same positive and fail-before legs passed with identical counts and signatures; the gate stopped on tidy only, and its root check never executed (stale-daemon journal lock plus the clone checkout defect). Full record: `receipts/SOLR-18537-encoded-key-main-round-1.md`.

## Draft PR description (for later owner review; no PR is authorized by this gate)

SOLR-18537: recognize encoded node-target query keys in NodeTargetProxyFilter. The filter now parses GET queries when the targeting parameter name arrives percent-encoded, so encoded keys resolve and are consumed like the plain form; header/query conflict checks, POST non-routing, and unrelated-query header preservation are retained, and query parse errors keep their actual status instead of being reclassified by the authentication filter. Tests cover encoded keys through the v1 and v2 endpoints, both hex cases, the encoded-key/header conflict, forwarding-hop directive consumption, authenticated routing, and malformed UTF-8 returning 400; each new test fails on unmodified main for the stated reason and passes with the change. A separate public issue/PR identity for this slice remains the owner's decision; no changelog fragment is included yet.

## Evidence on the gate host

- `~/workspace/tools/gate-18537-encoded-key-r2b/` (master.log, tidy/focused/neighbors/fail-before/root-check logs, fresh JUnit XML per leg, overlay diff, recorded check-clone HEAD). Attempt 1's lock-failure logs are under `~/workspace/tools/gate-18537-encoded-key-r2/`.
