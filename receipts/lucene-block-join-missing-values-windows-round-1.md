# Lucene block join repair: Windows verification receipt

Status: PASS for the required Windows proof at the exact candidate below. Final root validation finished 2026-11-05 04:57:45 UTC.

## Identity and authority

- Repository: [nick-boss-tech/lucene](https://github.com/nick-boss-tech/lucene), fork of apache/lucene.
- Branch: `fix-block-join-missing-values`.
- Candidate: `55c3174bd154b5cc987d9b24e1c77043e23fe71e`.
- Base: `aa366ae209ae8296dd70d2835c6d47f8fc201972`.
- Gate: `gates/lucene-block-join-missing-values-windows-round-2.md`, superseding Windows round 1 and the original Linux route.
- Claim: `claims/lucene-block-join-missing-values-windows-proof-round-1.md`.
- Execution host: Windows 11 amd64, Codex desktop. Nick explicitly authorized necessary Lucene Gradle execution here and requested keeping this testing off the Linux boxes. The earlier Windows ban is overridden for this Lucene task only; no Solr queue or Solr Gradle run was used.

## Runtime and reproducibility

Portable Temurin JDK `25.0.5+8`, obtained from Adoptium's official release metadata. The downloaded Windows x64 archive's SHA-256 was checked before extraction: `636bdc13dc649ddc7d5921c5c7d23cca6e44fd19d662b45cc9c02135a6c96c42`. Gradle `9.8.1` came through this checkout's checksum-verified wrapper. The JDK is under `research/lucene-windows/tools/jdk-25.0.5+8`; metadata/URL are retained in `tools/jdk25.json`.

All builds were serialized by `research/lucene-windows/build.lock`, with `--max-workers=2`. Tests used seed `18622C0DE` and `-Ptests.rerun=true`. The runner supplies the exact `--project-dir`, explicit Python/Perl tool paths and JDK, and records full arguments in each run.json. Candidate checkout: `C:\Users\shaninna\dev\Solr-issues\lucene`; fail-before checkout: `C:\Users\shaninna\dev\Solr-issues\wt\LUCENE-BEFORE`.

## Positive proof at the candidate

| Leg | Result | Fresh artifact directory under research/lucene-windows/results |
| --- | --- | --- |
| Focused TestBlockJoinSelector and TestBlockJoinSorting | PASS, rc 0; 10 tests, 0 failures/errors/skips | `focused-20261105-040412` |
| Full join module | PASS, rc 0; 107 total, 104 passed, 3 skipped, 0 failures/errors | `module-20261105-040929` |
| Exact-head join tidy | PASS, rc 0; no source changes | `tidy-join-20261105-042619` |
| Root check excluding tests | PASS, rc 0; BUILD SUCCESSFUL in 29m 38s; 530 tasks, 513 executed and 17 up-to-date | `root-checks-20261105-042751` |

The three skips are pre-existing @Nightly tests in TestBlockJoin and TestBlockJoinBulkScorer; the nightly group was not enabled. None of the changed test methods was skipped. The module produced 16 fresh suite XML files; the focused run produced two. The root invocation is `check -x test`, not the full repository's runtime test suite. Error Prone is disabled by the project's default non-CI configuration, and eclint is not configured; those are not claimed as passed checks.

Final candidate status is clean after root validation; the aggregate base-to-candidate `git diff --check` passes. The fork ref was independently read back as the exact candidate SHA above, and upstream main remained the pinned base. The Solr PR remained open at `978e2b1e352c7c39a68f7b952e6ab12eabf80890`, with empty conversation comments and reviews at the final refresh. No new source change was required during root validation.

## Runtime fail-before proof

Each run retained the candidate tests unchanged and restored only ToParentDocValues.java from the base. The restored production blob is `8ba6b0e01b433deb8a8e3f03eb7bd9f226bc3e58`. Test blobs matched the candidate exactly: Selector `765d40145d96bc266f0aeb0af15872ab08c1761c`, Sorting `66ce27705dadf2bec196182c6d309a0a86621ee3`. `baseline-integrity.json` records this, and each negative run retains the full source diff. After all four runs, the disposable source mutation was restored to the candidate, leaving that checkout clean.

| Method | Observed runtime failure on original production | Artifact directory |
| --- | --- | --- |
| TestBlockJoinSelector.testNumericMissingValuesRespectChildFilter | Assertion: expected 7, got missing sentinel -5 | `negative-numeric-20261105-041250` |
| TestBlockJoinSelector.testSortedMissingValuesAreAbsent | Assertion: expected valued parent doc 5, got missing-selected doc 3 | `negative-sorted-20261105-042248` |
| TestBlockJoinSorting.testFilteredChildMissingValueNestedSorting | Assertion: expected parent doc 7, got doc 2 | `negative-filter-sort-20261105-042402` |
| TestBlockJoinSorting.testChildMissingValueNestedSorting | IndexOutOfBoundsException in TermsDict.seekExact through SortedDVs.lookupOrd and TermOrdValComparator.copy | `negative-string-sort-20261105-042509` |

Every negative leg exited 1 with exactly one executed test, one failure, zero errors and zero skips. These are actual runtime failures, not missing APIs, compilation or setup errors. The same methods pass with the repair. Additional paging, parent-policy, iterator-access and multi-valued compatibility assertions pass on the repair; they are not all independently mutation-proven merely because these four failures discriminate the core defects.

## Repairs and earlier non-runtime outcomes

The first tidy command omitted the project directory and failed before any build/test work; the runner was corrected to pass it explicitly. Full root tidy then passed. The first focused attempt at `a9ce0a45074ac2708c2c0ade7cc67162870d2e88` failed test compilation because the four-argument searchAfter returns TopDocs. The typed five-argument overload repair was formatted, committed normally and pushed as the final candidate above; all positive and negative runtime evidence is at that final test head. Neither earlier failure is counted as a test result.

## Adjacent review and scope

The bounded follow-up traced ToParentBlockJoinSortField's numeric/string consumers and the SortedNumericSelector/SortedSetSelector adapters, including repeated/false advanceExact calls, nextDoc/advance transitions, missing-only parents, empty/no-matching-child blocks and independent parent missing-value policies. It found two useful coverage additions, now pushed: searchAfter pagination past missing/real string keys and genuine multi-valued numeric fields with both explicit missing sentinels and null/skip behavior. No further production defect was established in that pass.

The patch adds no public API, dependency or new fixture. The existing conjunction path is retained where missing values cannot affect selection. Paths that need missing-value semantics examine selected children rather than inferring missingness from physical block size. No performance benchmark is claimed.

One existing documentation ambiguity is preserved: the constructor Javadoc broadly describes null childMissingValue as skipping missing children, while the existing string test explicitly uses null to request missing-first MIN selection. This repair preserves that tested string behavior and the independent parent policy; it does not decide a new null-string policy. Clarifying that wording is a separate follow-up, not an additional production change established by this review.

This proves the repair on the pinned Lucene main source with JDK 25. It does not prove a maintenance backport or Solr integration with patched Lucene 10.6/JDK 21. The earlier Solr receipt's 10.4 comparison remains separate evidence: filtered-child ordering regresses in the upgraded stack; string missing-first changes from ordering failure to a collection exception; numeric missing-policy behavior improves; string missing-last passes on both.

## Publication and ownership

No upstream PR, issue or maintainer comment was created. Lucene's AI_POLICY.md permits coding assistance but requires human understanding/ownership and human-written public communication. This receipt is an internal technical record for Nick's review, not ready-to-post PR prose. Nick can submit as a contributor; maintainers handle review, merge and any maintenance backport/release.
