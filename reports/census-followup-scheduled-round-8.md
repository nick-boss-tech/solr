# Census follow-up, scheduled round 8 (a review of the SOLR-16630 fix chain, and the SOLR-3923 premise result)

- Trigger: the two-hourly scheduled check. Recorded tip `ec03f014b7f`; live tip at the check `82e64cd9330`. Claim: `claims/pool-census-followup-round-8-scheduled.md` (pushed at `f26d2c2bf57`).
- The new commits before the check were records: the SOLR-3923 base premise run (39 tests, one failure in `testPfPs`), and three SOLR-16630 receipt commits recording the node-name fix and the forbidden-apis fix.
- Fork tips checked by `git ls-remote origin`: `solr-16630-submit` at `c9caf1e4b8`; `solr-3923-submit` at `723f61ee4a`.
- Read only for branches: no build, test, Gradle or gate run; no commit or push on any `solr-*-submit` branch; no GitHub write; no receipt, gate file, claim, PR or live body edited.
- Written by the parent from the two subagent replies. The subagents could not write report `.md` files.

## Result

- 2 tasks: 1 review and 1 draft update. No gate jobs filed.

## SOLR-16630: review of the fix chain (read only)

**Heads and ancestry.** `136145b598d` (the all-sleeps conversion) is an ancestor of `fcd94a443c` (the node-name fix), which is an ancestor of `c9caf1e4b8` (the forbidden-apis fix). Each commit touches only `TestCoordinatorRole.java`, with the receipt's line counts. `Thread.sleep` count is 0 at all three heads.

**What each gate record states**
- **Conversion, `136145b598d`:** GATE FAILED, from four XML runs (NPE at line 364, which matches the file). No module-check result is stated. Its heading still says "gated green on the pushed head".
- **Node-name fix, `fcd94a443c`:** the heading says "gated green before pushing". The body says the module check FAILED (`forbiddenApisTest`, line 619), and that the head was pushed with its test runs green and its check leg red. The heading and the top status line contradict the body.
- **Forbidden-apis fix, `c9caf1e4b8`:** tidy clean, Error Prone and the module check BUILD SUCCESSFUL, four 9 of 9 XML runs, and a runner failure flag of 0, each with a named file. The file matches: no `currentTimeMillis`, `new Date()` at line 619. No correction in this section.

**Correction history** (commit author dates, in order)
1. `5eb510e8b4d`, 15:52Z: withdrew the posted reply (comment `6171998479`, posted 15:31Z) as the standing position. The owner deleted it later on the same day; the record gives no clock time.
2. `d5b2be3a78f`, 16:35Z: withdrew the conversion's green account (`f313b72`, 16:32Z, citing runs from 16:40Z to 17:08Z). Now: GATE FAILED, from XML mtimes 16:14Z to 16:20Z.
3. `4b6b4482a25`, 17:16Z: withdrew the node-name fix's "module check green" timeline (`b05aea6`, 16:52Z, citing runs from 17:44Z to 18:06Z). Now: the 16:35Z to 16:52Z log, with the check leg FAILED.

Both withdrawn green accounts were committed before the run windows they cite.

**Still contradicting the record**
- The node-name fix heading and its top status line.
- The conversion heading ("gated green on the pushed head").
- The node-name section's claim that the conversion's check leg "failed the same way", which the conversion section does not support.
- The stale top bullet, "Live head: 9c77a4db09d7".

**Live PR and draft**
- The live PR head is `c9caf1e4b8`, matching the fork tip. The live body names `c9caf1e4b8` as the current head, and its claims match the forbidden-apis section. The base links point at `3f5d4c5bf8`. The body was updated by someone other than the review side.
- The draft `pr-drafts/flaky-fixes/SOLR-16630.md` names `9c77a4db09d7` (code links, changelog link) and `9bea59741ac` (the six runs of 2026-10-10). It does not match `c9caf1e4b8` and predates the conversion. It is not edited here, because the scheduled rules bar edits to live PR text.

## SOLR-3923: premise result and draft update

- **Failing check.** The premise run (`gates/SOLR-3923-premise.md`, run 2026-10-14, seed `3923C0FFEE3923`) recorded 39 tests, 1 failure, 0 errors, 0 skipped, in `testPfPs`. Check (a) failed: `//str[@name='parsedquery'][contains(.,'phrase_sw:"zzzz xxxx"')]`. The base parsed query is `DisjunctionMaxQuery(((phrase_sw:"? ? zzzz xxxx")^10.0))`. Check (b) was not reached. The record does not quote the JUnit assertion text, so the draft states only the check and the parsed query.
- **Head.** The run used `723f61ee4ab7438220193db0c39dad32601dfe07`, which matches the job header and the live tip. The base `cabedd1d968` matches the local merge-base.
- **Draft.** `pr-drafts/edismax/SOLR-3923.md`: the base claim now cites check (a), the parsed query, the run record, the date and the full head. Two examples that no record supports are removed, and pf, pf2 and pf3 are narrowed to pf. The HOLD banner stays. Its step, in plain words: a branch fix for the signed-parenthesis gap (`+(` and `-)`), then a new focused gate at the new head, then the premise run re-read against the fix. No fix is claimed.
- **Flags.** The changelog title's pf2 and pf3 wording is not shown by any run. The record paths in the Proof are internal, and they should be dropped before anything is posted.

## Owner decisions and items this round raises

1. **SOLR-16630, the live PR head `c9caf1e4b8`.** Its record has four contradictions (listed above). The main side should clean up the heading, the status line and the stale top bullet. The draft `pr-drafts/flaky-fixes/SOLR-16630.md` needs updating before any body change.
2. **SOLR-16630, process.** Three green accounts were committed before the runs they cite and then withdrawn. Two heads were pushed before their gate passed. The rule recorded at `8e5239f1025` covers maintainer requests, not pushing ahead of a gate. The owner should decide whether that needs a rule.
3. **SOLR-3923.** The branch fix for `+(` and `-)` is the next step, and it is the owner's call to schedule it. The changelog's pf2 and pf3 wording needs a run that shows it.

## Still open from earlier rounds

- SOLR-17215 and SOLR-17393: satisfied; over the length guide, so trim or waive.
- SOLR-15712: HOLD on the corrected test (queued, not run).
- SOLR-16887: BATS job re-issued after a runner-defect classification.
- SOLR-8954: owner decision 1.
- SOLR-14200 and SOLR-15326: retire or keep.
- SOLR-17433: retire or ship.
- SOLR-10390: withdraw as superseded, or keep the test as a guard.
- SOLR-3684: submit or retire.
- SOLR-17987: confirm the rename is deferred to opening.
- SOLR-9968: confirm the top-up gate ran at `c31d2ff1a3f`.
- SOLR-14231: vm2 to restate the counts and add the fail-before line.
- SOLR-4754: HOLD from round 1.
- SOLR-8628 and SOLR-10234: opening held on gates and decision 8.
- SOLR-3865: owner decision 8.
- SOLR-13838: a fail-before run and the ticket text are owed.
- SOLR-14098: the ledger date conflict is with the main side.
- SOLR-18278: owner retire call.
- SOLR-15906: the vm2 gate on the sort-paren reading is OPEN.
- SOLR-10641: ship-alone against the DISCUSS item 11 in the answers file.
- SOLR-17292: receipt reconcile.
- SOLR-17752, SOLR-12505 and PR #5010: owner-brief mismatches from round 7.
