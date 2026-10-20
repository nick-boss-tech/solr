# Gate job: SOLR-18458 review pass 1 (vm4)

Status: DONE. GATE GREEN at head `344485485fb8e1706a1e04c02e373d8a97bcd1b8` (branch solr-18458-submit, base `c8ab71430f`), 2026-10-20T14:48:34Z.

- Claim: `claims/pool-newdev-critblock-review-round-1-solr-18458-submit.md`. Round: `assignments/pool-newdev-critblock-review-round-1.md`, unit 3.
- Host: vm4. Logs: `gates/logs/vm4/18458/` (`pass1.log` has the step return codes; `step2-errorprone.log`, `step4-core.log`, `step4-solrj.log`, `step4-ltr.log`, `step4-langmodels.log`, `step5-*.log`, `step1-tidy.log`, XML copies in `xml/`).
- Worktree: detached at the head; removed after closeout.

## Steps

- Step 0 changelog: rc 0 via uv (`step0-uv.log`).
- Step 1 tidy: rc 0, tree clean (`step1-tidy-status.txt`).
- Step 2 Error Prone compile, four modules main and test: rc 0.
- Step 3 proof leg: not run in pass 1 (the round 3 receipt holds it).
- Step 4 focused head run, seed `18458C0FFEE18458`, counts from fresh XML: core 30 classes / 303 tests (2 skipped) / 0 failures; solrj 1 / 2 / 0; ltr 15 / 91 (1 skipped) / 0; language-models 1 / 23 / 0.
- Step 5 module checks `check -x test`: rc 0 for all four modules.

## Failed attempts (runner, not test results)

1. Step 4 in one combined Gradle call with `--tests` for all four modules: the fail-on-no-tests guard stopped it (`step4-focused.log`).
2. Step 4 per module with the class list built without the `org.` prefix: the guard stopped each module (`step4-core.log` and the others, first rerun).
Both were fixed before the green run. No test failed in any attempt.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Regate with no change | decided: run it | the pass regate is its sign-off | Nick Shanin |
| Repairs | none in pass 1 | nothing in repair scope was needed | vm4 (review pass 1) |

## Checklist

- [x] head confirmed by ls-remote (`344485485f`)
- [x] diff scope confirmed (60 files)
- [x] step 0 changelog parse (uv)
- [x] step 1 tidy clean
- [x] step 2 Error Prone compile
- [ ] step 3 proof leg (not in pass 1)
- [x] step 4 focused head run, counted from fresh XML
- [x] step 5 module checks
- [x] receipt written (top section, pass 1)
