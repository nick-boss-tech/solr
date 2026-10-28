# Gate: SOLR-18601 cleanup repair, superseding round 4 at 45dbc3a

Status: OPEN. Immutable once posted. Supersedes `gates/pr-5253-cleanup-repair-round-3.md` (board `7f93a5fa3a9`) for proof at the exact head below. Round 3's requirements apply unchanged except the head values named here.

## Why superseded

Round 3's preflight tidy at I `6a99e731dadd4c3004b46e2bb42a0e6cad59cfc4` exited 0 but rewrote one tracked file, so I is not a clean tidy head and no proof is counted at I.

Tidy change (whitespace-only; verified: tokens identical with all whitespace removed): `solr/core/src/test/org/apache/solr/cloud/MigrateRouteKeyTest.java`, four line-wrap hunks.

Repair commit: `45dbc3ac0b21785f9d0f41112ee6dea98ee55394` on `nick-boss-tech/solr:solr-18601-submit`, parent I. Author Nick Shanin `<nick.boss.us@gmail.com>`, subject `SOLR-18601: apply tidy formatting to migration cleanup repair`, no attribution trailers. Plain fast-forward push; live ref read back as `45dbc3a...`.

## Exact heads

- Positive head J: `45dbc3ac0b21785f9d0f41112ee6dea98ee55394`.
- Previous head I: `6a99e731dadd4c3004b46e2bb42a0e6cad59cfc4` (not proof).
- Original PR head O: `32b91612edde057c19bf1a7963e8276b816551df`, unchanged.
- Negative C base: H `98d1127e4f5d4d149992e1dc494b95928bac057a`.

## Requirements

Use round 3's legs, commands, seeds, counts, assertions, negative-leg preparations and completion rules verbatim, with J in place of I. The negative legs overlay J's versions of `TestInjection.java` and `MigrateRouteKeyTest.java` (A), J's `MigrateRouteKeyTest.java` (C), and apply the B mutation at J. Recorded synthetic trees, hashes and diffs must name J.

Clean state, tidy, and remote read-back are checked at J. Any further change requires another pushed head and superseding gate.
