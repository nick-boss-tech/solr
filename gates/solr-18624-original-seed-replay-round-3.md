# Gate assignment: SOLR-18624 original failing seed replay, round 3

Status: OPEN

- Authority: Nick supplied the failing command and seed in chat on 2026-11-05 and requested a Linux handoff. This supplements the completed round-2 gate; it does not replace its results.
- Tags: gate, premise-run, review, records. Intended host: any capable Linux gate host with the branch's JDK 21 toolchain, Gradle wrapper/dependencies and the standard host runner; serialize behind its build lock.
- Target: draft [apache/solr PR 5294](https://github.com/apache/solr/pull/5294), `nick-boss-tech/solr:solr-18624-flaky-multiword-synonyms`.
- Offered head H1: `0af4f5b3f2784eba3693acdc33dc49f6c2802b6a`. Stop and reconcile if the live fork or PR head changes.
- Historical failing merge F: `fe84bf4e4844df854c3fbe821401ab4f9bf36b11`, from [Crave run 41911581722](https://github.com/apache/solr/actions/runs/41911581722). The workflow reports contributor head `600e0ec4afd82004a94852c419892897be14e6f0`; its executed merge F is a distinct revision. Do not substitute the contributor head for F.
- PR 5294 upstream base B: `07ccc73cd24e26e59da169ca35b3fd5f64c35986`. Use F for the historical replay and the matched test-file comparison below; B is context, not the historical failing revision.
- Original seed: `B3B4534A1A4D09F4`.
- Focus: `:solr:core:test`, `org.apache.solr.search.TestMultiWordSynonyms.testPf`.
- H1 test-file Git blob: `7f2e210d8ea93947dd246f76354b60848a4ebb94`, path `solr/core/src/test/org/apache/solr/search/TestMultiWordSynonyms.java`.
- Existing complete proof: `receipts/SOLR-18624-assertion-repair-round-2.md`. New receipt reserved for the executor: `receipts/SOLR-18624-original-seed-replay-round-3.md`.
- Discovery context: `reports/pr-5291-review-round-1.md` already records this seed and failing merge. The earlier PR 5294 statement that the original seed was unavailable is now outdated. Do not edit a live PR body from this job.

## Claim and environment

Refresh this job, the live fork and PR head, and any execution claim/receipt. Follow START-HERE/WORKFLOW detached preflight and claim rules. Use separate clean disposable Linux checkouts for F, F with the H1 test file, and H1. Record full revisions, host/OS, JDK vendor/version, runner, start/end times, logs and XML directories. Run commands only through the host's approved Gradle runner.

Nick provided this original command. Preserve every property and the JVM argument string as one argument; quote the timeout value including its literal exclamation mark:

```sh
./gradlew :solr:core:test --tests 'org.apache.solr.search.TestMultiWordSynonyms.testPf' '-Ptests.jvmargs=-XX:TieredStopAtLevel=1 -XX:+UseParallelGC -XX:ActiveProcessorCount=1 -XX:ReservedCodeCacheSize=120m' -Ptests.seed=B3B4534A1A4D09F4 '-Ptests.timeoutSuite=600000!' -Ptests.useSecurityManager=true -Ptests.file.encoding=US-ASCII
```

Confirm the effective forked JVM flags, security-manager setting and file encoding from logs. A single-core VM alone is not a substitute for `-XX:ActiveProcessorCount=1`. Retain the original wrapper metadata where available. The seed is known; other historical host/JDK details may still differ and must be disclosed.

## Required legs

Use the command above once per leg on a fresh checkout/test-output directory. Force execution with the runner's normal fresh-output procedure if necessary; document any execution-only addition such as `cleanTest` or `--rerun-tasks`. Archive fresh XML and complete test output before moving to another leg. Require exactly one selected test execution and no skips per leg. Never treat cached results, compile failures or setup errors as assertion proof.

1. **Historical failure:** fetch and verify F, then run its original test unchanged. Expected: a runtime assertion failure in `testPf`, with document 10 expected first and another equal-score ordering returned. Capture the exact failed request/assertion, all returned IDs and scores, parsed query if available, XML failure and command output. Verify against the original CI failure context rather than assuming any failure is the same one.
2. **Matched test repair:** start again at F, replacing only `TestMultiWordSynonyms.java` with its exact H1 contents (blob above). Verify the entire remaining tree matches F and save the one-file diagnostic diff. Run the same command and settings. Expected: one test passes, zero failures/errors/skips. This compares test assertions on the same production code; it is not a production bug fix or a new offered branch head. Keep this overlay local and disposable.
3. **Offered-head proof:** on clean H1, verify live PR/fork pins and run the same command/settings. Expected: one test passes, zero failures/errors/skips. Finish with clean H1 and a separate live remote read-back. No mutation or diagnostic overlay may be pushed.

If leg 1 passes, preserve that non-reproduction and still complete legs 2 and 3 when the environment is valid. Do not rerun until a failure appears, change seeds, or claim the historical failure was reproduced. Explain differences from the original CI environment and report the precise result. A diagnostic-only change to show the tied result order may be recorded separately, with its diff; it does not replace leg 1.

If any leg fails for an environment reason, diagnose and record it separately before a bounded retry under WORKFLOW. An executed assertion failure on leg 2 or 3 requires investigation and handback with logs; do not weaken assertions, alter production code or silently retry. A changed source head needs a new explicit repair and superseding proof.

## Receipt and handback

Write `receipts/SOLR-18624-original-seed-replay-round-3.md` with the three outcomes, exact commands, all effective JVM properties, full revision/blob checks, original CI comparison, UTC times, JDK/host, exit codes, fresh XML counts, logs, any setup repair, and final offered-head read-back. State whether the original failure was reproduced. Distinguish the F-plus-test-file overlay from H1. All required changes are temporary diagnostics; the PR stays at H1 and remains draft.

Include a short factual replacement for the PR's seed limitation based on observed outcomes. No PR body edit, public comment, JIRA write, force-push or source-branch change is authorized here. The owner handles public wording separately. Windows ran no tests; posting this job does not mean a Linux host has claimed or executed it.
