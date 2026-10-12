# Build and docs convergence, round 2

Verdict: **NOT CONVERGED.** All three drafts still open are waiting on something outside the drafts: a branch commit, or an owner ruling on the base. Each has its round 1 register text in place or held correctly.

Counts: drafts re-checked 3 (SOLR-5821, 6430, 17752). Round 1 dispositions checked 11 (BD-R1-06 to 09, 11 to 14, 30 to 32). Confirmed on the live state: 11, with three of them held correctly rather than applied. Disputed on the live state: 0. Response narratives disputed in detail: 11 (BD-R2-01, a record issue). Items still open: 3 (BD-R1-09, 13, 31). New findings BD-R2-nn: 5 (2 should-fix, 3 note).

Scope: `assignments/pool-build-docs-convergence-round-2.md`, checked against `reports/build-docs-convergence-round-1.md`, the main side's response, and the decision record `reports/build-docs-misc-round-1.md` (consolidated decisions, lines 77 to 103). One read-only subagent did the review. Heads were checked with `git ls-remote origin refs/heads/<branch>`. Nothing was edited, posted or built.

## Verdict per draft

- **SOLR-5821: NOT CONVERGED.** BD-R1-09 open (the branch-side wording commit). The head `b8af2d1ce2f5` matches the draft and the receipt.
- **SOLR-6430: NOT CONVERGED.** BD-R1-13 open (the branch-side changelog fragment drop). The head `c0c6e5dd21c4` matches the draft and the receipt's packaged head.
- **SOLR-17752: NOT CONVERGED.** BD-R1-31 open (the owner's base decision), and BD-R2-02 should-fix on the WAITING note. The head `e5c0a64f993c` matches the draft and the receipt.

## Items 1 to 3

1. **SOLR-5821, BD-R1-09: OPEN.** The tip changes one file against `14c7aac0d151`: `common-query-parameters.adoc`, by three lines. Line 76 still reads "When the replicas are in sync and have the same searcher, numFound is not affected." The draft's item 3 matches that line, so the wording commit has not landed. The response's "DistributedTest invariant bullet" is not in the diff, so it is not checked. Once the commit lands, the Proof, build and receipt need re-verifying at the new head.
2. **SOLR-6430, BD-R1-13: OPEN.** The tip still adds `changelog/unreleased/SOLR-6430-doc-numeric-missing-sort.yml`, which is absent at the base. The draft's Changelog line (line 36) remains. The changelog-lint sentence matches the receipt at the tip and needs re-verifying after the drop. Decision 13 is recommended, not ruled.
3. **SOLR-17752, BD-R1-31: OPEN.** No ruling is recorded in the WAITING note, the receipt or the decision record. The decision is consolidated item 9 (line 90): rebase onto current main and rerun, or open as is. The branch has not moved: `e5c0a64f993c` still descends from `56ec140e3636`, and `14c7aac0d151` is not its ancestor. The WAITING note matches item 9's options. The Proof names no base commit, and the held replacement is correctly absent. The node-name assertion (line 69) checks out at the tip.

## Register

**BD-R2-01 | response record (all three drafts) | should-fix, record only.** The response narratives for eleven dispositions name text or files that the drafts and the branch lack: solr.in.sh and enable.remote.streams (BD-R1-06 to 08), a DistributedTest bullet (BD-R1-09), a Lucene benchmark, toInternal and a Lucene corner bullet (BD-R1-11, 12, 14), SOLR-6430.yml (BD-R1-13), TestShardsWhitelist (BD-R1-30), a Proof citing `14c7aac0d151` (BD-R1-31), and RmTestSubject (BD-R1-32). None of those terms appears in any build-docs draft. The corrected records are:
- BD-R1-06: applied. The Proof opens "**This is a documentation build, not a test run, because the change is text only.**"
- BD-R1-07: applied. The Limits open "**The document counts and per-replica term statistics are not covered.**"
- BD-R1-08: applied. The SOLR-4260 link is in Limits.
- BD-R1-09: held, branch side. Line 76 of `common-query-parameters.adoc` changes to "hold the same committed documents".
- BD-R1-11: applied. The Limits open "**The sentences rest on reading the code, not on a sort run.**"
- BD-R1-12: applied. Paragraph 1 links `DatePointField` line 196.
- BD-R1-13: held, branch side. Delete `changelog/unreleased/SOLR-6430-doc-numeric-missing-sort.yml`.
- BD-R1-14: applied. The Limits carry the StrField bullet.
- BD-R1-30: applied. The Proof reads "**TestShardResponseLogging passed 1 of 1 at head e5c0a64f993c, verified 2026-10-04.**"
- BD-R1-31: held, owner decision 9. No base label is added until the ruling.
- BD-R1-32: applied. The Limits open "**The test builds its response by hand, and other log lines still print request params.**"

**BD-R2-02 | SOLR-17752, line 1 | should-fix.** The WAITING note cites "owner decision 1", which is SOLR-3684's item (line 80). The branch base is item 9 (line 90). Replacement for line 1: "[WAITING: owner decision 9 in reports/build-docs-misc-round-1.md, the branch base. The merge-base is 56ec140e3636, not the receipt's 14c7aac0d151. Rebase and focused rerun are the owner's call before posting. Do not post until settled.]"

**BD-R2-03 | SOLR-5821 | note.** The searcher wording is also item 11 (line 92), recorded as "plain words, recommended", with no ruling in the files read. The held item needs that ruling as well as the branch commit.

**BD-R2-04 | SOLR-17752, the held BD-R1-31 text | note.** The held text offers rebasing "onto `14c7aac0d151`". Item 9 offers current main, `upstream/main` at `d76fc14b9857`, which is 72 commits past `14c7aac0d151`. The receipt's failing leg ran at `14c7aac0d151`, which is neither the merge-base nor current main, so the ruling should name the proof base. The response's merge-base check used `origin/main`, which in this worktree is the fork and gives `e044bf20b405`. `upstream/main` gives `56ec140e3636`.

**BD-R2-05 | round 1 register | note.** BD-R1-29 is listed as a note under both SOLR-17252 and SOLR-17752, and no row defines it. BD-R1-33 has no row text. The response describes BD-R1-10 (SOLR-5821) differently from the register.

## Not checked

- The changelog fragment at the base (object absent at `c0c6e5dd21c4` and `cabedd1d968`) and the `changelog/unreleased/SOLR-6430.yml` file named by the assignment.
- The "DistributedTest invariant bullet" the response mentions (not in the diff).
- The receipts ledger, the takeover log and security material.

## Convergence line

NOT CONVERGED. Three drafts remain open: SOLR-5821 (BD-R1-09 and the searcher ruling), SOLR-6430 (BD-R1-13) and SOLR-17752 (BD-R1-31 and the WAITING note). The owner's rulings and the branch commits are the blockers.
