# CLI convergence, round 1: main-side response

Round 1 (`reports/cli-convergence-round-1.md`) returned NOT CONVERGED: 1 blocking, 15 should-fix and the rest note findings across the nine drafts in `pr-drafts/cli/`. Each blocking and should-fix finding was checked against the receipt in `receipts/` and the branch code before anything was applied. Every finding held. Nothing was rebutted. Applied items carry the round's exact replacement text, with three disclosed deviations below (CLI-R1-08, CLI-R1-09 and CLI-R1-04's note form).

## Counts

| Grade | Findings | Applied to drafts | Held (state recorded below) | Standing (record items) |
|---|---|---|---|---|
| Blocking | 1 | 0 | 1 (adopted; branch work owed) | 0 |
| Should-fix | 15 | 11 | 4 (branch-side) | 0 |
| Note | 29 | 0 | 0 | 29 |
| **Total** | **45** | **11** | **5** | **29** |

## Fact checks run before applying

- CLI-R1-04 (SOLR-10667): the category record carries a recommendation. Decision 10 in `reports/cli-bin-packaging-round-1.md` (line 129) reads "exclude `solrclient\*\*` (recommended)", corroborated in `reports/cli-bin-packaging-round-1-s5.md` (lines 52, 58, 110: explicit excludes recommended as the minimal fix). Under the decision default the recommendation is ADOPTED and recorded here: the `solrclient/` folder the Learning To Rank build writes into the example directory is excluded from the distribution, and the copy-all route in the current Change text is superseded. The branch change, the head move and the re-gate are owed before the draft's Change text is rewritten; the draft carries an OWNER NOTE hold line recording the adoption (see the disposition below).
- CLI-R1-06 and CLI-R1-07 (SOLR-10667): at base `cabedd1d968`, `modules/ltr/example` holds exactly seven tracked files (`.gitignore`, `README.md`, `config.json`, `exampleFeatures.json`, `libsvm_formatter.py`, `train_and_upload_demo_model.py`, `user_queries.txt`), its `.gitignore` lists `solrclient/`, and `learning-to-rank.adoc` line 788 at base reads "This example folder is not shipped in the Solr binary release." Both replacements hold.
- CLI-R1-01 (SOLR-9342): decision 1 in the round record recommends documenting `SOLR_TIMEZONE` as the only knob with a test. The replacement is applied; the precedence-test requirement the same decision names stays an owner item on the draft's record (the owner rules whether a precedence test is required before posting).
- CLI-R1-08 (SOLR-12347): the test at head `56d38389c75a` in `solr/packaging/test/test_start_solr.bats` (lines 135-144) is named "SOLR-12347 stop waits ten minutes by default and start wait stays at three" and asserts the 600-second stop message plus an explicit 45-second stop wait setting the start message. It does not assert the start default. The register's primary replacement presumes the decision-11 test rename, which has not landed, so the applied form is the register's own fallback: "It does not assert the start default (see Limits)." The Limits sentence about the start default was already present in the draft in substance; the checks sentence in Limits was verified against the bats file.
- CLI-R1-09 (SOLR-12347): the two facts (the bundled `solr.service` TimeoutSec; ref guide line 400) were already in the draft's Limits. The application appends only the register's follow-up offers to those bullets: "A follow-up can raise it on request." on the systemd bullet, and "It was not updated here; a follow-up can update it on request." on the ref-guide bullet. Decision 11's in-branch recommendations (raise the `solr.service` TimeoutSec and update ref guide line 400 in this change, both marked recommended in the round record) stand as owner items for the draft; they are recorded here and carried to round 2.
- CLI-R1-13 (SOLR-16272): `ClusterFileStore.java` is not in the branch diff, and its lines 111-131 at base contain the "Path already exists" throw, so the base-anchored citation in the replacement holds.
- CLI-R1-16 (SOLR-16813): the branch diff touches only `SolrPackageLoader.java`, `TestPackages.java` and the changelog. `RepositoryManager.java` lines 192-196 and `PackageUtils.java` lines 184-199 at base `14c7aac0d151` match the draft's claims, so the base-anchored citations hold.
- CLI-R1-22 (SOLR-18132): the branch diff does not touch `solr/bin/solr.cmd`, so the "solr.cmd is not touched" sentence holds. CLI-R1-21: the round-1 roll-up's decision 6 leaves the 18132 precedence open, so the plain statement of the already-set-TZ behavior is the applied form.
- Branch changelog titles were read at their live heads and all four are wrong as the register says: SOLR-9342 at `833e11192a7f` ("bin/solr now exports TZ from SOLR_TIMEZONE (default UTC) so the GC log uses the same timezone as the Solr log, unless TZ is already set."), SOLR-10667 at `1e3f022552be` ("The Solr distribution now includes the Learning To Rank example files (modules/ltr/example)."), SOLR-16272 at `d2cf817391692` (title ends "...so the install can be retried and uninstall no longer refuses"), SOLR-16813 at `1b288170e8aa` ("A node that joins a cluster with installed packages now also fetches...").

