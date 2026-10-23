# Pool assignment: failure atlas round 1

Status: WRITTEN, awaiting second-host confirmation (claims/pool-vm5-failure-atlas-round-1.md; classes 1 and 6 not entered)

Owner direction, 2026-10-19: red builds get classified before they get retried. This assignment writes `pool/guides/failure-atlas.md`: the failure classes the pool has already paid for, each with its signature, its first checks, its classification rule, and what to record. Entries are verified against real logs, gate files, and records on this branch; a class with no instance on record does not get an entry yet.

## Seeded classes (verify each against its instance, then write the entry)

1. **Step miscount.** A gate log's failures totalled across steps, so the pre-fix proof step's expected failures were reported as head failures. Instance: the SOLR-6045 round 2 summary, corrected on the record. Rule: quote per-step numbers only.
2. **Run-shape failure.** A check fails identically at the branch head and at the base, so it is the run's shape, not the change. Instance: the ref-guide `buildLocalAntoraSite` failure in a linked worktree, verified at head and base for SOLR-18555. Rule: base comparison before classification.
3. **Environment refusal.** The run never executed tests (disk guard, cache lock, queue starvation) and produced no test evidence. Rule: an environment refusal is recorded as such and may be relaunched once; it is never recorded as a test result.
4. **Wrong-name dispatch.** A run reports "No tests found" because the package or the module path was guessed. Instances: the four guessed-package dispatches of 2026-10-05. Rule: grep the exact package and project path from the branch before dispatch.
5. **Seed-dependent test defect.** A failure that comes and goes by seed, where the production code is innocent and the test's own synchronization is the defect. Instance: the SOLR-13059 diagnosis (multi-seed runs, overseer timeline). Rule: reproduce across seeds and read the timeline before touching production code.
6. **Harness hang.** A CI job produces no test output and hangs in the harness. Instance: the settled GCS harness classification on PR #5029. Rule: classify by comparing the same classes locally and on the base; the remedy is a re-run, not a code change.
7. **Tidy stop.** A gate stops because tidy reformats branch files. Rule: on our own prepared branches, verify the diff is formatting only, commit it, re-run; anything beyond formatting stops for a human read (the standing rule in the traps guide).

## The entry's fixed structure

Signature (what the failure looks like from the log alone), first checks (in order), classification rule (what evidence decides), what to record (where, in what words), and the instance (ticket, date, record path).

## Floor

- One claimant builds the atlas; a second host confirms each entry against its cited instance before the assignment is DONE.
- New classes enter the atlas only through a paid instance, added in the same push as that instance's records, following the entry structure. The atlas is a diagnosis tool, not a theory file.
