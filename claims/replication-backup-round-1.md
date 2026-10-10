# Claim: Replication and backup round 1

Claimed 2026-10-10 by Claude Code (AI agent), working for Nick Shanin.

Scope: `assignments/replication-backup-round-1.md` (commit `8db87734c3d`). Thirteen tickets: SOLR-5589, 6711, 8430, 9091, 9382, 9598, 9865, 11650, 12085, 12246, 17287, 18249, 18280. Audit first, then drafts under `pr-drafts/replication-backup/` for the draftable tickets.

Not in this round, per the assignment: SOLR-12651, 15863, 14919 and 7394 (SolrCloud round 1 owns them). The interactions with them are checked from this round's side only.

Not in this round, but new since the last round: receipts for 9595 and 17055 were refreshed (`5a4a4afb185`). Those tickets were audit-only in search components rounds. Their re-check is a separate, later round.

Cap: four subagents at once, within the cap of six.

## Heads checked live on 2026-10-10

`git ls-remote`, then an explicit fetch of all 13 branches, and a check that each is present locally.

| Ticket | Branch | Live head | Named in the assignment | Result |
|---|---|---|---|---|
| 5589 | `solr-5589-submit` | `c707aa95e2ae` | none (no gate) | audit only |
| 6711 | `solr-6711-submit` | `5a120cd69d64` | none (no gate) | audit only |
| 8430 | `solr-8430-submit` | `49af21be5892` | `49af21be589` | matches |
| 9091 | `solr-9091-submit` | `e31bdaa4d279` | none (no gate) | audit only |
| 9382 | `solr-9382-submit` | `c0b5fec1be21` | none (no gate) | audit only |
| 9598 | `solr-9598-submit` | `c8407773f76c` | `c8407773f76` | matches |
| 9865 | `solr-9865-submit` | `4937608bb181` | `4937608bb18` | matches |
| 11650 | `solr-11650-submit` | `e4f5e941cd8e` | `e4f5e941cd8` | matches |
| 12085 | `solr-12085-submit` | `c8dba5023396` | `c8dba502339` | matches |
| 12246 | `solr-12246-submit` | `3ffc2539ec76` | `3ffc2539ec7` | matches |
| 17287 | `solr-17287-submit` | `6957daf82610` | `6957daf8261` | matches |
| 18249 | `solr-18249-submit` | `deffea4c51be` | `deffea4c51b` (live PR) | matches; consistency only |
| 18280 | `solr-18280-submit` | `0da92abd9e96` | none (live PR) | consistency only |

## Shared rules for every part

- Read only, except the report file and any draft named for your part. No commit, push, checkout, reset, merge, stash, or fetch of anything new. `git show`, `git log`, `git diff`, `git merge-tree`, `git rev-parse`, `git cat-file -e`, `git grep`, `git ls-tree`, and `git merge-base` are fine. Trial merges with `git merge-tree --write-tree` are fine if they write no ref.
- No builds, no Gradle, no tests, no test runs of any kind. No `gh` write calls.
- `gh pr view`, `gh pr list`, and `gh pr checks` are allowed for read-only consistency passes, through `C:\Users\shaninna\dev\Solr-issues\research\gh.ps1` (quote a JSON field list as one argument). Nothing that writes.
- Post nothing anywhere. No PR, no comment, no JIRA. No new Jira ticket unless someone asks.
- Proof numbers come only from the receipt named for the ticket. Gate logs are often not on disk; say so. Where a receipt says timing-dependent, partial, or @Nightly-skipped, the draft says that, and does not claim a deterministic base failure.
- Drafts follow `pr-formula.md`: the AI header as line 1, the Jira link line, a bold one-line summary in each section, "What happens today", "What this change does", "Proof", "A choice to check" (only with a live alternative and a pointed question), "Limits", the Changelog line with a link, and the AI assistance footer. Length guide about 3,500 characters. Each draft names the head it was written against in its Proof.
- Public text carries no internal process vocabulary (gate, receipt, ledger, handoff, top-up, takeover, audit, rc=0, fresh JUnit XML, pre-fix proof as a label, "premise run", internal log names, "gate record"). Plain words. No em dash and no en dash.
- Lucene version claims: main and branch_10x pin Lucene 10.4.0, and branch_9x pins 9.12.3. If one version is named in a draft, name every version that applies.
- A moved tip, or a diff that differs from the receipt, is flagged, not silently adopted.
- Every finding: file and line, evidence (SHA and line, receipt line, or command output), and exact replacement wording. Mark FIX or NOTE. If you cannot check something, say so under "Not checked". Do not guess.

