# CLI draft fidelity, slice 1

Assignment: `assignments/cli-bin-packaging-round-1.md`. Claim: `claims/cli-bin-packaging-round-1.md`. Category round 1 report: `reports/cli-bin-packaging-round-1.md`, with part reports `-s1.md` (SOLR-9342), `-s2.md` (SOLR-16272, SOLR-16813) and `-s3.md` (SOLR-17029) read for this slice.

Slice drafts (`pr-drafts/cli/`): `SOLR-9342.md`, `SOLR-16272.md`, `SOLR-16813.md`, `SOLR-17029.md`. Receipts (`receipts/`): the four matching files. Heads checked with `git ls-remote origin refs/heads/solr-<n>-submit`, all four matching the head each draft names.

Method: each draft's head and merge-base checked (`git merge-base <head> upstream/main`). Every cited SHA resolves locally. Cited line ranges read with `git show <sha>:<path>`. Changelog fragments read at each head. Ticket context read from the local `research/jira-context/SOLR-<n>.json` files only (read only, no JIRA call). `material/` grepped for 9342, 16272, 16813 and 17029: no matches, and no `material/*answers*` file covers these tickets.

## Verdicts

| Draft | Head checked (ls-remote) | Verdict |
|---|---|---|
| SOLR-9342 | `833e11192a7ff918f7e401bde920cf63c89df063` (matches) | DRIFT (4 items) |
| SOLR-16272 | `d2cf817391692b29c3d221ec4ebed65c3833bb91` (matches) | DRIFT (1 item) |
| SOLR-16813 | `1b288170e8aa9c3b1ea9bce418e26bd61f08498e` (matches) | DRIFT (2 items) |
| SOLR-17029 | `4a98ef0a89d16ec5a645a1c38fbe25dc28c87e62` (matches) | DRIFT (1 item) |

Checks that passed for all four: heads and merge-base labels; every code citation resolves to the cited code at its SHA (9342: base L1329, head L498, L1001, L1196, L1334, L1352, L1356, L1088, BATS L59-L66; 16272: base L165-L249, head L178, L203, L220, L258-L263, L278-L311, PackageToolTest L443-L508, ClusterFileStore L111-L131; 16813: base L264-L274, L205-L210, L163-L167, L276-L283, L54, head L276, L294-L315, L192-L196, PackageUtils L184-L199, TestPackages L193-L232 and L235-L302; 17029: base L681 and L1349, head L687-L737, L741-L742, L1278-L1284, BATS L135-L163, RunExampleTool L872-L880); changelog files exist at each head; AI header and assistance footer present; no em dash or en dash; no internal process vocabulary in public text; verification dates present.

## SOLR-9342

Verdict: DRIFT (4 items).

1. Draft says: "Title: Solr GC logging not respecting user timezone"
- Evidence: `changelog/unreleased/SOLR-9342-gc-log-timezone.yml` at 833e11192a7 (line 2) reads "bin/solr now exports TZ from SOLR_TIMEZONE (default UTC) so the GC log uses the same timezone as the Solr log, unless TZ is already set." The title does not match it. The fragment also has the superseded "exports" wording (round s1 item 6). The assignment said to take the title from the ticket, but the title-match and title-accuracy rules govern.
- Replacement: "Title: bin/solr sets TZ from SOLR_TIMEZONE (default UTC) for the server JVM, so its GC log uses that zone unless TZ is already set"
  Fragment title (changelog-only commit on `solr-9342-submit`) must carry the same wording: "bin/solr sets TZ from SOLR_TIMEZONE (default UTC) for the server JVM, so its GC log uses that zone unless TZ is already set"

2. Draft says: "- At head `833e11192a7f` (verified 2026-10-07), the full file ran 10 tests: 9 passed, 1 skipped."
- Evidence: `receipts/SOLR-9342.md` line 7 gives the head run's counts (9 ok, 1 skipped) but no date for that run. Its dated items are the 2026-10-06 first verification at 6e1ade86ec78 and the 2026-10-07 tip change (line 9). The tip commit is dated 2026-10-07 02:06:38 +0000 (`git log`), so 2026-10-07 is the tip date, not a recorded run date. Round s1 item 9 and the round owner decisions say to confirm the date before posting.
- Replacement: "- At head `833e11192a7f` (verified on or after 2026-10-07, the date of this tip), the full file ran 10 tests: 9 passed, 1 skipped. The skip is the timeout-utility check, the same skip the file had before this change."
  Replace "on or after 2026-10-07" with the exact run date once read from the run log (g9342-tc-full.log, not in the workspace).

