# Build and docs convergence, round 1

Verdict: **NOT CONVERGED.** None of the eight drafts converges on the first pass. Each has at least one open should-fix or blocking item.

Counts: drafts checked 8 (two slices of four). Drafts with no finding 0. Drafts with a blocking finding 2 (SOLR-16322, SOLR-17842). Drafts with should-fix or note findings only 6. Findings 36: 2 blocking, 23 should-fix, 11 note. All are new on this first pass. Register IDs BD-R1-01 to 19 come from slice A and BD-R1-21 to 37 from slice B.

Scope: `assignments/pool-build-docs-convergence-round-1.md`. The decision record is `reports/build-docs-misc-round-1.md` and its parts, with the draft-fidelity settlements. Head checks used `git ls-remote origin refs/heads/<branch>`. Every head matches its draft, so none is on hold, except where a row says otherwise. Two read-only subagents did the review. Nothing was edited, posted or built.

## Verdict per draft

- **SOLR-3684: NOT CONVERGED.** BD-R1-01, 02, 03 (should-fix); BD-R1-04, 05 (note). The HOLD on line 1 matches open owner decision 1 and is not a finding.
- **SOLR-5821: NOT CONVERGED.** BD-R1-06, 07, 08, 09 (should-fix); BD-R1-10 (note). BD-R1-09 needs a branch commit first.
- **SOLR-6430: NOT CONVERGED.** BD-R1-11, 12, 13, 14 (should-fix). BD-R1-13 needs a branch commit (drop the changelog).
- **SOLR-16322: NOT CONVERGED.** BD-R1-15 (blocking); BD-R1-16, 17 (should-fix); BD-R1-18, 19 (note).
- **SOLR-16914: NOT CONVERGED.** BD-R1-21, 22, 23 (should-fix); BD-R1-24, 25 (note).
- **SOLR-17252: NOT CONVERGED.** BD-R1-26, 27 (should-fix); BD-R1-28, 29 (note).
- **SOLR-17752: NOT CONVERGED.** BD-R1-30, 31, 32 (should-fix); BD-R1-29, 33 (note).
- **SOLR-17842: NOT CONVERGED.** BD-R1-34 (blocking); BD-R1-35, 36 (should-fix); BD-R1-37 (note).

## Register: blocking and should-fix rows

**BD-R1-15 | SOLR-16322 | blocking.** The draft has no hold line and reads as postable. Owner decision 17 is open, and an exercised non-beast failing run and a passing build are owed before any PR. Insert as line 1: "[HOLD: do not post. Owner decision 17 is open. An exercised non-beast failing run and a passing build are owed before any PR. Delete this line when they land.]"

**BD-R1-34 | SOLR-17842 | blocking.** Line 19 says the draft "names one or two existing benchmark classes per area". The table at the head (lines 41 to 48) names three for search and eight in total. "One or two" is the guide's advice, not what the table names. Replacement: "The guide names the existing benchmark classes for each area ([lines 41 to 48]) and suggests covering one or two benchmarks per area in a first pass ([lines 26 to 28])."

**BD-R1-01 | SOLR-3684 | should-fix.** The Choice says "...in JettySolrRunner, which is Jetty's own default." The 254 figure is Robert Muir's 2012 comment, and the head uses a different Jetty version whose default was not checked. Replacement: "The option considered was to set the default to 254 in `jetty.xml` and in JettySolrRunner, the figure Robert Muir cites as Jetty's default in 2012."

**BD-R1-02 | SOLR-3684 | should-fix.** "## Limits" has no bold summary. Insert under it: "**This change leaves the default, the production setting and the ticket's memory problem as they are.**"

**BD-R1-03 | SOLR-3684 | should-fix.** The `jetty.xml` link points at the head, but the branch does not touch that file. Replacement: "([jetty.xml, line 34 at the base commit](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/server/etc/jetty.xml#L34))".

**BD-R1-06 | SOLR-5821 | should-fix.** "## Proof" opens with plain text. Put this first: "**This is a documentation build, not a test run, because the change is text only.**"

**BD-R1-07 | SOLR-5821 | should-fix.** "## Limits" has no bold summary. Insert: "**The document counts and per-replica term statistics are not covered.**"

**BD-R1-08 | SOLR-5821 | should-fix.** "A commenter linked that to SOLR-4260." The draft has no link. Replacement: "A commenter linked that to [SOLR-4260](https://issues.apache.org/jira/browse/SOLR-4260)."

**BD-R1-09 | SOLR-5821 | should-fix; needs a branch commit.** Line 21 says "have the same searcher." The roll-up recommends plain words. The branch must change first, then links and Proof re-point. Replacement after that commit: "3. `numFound` is not affected when the replicas are in sync and hold the same committed documents."

**BD-R1-11 | SOLR-6430 | should-fix.** "## Limits" has no bold summary. Insert: "**The sentences rest on reading the code, not on a sort run.**"

