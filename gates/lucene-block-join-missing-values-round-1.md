# Lucene block join missing-value repair: Linux proof, round 1

Status: OPEN

Tags: gate, review, records.
Intended host: any capable Linux pool host with JDK 25, Git, Python 3, Perl, the Lucene wrapper/build dependencies and the host's serialized build lock.

## Pins and ownership

- Repository: https://github.com/nick-boss-tech/lucene.git (a fork of apache/lucene).
- Branch: `fix-block-join-missing-values`.
- Candidate head: `c015935c04a39bda74c898eb80a3e5d6c0a0eb8c`.
- Upstream main base: `aa366ae209ae8296dd70d2835c6d47f8fc201972`.
- Source claim: `claims/lucene-block-join-missing-values-windows-round-1.md`.
- Source report: `reports/lucene-block-join-missing-values-windows-round-1.md`.
- Execution-host receipt: `receipts/lucene-block-join-missing-values-round-1.md`.
- Provenance: Solr `receipts/pr-5293-child-sort-missing-round-1.md` including the 10.4 comparison. The Solr failures are reproduced; this Lucene repair is not yet tested.

This is Lucene, not Solr. Clone a clean full checkout of the named repository; do not use a Solr checkout or the Windows sparse checkout. Verify the live fork ref equals the candidate and record current upstream main. If the fork moved, reconcile before running; do not silently change the target.

Read CONTRIBUTING.md, AI_POLICY.md, README.md and help/tests.md. Record the JDK 25 and wrapper versions, detached runner, host build lock and fresh log/XML destinations in an add-only claim. No public issue, PR or maintainer comment from the gate host. Nick must review and author the public contribution under Lucene's AI policy.

## Preflight and repairs

Run the host's detached tidy preflight at the exact head before a build claim. In a separate disposable checkout, run `./gradlew tidy` and require no tracked diff. If formatting differs, retain it and make only that formatting repair as a normal fork commit under Nick's identity; push and separately read back the new fork head, post a superseding exact-head gate/report, and obtain all proof for that head. No formatting result on an altered tree proves the old pin. Do not force-push.

Seed: `18622C0DE`. Use clean separate checkouts for candidate and fail-before work. `-Ptests.rerun=true` forces fresh execution with a fixed seed. Save logs and fresh XML after each leg before another invocation can overwrite output.

## Required positive proof

At the candidate pin run:

```bash
./gradlew :lucene:join:test --tests org.apache.lucene.search.join.TestBlockJoinSelector --tests org.apache.lucene.search.join.TestBlockJoinSorting -Ptests.seed=18622C0DE -Ptests.rerun=true
./gradlew :lucene:join:test -Ptests.seed=18622C0DE -Ptests.rerun=true
```

Both focused suites and the full join module must pass with zero failures/errors and no new skips. They cover both selector consumers, all iterator entry points, mixed access, real sort collection, exclusion filters, real missing children and independent parent missing policies. The shared iterator rewrite justifies the full module.

After positive focused/module proof, run `./gradlew check -Ptests.seed=18622C0DE -Ptests.rerun=true` as required by Lucene's contribution guidance. Do not silently replace it with check excluding tests or introduce exclusions. Report an incomplete root run as incomplete, with exact tasks completed; it is not a green full-check result. Perform the guide's staged-file prek checks if available in the host workflow, recording the actual tool/version and result.

## Required fail-before proof

Create a separate disposable checkout at the candidate head, retain the two test files unchanged, and restore only:

`lucene/join/src/java/org/apache/lucene/search/join/ToParentDocValues.java`

from base `aa366ae209ae8296dd70d2835c6d47f8fc201972`. Record the exact restoration diff and blob identities. No new test relies on a new public API, so no synthetic API scaffold is needed.

Run the following methods individually via `:lucene:join:test`, with the same seed and rerun property:

- `org.apache.lucene.search.join.TestBlockJoinSelector.testNumericMissingValuesRespectChildFilter`: must fail on an assertion that an excluded sibling does not supply the numeric sentinel.
- `org.apache.lucene.search.join.TestBlockJoinSelector.testSortedMissingValuesAreAbsent`: must fail because the old iterator reports a missing-selected parent as having an ordinal.
- `org.apache.lucene.search.join.TestBlockJoinSorting.testFilteredChildMissingValueNestedSorting`: must fail on parent result order, with the excluded sibling affecting selection.
- `org.apache.lucene.search.join.TestBlockJoinSorting.testChildMissingValueNestedSorting`: must fail at runtime in the added MAX/string-missing selection scenarios (invalid sentinel lookup), rather than compile/setup failure.

Retain log, exit code and fresh XML for each negative leg. Do not count missing APIs, compilation errors, startup failures, skips or unexpected passes as regression proof. The changed existing selector assertions are bug corrections; the four named methods provide the independently identified regression failures.

## Failure policy and closeout

Only one documented setup repair/relaunch is allowed when the first attempt never executed tests. An executed test failure requires investigation before retry; record actual evidence and whether the implementation, test expectation or host setup is responsible. Do not change assertions merely to obtain green. Source repairs require a normal pushed commit, separate remote SHA check and superseding gate before positive results apply to the new head. No broad exclusions or disabled checks.

The receipt must report each positive suite/module/root task, tidy and negative method separately, exact heads, JDK/wrapper, command lines, counts, exits and fresh log/XML paths. If execution cost changes are observed, report them; the patch retains conjunction skipping for numeric null missing values and strings where missing cannot win. No new bitset materialization is intended.

Do not claim Solr 10.6 integration or maintenance backport proof from this JDK 25 main run. That is a separate phase after the main fix is verified. Until all required proof is complete, status remains IMPLEMENTED, PROOF PENDING. Public PR submission belongs to Nick; the final receipt provides technical facts for his review, not AI-generated maintainer prose.
