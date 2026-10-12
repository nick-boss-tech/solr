# Moved-heads draft re-check, round 1

Verdict: **PATCHED AND RE-CHECKED for three drafts. NO DRAFT ON RECORD for three tickets.** Each patched draft was checked a second time against its receipt by a fresh subagent and came back CONVERGED UNCHANGED. Two claims in the SOLR-17731 draft are not in its receipt and are left as notes for the main side.

Scope: `assignments/pool-moved-heads-draft-recheck-round-1.md`. Claim: `claims/pool-moved-heads-draft-recheck-round-1.md`. The six branch tips were confirmed by ls-remote at claim time (2026-10-12T14:38Z), and all six match the heads the assignment names. Patches are committed in `17e9996c7bc`. Pass 1 used three read-only subagents, one per draft. Pass 2 used three fresh read-only subagents on the patched drafts. Nothing was built, gated, posted or edited on any branch.

## SOLR-11939, `pr-drafts/core-admin/SOLR-11939.md`: PATCHED AND RE-CHECKED

Head `cdb9903506e9` and base `cabedd1d968` were checked link by link. The changelog link points at the head, and the file exists there.

- **Limits summary (line 27).** Changed "ADDREPLICA reads `property.name` as the new core name" to "ADDREPLICA uses `property.name` as the core name when `name` is blank". The receipt records the fallback as applying only when the name is blank.
- **AddReplicaCmd link (line 29).** Changed the range from L355-L357 to L351-L356. At the base, line 355 is the blank check and line 356 is the fallback.
- **Kept as written.** "verified 2026-10-12" (line 21). The receipt's lower main-side note is dated 2026-10-12 and records the green gate at the packaged head.
- **Pass 2.** CONVERGED UNCHANGED.

Notes for the main side (not patched):
- The receipt still holds an older "Status: NO GATE" block dated 2026-10-06 for head `d4cff5e` (receipt lines 27 to 33). It conflicts with the top section. The draft does not cite it, so the draft is unaffected, but the receipt should mark that block as superseded.
- The "follow-up" wording in the Limits (line 38) could name a follow-up ticket and PR, as the formula's offer does. This is optional.

## SOLR-17731, `pr-drafts/core-admin/SOLR-17731.md`: PATCHED AND RE-CHECKED, with two notes

Head `18165d35a7e3` and base `e2cdb2d7e8ae` were checked. The base is confirmed as `git merge-base 18165d35a7 upstream/main`. The changelog file exists at the head, and the test files exist there.

- **Proof bullet (line 23).** Replaced "On the base code, both tests fail with HTTP 405. With this change, the alias GET returns the alias ..., and the snapshot POST creates a snapshot ..." with "On the base code, `V2ResourcePathOverlapTest` fails with HTTP 405 (2 tests, 2 failures). With this change, both classes pass." The receipt records a base run only for `V2ResourcePathOverlapTest`, and it records both classes at 2 of 2 passing at the head. The removed behaviour sentences were not recorded in the receipt.
- **Pass 2.** CONVERGED UNCHANGED.

Notes for the main side (not patched, because they are not in the receipt):
- **Line 16, "The lookup logic is the same as before."** A code claim the receipt does not record. Verify it against the code at the base and the head, or delete it.
- **Line 39, "The alias response still uses the existing wire key `name` for the alias field."** A code claim the receipt does not record. Verify it or delete it.
- The receipt's delta statement (for the fix commits `1baea8f9b8` and `18165d35a7`) omits a tidy rewrap of `GetAliasByNameApi.java` that the receipt's own lines record. This does not change the draft, but the receipt should carry the rewrap in its delta statement.
- If the behaviour sentences removed from line 23 are wanted back, they need a test-body check or a receipt entry first.

## SOLR-16437, `pr-drafts/solrcloud/SOLR-16437.md`: PATCHED AND RE-CHECKED

Head `f72a9edd0a8b` and base `e2cdb2d7e8ae` were checked, and the fork tip matches the head.

- **Line 23.** Changed the short head from "f72a9edd0a6f" to "f72a9edd0a8b", which is the correct 12-character prefix of the head. Changed the date from "recorded 2026-10-10" to "recorded 2026-10-12", which matches the gate heading of the top receipt section.
- **Counts.** 25 tests, 1 skipped, 0 failures at the head. 25 tests, 1 skipped, 1 failure at the base. Both match the receipt.
- **Pass 2.** CONVERGED UNCHANGED.

## SOLR-6711, SOLR-17612 and SOLR-16013: NO DRAFT ON RECORD

Checked by filename and content search of `pr-drafts/` at the live tip. No draft exists for any of the three tickets. Their receipt facts are the ones the assignment lists: SOLR-6711 at `73c5e6e0d243`, SOLR-17612 at `3a456c27dda5` and SOLR-16013 at `966bb4c4eb44`. These are being commissioned in `assignments/pool-drafts-6711-17612-16013-round-1.md`, which is a separate round.

## Checks that held

- Every head SHA and link in the three drafts uses the right commit, and no pre-fix head remains in a link.
- No em dash or en dash in any of the three drafts.
- No internal process words in any patched line.
- The AI header and the AI assistance footer are present.

## Convergence line

PATCHED AND RE-CHECKED: SOLR-11939, SOLR-17731 and SOLR-16437. Each returns CONVERGED UNCHANGED on the second pass. NO DRAFT ON RECORD: SOLR-6711, SOLR-17612 and SOLR-16013. Open notes for the main side: the receipt's stale NO GATE block for SOLR-11939, the two unrecorded code claims and the receipt delta gap for SOLR-17731.