**BD-R1-12 | SOLR-6430 | should-fix.** Line 11 links `DatePointField` line 163, which is inside a query path. Index encoding is at line 196. Replacement: "A date is indexed as milliseconds since the epoch ([DatePointField, line 196](https://github.com/nick-boss-tech/solr/blob/cabedd1d968059215188f4e7563fb303241899ed/solr/core/src/java/org/apache/solr/schema/DatePointField.java#L196))."

**BD-R1-13 | SOLR-6430 | should-fix; needs a branch commit.** The changelog line points at a fragment that the owner decision (drop it, recommended) removes. Delete the line after the branch commit, and re-verify the changelog-lint sentence at the new head.

**BD-R1-14 | SOLR-6430 | should-fix.** Limits omit items the record names as not checked. Add: "- The string sentence rests on the StrField test. Fields without docValues (uninverted), and the TextField and SortableTextField defaults, were not checked."

**BD-R1-16 | SOLR-16322 | should-fix.** Line 19 implies a visible change on every failing build. Replacement: "The helper runs for every failing test and failing suite, not only for beast runs. On an ordinary failing build the printed command keeps the same seed text as before."

**BD-R1-17 | SOLR-16322 | should-fix.** The Proof has no verification date. Append: "Verified [run date from the run record, main side] at `68dd5e2b154e3b9e61c715db94ca91fadad545a6`." The receipt has no date.

**BD-R1-21 | SOLR-16914 | should-fix.** Line 33 says "A token stream check was not run." The receipt records a direct tokenizer run. Replacement: "The statement that `false` gives a non-linear stream rests on the tokenizer description and a direct run of the Lucene 10.4.0 tokenizer, which kept the compound at position increment 0 in `search` and `extended` modes. The receipt records no run with a synonym filter after the tokenizer."

**BD-R1-22 | SOLR-16914 | should-fix.** Proof and Limits open with no bold summary. Proof: "**No test applies. The docs build and the tidy check ran at this head.**" Limits: "**The change does not show that the reported search problem is fixed.**"

**BD-R1-23 | SOLR-16914 | should-fix.** "(line 2302)" is bare text. Replacement: "[line 2302](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/solr-ref-guide/modules/indexing-guide/pages/language-analysis.adoc#L2302), merge-base, row unchanged by this change".

**BD-R1-26 | SOLR-17252 | should-fix.** The title says "EDITOR names a terminal editor", but only five names are refused; `nvim` passes. Replacement title: "Stop the release wizard when EDITOR names vi, vim, nano, pico or emacs". Summary at line 11: "With EDITOR set to a terminal editor such as nano, the wizard hands the terminal to that editor, and the wizard stops responding."

**BD-R1-27 | SOLR-17252 | should-fix.** "## Limits" has no bold summary. Insert: "**The check covers five names and only the first word of EDITOR.**"

**BD-R1-30 | SOLR-17752 | should-fix.** The head test result has no verification date. Replacement once confirmed: "**TestShardResponseLogging passed 1 of 1 at head e5c0a64f993c, verified 2026-10-04.**"

**BD-R1-31 | SOLR-17752 | should-fix.** The base run names no base commit. Replacement: "On the base commit `14c7aac0d151`, the test fails at the node-name assertion (line 69), and the captured warning holds the query text in both the request and the response header." Re-point after the owner's base decision.

**BD-R1-32 | SOLR-17752 | should-fix.** Limits have no bold summary. Insert: "**The test builds its response by hand, and other log lines still print request params.**"

**BD-R1-35 | SOLR-17842 | should-fix.** "exist on main at 3f5d4c5bf8ac" has no check date. Replacement: "All eight benchmark classes named in the guide, and the `jmh.sh` script in its example, are present on main at `3f5d4c5bf8ac` (tree listing re-checked on [posting date])."

**BD-R1-36 | SOLR-17842 | should-fix.** Limits have no bold summary. Insert: "**This PR gives instructions only, and it publishes no benchmark results.**"

## Notes (carried in the register, not holding a draft by themselves)

BD-R1-04, 05, 10, 18, 19, 24, 25, 28, 29, 33, 37 are recorded in the register. Notable: BD-R1-05 (a test closes a runner it never started), BD-R1-10 (no build on current main was run), BD-R1-18 (no title line on SOLR-16322), BD-R1-28 (a docs sentence the owner may adopt), and BD-R1-37 (SOLR-17842's head will change with the owner's `<build>` fix).

## Not checked

- Gate logs named by the receipts are not on disk, so the Proofs rest on receipt wording.
- The Lucene 9.12.3 pin for `branch_9x` (no local ref).
- The master receipts ledger, the takeover log and security material.

## Convergence line

NOT CONVERGED. Two blocking and twenty-three should-fix findings are open across the eight drafts.
