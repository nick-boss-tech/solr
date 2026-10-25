# Assignment: accurate allowPaths assertion for the BATS security manager test (round 1)

Status: OPEN
Tags: implementation, gate
Intended host: a host that can run the packaging BATS tests locally (the solr/bin and BATS class stays on a Linux host with the local toolchain). One claim covers this round.

## Context

apache/solr PR 5225 (dsmiley) fixes `test_security_manager.bats` for Java 24+: the `allowPaths - backup` case now accepts `access denied|Could not backup all shards`. A review posted for the owner on that PR (2026-10-24T21:39Z) found that the generic wrapper text can pass on an unrelated backup failure, so on Java 24+ the test no longer proves the forbidden path was rejected. Smiley replied (2026-10-24T22:09Z) that a more accurate test is fine if the owner wants to make one. This assignment prepares that follow-up; it does not touch PR 5225 itself.

## Work

1. Read `CoreContainer.assertPathAllowed` and the backup path to pin the exact path-specific evidence the Java 24+ rejection leaves: the exception text and where it is logged (the PR body notes the specific reason goes to solr.log while the API reports only the generic wrapper).
2. On a branch cut from the PR 5225 head (so the follow-up diff sits on top of his change), rewrite the final assertion of the `allowPaths - backup` case: keep the denial-specific `access denied` assertion where the Security Manager is supported (Java before 24, per how `bin/solr` decides), and for Java 24+ require the path-specific evidence from step 1 (the log line naming the forbidden location, or the API error detail if it carries it), not the generic wrapper.
3. Run the test file locally on JDK 21 and record the result; the Java 24+ leg cannot be executed on any factory host (the fleet runs JDK 21), so that leg is verified by code reading only and the PR description must say so plainly.
4. Deliverables: the branch on the fork, a gate-style report at `reports/bats-5225-accurate-assertion-round-1.md` with the run output and the exact evidence text used, and a PR description draft at `drafts/pr-bats-5225-followup-draft.md` that credits PR 5225 and states the verification limit. The follow-up PR opens only after PR 5225 merges, and only on the owner's go.