## Parts

Four subagents. Reports go to `reports/replication-backup-round-1-<part>.md`. Drafts go to `pr-drafts/replication-backup/`.

**Part g1: RestoreCore cluster (SOLR-9091, 9865, 17287).** The three branches all change `RestoreCore.java`. Check the three diffs against each other, and state a landing order. Check that 9865's rollback and 17287's update-log reset do not contradict each other. 9091 is audit only (no gate): read its premise against the same file the other two change. 9865 is gated (11 of 11 across three classes); its proof needed a test correction (a core reload step) before it discriminated. The draft's Proof must carry that explanation. 17287 is gated (`TestRestoreCore` 4 of 4, `UpdateLogTest` 6 of 6). Draft 9865 and 17287 if draftable.

**Part g2: IndexFetcher cluster (SOLR-6711, 11650, 12085, 12246).** All four change `IndexFetcher.java`. Check the four for overlapping hunks and conflicting assumptions, and state a landing order for the three gated ones. 6711 is audit only (no gate). 11650 is gated (`URLUtilTest` 18 of 18, and two more classes), and is cross-filed under SolrJ and clients: its draft must state the cross-area effect plainly. 12085 is gated (`IndexFetcherUnusedFilesTest` 1 of 1; the replication suite is `@Nightly` and was skipped). Its Proof must not claim end-to-end replication coverage. 12246 is gated (`IndexFetcherCompareFileTest` 1 of 1; the same `@Nightly` skip). Its record carries a Choice: lowering the whole "did not match" statement from WARN to INFO, or keeping WARN when lengths also differ. The real cause of the differing `.liv` checksums stays a Limits line. Draft the draftable gated ones.

**Part g3: ReplicationHandler and backup (SOLR-5589, 9382, 8430, 9598).** 5589 and 9382 are audit only (no gate); 5589 has a `TESTING.md` note. Read them together with each other and with 11650 (group g2) against the same current code of `ReplicationHandler.java`, so their premises are judged the same way. 8430 is gated (`ReplicationRateLimiterTest` 3 of 3, probe 1 of 1); it changes the replication admin API, so check that API surface in the draft. 9598 is gated (`TestLocalFSCloudBackupRestore` 2 of 2, `RestoreCollectionAPITest` 7 of 7, `BackupRestoreApiErrorConditionsTest` 4 of 4). Its proof is timing-dependent, and the receipt says so: the draft states that, and claims no deterministic base failure. 9598 shares `TestLocalFSCloudBackupRestore` with SOLR-12651 and SOLR-15863 (SolrCloud round 1): check the overlap from 9598's side and say which lands first. Do not re-audit the SolrCloud pair. Draft 8430 and 9598 if draftable.

**Part g4: live PR consistency (SOLR-18249, 18280).** Both are live PRs on apache/solr. 18249 is gate green at `deffea4c51b`; it is consistency only. Check the live PR's head against the branch and the receipt, and report drift. 18280 has no gate on the main side; its test state rests on the live PR's own CI at the head, as the receipt states. Consistency only. Do not draft replacement PR text. Read the live PR state with read-only `gh pr list` and `gh pr view` (quote one JSON field list). PR numbers are not needed for the report.

## Deliverables

1. `reports/replication-backup-round-1-g1.md` through `-g4.md`. The lead writes `reports/replication-backup-round-1.md`, with per-ticket verdicts, interaction results, live PR results, and owner decisions.
2. Drafts in `pr-drafts/replication-backup/` for the draftable tickets, each naming its head.

Not in scope: opening PRs, posting comments, editing branches or live PR descriptions, builds, Gradle, and test runs.