## Per-finding dispositions

### SOLR-9342

- CLI-R1-01 (should-fix) APPLIED: the Choice now reads in the register's replacement form (documenting `SOLR_TIMEZONE` as the only knob with a test). The precedence-test part of decision 1 stands as an owner item on the draft's record.
- CLI-R1-02 (should-fix) APPLIED: the Choice bullet now reads in the register's replacement form (the detached GC-log question posed without the "same zone" claim).
- CLI-R1-03 (should-fix) HELD, branch-side. Recorded verbatim for the branch commit: the fragment title becomes "The Unix start script (bin/solr) sets TZ from SOLR_TIMEZONE (default UTC) for the server JVM, so its GC log uses that zone unless TZ is already set. solr.cmd is not changed." The draft's changelog link re-points at the new head after the commit.

### SOLR-10667

- CLI-R1-04 (blocking) ADOPTED, held for branch work: decision 10's recommendation (exclude `solrclient/**`) is adopted under the decision default, as recorded in the fact checks. The draft now carries an OWNER NOTE hold line at the top, in the sibling form used by `pr-drafts/replication-backup/SOLR-8430.md`: "OWNER NOTE (not PR text; delete before posting). HOLD. Written against 1e3f022552be4d281a020e015f4c8c05ce4eec6c. Owner decision 10's recommendation (exclude `solrclient/**`) was adopted under the decision default on 2026-10-11. Before posting: the branch gains the exclusion for the `solrclient/` folder the Learning To Rank build writes into the example directory, the head moves, a re-gate runs at the new head, and this text is rewritten against it. The changelog title fix (CLI-R1-05) rides the same branch work." The copy-all Change text was NOT rewritten now, and the register's copy-all Limit sentence was NOT added: both would contradict the adopted route. The draft stays NOT CONVERGED until the branch work lands and the text is rewritten against the new head.
- CLI-R1-05 (should-fix) HELD, branch-side, rides the CLI-R1-04 branch work. Recorded verbatim: the fragment title becomes "The full Solr distribution now includes the Learning To Rank example files (modules/ltr/example)."
- CLI-R1-06 (should-fix) APPLIED: the summary now reads "It holds seven tracked files, among them config.json, train_and_upload_demo_model.py and exampleFeatures.json, and none of them reaches the distribution."
- CLI-R1-07 (should-fix) APPLIED: the new Limits bullet reads "The Learning To Rank reference page still says the example folder is not shipped in the binary release (learning-to-rank.adoc, line 788). It is not changed here; a follow-up can update it on request."

### SOLR-12347

- CLI-R1-08 (should-fix) APPLIED in the register's fallback form (see fact checks): the checks sentence now ends "It does not assert the start default (see Limits)."
- CLI-R1-09 (should-fix) APPLIED as follow-up-offer additions to the existing Limits bullets (see fact checks). Decision 11's in-branch recommendations stand as owner items, recorded here.
- CLI-R1-10 (note) STANDS as a record item (the source slice is not on this branch).

