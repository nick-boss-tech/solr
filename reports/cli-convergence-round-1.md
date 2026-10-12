# CLI convergence, round 1

Verdict: **NOT CONVERGED.** None of the nine drafts converges on the first pass. SOLR-10667 has a blocking finding, and every other draft has at least one should-fix item or a note.

Counts: drafts checked 9 (slice A: 9342, 10667, 12347, 16272, 16813; slice B: 17029, 17598, 18132, 18339). Drafts with a blocking finding 1 (SOLR-10667). Drafts with should-fix findings 6. Drafts with note findings only 2 (SOLR-17029, 18339). Findings 45 in total, all new on this pass. Register IDs CLI-R1-01 to 19 come from slice A; CLI-R1-21 to 28 from slice B.

Scope: `assignments/pool-cli-convergence-round-1.md`. The decision record is `reports/cli-bin-packaging-round-1.md` and its parts, with the draft-fidelity settlements. Heads were checked with `git ls-remote origin refs/heads/<branch>`, and every draft matches its named head. Two read-only subagents did the review. Nothing was edited, posted or built.

## Verdict per draft

- **SOLR-9342: NOT CONVERGED.** CLI-R1-01, 02, 03 (should-fix). CLI-R1-03 needs a branch title change.
- **SOLR-10667: NOT CONVERGED (blocking).** CLI-R1-04 (blocking, owner decision required first); CLI-R1-05, 06, 07 (should-fix).
- **SOLR-12347: NOT CONVERGED.** CLI-R1-08, 09 (should-fix); CLI-R1-10 (note).
- **SOLR-16272: NOT CONVERGED.** CLI-R1-11, 12, 13 (should-fix); CLI-R1-14 (should-fix, branch title).
- **SOLR-16813: NOT CONVERGED.** CLI-R1-15, 16 (should-fix); CLI-R1-17 (should-fix, branch title); CLI-R1-18 (note).
- **SOLR-17029: NOT CONVERGED (notes only).** CLI-R1-25, 26, 27 (note).
- **SOLR-17598: NOT CONVERGED.** CLI-R1-23 (should-fix); CLI-R1-24 (note).
- **SOLR-18132: NOT CONVERGED.** CLI-R1-21, 22 (should-fix); CLI-R1-28 (note).
- **SOLR-18339: NOT CONVERGED (notes only).** CLI-R1-24, 25 (note).

## Register: blocking and should-fix rows

**CLI-R1-04 | SOLR-10667 | blocking.** The Change says "Every file in the directory is copied, including its .gitignore and README.md." Nothing excludes the `solrclient/` folder, which the Learning To Rank build writes into the example directory, and the example's `.gitignore` lists it. The roll-up's decision 10 is "Exclude solrclient/** (recommended)". The owner rules first. If copy-all stays, add a Limit: "A local solrclient/ folder, which the Learning To Rank build writes into the example directory and git ignores, is copied too when it exists, so the distribution depends on local state." If the folder is excluded, the branch changes, the head moves, and a re-gate is owed.

**CLI-R1-01 | SOLR-9342 | should-fix.** The Limits say "Making one value drive both logs is not part of this change." The owner decision 1 recommends option B, documenting SOLR_TIMEZONE as the only knob, with a test. Replacement: "SOLR_TIMEZONE is the setting to use for both logs. This change adds no test for the precedence between TZ, SOLR_TIMEZONE and SOLR_OPTS." The owner rules whether a precedence test is required before posting.

**CLI-R1-02 | SOLR-9342 | should-fix.** The Proof says "Evidence shape". Replacement: "How it was run: a BATS suite, run by hand against a packaged distribution as a non-root user; the distribution's start script was the base script for the run on the unfixed code and this head's script for the head run."

**CLI-R1-03 | SOLR-9342 | should-fix (branch title).** The changelog title says "bin/solr now exports TZ", which describes the superseded approach and contradicts the Limits. Branch title: "The Unix start script (bin/solr) sets TZ from SOLR_TIMEZONE (default UTC) for the server JVM, so its GC log uses that zone unless TZ is already set. solr.cmd is not changed."

**CLI-R1-05 | SOLR-10667 | should-fix (branch title).** The changelog title says "The Solr distribution now includes...", but the slim distribution has no modules. Branch title: "The full Solr distribution now includes the Learning To Rank example files (modules/ltr/example)."

**CLI-R1-06 | SOLR-10667 | should-fix.** "It holds config.json, train_and_upload_demo_model.py and exampleFeatures.json." The directory holds seven tracked files. Replacement: "It holds seven tracked files, among them config.json, train_and_upload_demo_model.py and exampleFeatures.json, and none of them reaches the distribution."