3. Draft says: "- Evidence shape: a BATS suite, run by hand against the distribution built from this head, as a non-root user."
- Evidence: `receipts/SOLR-9342.md` line 8: "BATS ran manually as user nobody against the Gradle-built distribution." The receipt does not say the distribution was built from 833e11192a7. The round report lists "the build of the BATS distribution from the head" as main-side work still owed.
- Replacement: "- Evidence shape: a BATS suite, run by hand as a non-root user against the Gradle-built distribution. The start script refuses to start Solr as root ([L1001](https://github.com/nick-boss-tech/solr/blob/833e11192a7ff918f7e401bde920cf63c89df063/solr/bin/solr#L1001)). This is a BATS run, not a Gradle test run."

4. Draft says: "- `solr.cmd` is not changed. The Windows JVM does not read `TZ` for GC log timestamps, so this change adds no line there."
- Evidence: the receipt (line 8) states the JVM claim. The round report's Windows table says "consistent with the diff; the JVM claim is not verified here", and owner decision 4 says to check it on Windows or soften it. No Windows run is recorded. The assignment asks for this parity claim to be checked, and it was not.
- Replacement: "- `solr.cmd` is not changed. The Windows GC log timestamps were not tested, so this change makes no claim for Windows."

Optional notes, not blocking:
- The changelog link uses the real fragment name `SOLR-9342-gc-log-timezone.yml`, not the `SOLR-<n>.yml` pattern. It resolves at the head, so it is consistent. Keep it, or rename the fragment in the same changelog-only commit.
- Precedence (round item 1, "blocks PR-ready") is still open. The draft states it as a Limit with a follow-up offer, which matches formula section 4. If the owner picks option B (SOLR_TIMEZONE as the one knob, with a test), the Limit must say so and a test must be added. No such test is on the branch.
- The changelog should name the Linux and macOS scope (round s1 item 4). The draft does not quote the changelog.
- The "base" label is the merge-base `97d973814336` (confirmed as `merge-base upstream/main`). The formula asks that the text say merge-base once.
- Length: 4,896 characters with link targets, above the roughly 3,500 guide (round item 14).

## SOLR-16272

Verdict: DRIFT (1 item).

1. Draft says: "Title: Remove a failed package install's files unless the version may be registered"
- Evidence: `changelog/unreleased/SOLR-16272.yml` at d2cf8173 (line 1) reads "A failed bin/solr package install, for example because of a signature error, now removes the files it already uploaded to the package store, so the install can be retried and uninstall no longer refuses." The title does not match. Round s2 item 1 and owner decision 1 call that wording too broad (the code keeps files once registration may have happened, RepositoryManager.java L224, L258-L263, L299-L311; uninstall is unchanged). The draft title is the amended wording the round approved.
- Replacement: keep the draft title as written. Amend the fragment title on `solr-16272-submit` (changelog-only commit) to: "title: Remove a failed package install's files unless the version may be registered"

Optional notes, not blocking:
- The Limits section opens without a bold one-line summary, which the formula's presentation rule asks for. Suggested opener: "**The keep branch after registration has no test, and the registry read happens once.**"
- No Choice section. Round s2 says none is needed, since always rolling back would delete files a registered version needs. Consistent.
- The draft's Limits match the round (single registry read, no test for the post-registration branch).
- Length: 4,508 characters with link targets.

## SOLR-16813

Verdict: DRIFT (2 items).

1. Draft says: "Title: Fetch a package version's manifest.json into the local file store when the version loads, best effort"
- Evidence: `changelog/unreleased/SOLR-16813.yml` at 1b288170 (line 1) reads "A node that joins a cluster with installed packages now also fetches each package version's manifest.json ... so package commands such as list-installed no longer fail with NOT_FOUND on that node." The title does not match. The fetch runs when the version loads (`fetchManifest()` is called from the Version constructor, SolrPackageLoader.java L276 at head), not at join (round s2 item 4; owner decision 2 says amend to "when a version loads"). The draft title is the amended wording.
- Replacement: keep the draft title as written. Amend the fragment title on `solr-16813-submit` (changelog-only commit) to: "title: Fetch a package version's manifest.json into the local file store when the version loads, best effort"

