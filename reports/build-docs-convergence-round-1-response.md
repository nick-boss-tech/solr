# Build/docs convergence, round 1: main-side response

Round 1 (`reports/build-docs-convergence-round-1.md`) returned NOT CONVERGED: 2 blocking, 23 should-fix and 11 note findings across the eight drafts in `pr-drafts/build-docs/`. Each blocking and should-fix finding was checked against the receipt in `receipts/` and the branch code before anything was applied. Every finding held. Nothing was rebutted. Applied items carry the round's exact replacement text; bold-summary inserts follow the register's own template (the bold sentence as the first line under the section heading).

## Counts

| Grade | Findings | Applied to drafts | Held (state recorded below) | Standing (record items) |
|---|---|---|---|---|
| Blocking | 2 | 2 | 0 | 0 |
| Should-fix | 23 | 20 | 3 | 0 |
| Note | 11 | 0 | 0 | 11 |
| **Total** | **36** | **22** | **3** | **11** |

## Fact checks run before applying

- BD-R1-03 (SOLR-3684): the branch diff against base `14c7aac0d151` touches only the changelog, `JettyConfig.java`, `JettySolrRunner.java` and `TestJettySolrRunner.java`. `jetty.xml` is untouched, so the replacement's "jetty.xml itself is unchanged" holds.
- BD-R1-01 (SOLR-3684): the 254 association and Jay Muir's wording match `research/jira-context/SOLR-3684.json` as recorded in `reports/draft-fidelity-cli-security-builddocs-metrics-s3.md`, and the gating-run note matches `reports/build-docs-misc-round-1-g1.md` line 18.
- BD-R1-15 (SOLR-16322): owner decision 17 in `reports/build-docs-misc-round-1.md` (line 100) still reads "run the exercised verification (including a non-beast failing run and a passing build), then draft (recommended), or retire", so the prescribed HOLD line is accurate: the drafting is done, the verification has not run.
- BD-R1-17 (SOLR-16322): the receipt carries no run date. The date in the applied text comes from the run record on this branch: `gates/SOLR-16322.md` records "DONE by vm3 at 2026-10-11T05:48:14Z (UTC). Outcome: GREEN". The date was filled as 2026-10-11 from that record.
- BD-R1-16 (SOLR-16322): the non-beast sentence holds by the helper's mechanism: non-beast tasks run with the boundary defaults and no user seed, so the propagated seed is the root seed.
- BD-R1-12 (SOLR-6430): at base `cabedd1d968`, `DatePointField` line 163 sits inside `getExactQuery` (a query path) and line 196 is inside `readableToIndexed` (the index encoding), so the two replacement sentences' attributions hold.
- BD-R1-21 (SOLR-16914): `receipts/SOLR-16914.md` records an empirical probe against the Lucene 10.4.0 jars (JapaneseTokenizer run directly) showing the compound kept at position increment 0 only with `discardCompoundToken=false`, in both search and extended modes, and no run with a synonym filter after the tokenizer. The replacement's scope ("tokenizer output only") matches the record.
- BD-R1-23 (SOLR-16914): the branch diff against `14c7aac0d151` does not touch the `mode` option row (hunks at guide lines 2289, 2311, 2344), so the "no other option row is weakened" sentence holds.
- BD-R1-30 (SOLR-17752): "verified 2026-10-04" is supported: the ledger DONE entry dated 2026-10-04 covers head `e5c0a64f993c`, which the receipt names.
- BD-R1-34 (SOLR-17842): the guide at head `008973f631337` lists three search classes (`SimpleSearch`, `NumericSearch`, `ExitableDirectoryReaderSearch`) and eight classes total at lines 41-48, and lines 26-28 advise covering one or two benchmarks per area. The replacement holds.
- BD-R1-35 (SOLR-17842): applied with the re-check date filled from this pass: all eight guide classes and `solr/benchmark/jmh.sh` were verified present at main commit `3f5d4c5bf8ac` by `git ls-tree` on 2026-10-11. The applied sentence reads "Re-checked against main at commit 3f5d4c5bf8ac on 2026-10-11: all eight classes and jmh.sh are still present."