**CLI-R1-07 | SOLR-10667 | should-fix.** The Limits do not mention the Learning To Rank reference page, which says the example folder is not shipped in the binary release (learning-to-rank.adoc, line 788). Add: "The Learning To Rank reference page still says the example folder is not shipped in the binary release (learning-to-rank.adoc, line 788). It is not changed here; a follow-up can update it on request."

**CLI-R1-08 | SOLR-12347 | should-fix.** The Proof names a test "SOLR-12347 stop waits ten minutes by default and start wait stays at three", but the test checks the 600-second stop message, not a start check. Replacement: "The test is 'SOLR-12347 stop waits ten minutes by default'. It checks the 600-second stop message and that an explicit 45-second stop wait sets the start message." If the name stays, add: "It does not assert the start default (see Limits)."

**CLI-R1-09 | SOLR-12347 | should-fix.** The Control Script Reference still says 180 seconds, and the systemd unit still sets `TimeoutSec=180s`. Add: "The Control Script Reference still says 180 seconds (line 400). It was not updated here; a follow-up can update it on request." and "The systemd unit still sets TimeoutSec=180s, so under systemd a stop may end at 180 seconds. It was not changed here; a follow-up can raise it on request." The owner rules whether 600 ships without these.

**CLI-R1-11 | SOLR-16272 | should-fix.** The Limits have no bold summary. Insert first: "**The new test covers only a failure before registration.**"

**CLI-R1-12 | SOLR-16272 | should-fix.** The Limit "No test covers the rule for files after the registration request." has no follow-up offer. Append: "A follow-up test can follow on request."

**CLI-R1-13 | SOLR-16272 | should-fix.** The `ClusterFileStore.java` link is at the head for code the branch does not change. Replacement: "[ClusterFileStore.java, merge-base, L111 to L131](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/filestore/ClusterFileStore.java#L111-L131)".

**CLI-R1-14 | SOLR-16272 | should-fix (branch title).** The title says uninstall "no longer refuses", but uninstall is unchanged. Branch title: "A failed bin/solr package install now removes the files it posted, unless the version may already be registered, so a retry is not blocked by leftover files."

**CLI-R1-15 | SOLR-16813 | should-fix.** The Limits have no bold summary. Insert first: "**The fetch runs only on nodes that load the version, and no test checks the warning.**"

**CLI-R1-16 | SOLR-16813 | should-fix.** The `RepositoryManager.java` and `PackageUtils.java` links are pre-change code at the head. Re-point both to the merge-base `14c7aac0d151402b00259e2fb9bf5eed7049ec5d` and say so in the text.

**CLI-R1-17 | SOLR-16813 | should-fix (branch title).** The title says "A node that joins a cluster", but the trigger is the version load. Branch title: "When a package version loads, a node fetches its missing manifest.json from another node that has it. If the fetch fails, the version still loads with a warning."

**CLI-R1-23 | SOLR-17598 | should-fix.** The linked changelog title says the fix is done, beside a Proof that says the change is verified by construction and not run on Windows. The receipt keeps the Windows check owed. Add after the Limits: "- The changelog line describes the intended result. The evidence for it is the construction proof above, and the Windows run is still owed."

**CLI-R1-21 | SOLR-18132 | should-fix.** "This is the intended behavior." No recorded decision calls the precedence intended; owner decision 6 leaves it open. Replacement: "Was it right to let an explicit solr.ssl.enabled=false override an https urlScheme for tools?"

**CLI-R1-22 | SOLR-18132 | should-fix.** The Limits have no Windows bullet, although the solr.cmd lines were dropped under owner-provisional decision 13. Add after the last Limits line: "- Not covered: Windows. solr/bin/solr.cmd is not changed by this branch, and the Windows tool path has not been run."

## Notes

CLI-R1-10 (the forced-kill timing was read from code), CLI-R1-18 (install and uninstall through a node missing the manifest), CLI-R1-19 (length over the 3,500 guide, owner decision 14), CLI-R1-24 (the "base" link label), CLI-R1-25 (two links to unchanged files), CLI-R1-26 and 27 (the 17029 control-run wording and the "roughly the same rate" phrase, both not checked against the logs), and CLI-R1-28 (the reference guide edit is missing from the "What this change does" section).

## Not checked

- The Windows run and the 17029 control and rerun logs are not on disk.
- The 9342 run date rests on the receipt's citation of the takeover log.
- `reports/draft-fidelity-cli-security-builddocs-metrics.md` is named by one part as security-category material. Its CLI rows were not used in this pass, so they are unchecked; the main side should confirm.
- The master receipts ledger, the takeover log and security material.

## Convergence line

NOT CONVERGED. One blocking and fifteen should-fix findings are open across the nine drafts.
