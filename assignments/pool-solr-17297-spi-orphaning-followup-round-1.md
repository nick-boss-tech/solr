# Pool assignment: SOLR-17297 follow-up, module SPI orphaning, round 1

Status: DONE (vm5, 2026-10-22: probe confirmed; fix on solr-17297-spi-followup at 46a5476bf87, gate green; see gates/SOLR-17297-spi-followup.md)

- Posted by: vm5, 2026-10-22, from `reports/owner-items-scrub-2026-10-22.md` (row SOLR-17297, rung 4).
- Source: `reports/newdev-critblock-review-round-1/solr-17297-submit.md` (finding, major) and `receipts/SOLR-17297.md` (round-35 probe, CONFIRMED BY RUN at head `c0ab38fc0a`).
- Capability tags: implementation, gate.
- Intended host: any pool Linux host with the Gradle toolchain.
- Claim: `claims/pool-solr-17297-spi-followup-<slice>.md` before any change. First post wins.
- Branch for the fix: a new branch `solr-17297-spi-followup` on the fork, based on `solr-17297-submit`. Do not change `solr-17297-submit`; it stays as gated.

## Why this is a follow-up and not part of the shipped fix

The shipped branch fixes the ordering for lib plugins: module-stage initialization now runs before the shared-lib step. The residual is the same mechanism in the other direction. A module SPI provider loaded in the module stage is orphaned when the shared-lib step later closes the module classloader. The shipped branch's tests do not pin this, and a green gate does not show it is fixed. The fix changes the loader design, so it is kept out of the shipped branch (rung 4 choice, recorded in `reports/owner-items-scrub-2026-10-22.md`).

## Step 1: re-run the probe (rung 2, required first)

The round-35 probe log `g17297r35-probe.log` is not on this host or on coordination, so the claim is not yet proven again. Recreate a probe test on `solr-17297-submit` at `c0ab38fc0a`, not committed to the branch:

- Put a module that declares an SPI provider on the module path, with a class the provider depends on that loads only after the shared-lib step.
- Assert the provider is still usable after the core is loaded.
- Record the result in the claim with the log path.

If the probe passes, the residual is not confirmed. Record that and stop. Do not build the fix.

## Step 2: fix (only if the probe fails)

Rung 4 choice, recorded before the change: stop closing the module loader while it is still in use, so the shared-lib step does not orphan module classes. Option (b) in the review finding. Option (a), one combined add before the shared-lib step, is the fallback if (b) breaks an existing loader test.

- Add a test that fails on `c0ab38fc0a` and passes on the fix (rung 2 mutation).
- Gate with tidy, compile with `-Werror`, `:solr:core:test` for the loader tests, and `CoreContainerTest` or the matching class.

## Done when

The probe result is in the claim and a receipt on `receipts/SOLR-17297.md` names it. If the fix is made, its gate is at the new head and its branch is named in the receipt.