### SOLR-16272

- CLI-R1-11 (should-fix) APPLIED: the Changes summary now lists only what this pull request ships.
- CLI-R1-12 (should-fix) APPLIED: the Summary line now carries the already-registered condition and the partial-failure behaviour.
- CLI-R1-13 (should-fix) APPLIED: the cleanup citation now anchors `ClusterFileStore.java` at the base commit and says so.
- CLI-R1-14 (should-fix) HELD, branch-side. Recorded verbatim: the fragment title becomes "A failed bin/solr package install now removes the files it posted, unless the version may already be registered, so a retry is not blocked by leftover files."

### SOLR-16813

- CLI-R1-15 (should-fix) APPLIED: the Summary line now carries the storage description.
- CLI-R1-16 (should-fix) APPLIED: the `classLoader` citation now anchors at the base commit with "at the base commit", and the `writeZipFromSolrHome` sentence now carries the plain-HTTP wording and its base-commit anchor.
- CLI-R1-17 (should-fix) HELD, branch-side. Recorded verbatim: the fragment title becomes "When a package version loads, a node fetches its missing manifest.json from another node that has it. If the fetch fails, the version still loads with a warning."

### SOLR-17029

- CLI-R1-25, CLI-R1-26, CLI-R1-27 (notes) STAND as record items. The "roughly the same rate" phrase at draft line 37 was not checked against logs in this pass; that record gap is noted for round 2's owner, not silently filled.

### SOLR-17598

- CLI-R1-23 (should-fix) APPLIED: the prescribed Limits bullet is in ("The verification for this change is still owed on Windows: a run of the fixed script there, and the same run on the unfixed script for the premise. The Windows host could not build it locally at verification time, and no run on macOS was made."). The receipt still shows the Windows run as owed; the draft states the limit plainly.

### SOLR-18132

- CLI-R1-21 (should-fix) APPLIED: the Changes section now states the already-set-TZ behavior plainly, without the unsettled precedence framing.
- CLI-R1-22 (should-fix) APPLIED: the Summary line now says solr.cmd is not touched.
- CLI-R1-18, CLI-R1-19 (notes) STAND as record items.

### SOLR-18339

- CLI-R1-24, CLI-R1-25 (notes) STAND as record items (the secured-node Choice and the opening-timing call are the owner's, already on the draft).

## Verdicts after application

- SOLR-9342: CONVERGED on text. CLI-R1-03 is owed branch-side before opening; the precedence-test question from decision 1 stands as an owner item.
- SOLR-10667: NOT CONVERGED. CLI-R1-04 is adopted and held for the branch exclusion change, the head move and the re-gate; CLI-R1-05 rides the same branch work. CLI-R1-06 and CLI-R1-07 are applied.
- SOLR-12347: CONVERGED on text. Decision 11's in-branch recommendations stand as owner items.
- SOLR-16272: CONVERGED on text. CLI-R1-14 is owed branch-side before opening.
- SOLR-16813: CONVERGED on text. CLI-R1-17 is owed branch-side before opening.
- SOLR-17029: CONVERGED on text (notes only, standing).
- SOLR-17598: CONVERGED on text. The Windows verification run stays owed, as the draft now states.
- SOLR-18132: CONVERGED on text.
- SOLR-18339: CONVERGED on text (notes only, standing).

## What round 2 must settle

Round 2 is posted as `assignments/pool-cli-convergence-round-2.md`, scoped to SOLR-10667 (NOT CONVERGED; CLI-R1-04 and CLI-R1-05 states) and the three branch-side title items whose state can move: CLI-R1-03 (SOLR-9342), CLI-R1-14 (SOLR-16272) and CLI-R1-17 (SOLR-16813). If a branch commit has landed by then, round 2 checks the fragment title against the replacement recorded here and the draft's changelog link against the new head; if not, it confirms the item still stands.