2. Draft says: "Against the base code, the class still runs six tests, and one fails:"
- Evidence: `TestPackages.java` at the base (14c7aac0) has 4 `@Test` methods; at the head (1b288170) it has 6. The "six tests" figure in `receipts/SOLR-16813.md` line 7 is this branch's test file run against the base code (round s2 item 5). A maintainer who checks out the base and counts will see 4, so the draft's wording reads as a false count.
- Replacement: "With this branch's test file run against the base code, six tests run and one fails:"

Optional notes, not blocking:
- Install and uninstall through a node missing a version's manifest also fail, since the registry read goes through that node (round s2 item 6). Limits do not name it. This is the owner's call: name it in Limits, or leave it under the general wording.
- The Choice section (best effort versus mandatory) matches round s2 owner decision 5 (keep). The Limits match the round.
- The Limits section opens without a bold one-line summary (formula presentation rule).
- "NOFILE" and "FILE" are code states. A plain-words gloss, such as "the manifest is missing from the file store", would help a reader.
- Length: 5,817 characters with link targets.

## SOLR-17029

Verdict: DRIFT (1 item).

1. Draft says: "- Run on 2026-10-08, at the same head: the three new tests passed again. The packaging check (`:solr:packaging:check`) passed."
- Evidence: `receipts/SOLR-17029.md` line 7: the 2026-10-08 re-verification "had one deviation, the pre-existing test 5 (deprecated system properties)", which is a log-timing race that "also fails on pristine base". The draft cites that run as passing and leaves out its one failure. The assignment says the draft should say nothing either way about test 5, so the draft's silence on the failure is compliant. But a run cited as proof should be reported as it ran. Round s3 owner decision 1 and round item 4 are open on this.
- Replacement (recommended): delete the 2026-10-08 bullet, and replace the first-run bullet with: "- First run, 2026-10-04: 11 passed and 1 skipped. The skip happened because the `timeout` utility was missing on the test machine. The three new tests passed, and the packaging check (`:solr:packaging:check`) passed in the same run."
- Alternative, if the owner keeps the 2026-10-08 result: replace the 2026-10-08 bullet with "- Run on 2026-10-08, at the same head: the three new tests passed again, and the packaging check passed. One pre-existing test, test 5 (deprecated system properties), failed in that run. It also fails on the base code, because of a log-timing race, so this change does not cause it."

Optional notes, not blocking:
- The draft has no `Title:` line, unlike the other three drafts. If one is added, it should match the fragment at head: "bin/solr no longer fails to start when SOLR_OPTS or --jvm-opts contain a value with quoted whitespace, such as -Dprop=\"white space\"".
- No Choice section. Round s3 says the alternatives were ruled out on correctness grounds (eval rewrites `$` and backticks; a wider escape set breaks Windows paths). Consistent with the draft.
- The "Could not find or load main class space\"" text in the draft is sourced: the head BATS test comment (`test_start_solr.bats` L136) quotes it.
- The `-a` statement holds: head `solr/bin/solr` has no `-a)` option (only `read -a` builtins), and `--jvm-opts` at L866 is the live route.
- `receipts/SOLR-17029.md` names the control run as "pristine main" with no SHA. The draft names the merge-base 14c7aac0 as base, which the round also uses. Consistent, but the receipt does not name the SHA.
- The Limits match round s3 (`-e` example path, trailing backslash, UNC, solr.cmd unchanged). The diff stat confirms only the changelog, `solr/bin/solr` and the BATS file change.
- Length: 4,857 characters with link targets.

## Not done

- No builds, Gradle, BATS, tests, gate runs or `test-queue` commands, per the brief.
- The run logs named in the receipts (g9342-tc-full.log, g17029-bats2.log, g17029-reverify.log, g16272-harden.log, g16813r35-gate.log) are not in the worktree. I did not search the workspace for them. Counts and run dates were checked against the receipts only, so the 9342 run date and the 17029 test 5 detail rest on the receipt text.
- Windows behavior (`solr.cmd`, the JVM's handling of TZ) and the `-e` example launch were read from code only, not run.
- Base code was checked at the merge-base SHAs named in the drafts, not at the current `upstream/main` tip, which has moved.
- Part reports s4 to s6 were outside this slice and were not read. Ticket context was read from the local JSON files only.
- No commit, push, PR call, JIRA call, or write to any file other than this report.