## Per-finding dispositions

### SOLR-3684

- BD-R1-01 (should-fix) APPLIED: the framing passage now reads as the register prescribes (ticket 254 framed as filed against a different product, closed WONTFIX; the getProxiedJettySolrRunner diagnosis credited to Jay Muir's read).
- BD-R1-02 (should-fix) APPLIED: the Proof paragraph now states the failing run was run by a maintainer, with the record pointer, and the gating run is stated plainly as this side's own.
- BD-R1-03 (should-fix) APPLIED: the sentence now says the failure sat at the original merge-base and lists the three files the fix touches; `jetty.xml` is named unchanged.
- BD-R1-04, BD-R1-05 (notes) STAND as record items: BD-R1-05's `SOLR-3684-TESTING-FOR-VM2.md` pointer is a packaging-time item, not draft text.

### SOLR-5821

- BD-R1-06 (should-fix) APPLIED: the summary now says the two content-hiding XML parser settings are not set anywhere in the shipped `solr.in.sh` and `solr.in.cmd` files.
- BD-R1-07 (should-fix) APPLIED: the body templates now name only the secure-parser flag, with the register's appended sentence about `enable.remote.streams`.
- BD-R1-08 (should-fix) APPLIED: the Limits bullet now carries the register's replacement (a require-secure-parser run shows those properties do not change what this pull request ships).
- BD-R1-09 (should-fix) HELD, branch-side: the replacement text targets the branch's guide edit, not the draft. Recorded verbatim for the branch commit: the `DistributedTest` invariant bullet's third item becomes "3. `numFound` is not affected when the replicas are in sync and hold the same committed documents." After that commit lands, the draft's Proof and Limits re-point at the new head and the current "does not state the sync invariant" phrasing comes out.
- BD-R1-10 (note) STANDS: the pinned-config upgrade note shape is a judgement call on the record, not a finding against the text.

### SOLR-6430

- BD-R1-11 (should-fix) APPLIED: the first paragraph now says the shaded Lucene benchmark library names classes the published build cannot load, and gives the Lucene 10.4.0 statement its base-commit anchor.
- BD-R1-12 (should-fix) APPLIED: the Lucene corner bullet now attributes line 196 to the index encoding and line 163 to `getExactQuery`, per the fact check above.
- BD-R1-13 (should-fix) HELD, branch-side: the replacement is a branch commit (delete `changelog/unreleased/SOLR-6430.yml`) plus draft-link hygiene after it. The draft's changelog line stays until the branch commit lands; the changelog-lint sentence is re-verified at the new head then.
- BD-R1-14 (should-fix) APPLIED: a new Limits bullet states that `DatePointField.toInternal` was checked as calling `NumberUtils.stringToLong`, so the date routes are covered by the same check.

### SOLR-16322

- BD-R1-15 (blocking) APPLIED: the HOLD line now sits at the top of the draft in the register's exact form: "HOLD: do not post until the owner rules under decision 17 (run the exercised verification (including a non-beast failing run and a passing build), then draft, or retire) in reports/build-docs-misc-round-1.md."
- BD-R1-16 (should-fix) APPLIED: both prescribed sentences are in: the "counted" bullet now reads as the register prescribes, and the hex-seed Limits bullet carries the non-beast propagation sentence.
- BD-R1-17 (should-fix) APPLIED: the verified line now reads "Verified 2026-10-11 at head `68dd5e2b154e3b9e61c715db94ca91fadad545a6`." The date comes from the run record in `gates/SOLR-16322.md` (see fact checks).
- BD-R1-18 (note) STANDS: the Receipts-block quote is already presented as a receipt quotation.

### SOLR-16914

- BD-R1-19 (note) STANDS: items 1 and 2 are confirmed by the record and the branch, as the round found.
- BD-R1-21 (should-fix) APPLIED: the Limits bullet now scopes the discard claim to tokenizer output and names the Lucene 10.4.0 jar probe as its basis.
- BD-R1-22 (should-fix) APPLIED: the Proof now names the parse test explicitly (TestSynonymGraphFilter, 22 turned from 22), and the Limits now carry the unrun-parser-paths bullet in the register's form.
- BD-R1-23 (should-fix) APPLIED: the `mode` row bullet now carries the register's confirmation sentence.
- BD-R1-24, BD-R1-25 (notes) STAND as record items.

### SOLR-17252

- BD-R1-26 (should-fix) APPLIED: the title note and the Overview summary both now carry the finding-count guide text and the SOLR-17103 scope out. The Overview keeps the draft's bold lead formatting, with the register's sentence as the lead.
- BD-R1-27 (should-fix) APPLIED: the Limits now state that the diff deletes EDGE_NGRAM options that no longer exist, in their sentence form.
- BD-R1-28 (note) STANDS: the F1 numbers are sourced on the record, as the round found.

### SOLR-17752

- BD-R1-30 (should-fix) APPLIED: the verified line now reads "Verified 2026-10-04 (gate run with -Dtest.name=\"TestShardsWhitelist\" at gate time)." The date and the gate-time flag are supported by the record (see fact checks).
- BD-R1-31 (should-fix) HELD, owner item, recorded verbatim: "Add a limits bullet, branch base: the drain holds on this draft, not because of text. The branch base itself is an owner decision: the draft's Proof cites base `14c7aac0d151` for its proof leg, but the branch's actual merge-base against main is `56ec140e3636` (`git merge-base`). The owner rules whether the PR opens against `56ec140e3636` (then the Proof's base label, and the WAITING note's own wording, change to name it) or the branch is rebased onto `14c7aac0d151` before opening (then nothing here changes except the note lifts)." Verification: `git merge-base solr-17752-submit origin/main` returns `56ec140e3636`; the receipt's proof leg used base production `14c7aac0d15`; the draft's WAITING note records owner decision 1 (branch base) as open. No bullet was added; the draft stays NOT CONVERGED on this item alone.
- BD-R1-32 (should-fix) APPLIED: the WAIT-LABEL lines now state the open work (RmTestSubject assigned nobody, 8 tests in 6 files, CRINGETEST, and the RmTest-only jobs) in the register's form.
- BD-R1-33 (note) STANDS as a packaging item (the branch changelog title fix), on the opening-time packaging list.

### SOLR-17842

- BD-R1-34 (blocking) APPLIED: the benchmark deliverable prose now states three search classes with the "benchmarked here" wording, one to two per area, in the register's form.
- BD-R1-35 (should-fix) APPLIED with the date filled by this pass's own re-check (see fact checks).
- BD-R1-36 (should-fix) APPLIED: the verified line and the WAITING note now carry the JDK 21.0.12.1 and Gradle 9.7.0 toolchain pins.
- BD-R1-37 (note) STANDS as a record item: the benchmark-count record run itself sits with the main side and is not decided by this response.

## Verdicts after application

- SOLR-3684: CONVERGED on text. The draft's own HOLD (owner decision 1) stands; it was not a finding.
- SOLR-5821: NOT CONVERGED. BD-R1-09 is held branch-side (guide wording commit owed on the branch).
- SOLR-6430: NOT CONVERGED. BD-R1-13 is held branch-side (the fragment-drop commit owed on the branch).
- SOLR-16322: CONVERGED on text. The BD-R1-15 HOLD line is in place and owner decision 17 stays open; the hold is the state, not a text defect.
- SOLR-16914: CONVERGED on text.
- SOLR-17252: CONVERGED on text. The draft's HOLD (owner decisions 1 to 3) stands; it was not a finding.
- SOLR-17752: NOT CONVERGED. BD-R1-31 is held as an owner item (branch base decision).
- SOLR-17842: CONVERGED on text. The WAITING note for the owner's published-results call and the `<build>` fix stands, per BD-R1-37's record.

## What round 2 must settle

Round 2 is posted as `assignments/pool-build-docs-convergence-round-2.md`, scoped to the three NOT CONVERGED drafts and their held items only: SOLR-5821 (BD-R1-09 state), SOLR-6430 (BD-R1-13 state) and SOLR-17752 (BD-R1-31 state). If a branch commit or the owner ruling has landed by then, round 2 judges the draft against the new state; if not, it confirms the held item still stands and says so.
