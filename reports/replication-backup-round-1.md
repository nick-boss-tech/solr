# Replication and backup round 1: round roll-up

Claim: `claims/replication-backup-round-1.md` (commit `666c10c2e2f`). Assignment: `assignments/replication-backup-round-1.md` (commit `8db87734c3d`). Per-part reports: `reports/replication-backup-round-1-g1.md` through `-g4.md`.

Done 2026-10-10 by Claude Code (AI agent), working for Nick Shanin. Four read-only subagents did the audit. No build, Gradle run, or test was run. No submit branch, live PR, JIRA item, or comment was touched. Nothing was posted.

## Heads

Every named head matches its live branch. The two live PRs were checked read-only. Drafts name the live origin heads.

## Per-ticket verdicts

| Ticket | Verdict | Draft | Live head | What blocks it or what the draft must say |
|---|---|---|---|---|
| SOLR-9865 | Draftable, held | `SOLR-9865.md` | `4937608bb181` | The `TestRestoreCore` count: the receipt says 4, and the head file has 3 `@Test` methods. The test comment at `TestRestoreCore.java` 237-238 describes a mechanism the code does not show. Replace it with "reload the core so the check below reads `index.properties` on a fresh open". Confirm the counts from the gate JUnit XML, which is not on disk |
| SOLR-17287 | Draftable, held | `SOLR-17287.md` | `6957daf82610` | The same `TestRestoreCore` count mismatch (receipt 4, head 3) |
| SOLR-9091 | Audit only; not drafted | none | `e31bdaa4d279` | No gate. The premise holds against the public JIRA and base `RestoreCore`. Owed: a focused proof with a fail-before run, and removal of `SOLR-9091-TESTING.md`. Correct the branch note that `doRestore` rolls back on a corrupt file: that failure happens before the switch |
| SOLR-12246 | Draftable | `SOLR-12246.md` | `3ffc2539ec76` | Choice in the draft: lower the whole "did not match" statement to INFO, or keep WARN when lengths also differ. The `.liv` checksum cause stays a Limits line |
| SOLR-12085 | Draftable | `SOLR-12085.md` | `c8dba5023396` | No choice section. The test is helper-level only, and the replication suite is `@Nightly` and was skipped. The Proof must not claim end-to-end coverage |
| SOLR-11650 | Draftable only after fixes | `SOLR-11650.md` | `e4f5e941cd8e` | The changelog title says the password is hidden in "IndexFetcher log and error messages". Two "Leader at ... is not available" warnings (`IndexFetcher.java` 501-510) and the client's connection error still print the URL with the password. Narrow the title to the three messages the receipt covers. The `URLUtil` javadoc (109-114) and the test comment (154-156) also need correcting. Those edits move the head, so decide whether to re-run |
| SOLR-6711 | Held; audit only | none | `5a120cd69d64` | No gate. The handoff file must be removed. `disablereplication` is not covered. Opt-in persistence is an owner decision |
| SOLR-5589 | Held; audit only | none | `c707aa95e2ae` | The premise holds for leader-only and both-disabled configurations. The follower-only case is untested, and it goes against the ticket thread. `TESTING.md` note on the branch |
| SOLR-9382 | Held; audit only | none | `c0b5fec1be21` | The premise holds for the glob only. The changelog example path is wrong for default standalone storage |
| SOLR-8430 | Draft written, held | `SOLR-8430.md` (HOLD) | `49af21be5892` | Finding 1: the static limiter map in `ReplicationAPIBase.java` 99-110 is keyed by the client-supplied `maxWriteMBPerSec` and never shrinks, so a client can grow it without limit. Needs a capped replacement. Changelog retitle. Owner: one limiter per rate, or one per node |
| SOLR-9598 | Draft written, held | `SOLR-9598.md` (HOLD) | `c8407773f76c` | Owner decision on the `RESTORE` default. The Javadoc wrongly says it follows SOLR-17712. Lands before SOLR-12651: the two merge cleanly |
| SOLR-18249 (live PR) | Consistency: hold the receipt | none | `deffea4c51be` | The receipt's gated head is ambiguous (`c4cd38c` against `deffea4`), and the receipt's gate date predates the tip's commit date. The PR body has process words and two fail-on-base claims no receipt records. Full list in `replication-backup-round-1-g4.md` |
| SOLR-18280 (live PR) | Consistency: the PR is ready as code | none | `0da92abd9e96` | The title says "flaky" where the JIRA says reproducible. The changelog line names a file that was dropped. The body says 25 tests where the head has 24. The receipt's counts are for an older head, and its CI basis overstates coverage. Full list in `replication-backup-round-1-g4.md` |

## Landing order and interactions

- **RestoreCore (9865, 17287, 9091):** the three hunks do not overlap, and pairwise trial merges are clean. 9865's rollback and 17287's clear act on different paths and complement each other. Landing order: 9865, 17287, 9091.
- **IndexFetcher (12246, 12085, 11650, 6711):** no textual conflicts. Landing order: 12246, then 12085, then 11650, with 6711 last.
- **ReplicationHandler and backup (9598, 8430, 5589, 9382):** 9598 lands before SOLR-12651, and the two merge cleanly. The overlap with SOLR-15863 that the assignment named does not exist. The audit-only tickets share one handler, and were read together against the same current code.
- **Live PRs (18249, 18280):** they share no file, so they do not overlap.
- **Cross-filed:** 11650's draft states the SolrJ effect plainly, and 17287's draft states the Update processing effect plainly, since each category's round would otherwise miss it.

## Owner decisions

1. SOLR-9598: the `RESTORE` default.
2. SOLR-8430: one limiter per rate, or one per node; and the capped replacement for the static map.
3. SOLR-11650: narrow the changelog title to the three messages (recommended), and decide whether the comment edits need a re-run.
4. SOLR-6711: opt-in persistence.
5. SOLR-18249 gated head: accept `c4cd38c` with `deffea4` as an ungated Javadoc tip (recommended), or ask for a verify at `deffea4`.
6. SOLR-18249 PR body edits (items 1, 4 and 6 in the g4 report): your call.
7. SOLR-18280 title: JIRA wording (recommended). Counts: record a run at `0da92`, or cut the counts. Changelog line: delete (recommended).
8. SOLR-9091: a focused fail-before proof, and removal of the TESTING file, before opening.
9. SOLR-9865 and SOLR-17287: confirm the `TestRestoreCore` counts from the gate JUnit XML, which is not on disk.

## Draft fixes before posting

- `SOLR-9865.md` and `SOLR-17287.md`: held until the count is confirmed.
- `SOLR-8430.md` and `SOLR-9598.md`: HOLD notes at the top come out only when the owner decisions are made.

## Not done

No build, test, Gradle run, `gh` write call, commit, or post. Gate logs named in the receipts are not on disk, so counts are receipt-only. Live JIRA was not queried.
