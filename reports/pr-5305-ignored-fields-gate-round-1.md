# Execution report: PR 5305 ignored-field sibling fixes gate, round 1 (phone agent)

Outcome: GATE GREEN at F 219fee78bf7cae5039e50fe885108a1d4937ccd3. Receipt: receipts/PR-5305-ignored-fields-round-1.md. Execution claim: claims/pr-5305-ignored-fields-round-1-phone-agent.md.

## What ran

- Live verification before claiming: fork ref equal to F; F's tree 0f979d6c070da09f889b829f51c915743df77239; O 00292cd3729126194235e939afe38e351d850c70 is F's direct parent. Public PR 5305 open at O throughout; never written to.
- Detached tidy preflight at F before the claim: root tidy rc 0, no tracked changes, diff-check rc 0 (2026-11-07T18:27:34Z to 18:29:36Z). No repair commit needed.
- Fail-before overlay (seven O implementations restored under F's tests; overlay tree c8e5776b4d03c58e21ac5932ae4db97541d36512; all hashes verified): PrimitiveFieldTypeTest 10 tests with exactly the six named methods failing on NullPointerException at java.util.List.of in the named createFields; TestICUCollationField 9 tests with only testIgnoredField failing the same way at ICUCollationField.createFields; BasicFunctionalityTest.testIgnoredFields control passes at the overlay, as the job expects.
- Positive at clean F: PrimitiveFieldTypeTest 10/10, TestICUCollationField 9/9, neighboring core suites 81 tests with zero failures and one characterized pre-existing assumption skip (TestTrie.testTrieFacet_PrecisionStep, seed-dependent points/docValues randomizer assumption), TestICUCollationFieldDocValues 7/7. Positive total 107 tests, 0 failures, 0 errors.
- Root check -x test: first attempt failed only at :solr:webapp:verifyUiWasm because the egress relay corrupted the solr-ui-wasm.zip download (received checksum fe065385e8 against the pinned 654494303e4a); the zip was re-fetched with curl, verified against the pin, seeded into the build directory, and the rerun passed rc 0 with no added exclusions.
- Final: HEAD/tree unchanged, status clean, live fork ref re-read equal to F.

## Notes for the follow-up decision

- Eric Pugh asked on PR 5305 (2026-11-07) whether the sibling fixes belong in that PR rather than a follow-up. The gate is neutral on that question: F is proven as a standalone head, and its single commit sits directly on O, so the same content can be offered to the contributor for folding into PR 5305 or opened as the follow-up PR after 5305 merges. The receipt carries a factual description draft for the follow-up route.
- The docValues assertions remain preservation coverage pins, not mutation-proven claims.
