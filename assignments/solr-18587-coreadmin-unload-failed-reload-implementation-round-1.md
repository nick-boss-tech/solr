# Assignment: SOLR-18587 fix, CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure (implementation, round 1)

Status: OPEN
Tags: implementation, premise-run, records
Intended host: any pool Linux host with the `implementation` tag (per its `hosts/` file).

Owner direction (2026-10-25): the RC3 failures must be fixed, the fixes applied to RC3, and the result run through the smoke tester. This item is one of the two without a fix. Bring the branch back to the owner for gate; the gate itself is not this assignment.

## Facts (from vm5's round, do not re-derive)

- Failing test: `CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure` (`solr/core/src/test/org/apache/solr/handler/admin/CoreAdminHandlerTest.java`, lines 403 to 445). It reloads a core with a bad `solrconfig.xml`, unloads with `deleteDataDir=true`, then asserts the data directory does not exist.
- Reproduces at the RC3 tip `749712b71940c9f96d824c533f32f5c56dcd3924` and at apache/main `13c693d805f`, seed `787C6229591C6F1E`: "The data directory was not cleaned up on unload after a failed core reload" (`/workspace/smoke-rc3/coreadmin-repro/rc3-tip-run1.log`, `main-run1.log`; archived XML under `/workspace/smoke-rc3/repro-archive/coreadmin-2026-10-25/`).
- Lead, from code (rung 1, unconfirmed): `CoreContainer.reload` (about line 1961 to 2030) puts a failed reload into `coreInitFailures` but leaves the old core loaded in `solrCores`. `unloadWithoutCoreOp` (about line 2084 to 2095) then takes the `coreInitFailures` branch, calls `SolrCore.deleteUnloadedCore` with the re-read descriptor's data directory, and returns before the loaded core is removed and closed. Whether the two data directory paths differ is the question the probe answers.
- Related ticket: SOLR-9433 (Reopened), the adjacent clean-up path. Not in this item's scope unless the probe shows it is the same path.

## Work

1. Base: RC3 tip `749712b71940c9f96d824c533f32f5c56dcd3924`, scratch worktree, host lock, JDK 21.0.12.1 (or the host's equivalent; record the version).
2. Probe first (rung 2): instrument the unload branch and print the two data directory paths and which branch ran; run the one method at seed `787C6229591C6F1E`. Record the probe diff and the output in the report. The probe is scaffolding and is not pushed.
3. Fix only what the probe supports, on a branch named `solr-18587-coreadmin-unload-failed-reload` off the RC3 tip, pushed to the fork (nick-boss-tech/solr). Keep it small. A regression assertion is already in the test; add nothing that is not needed.
4. Matched pair (owner's rule, `assignments/rc3-proof-pair-requirement-round-1.md`): the base leg is the RC3 failure at seed `787C6229591C6F1E` (already recorded above). The fixed leg is the same method and the same seed on the fix branch. Use the same seed. Record both legs with logs and XML paths. Archive each run's XML to a per-run directory before the next run in the same worktree (the TLOG XML was lost that way earlier).
5. Report: `reports/solr-18587-coreadmin-fix-round-1.md`, with the probe result, the root cause (or "not established" with the next step), the diff, the matched pair, and the branch name and head SHA.

## Rules

- Never force-push. Fork branch only. No PR, no JIRA or GitHub comment, no dev-list text (opening a PR needs the owner's approval).
- One host lock at a time. Run builds detached, with `flock /workspace/tools/.gradle-vm5.lock` if you are on vm5; on another host use its own lock.
- Record each decision with its rung (`material/decision-ladder.md`). Ask the owner (rung 5) only for a question the ladder cannot settle.

## Deliverables

- `reports/solr-18587-coreadmin-fix-round-1.md`
- The fix branch `solr-18587-coreadmin-unload-failed-reload` on the fork, with its head SHA written into the report.
