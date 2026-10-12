# Draft fidelity confirmation, round 2

Verdict: **5 CONFIRMED, 2 DRIFT** (of the seven drafts round 1 found drifting). The two drifts are small: a lead-in that asks a question the record has already closed (SOLR-18368), and a Choice that names one spelling of a row reference the code also scans (SOLR-9396).

Read-only. The seven drafts were checked in place against the round 1 record (`reports/draft-fidelity-confirmation-round.md`), the round response (`reports/draft-fidelity-confirmation-round-response.md`), receipts, and the head and base source named in the assignment. Nothing was edited, built, tested or posted.

## Verdicts

1. **`metrics/SOLR-17987.md`: CONFIRMED.** Line 15 carries "The environment variable `SOLR_METRICS_DISABLEDREGISTRIES` sets the same property." This matches the head: `metrics-reporting.adoc` line 106 at `38abf6423126`, and `EnvToSyspropMappings.properties` line 39. It is the head name, not the branch rename. Line 31 (Choice) no longer restates the cost; one cost sentence remains and reads coherently with the bold summary at line 29. No claim in the checked passages goes beyond the guide or the mapping.

2. **`search-components/SOLR-12556.md`: CONFIRMED.** Line 13 contains "buckets known before refinement that were not refined against every shard that returned the facet are now dropped", with "instead of returned with partial stats" added. `receipts/SOLR-12556.md` line 9 says the qualification is required, and it is now present. The body (line 17), Choice (lines 27 to 29) and Limits (line 35) agree.
   - **Record note, not a draft drift:** `reports/draft-fidelity-fixes-application-response.md` line 202 (item 32) still records the unqualified changelog summary as settled. The round response (lines 20 to 23) supersedes it. Narrowing the changelog title stays a packaging item.

3. **`edismax/SOLR-4362.md`: CONFIRMED.** Line 13 now reads "A boost after the slop, such as `~10^2`, is skipped too". This matches `isSlopClause` at `e96a057439` (`ExtendedDismaxQParser.java` lines 296 to 331): `~10^2` is recognised, `^2~10` is not, and `~2.5` is not, at the decimal point. The skip is at lines 344 to 350. Line 30 names `~2.5` and `^2~10` as uncovered, with a named follow-up. The removed pf2-shingle sentence is absent, and the bare-word case at line 31 stays flagged as untraced.

4. **`build-docs/SOLR-16914.md`: CONFIRMED (unchanged; the response's reason holds against the base).** At the base `14c7aac0d151`, the Japanese tokenizer introduction (`language-analysis.adoc` line 2292) describes segmentation and keeps the compound terms as synonyms, with no option named. The base `discardCompoundToken` entry reads "Optional | Default: none", and its description says what the option keeps. This supports "does not name the option" and "gives it no default" as the base state. The branch diff from the base touches one file, `language-analysis.adoc` (7 insertions, 3 deletions). The `filters.adoc` note at line 3131 holds at the base. The head `cd878023d3d0` matches the cited head.

5. **`cli/SOLR-17029.md`: CONFIRMED.** The "few seconds" parenthetical is gone. Line 37 now reads "an upstream log-timing race that also fails on the base code at roughly the same rate", which matches `receipts/SOLR-17029.md` line 7 and the settlement's F5. "Ran it repeatedly on both trees" comes from the settlement, an allowed source. Line 40 ("passes on both trees") matches receipt line 6.

6. **`security/SOLR-18368.md`: DRIFT.**
   - **Passage:** line 1 (banner), the lead-in "Do not post until the owner decides whether a PR opens and under which key". Option (c) is correctly gone, and options (a) and (b) remain.
   - **Record:** `receipts/SOLR-18368.md` line 9 says that not opening at all is foreclosed by the still-an-issue verdict and the resolved-ticket rule, and that only the key question is settled at opening approval. The lead-in still asks whether a PR opens, which the record has closed. Only the key is open.
   - **Fix:** change the lead-in to "decides under which key the PR opens".
   - **Owner flag, not the basis of this verdict:** the receipt names two alternatives, carrying the SOLR-18368 key or opening as a follow-up reference. Banner option (a), "no key, Jira line deleted", is a shape the receipt does not name. The round response kept (a) and (b) as written. The owner should confirm (a) before opening.

7. **`search-components/SOLR-9396.md`: DRIFT.**
   - The applied text is present as recorded: line 15 names `$row.field` and `${row.field}`, Limits bullet 1 (line 34) names both, and the Limits summary (line 32) is present. The head pattern at `a5ab2eda4e6` (`SubQueryAugmenterFactory.java` line 307, `\$\{?row\.`) matches both spellings.
   - **Passage:** the Choice bold summary (line 26), "The transformer reads `$row.` names from the subquery's own parameters, not from every field of the request.", and line 28, "scan the subquery's parameter values for `$row.field` references (implemented)". Both still name only the `$row.` spelling. The implemented scan also matches `${row.`, as line 35 says and the regex confirms, so the Choice contradicts its own section's bullets and the code.
   - **Record:** `reports/draft-fidelity-searchcomponents-edismax-s10.md` lines 51 and 52 name both spellings. The round 1 fix named only line 15 and the first Limits bullet, so the Choice was not reached.
   - **Fix:** name both spellings at lines 26 and 28.
   - **Not a drift:** bullet 1 (line 34) calls `${row.field}` a scanned reference, and bullet 2 (line 35) says the parser never treats that spelling as a row reference. The record's replacement uses the same wording.

## Scope

Read-only. No draft, receipt, report or branch was edited. The two drifts are for the main side to apply.
